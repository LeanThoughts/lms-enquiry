package pfs.lms.enquiry.reports.covenants;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Column;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Report;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Covenants Report (Reports › Loans Management): the covenants of the loans read through the gateway entity set
 * {@code CovenantsReportSet} of ZPFS_LMS_REPORTS_SRV, one row per covenant. The selection screen is sent as the
 * filterable properties of the entity: the company code (Bukrs, always) and the loan contract range (Ranl, only when
 * filled in).
 * <p>
 * The columns are the properties of the entity in business order with readable headings; the company code and the
 * technical covenant GUID are not shown, and a property that SAP sends and that is not listed here is added at the end,
 * so a new field of the entity shows without a portal change.
 */
@Service
public class CovenantsService {

    /** The properties of CovenantsReport in the order of the result table, with heading and kind. */
    static final List<Column> COLUMNS = Arrays.asList(
            new Column("SerialNo", "Serial no.", "number"),
            new Column("Ranl", "Contract number", "text"),
            new Column("ProjectName", "Project name", "text"),
            new Column("CovType", "Covenant type code", "text"),
            new Column("CovTypeDesc", "Covenant type", "text"),
            new Column("CovCategory", "Covenant category code", "text"),
            new Column("CovCategoryDesc", "Covenant category", "text"),
            new Column("CovValueType", "Covenant value type", "text"),
            new Column("CovKeyFigure", "Key figure code", "text"),
            new Column("CovKeyFigureDesc", "Key figure", "text"),
            new Column("CovenantOperator", "Operator", "text"),
            new Column("CovenantValueFrom", "Covenant value from", "text"),
            new Column("CovenantValueTo", "Covenant value to", "text"),
            new Column("ValidFrom", "Valid from", "date"),
            new Column("ValidTo", "Valid to", "date"),
            new Column("ContractConseqType", "Consequence type code", "text"),
            new Column("ContractConseqTypeDesc", "Consequence type", "text"),
            new Column("MaxPenalCharges", "Max penal charge %", "number"),
            new Column("PenalChgPct", "Penal charges %", "number"),
            new Column("MonitoringFrequency", "Invocation frequency", "text"),
            new Column("MonitoringPeriod", "Invocation period code", "text"),
            new Column("MonitoringPeriodDesc", "Invocation period", "text"),
            new Column("LastMonitoringDate", "Last invocation date", "date"),
            new Column("NextMonitoringDate", "Next invocation date", "date"),
            new Column("Remarks", "Remarks", "text"));

    /** Selection-only properties of the entity and the technical covenant GUID (Edm.Binary): not shown as columns. */
    static final Set<String> HIDDEN = new HashSet<>(Arrays.asList("Bukrs", "CovId"));

    private static final Pattern ODATA_DATE = Pattern.compile("^/Date\\((-?\\d+)([+-]\\d{4})?\\)/$");

    private final SapReportsClient client;
    private final String entitySet;

    public CovenantsService(SapReportsClient client,
                                     @Value("${reports.covenants.entity-set:CovenantsReportSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        Selection sel = selection == null ? new Selection() : selection;
        String filter = filter(sel);
        List<JsonNode> results;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Covenants Report", null, null)) {
            results = client.read(entitySet, filter);
        }
        Report report = toReport(results);
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** "Bukrs eq 'PFS'", and the loan contract range when one is entered. */
    static String filter(Selection sel) {
        List<String> terms = new ArrayList<>();
        String companyCode = sel.companyCode == null || sel.companyCode.trim().isEmpty() ? "PFS" : sel.companyCode.trim().toUpperCase();
        terms.add("Bukrs eq '" + companyCode.replace("'", "''") + "'");
        if (sel.contract != null && !SelectOptions.normalize(sel.contract, Kind.TEXT, "Loan Contract").isEmpty()) {
            terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Loan Contract"));
        }
        return String.join(" and ", terms);
    }

    // ------------------------------------------------------------------------------------------------ result

    /** The rows with their values as text; the known columns first, then any further property SAP sent. */
    static Report toReport(List<JsonNode> results) {
        Map<String, Column> columns = new LinkedHashMap<>();
        COLUMNS.forEach(column -> columns.put(column.getKey(), column));
        List<Map<String, String>> rows = new ArrayList<>();
        for (JsonNode result : results) {
            Map<String, String> row = new LinkedHashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = result.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                if (key.startsWith("__") || HIDDEN.contains(key) || field.getValue().isObject() || field.getValue().isArray()) {
                    continue;          // __metadata, navigation properties
                }
                Column column = columns.computeIfAbsent(key, k -> new Column(k, heading(k), kind(field.getValue())));
                row.put(key, value(field.getValue(), column.getType()));
            }
            rows.add(row);
        }
        Report report = new Report();
        report.setColumns(new ArrayList<>(columns.values()));
        report.setRows(rows);
        report.setTotal(rows.size());
        return report;
    }

    /** A value as text: Edm.DateTime as yyyy-MM-dd, Edm.Boolean as Yes / No, decimals without SAP's padding, texts trimmed; empty as null. */
    static String value(JsonNode node, String type) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isBoolean()) {
            return node.asBoolean() ? "Yes" : "No";            // Edm.Boolean
        }
        String text = node.asText().trim();
        if (text.isEmpty()) {
            return null;
        }
        Matcher date = ODATA_DATE.matcher(text);
        if (date.matches()) {
            return Instant.ofEpochMilli(Long.parseLong(date.group(1))).atZone(ZoneOffset.UTC).toLocalDate().toString();
        }
        if (("amount".equals(type) || "number".equals(type)) && text.matches("-?\\d+(\\.\\d+)?")) {
            BigDecimal number = new BigDecimal(text);
            if ("amount".equals(type)) {
                return number.setScale(Math.max(2, Math.min(number.stripTrailingZeros().scale(), 3)), BigDecimal.ROUND_HALF_UP).toPlainString();
            }
            return number.stripTrailingZeros().toPlainString();
        }
        return text;
    }

    private static String kind(JsonNode value) {
        String text = value == null ? "" : value.asText();
        if (ODATA_DATE.matcher(text).matches()) {
            return "date";
        }
        return value != null && value.isNumber() ? "number" : "text";
    }

    /** "ZcolNewField" → "Zcol new field" (for properties the portal does not know yet) */
    static String heading(String property) {
        String words = property.replaceAll("([a-z0-9])([A-Z])", "$1 $2").replace('_', ' ').trim().toLowerCase();
        return words.isEmpty() ? property : Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }
}
