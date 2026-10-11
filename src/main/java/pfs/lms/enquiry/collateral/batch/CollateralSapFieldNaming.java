package pfs.lms.enquiry.collateral.batch;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Names and simple type facts of the gateway OData service ZPFS_LMS_COLLATERAL_SRV, confirmed against its $metadata.
 * <p>
 * The OData property name is a deterministic transform of the SAP column on the {@code @SapField} annotation: each
 * underscore-separated token becomes first-letter-upper, rest-lower, concatenated — ZID_NO → ZidNo, VALID_FROM_DATE →
 * ValidFromDate, ITEM_ID → ItemId. This matches every mapped field in the service, so no per-field table is needed; a
 * column may still be overridden in {@link #OVERRIDES} if a future field deviates.
 * <p>
 * Most SAP flag fields are 1-character {@code Edm.String} ("X" / ""); the few that are real {@code Edm.Boolean}
 * (true/false) are listed in {@link #BOOLEAN_PROPERTIES}.
 */
public final class CollateralSapFieldNaming {

    /** SAP column -> OData property, for the rare field whose name is not the standard transform. Normally empty. */
    private static final Map<String, String> OVERRIDES = new HashMap<>();

    /** OData properties typed {@code Edm.Boolean} (sent as true/false); all other flags are 1-char "X"/"". */
    private static final Set<String> BOOLEAN_PROPERTIES = new HashSet<>();

    static {
        BOOLEAN_PROPERTIES.add("ZapplEryDisbur");
        BOOLEAN_PROPERTIES.add("PenalChgAppl");
        BOOLEAN_PROPERTIES.add("Zrecurring");
        BOOLEAN_PROPERTIES.add("WaivedOff");
        BOOLEAN_PROPERTIES.add("ZwaiPossible");
    }

    private CollateralSapFieldNaming() {
    }

    /** OData property name for a SAP column. */
    public static String propertyName(String sapColumn) {
        String override = OVERRIDES.get(sapColumn);
        if (override != null && !override.isEmpty()) {
            return override;
        }
        StringBuilder name = new StringBuilder();
        for (String token : sapColumn.split("_")) {
            if (token.isEmpty()) {
                continue;
            }
            name.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1).toLowerCase());
        }
        return name.toString();
    }

    /** True when the OData property is {@code Edm.Boolean} and must be sent as true/false rather than "X"/"". */
    public static boolean isBooleanProperty(String property) {
        return BOOLEAN_PROPERTIES.contains(property);
    }

    static Map<String, String> overrides() {
        return Collections.unmodifiableMap(OVERRIDES);
    }
}
