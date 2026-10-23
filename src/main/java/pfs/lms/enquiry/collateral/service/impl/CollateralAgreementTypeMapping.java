package pfs.lms.enquiry.collateral.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.collateral.batch.CollateralSapGatewayClient;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Which collateral agreement types (ZCMS_COL_AGMT_TYPE) may be chosen for each collateral object type
 * (ZCOLLATERAL_TYPE). The derivation (the F4 logic of COLAT_AGG_F4: object type → asset type → TCMS_CHG_TYP, with
 * the real-estate and guarantee special cases) runs in SAP and is read through the collateral gateway service
 * ZPFS_LMS_COLLATERAL_SRV, entity {@code CollateralObjectTypeAgreementType}, in <b>one</b> unfiltered call:
 * <pre>
 * GET …/CollateralObjectTypeAgreementTypeSet
 * </pre>
 * Every row carries the collateral object type ({@code CollObjectType}), the agreement type code ({@code CagmtType})
 * and its description ({@code CagmtTypeDesc}); the rows are grouped by object type. The property names are
 * configurable ({@code collateral.sap.agreement-type.*}); when a configured name is not in the response the matching
 * property is found by name pattern.
 * <p>
 * The whole table is cached for a few minutes (it is customizing, not transactional data), so the collateral
 * screen loads it once and switches the dropdown locally when the object type changes. When SAP is not configured
 * or does not answer, the methods return {@code null} and {@link #getLastFailure()} says why.
 */
@Component
public class CollateralAgreementTypeMapping {

    private static final Logger log = LoggerFactory.getLogger(CollateralAgreementTypeMapping.class);
    private static final String DEFAULT_SERVICE_PATH = "/sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV";

    private final ObjectProvider<CollateralSapGatewayClient> gatewayProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String servicePath;
    private final String entitySet;
    private final String objectTypeProperty;
    private final String codeProperty;
    private final String descriptionProperty;
    private final long cacheMillis;
    private volatile CacheEntry cache;
    private volatile String lastFailure;

    public CollateralAgreementTypeMapping(ObjectProvider<CollateralSapGatewayClient> gatewayProvider,
                                          @Value("${sap.collateralServiceUri:}") String servicePath,
                                          @Value("${collateral.sap.agreement-type.entity-set:CollateralObjectTypeAgreementTypeSet}") String entitySet,
                                          @Value("${collateral.sap.agreement-type.object-type-property:CollObjectType}") String objectTypeProperty,
                                          @Value("${collateral.sap.agreement-type.code-property:CagmtType}") String codeProperty,
                                          @Value("${collateral.sap.agreement-type.description-property:CagmtTypeDesc}") String descriptionProperty,
                                          @Value("${collateral.sap.agreement-type.cache-seconds:300}") long cacheSeconds) {
        this.gatewayProvider = gatewayProvider;
        String path = servicePath == null ? "" : servicePath.trim().replaceAll("/+$", "");
        if (path.isEmpty()) {
            log.warn("sap.collateralServiceUri is blank; defaulting agreement-type service path to {}", DEFAULT_SERVICE_PATH);
            path = DEFAULT_SERVICE_PATH;
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
        this.servicePath = path;
        this.entitySet = entitySet;
        this.objectTypeProperty = objectTypeProperty;
        this.codeProperty = codeProperty;
        this.descriptionProperty = descriptionProperty;
        this.cacheMillis = Math.max(0, cacheSeconds) * 1000;
    }

    /**
     * All agreement types SAP allows, by collateral object type: code and description as SAP returns them, in SAP's
     * order; an object type without rows is not in the map. {@code null} when SAP could not be asked (not configured,
     * unreachable, error, unreadable answer).
     */
    public Map<String, List<ValueEntryDto>> getAgreementTypesByObjectType() {
        CacheEntry cached = cache;
        long now = System.currentTimeMillis();
        if (cached != null && now - cached.readAt < cacheMillis) {
            return copy(cached.byObjectType);
        }
        Map<String, List<ValueEntryDto>> table = readFromSap();
        if (table != null) {
            cache = new CacheEntry(table, now);
        } else if (cached != null) {
            // SAP is down: keep serving the last good answer rather than dropping the restriction
            log.warn("Collateral agreement types: SAP did not answer, using the cached table");
            return copy(cached.byObjectType);
        }
        return copy(table);
    }

    /**
     * Agreement types SAP allows for one collateral object type (from the cached table). {@code null} when SAP could
     * not be asked or no object type is given; an empty list when SAP allows nothing for it.
     */
    public List<ValueEntryDto> getAgreementTypes(String collateralObjectType) {
        if (collateralObjectType == null || collateralObjectType.trim().isEmpty()) {
            return null;
        }
        Map<String, List<ValueEntryDto>> table = getAgreementTypesByObjectType();
        if (table == null) {
            return null;
        }
        List<ValueEntryDto> types = table.get(collateralObjectType.trim());
        return types != null ? types : new ArrayList<>();
    }

    /**
     * Agreement type codes allowed for the collateral object type, or an empty list when every agreement type is
     * allowed (also when SAP could not be asked).
     *
     * @param collateralObjectType collateral object type, e.g. "Z00007"
     */
    public List<String> getAllowedAgreementTypes(String collateralObjectType) {
        List<ValueEntryDto> types = getAgreementTypes(collateralObjectType);
        return types == null ? Collections.emptyList()
                : types.stream().map(ValueEntryDto::getCode).collect(Collectors.toList());
    }

    /** Why the last SAP read failed (for the error shown to the user); null when it succeeded. */
    public String getLastFailure() {
        return lastFailure;
    }

    /** Forgets the cached table (e.g. after customizing changed in SAP). */
    public void clearCache() {
        cache = null;
    }

    // ---------------------------------------------------------------------------------------------- SAP read

    private Map<String, List<ValueEntryDto>> readFromSap() {
        CollateralSapGatewayClient gateway = gatewayProvider.getIfAvailable();
        if (gateway == null || !gateway.isConfigured()) {
            log.warn("Collateral agreement types: SAP gateway not configured (sap.baseUrl)");
            lastFailure = "the SAP connection is not configured";
            return null;
        }
        CollateralSapGatewayClient.Result result = gateway.get(servicePath + "/" + entitySet);
        if (!result.isSuccess()) {
            log.warn("Collateral agreement types: SAP answered {}", result);
            lastFailure = "SAP answered " + result + (result.getBody() == null ? "" : ": " + abbreviate(result.getBody()));
            return null;
        }
        try {
            Map<String, List<ValueEntryDto>> table = parse(result.getBody());
            log.info("Collateral agreement types: SAP returned {} rows for {} collateral object types",
                    table.values().stream().mapToInt(List::size).sum(), table.size());
            lastFailure = null;
            return table;
        } catch (IOException | RuntimeException ex) {
            log.warn("Collateral agreement types: SAP answer not readable: {}", ex.getMessage());
            lastFailure = "the SAP answer could not be read (" + ex.getMessage() + ")";
            return null;
        }
    }

    /**
     * Reads the rows of an OData V2 answer ({@code d.results}, or {@code d} / {@code value} as an array) into
     * code/description pairs grouped by collateral object type, in SAP's order; a duplicate code within an object
     * type is kept once.
     */
    Map<String, List<ValueEntryDto>> parse(String body) throws IOException {
        Map<String, List<ValueEntryDto>> table = new LinkedHashMap<>();
        if (body == null || body.trim().isEmpty()) {
            return table;
        }
        JsonNode root = objectMapper.readTree(body);
        JsonNode rows = root.path("d").path("results");
        if (!rows.isArray()) {
            rows = root.path("d").isArray() ? root.path("d") : root.path("value");
        }
        if (!rows.isArray()) {
            // a single entity (key read) comes back as d.{…}
            rows = root.path("d").isObject() ? objectMapper.createArrayNode().add(root.path("d")) : rows;
        }
        String codeName = null;
        String descriptionName = null;
        String objectTypeName = null;
        for (JsonNode row : rows) {
            if (!row.isObject()) {
                continue;
            }
            if (codeName == null) {
                codeName = resolve(row, codeProperty, "code");
                descriptionName = resolve(row, descriptionProperty, "description");
                objectTypeName = resolve(row, objectTypeProperty, "objectType");
                if (codeName == null || objectTypeName == null) {
                    throw new IOException("no agreement type / object type property in " + fieldNames(row));
                }
            }
            String objectType = text(row, objectTypeName);
            String code = text(row, codeName);
            if (objectType == null || objectType.isEmpty() || code == null || code.isEmpty()) {
                continue;
            }
            String description = descriptionName == null ? null : text(row, descriptionName);
            List<ValueEntryDto> types = table.computeIfAbsent(objectType, k -> new ArrayList<>());
            if (types.stream().noneMatch(t -> t.getCode().equals(code))) {
                types.add(new ValueEntryDto(code, description == null || description.isEmpty() ? null : description));
            }
        }
        return table;
    }

    /**
     * The property to read: the configured name when the row has it, otherwise the first property whose name matches
     * the role (agreement-type code, its description, or the object type), ignoring case and underscores.
     */
    private static String resolve(JsonNode row, String configured, String role) {
        if (row.has(configured)) {
            return configured;
        }
        List<String> names = fieldNames(row);
        for (String name : names) {
            String n = name.toLowerCase(Locale.ROOT).replace("_", "");
            boolean descriptive = n.endsWith("desc") || n.endsWith("description") || n.endsWith("text")
                    || n.endsWith("txt") || n.endsWith("name") || n.endsWith("descr");
            switch (role) {
                case "code":
                    if (!descriptive && (n.contains("agmttype") || n.contains("agreementtype") || n.contains("agmt")
                            || n.contains("agreement") || n.contains("cagmt"))) {
                        return name;
                    }
                    break;
                case "description":
                    if (descriptive && (n.contains("agmt") || n.contains("agreement"))) {
                        return name;
                    }
                    break;
                default:
                    if (!descriptive && (n.contains("objecttype") || n.contains("collateraltype")
                            || n.contains("coltype") || n.contains("objtype") || n.contains("collobject"))) {
                        return name;
                    }
            }
        }
        if ("description".equals(role)) {
            for (String name : names) {   // any description-like property, e.g. "Name" or "Text"
                String n = name.toLowerCase(Locale.ROOT);
                if (n.endsWith("desc") || n.endsWith("description") || n.endsWith("text") || n.endsWith("name")) {
                    return name;
                }
            }
        }
        return null;
    }

    private static List<String> fieldNames(JsonNode row) {
        List<String> names = new ArrayList<>();
        for (Iterator<String> it = row.fieldNames(); it.hasNext(); ) {
            String name = it.next();
            if (!"__metadata".equals(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private static String text(JsonNode row, String name) {
        JsonNode node = row.get(name);
        return node == null || node.isNull() ? null : node.asText().trim();
    }

    /** RFC 3986 percent-encoding of a query value (space → %20, quote → %27), ASCII unreserved left as is. */
    static String percentEncode(String value) {
        StringBuilder out = new StringBuilder();
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int c = b & 0xff;
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '.' || c == '_' || c == '~') {
                out.append((char) c);
            } else {
                out.append('%').append(String.format("%02X", c));
            }
        }
        return out.toString();
    }

    private static String abbreviate(String text) {
        String oneLine = text.replaceAll("\\s+", " ").trim();
        return oneLine.length() <= 300 ? oneLine : oneLine.substring(0, 300) + "…";
    }

    private static Map<String, List<ValueEntryDto>> copy(Map<String, List<ValueEntryDto>> table) {
        if (table == null) {
            return null;
        }
        Map<String, List<ValueEntryDto>> copy = new LinkedHashMap<>();
        table.forEach((objectType, types) -> copy.put(objectType, types.stream()
                .map(t -> new ValueEntryDto(t.getCode(), t.getDescription())).collect(Collectors.toList())));
        return copy;
    }

    private static final class CacheEntry {
        final Map<String, List<ValueEntryDto>> byObjectType;
        final long readAt;

        CacheEntry(Map<String, List<ValueEntryDto>> byObjectType, long readAt) {
            this.byObjectType = byObjectType;
            this.readAt = readAt;
        }
    }
}
