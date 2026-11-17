package pfs.lms.enquiry.collateral.batch;

import org.springframework.stereotype.Component;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.domain.SapField;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Builds the OData JSON body and the key predicate for a collateral component from its {@code @SapField} annotations,
 * using the property names and types confirmed from the service $metadata.
 * <ul>
 *   <li>property name: {@link CollateralSapFieldNaming#propertyName(String)} (ZID_NO → ZidNo, …);</li>
 *   <li>dates (Edm.DateTime): OData V2 {@code /Date(ms)/} at UTC midnight;</li>
 *   <li>flags: real Edm.Boolean properties as {@code true}/{@code false}, the 1-char Edm.String flags as "X" / "";</li>
 *   <li>decimals: plain string; ZID_NO (Edm.String, length 10): zero-padded.</li>
 * </ul>
 * Fields without a {@code @SapField}, with an empty SAP name, or marked {@code longText} are left out; {@code null}
 * values are omitted (booleans are always sent).
 */
@Component
public class CollateralSapPayloadMapper {

    public static final String ZID_NO = "ZID_NO";
    public static final String ITEM_ID = "ITEM_ID";
    /** Loan contract number on the checklist header (ChecklistHeader.ZloanNumber, Edm.String 13, not nullable). */
    public static final String ZLOAN_NUMBER = "ZLOAN_NUMBER";
    /** Portal id of a child record (PtlId on Document, Coverage, SecPosition, Perf*; Edm.String 40, not nullable). */
    public static final String PTL_ID = "PTL_ID";
    /** Portal id of the collateral item on the header (ChecklistHeader.PortalId; Edm.String 40, not nullable). */
    public static final String PORTAL_ID = "PORTAL_ID";
    /** SAP checklist-number width (Edm.String, MaxLength 10). */
    private static final int ZID_NO_WIDTH = 10;

    public Map<String, Object> toPayload(Object entity, String entitySet) {
        Map<String, Object> payload = new LinkedHashMap<>();
        for (Class<?> type = entity.getClass(); type != null && type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                SapField sapField = field.getAnnotation(SapField.class);
                if (sapField == null || sapField.longText() || sapField.value().isEmpty()) {
                    continue;
                }
                String property = CollateralSapFieldNaming.propertyName(sapField.value());
                Object formatted = format(sapField.value(), property, read(field, entity));
                if (formatted == null) {
                    continue;
                }
                payload.putIfAbsent(property, formatted);
            }
        }
        return payload;
    }

    /** Sets the checklist number (ZID_NO, the collateral key), zero-padded, when not already present. */
    public void putChecklistNumber(Map<String, Object> payload, Long checklistIdNo) {
        if (checklistIdNo != null) {
            payload.put(CollateralSapFieldNaming.propertyName(ZID_NO), padZidNo(checklistIdNo.toString()));
        }
    }

    /** Sets the child's ItemId (Edm.Guid key) to the record's own id. */
    public void putItemId(Map<String, Object> payload, String itemGuid) {
        if (itemGuid != null && !itemGuid.isEmpty()) {
            payload.put(CollateralSapFieldNaming.propertyName(ITEM_ID), itemGuid);
        }
    }

    /**
     * Sets the loan contract number (ZLOAN_NUMBER → ZloanNumber) on the checklist header. SAP declares it not
     * nullable, so the header cannot be posted without it. The contract number is not a field of the collateral item
     * itself; it comes from the item's checklist / loan, so the caller supplies it.
     */
    public void putLoanNumber(Map<String, Object> payload, String loanContractId) {
        if (loanContractId != null && !loanContractId.trim().isEmpty()) {
            payload.put(CollateralSapFieldNaming.propertyName(ZLOAN_NUMBER), loanContractId.trim());
        }
    }

    /**
     * Sets the portal id of the entity being sent — its own portal UUID — so SAP can refer back to the portal record:
     * {@code PtlId} (PTL_ID) on every entity, and on the checklist header (the collateral item) additionally
     * {@code PortalId} (PORTAL_ID), which the header declares not nullable. The value always reflects the entity's id
     * (it overrides whatever the reflective mapping carried for those columns).
     *
     * @param header {@code true} for the checklist header (PTL_ID and PORTAL_ID), {@code false} for a child (PTL_ID)
     */
    public void putPortalId(Map<String, Object> payload, UUID entityId, boolean header) {
        if (entityId != null) {
            payload.put(CollateralSapFieldNaming.propertyName(PTL_ID), entityId.toString());
            if (header) {
                payload.put(CollateralSapFieldNaming.propertyName(PORTAL_ID), entityId.toString());
            }
        }
    }

    // ------------------------------------------------------------------------------- DocumentAttachment (media)

    /** The gateway entity set through which uploaded files are sent (media entity DocumentAttachment). */
    public static final String DOCUMENT_ATTACHMENT_SET = "DocumentAttachmentSet";

    /**
     * Entity set of the collateral item's long texts (entity type {@code CollateralItemLongText}): the four LONGTEXT
     * fields of {@link pfs.lms.enquiry.collateral.domain.CollateralItem} that are too long for the header. Keyed by
     * ZidNo only; always written with PUT after the header has been sent.
     */
    public static final String COLLATERAL_ITEM_LONG_TEXT_SET = "CollateralItemLongTextSet";
    /** Long-text properties of CollateralItemLongText in gateway spelling, in SEGW order. */
    public static final String LONG_TEXT_SECURITY_TYPE = "Securitytypetext";
    public static final String LONG_TEXT_STIPULATED_SECURITY = "Stipulatedsecurity";
    public static final String LONG_TEXT_COMPLIANCE_REMARKS_LEGAL = "Complianceremarkslegal";
    public static final String LONG_TEXT_ACTION_TAKEN = "Actiontaken";
    /** Key of DocumentAttachment: the portal document id, i.e. the vault file id (PTL_DOCU_ID, Edm.String 40). */
    public static final String PTL_DOCU_ID = "PTL_DOCU_ID";
    /** The main entity the file is attached to (BUSINESS_OBJECT_NAME, Edm.String 60). */
    public static final String BUSINESS_OBJECT_NAME = "BUSINESS_OBJECT_NAME";

    /**
     * DocumentAttachment properties that a child record's own payload already carries under the same name (BUKRS,
     * RANL, SERIAL_NO, DOC_TYPE, DOC_TYPE_DESC, DOC_TITLE, REMARKS, BDS_DOC_ID): copied across when present.
     */
    private static final String[] SHARED_ATTACHMENT_COLUMNS = {
            "BUKRS", "RANL", "SERIAL_NO", "DOC_TYPE", "DOC_TYPE_DESC", "DOC_TITLE", "REMARKS", "BDS_DOC_ID"};

    /**
     * The DocumentAttachment entity for a file uploaded to a collateral entity, built from the owner's own (already
     * mapped) payload:
     * <ul>
     * <li>{@code BusinessObjectName}: the owner's entity type ({@code ChecklistHeader}, {@code Document}, …);</li>
     * <li>{@code ZidNo}: the checklist number, as on the owner; {@code ItemId}: the owner record's GUID;</li>
     * <li>a child record's Bukrs, Ranl, SerialNo, DocType, DocTypeDesc, DocTitle, Remarks and BdsDocId as they are;
     * for the checklist header (its post-execution document) DocType / DocTitle come from PostExecDocType /
     * PostExecDocTitle and Ranl from ZloanNumber;</li>
     * <li>{@code PtlId}: the owner's portal id; {@code PtlDocuId} (the key): the vault file id.</li>
     * </ul>
     *
     * @param target       the owner's entity set
     * @param ownerPayload the owner's mapped payload (as sent for the owner itself)
     * @param ownerId      the owner record's id (portal id)
     * @param fileId       the vault file id of the uploaded document (the attachment's key)
     */
    /**
     * Payload of {@code CollateralItemLongTextSet} for a collateral item: {@code ZidNo} (the checklist number, as on
     * the header payload) and the four long texts {@code Securitytypetext}, {@code Stipulatedsecurity},
     * {@code Complianceremarkslegal} and {@code Actiontaken}. Every text is always present — an empty string when the
     * portal field is blank — so that a PUT clears a text that was removed in the portal rather than leaving the old
     * value in SAP. Returns {@code null} when the header payload carries no ZidNo (nothing to key the text on).
     */
    public Map<String, Object> toLongTextPayload(CollateralItem item, Map<String, Object> headerPayload) {
        Object zidNo = headerPayload == null ? null : headerPayload.get(CollateralSapFieldNaming.propertyName(ZID_NO));
        if (zidNo == null || zidNo.toString().isEmpty()) {
            return null;
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(CollateralSapFieldNaming.propertyName(ZID_NO), zidNo.toString());
        payload.put(LONG_TEXT_SECURITY_TYPE, text(item.getSecurityTypeText()));
        payload.put(LONG_TEXT_STIPULATED_SECURITY, text(item.getStipulatedSecurity()));
        payload.put(LONG_TEXT_COMPLIANCE_REMARKS_LEGAL, text(item.getComplianceRemarksLegal()));
        payload.put(LONG_TEXT_ACTION_TAKEN, text(item.getActionTaken()));
        return payload;
    }

    private static String text(String value) {
        return value == null ? "" : value;
    }

    public Map<String, Object> toAttachmentPayload(CollateralSapEntitySet target, Map<String, Object> ownerPayload,
                                                   UUID ownerId, UUID fileId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(CollateralSapFieldNaming.propertyName(BUSINESS_OBJECT_NAME), target.getEntityType());

        String zidNo = CollateralSapFieldNaming.propertyName(ZID_NO);
        if (ownerPayload.get(zidNo) != null) {
            payload.put(zidNo, ownerPayload.get(zidNo));
        }
        putItemId(payload, ownerId != null ? ownerId.toString() : null);

        if (target.isHeader()) {
            copy(ownerPayload, "POST_EXEC_DOC_TYPE", payload, "DOC_TYPE");
            copy(ownerPayload, "POST_EXEC_DOC_TITLE", payload, "DOC_TITLE");
            copy(ownerPayload, ZLOAN_NUMBER, payload, "RANL");
        } else {
            for (String column : SHARED_ATTACHMENT_COLUMNS) {
                copy(ownerPayload, column, payload, column);
            }
        }

        putPortalId(payload, ownerId, false);
        if (fileId != null) {
            payload.put(CollateralSapFieldNaming.propertyName(PTL_DOCU_ID), fileId.toString());
        }
        return payload;
    }

    /**
     * The key properties of DocumentAttachment in their SEGW order with their maximum lengths — every property except
     * BdsDocId is a key, so that the gateway hands all of them to CREATE_STREAM / UPDATE_STREAM in {@code it_key_tab}.
     * ItemId is the Edm.Guid key (length not applicable).
     */
    private static final String[][] ATTACHMENT_KEYS = {
            {"BusinessObjectName", "60"}, {"ZidNo", "10"}, {"ItemId", "guid"}, {"Bukrs", "4"}, {"Ranl", "13"},
            {"SerialNo", "10"}, {"DocType", "10"}, {"DocTypeDesc", "40"}, {"DocTitle", "100"}, {"Remarks", "60"},
            {"PtlId", "40"}, {"PtlDocuId", "40"}};

    private static final String NIL_GUID = "00000000-0000-0000-0000-000000000000";

    /**
     * The OData key predicate of a DocumentAttachment with <em>all</em> its key properties, e.g.
     * {@code (BusinessObjectName='Document',ZidNo='0000000912',ItemId=guid'…',Bukrs='1000',…,PtlDocuId='…')}. A key
     * whose value is missing is sent as an empty string ({@code ''}); a missing ItemId as the nil GUID. Values are
     * cut to the property's maximum length, an inner quote is doubled (OData escaping) and the text is percent-encoded
     * so that spaces, {@code /}, {@code &} and non-ASCII characters are safe in a URL path; the delimiters stay as
     * they are. The resulting path is a valid URI and is used as is (not re-encoded).
     */
    public String attachmentKeyPredicate(Map<String, Object> attachmentPayload) {
        StringBuilder predicate = new StringBuilder("(");
        for (String[] key : ATTACHMENT_KEYS) {
            String name = key[0];
            Object raw = attachmentPayload.get(name);
            String value = raw == null ? "" : raw.toString().trim();
            if (predicate.length() > 1) {
                predicate.append(',');
            }
            predicate.append(name).append('=');
            if ("guid".equals(key[1])) {
                predicate.append("guid'").append(value.isEmpty() ? NIL_GUID : value).append('\'');
            } else {
                int max = Integer.parseInt(key[1]);
                if (value.length() > max) {
                    value = value.substring(0, max);
                }
                predicate.append('\'').append(percentEncode(value.replace("'", "''"))).append('\'');
            }
        }
        return predicate.append(')').toString();
    }

    /** Slug entry carrying the file name (not an entity property). */
    public static final String SLUG_FILE_NAME = "FileName";

    /**
     * The {@code Slug} header for creating a DocumentAttachment through CREATE_STREAM: the entity's properties and the
     * file name as {@code Name=value;Name=value;…}, in the order of {@link #toAttachmentPayload}, with each value
     * percent-encoded (UTF-8) so that {@code ;}, {@code =}, spaces and non-ASCII characters in a title or file name
     * cannot break the format. The names are the OData property names (BusinessObjectName, ZidNo, ItemId, Bukrs, Ranl,
     * SerialNo, DocType, DocTypeDesc, DocTitle, Remarks, BdsDocId, PtlId, PtlDocuId) plus {@value #SLUG_FILE_NAME}.
     * On the ABAP side: split at {@code ;}, then at the first {@code =}, and decode each value with
     * {@code cl_http_utility=>unescape_url}.
     */
    public String attachmentSlug(Map<String, Object> attachmentPayload, String fileName) {
        StringBuilder slug = new StringBuilder();
        for (Map.Entry<String, Object> entry : attachmentPayload.entrySet()) {
            if (entry.getValue() != null) {
                appendSlugEntry(slug, entry.getKey(), entry.getValue().toString());
            }
        }
        if (fileName != null && !fileName.trim().isEmpty()) {
            appendSlugEntry(slug, SLUG_FILE_NAME, fileName.trim());
        }
        return slug.toString();
    }

    private static void appendSlugEntry(StringBuilder slug, String name, String value) {
        if (slug.length() > 0) {
            slug.append(';');
        }
        slug.append(name).append('=').append(percentEncode(value));
    }

    /** RFC 3986 percent-encoding (UTF-8); unreserved characters stay as they are, a space becomes %20. */
    static String percentEncode(String value) {
        StringBuilder out = new StringBuilder();
        for (byte b : value.getBytes(java.nio.charset.StandardCharsets.UTF_8)) {
            int c = b & 0xFF;
            boolean unreserved = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '~';
            if (unreserved) {
                out.append((char) c);
            } else {
                out.append('%').append(String.format("%02X", c));
            }
        }
        return out.toString();
    }

    private static void copy(Map<String, Object> from, String fromColumn, Map<String, Object> to, String toColumn) {
        Object value = from.get(CollateralSapFieldNaming.propertyName(fromColumn));
        if (value != null) {
            to.put(CollateralSapFieldNaming.propertyName(toColumn), value);
        }
    }

    /**
     * OData key predicate with every key property of the entity set, e.g.
     * {@code (ZidNo='0000000912',ItemId=guid'1a2b…',PtlId='1a2b…')}. Edm.Guid keys use the {@code guid'…'} literal,
     * Edm.String keys the quoted form; values are percent-encoded. A key that is empty is still sent: an Edm.String
     * key as a space ({@code ' '}), the Edm.Guid key ItemId as the nil GUID — both arrive in SAP as an initial value.
     * Returns {@code null} only when the checklist number (ZID_NO) itself is missing, since no row can be addressed
     * without it.
     */
    public String keyPredicate(Map<String, Object> payload, String entitySet, String[] keyColumns) {
        StringBuilder predicate = new StringBuilder("(");
        for (int i = 0; i < keyColumns.length; i++) {
            String column = keyColumns[i];
            String property = CollateralSapFieldNaming.propertyName(column);
            Object raw = payload.get(property);
            String value = raw == null ? "" : raw.toString().trim();
            if (value.isEmpty() && ZID_NO.equals(column)) {
                return null;
            }
            if (i > 0) {
                predicate.append(',');
            }
            predicate.append(property).append('=');
            if (ITEM_ID.equals(column)) {
                predicate.append("guid'").append(value.isEmpty() ? NIL_GUID : percentEncode(value)).append('\'');
            } else {
                predicate.append('\'').append(value.isEmpty() ? "%20" : percentEncode(value.replace("'", "''"))).append('\'');
            }
        }
        return predicate.append(')').toString();
    }

    private static Object read(Field field, Object entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read " + field.getName(), e);
        }
    }

    /** SAP/OData value formatting by property type; returns null when the value should be omitted. */
    static Object format(String sapColumn, String property, Object value) {
        if (value instanceof Boolean) {
            if (CollateralSapFieldNaming.isBooleanProperty(property)) {
                return value;                       // Edm.Boolean -> true/false
            }
            return ((Boolean) value) ? "X" : "";    // 1-char Edm.String flag
        }
        if (value == null) {
            return null;
        }
        if (ZID_NO.equals(sapColumn)) {
            return padZidNo(value.toString().trim());
        }
        if (value instanceof LocalDate) {
            return sapDate(((LocalDate) value).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        }
        if (value instanceof Date) {
            return sapDate(((Date) value).getTime());
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if (value instanceof Number) {
            return value.toString();
        }
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }

    /** OData V2 date literal, e.g. {@code /Date(1772668800000)/}. */
    private static String sapDate(long epochMillis) {
        return "/Date(" + epochMillis + ")/";
    }

    private static String padZidNo(String value) {
        String trimmed = value.trim();
        if (trimmed.isEmpty() || !trimmed.chars().allMatch(Character::isDigit) || trimmed.length() >= ZID_NO_WIDTH) {
            return trimmed;
        }
        StringBuilder padded = new StringBuilder();
        for (int i = trimmed.length(); i < ZID_NO_WIDTH; i++) {
            padded.append('0');
        }
        return padded.append(trimmed).toString();
    }
}
