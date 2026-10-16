package pfs.lms.enquiry.reports;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import pfs.lms.enquiry.exception.LmsException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ABAP SELECT-OPTIONS semantics for the report selection screens: parsing and normalising the lines of a selection
 * parameter, turning them into an OData V2 {@code $filter} expression and into a readable text for the Excel header.
 *
 * <p>Included lines (SIGN I) are combined with OR, excluded lines (SIGN E) are negated and combined with AND, exactly
 * as ABAP evaluates a range table: {@code (inc1 or inc2 …) and exc1' and exc2' …}. An empty selection is sent as an
 * empty value ({@code Prop eq ''}, or the lowest .. highest date) so SAP always receives the full filter.
 */
public final class SelectOptions {

    /** The kind of the property the lines select on. */
    public enum Kind { TEXT, DATE }

    private static final List<String> OPTIONS = Arrays.asList("EQ", "NE", "GT", "GE", "LT", "LE", "BT", "NB", "CP", "NP");
    private static final ObjectMapper JSON = new ObjectMapper();

    private SelectOptions() {
    }

    // ------------------------------------------------------------------------------------------------ parsing

    /** The lines from the JSON array of a query parameter; null / blank = no lines. */
    public static List<SelectOption> parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            List<SelectOption> lines = JSON.readValue(json, new TypeReference<List<SelectOption>>() { });
            return lines == null ? new ArrayList<>() : lines;
        } catch (Exception ex) {
            throw new LmsException("The selection is not valid.", HttpStatus.PRECONDITION_FAILED);
        }
    }

    /**
     * Cleans the lines as ABAP would see them: SIGN defaults to I, empty lines are dropped, BT without HIGH is EQ, a
     * line with only HIGH is LE, values are trimmed, dates validated. The result is never null.
     */
    public static List<SelectOption> normalize(List<SelectOption> lines, Kind kind, String label) {
        List<SelectOption> out = new ArrayList<>();
        if (lines == null) {
            return out;
        }
        for (SelectOption line : lines) {
            if (line == null) {
                continue;
            }
            String sign = "E".equalsIgnoreCase(trim(line.getSign())) ? "E" : "I";
            String option = trim(line.getOption()).toUpperCase();
            String low = trim(line.getLow());
            String high = trim(line.getHigh());
            if (option.isEmpty()) {
                option = high.isEmpty() ? "EQ" : "BT";
            }
            if (!OPTIONS.contains(option)) {
                throw new LmsException(label + ": unknown selection option " + option + ".", HttpStatus.PRECONDITION_FAILED);
            }
            if (low.isEmpty() && high.isEmpty()) {
                continue;
            }
            if (low.isEmpty()) {                             // only an upper limit: "up to"
                low = high;
                high = "";
                option = "LE";
            }
            boolean between = option.equals("BT") || option.equals("NB");
            if (between && high.isEmpty()) {
                option = option.equals("BT") ? "EQ" : "NE";
            }
            if (!between) {
                high = "";
            }
            if (kind == Kind.DATE) {
                LocalDate lo = date(low, label);
                LocalDate hi = high.isEmpty() ? null : date(high, label);
                if (hi != null && hi.isBefore(lo)) {
                    throw new LmsException(label + ": the upper date " + hi + " is before the lower date " + lo + ".",
                            HttpStatus.PRECONDITION_FAILED);
                }
                low = lo.toString();
                high = hi == null ? "" : hi.toString();
            }
            out.add(new SelectOption(sign, option, low, high));
        }
        return out;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The OData V2 filter terms of one selection parameter, joined with " and "; never empty. */
    public static String filter(String property, List<SelectOption> lines, Kind kind, String label) {
        List<SelectOption> sel = normalize(lines, kind, label);
        List<String> includes = new ArrayList<>();
        List<String> excludes = new ArrayList<>();
        for (SelectOption line : sel) {
            if (line.getSign().equals("I")) {
                includes.add(expression(property, line.getOption(), line.getLow(), line.getHigh(), kind, label));
            } else {
                excludes.add(expression(property, negate(line.getOption()), line.getLow(), line.getHigh(), kind, label));
            }
        }
        List<String> terms = new ArrayList<>();
        if (includes.size() == 1) {
            terms.add(includes.get(0));
        } else if (includes.size() > 1) {
            terms.add(includes.stream().map(e -> e.contains(" and ") ? "(" + e + ")" : e)
                    .collect(Collectors.joining(" or ", "(", ")")));
        } else if (excludes.isEmpty()) {
            // nothing selected: the empty value, so that SAP always gets the filter
            terms.add(kind == Kind.DATE
                    ? property + " ge datetime'0001-01-01T00:00:00' and " + property + " le datetime'9999-12-31T23:59:59'"
                    : property + " eq ''");
        }
        terms.addAll(excludes);
        return String.join(" and ", terms);
    }

    /** One comparison (or a few joined) for an option, low and high. */
    static String expression(String p, String option, String low, String high, Kind kind, String label) {
        switch (option) {
            case "EQ": return p + " eq " + value(low, kind, "start");
            case "NE": return p + " ne " + value(low, kind, "start");
            case "GE": return p + " ge " + value(low, kind, "start");
            case "GT": return p + " gt " + value(low, kind, "end");
            case "LE": return p + " le " + value(low, kind, "end");
            case "LT": return p + " lt " + value(low, kind, "start");
            case "BT": return p + " ge " + value(low, kind, "start") + " and " + p + " le " + value(high, kind, "end");
            case "NB": return "(" + p + " lt " + value(low, kind, "start") + " or " + p + " gt " + value(high, kind, "end") + ")";
            case "CP": return pattern(p, low, label, false);
            case "NP": return pattern(p, low, label, true);
            default: throw new LmsException(label + ": unknown selection option " + option + ".", HttpStatus.PRECONDITION_FAILED);
        }
    }

    /** The option of an excluded line: what remains selected is its complement. */
    private static String negate(String option) {
        switch (option) {
            case "EQ": return "NE";
            case "NE": return "EQ";
            case "GT": return "LE";
            case "LE": return "GT";
            case "GE": return "LT";
            case "LT": return "GE";
            case "BT": return "NB";
            case "NB": return "BT";
            case "CP": return "NP";
            default: return "CP";
        }
    }

    /** A pattern with * at its start and / or end as startswith / endswith / substringof; others are not supported. */
    private static String pattern(String p, String pattern, String label, boolean not) {
        boolean lead = pattern.startsWith("*");
        boolean trail = pattern.length() > 1 && pattern.endsWith("*");
        String core = pattern.substring(lead ? 1 : 0, pattern.length() - (trail ? 1 : 0));
        if (core.isEmpty() || core.contains("*") || core.contains("+")) {
            throw new LmsException(label + ": the pattern " + pattern + " is not supported (use * at the start and / or end).",
                    HttpStatus.PRECONDITION_FAILED);
        }
        String lit = literal(core);
        String expr;
        if (lead && trail) {
            expr = "substringof(" + lit + "," + p + ")";
        } else if (lead) {
            expr = "endswith(" + p + "," + lit + ")";
        } else if (trail) {
            expr = "startswith(" + p + "," + lit + ")";
        } else {
            return p + (not ? " ne " : " eq ") + lit;
        }
        return not ? "not " + expr : expr;
    }

    private static String value(String v, Kind kind, String edge) {
        return kind == Kind.DATE ? "datetime'" + v + (edge.equals("end") ? "T23:59:59" : "T00:00:00") + "'" : literal(v);
    }

    private static String literal(String v) {
        return "'" + v.replace("'", "''") + "'";
    }

    // ------------------------------------------------------------------------------------------------ text

    /** A readable text of the lines, e.g. {@code 100 – 200; 300; excl. 150}; empty when nothing is selected. */
    public static String describe(List<SelectOption> lines) {
        List<String> parts = new ArrayList<>();
        for (SelectOption l : lines == null ? Collections.<SelectOption>emptyList() : lines) {
            String low = trim(l.getLow());
            String high = trim(l.getHigh());
            if (low.isEmpty() && high.isEmpty()) {
                continue;
            }
            String option = trim(l.getOption()).toUpperCase();
            if (option.isEmpty()) {
                option = high.isEmpty() ? "EQ" : "BT";
            }
            String text;
            switch (option) {
                case "BT": text = high.isEmpty() ? low : low + " – " + high; break;
                case "NB": text = "not " + low + " – " + high; break;
                case "NE": text = "≠ " + low; break;
                case "GT": text = "> " + low; break;
                case "GE": text = "≥ " + low; break;
                case "LT": text = "< " + low; break;
                case "LE": text = "≤ " + (low.isEmpty() ? high : low); break;
                case "NP": text = "not " + low; break;
                default: text = low;
            }
            parts.add("E".equalsIgnoreCase(trim(l.getSign())) ? "excl. " + text : text);
        }
        return String.join("; ", parts);
    }

    /** As {@link #describe}, null when nothing is selected (for the Excel header). */
    public static String describeOrNull(List<SelectOption> lines) {
        String text = describe(lines);
        return text.isEmpty() ? null : text;
    }

    private static LocalDate date(String iso, String label) {
        try {
            return LocalDate.parse(iso);
        } catch (DateTimeParseException ex) {
            throw new LmsException(label + ": invalid date " + iso + " (expected yyyy-MM-dd).", HttpStatus.PRECONDITION_FAILED);
        }
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
