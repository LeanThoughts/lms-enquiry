package pfs.lms.enquiry.collateral.migration;

import com.fasterxml.jackson.databind.JsonNode;
import pfs.lms.enquiry.collateral.domain.SapField;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Copies a record sent by the SAP migration program onto a portal entity. Keys are SAP column names (e.g. "ZID_NO")
 * or portal field names (e.g. "checklistIdNo", needed for the long texts), in any case. Values are converted from
 * SAP formats: dates "YYYYMMDD" or "YYYY-MM-DD" ("00000000" = empty), flags "X" / "" (or true / false), numbers as
 * JSON numbers or strings (SAP trailing minus "12.50-" accepted).
 */
class SapRecordMapper {

    private static final DateTimeFormatter SAP_DATE = DateTimeFormatter.BASIC_ISO_DATE;

    /** Keys the SAP tables have but the portal entities cover otherwise; they are ignored without a message. */
    private static final Set<String> IGNORED_KEYS = new HashSet<>(Arrays.asList(
            "MANDT", "CREATED_BY", "CREATED_AT", "CHANGED_BY", "CHANGED_AT", ".INCLUDE"));

    private final Class<?> type;
    private final Map<String, Field> fieldsByKey = new LinkedHashMap<>();
    /** Coded fields: their length is checked after the caller converted descriptions to codes. */
    private final Set<String> codedFields;

    SapRecordMapper(Class<?> type, Set<String> codedFields) {
        this.type = type;
        this.codedFields = codedFields;
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                SapField sapField = field.getAnnotation(SapField.class);
                if (sapField == null) {
                    continue;
                }
                field.setAccessible(true);
                if (!sapField.value().isEmpty()) {
                    fieldsByKey.putIfAbsent(sapField.value().toUpperCase(Locale.ROOT), field);
                }
                fieldsByKey.putIfAbsent(field.getName().toUpperCase(Locale.ROOT), field);
            }
        }
    }

    /**
     * Copies the values of the record onto the target.
     *
     * @param skipKeys keys handled by the caller (e.g. child table arrays)
     * @param problems receives one text per value that could not be used; that value is left unchanged
     */
    void apply(JsonNode record, Object target, Set<String> skipKeys, List<String> problems) {
        Iterator<Map.Entry<String, JsonNode>> entries = record.fields();
        while (entries.hasNext()) {
            Map.Entry<String, JsonNode> entry = entries.next();
            String key = entry.getKey().toUpperCase(Locale.ROOT);
            if (skipKeys.contains(key) || IGNORED_KEYS.contains(key)) {
                continue;
            }
            Field field = fieldsByKey.get(key);
            if (field == null) {
                problems.add("Unknown field " + entry.getKey() + " ignored.");
                continue;
            }
            try {
                Object value = convert(field, entry.getValue());
                if (!codedFields.contains(field.getName())) {
                    checkSize(field, value);
                }
                field.set(target, value);
            } catch (IllegalArgumentException ex) {
                problems.add(entry.getKey() + ": " + ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /** Reads a field value of the target by SAP or portal name (null when unknown). */
    Object get(Object target, String key) {
        Field field = fieldsByKey.get(key.toUpperCase(Locale.ROOT));
        try {
            return field == null ? null : field.get(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /** Maximum length of a text field (0 when unknown or unlimited). */
    int maxLength(String key) {
        Field field = fieldsByKey.get(key.toUpperCase(Locale.ROOT));
        Column column = field == null ? null : field.getAnnotation(Column.class);
        return column == null || field.getType() != String.class || isLongText(field) ? 0 : column.length();
    }

    void set(Object target, String key, Object value) {
        Field field = fieldsByKey.get(key.toUpperCase(Locale.ROOT));
        try {
            if (field != null) {
                field.set(target, value);
            }
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /** Text of a record value by SAP or portal name, trimmed; null when missing or empty. */
    static String text(JsonNode record, String... keys) {
        for (String key : keys) {
            Iterator<Map.Entry<String, JsonNode>> entries = record.fields();
            while (entries.hasNext()) {
                Map.Entry<String, JsonNode> entry = entries.next();
                if (entry.getKey().equalsIgnoreCase(key) && !entry.getValue().isNull()) {
                    String value = entry.getValue().asText().trim();
                    if (!value.isEmpty()) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /** Array of a record by any of the keys (case-insensitive); empty list when missing. */
    static List<JsonNode> array(JsonNode record, String... keys) {
        List<JsonNode> rows = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> entries = record.fields();
        while (entries.hasNext()) {
            Map.Entry<String, JsonNode> entry = entries.next();
            for (String key : keys) {
                if (entry.getKey().equalsIgnoreCase(key) && entry.getValue().isArray()) {
                    entry.getValue().forEach(rows::add);
                }
            }
        }
        return rows;
    }

    private Object convert(Field field, JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        Class<?> fieldType = field.getType();
        String raw = node.isTextual() ? node.asText() : node.toString();
        if (fieldType == String.class) {
            String value = isLongText(field) ? stripTrailing(raw) : raw.trim();
            return value.isEmpty() ? null : value;
        }
        String value = raw.trim();
        if (value.isEmpty()) {
            return null;
        }
        if (fieldType == LocalDate.class) {
            return date(value);
        }
        if (fieldType == Boolean.class) {
            return flag(value);
        }
        if (fieldType == Integer.class) {
            BigDecimal number = number(value);
            try {
                return number.intValueExact();
            } catch (ArithmeticException ex) {
                throw new IllegalArgumentException("'" + value + "' is not a whole number.");
            }
        }
        if (fieldType == Long.class) {
            BigDecimal number = number(value);
            try {
                return number.longValueExact();
            } catch (ArithmeticException ex) {
                throw new IllegalArgumentException("'" + value + "' is not a whole number.");
            }
        }
        if (fieldType == BigDecimal.class) {
            return number(value);
        }
        throw new IllegalArgumentException("unsupported field type " + fieldType.getSimpleName() + ".");
    }

    private static LocalDate date(String value) {
        if (value.matches("0+") || value.equals("0000-00-00")) {
            return null;
        }
        try {
            return value.length() == 8 && value.matches("\\d{8}")
                    ? LocalDate.parse(value, SAP_DATE)
                    : LocalDate.parse(value.substring(0, Math.min(10, value.length())));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("'" + value + "' is not a date (YYYYMMDD or YYYY-MM-DD).");
        }
    }

    private static Boolean flag(String value) {
        switch (value.toUpperCase(Locale.ROOT)) {
            case "X":
            case "TRUE":
            case "1":
            case "Y":
                return true;
            case "FALSE":
            case "0":
            case "N":
            case "-":
                return false;
            default:
                throw new IllegalArgumentException("'" + value + "' is not a flag (X or empty).");
        }
    }

    private static BigDecimal number(String value) {
        String normalized = value.replace(",", "");
        boolean negative = normalized.endsWith("-");
        if (negative) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }
        try {
            BigDecimal number = new BigDecimal(normalized);
            return negative ? number.negate() : number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("'" + value + "' is not a number.");
        }
    }

    private static void checkSize(Field field, Object value) {
        Column column = field.getAnnotation(Column.class);
        if (column == null || value == null) {
            return;
        }
        if (value instanceof String && !isLongText(field) && ((String) value).length() > column.length()) {
            throw new IllegalArgumentException("longer than " + column.length() + " characters.");
        }
        if (value instanceof BigDecimal && column.precision() > 0) {
            BigDecimal number = ((BigDecimal) value).stripTrailingZeros();
            if (number.scale() > column.scale()) {
                throw new IllegalArgumentException("more than " + column.scale() + " decimal places.");
            }
            if (number.precision() - number.scale() > column.precision() - column.scale()) {
                throw new IllegalArgumentException("too large.");
            }
        }
    }

    private static boolean isLongText(Field field) {
        SapField sapField = field.getAnnotation(SapField.class);
        return sapField != null && sapField.longText();
    }

    private static String stripTrailing(String value) {
        int end = value.length();
        while (end > 0 && Character.isWhitespace(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(0, end);
    }

    Class<?> getType() {
        return type;
    }
}
