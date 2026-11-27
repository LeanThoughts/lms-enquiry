package pfs.lms.enquiry.reports.collateralsecurity;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Column;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Report;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Selection;
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
 * Collateral Securities Report (Reports › Collaterals): the SAP Collateral Securities Report read through the gateway
 * entity set {@code CollateralSecurityReportSet} of ZPFS_LMS_REPORTS_SRV, one row per collateral condition of a loan.
 * The selection screen is sent as the filterable properties of the entity: company code (Ccode), loan number (RRanl),
 * loan partner (RGpart) and the compliance filter checkboxes (Co, Nc, Ip, Pc, Ta). Every one is always sent, an empty
 * select-option as {@code eq ''} (all), like the other reports of the service.
 * <p>
 * The columns are the properties of the entity in business order with readable headings; the selection properties
 * are not shown, and a property that SAP sends and that is not listed here is added at the end, so a new field of the
 * entity shows without a portal change.
 */
@Service
public class CollateralSecurityService {

    /** The properties of CollateralSecurityReport in the order of the result table, with heading and kind. */
    static final List<Column> COLUMNS = Arrays.asList(
            new Column("ZslNo", "Serial no.", "number"),
            new Column("Ranl", "Contract number", "text"),
            new Column("ZloanAcName", "Project name", "text"),
            new Column("ZborrowerCode", "Borrower", "text"),
            new Column("ZpartyName", "Borrower name", "text"),
            new Column("EclClassificationDesc", "ECL stage", "text"),
            new Column("ZidNo", "Checklist ID no.", "text"),
            new Column("ZcagmtType", "Agreement type", "text"),
            new Column("ZconditionGrp", "Condition group", "text"),
            new Column("ZconditionDesc", "Condition description", "text"),
            new Column("ZcollateralTypeDesc", "Collateral object type", "text"),
            new Column("ZcolSecType", "Collateral object description", "text"),
            new Column("ZdocType", "Document type", "text"),
            new Column("Zcompliance", "Compliance", "text"),
            new Column("ZcomplianceText", "Compliance status", "text"),
            new Column("ZcomplianceDate", "Compliance date", "date"),
            new Column("ZobjId", "Object ID", "text"),
            new Column("ZobjDesc", "Object description", "text"),
            new Column("ZcolTimelinesText", "Timelines", "text"),
            new Column("ZcolActDaysPrfx", "Action days prefix code", "text"),
            new Column("ZcolActDaysPrfxTxt", "Action days prefix", "text"),
            new Column("ZcolActDays", "Action period", "text"),
            new Column("ZcolActDaysSuffix", "Action days suffix code", "text"),
            new Column("ZcolActDaysSuffixTxt", "Action days suffix", "text"),
            new Column("ZcolEvent", "Event code", "text"),
            new Column("ZcolEventTxt", "Collateral event", "text"),
            new Column("ZcolEventDate", "Event date", "date"),
            new Column("ZcolTimelineDate", "Timeline date", "date"),
            new Column("RocSecuritySatisfactionDate", "RoC security satisfaction date", "date"),
            new Column("CersaiSatisfactionDate", "CERSAI satisfaction date", "date"),
            new Column("NeslSatisfactionDate", "NeSL satisfaction date", "date"),
            new Column("WaiverDate", "Waiver date", "date"),
            new Column("PenalChgAppl", "Penal charges applicable", "text"),
            new Column("PenalChgPct", "Penal charges %", "number"),
            new Column("PenalChgWaiverPct", "Penal charges waiver %", "number"),
            new Column("ZcolValPeriod", "Validity period", "text"),
            new Column("ZcolExpValue", "Expected value", "text"),
            new Column("ZcolSecPerDate", "Security perfection date", "date"),
            new Column("ZcolRemarks", "Remarks", "text"),
            new Column("ZcolLocation", "Location", "text"),
            new Column("ZcolAddtlText", "Additional text", "text"),
            new Column("ZsecNoOfUnits", "No. of units", "number"),
            new Column("ZsecValue", "Nominal value", "amount"),
            new Column("ZsecValueCurr", "Currency", "text"),
            new Column("ZsecPctHolding", "Holding %", "number"),
            new Column("ZreArea", "Total land area", "number"),
            new Column("ZreAreaUom", "Area unit", "text"),
            new Column("CreatedAt", "Created on", "date"));

    /** Selection-only properties of the entity (and the technical GUID): not shown as columns. */
    static final Set<String> HIDDEN = new HashSet<>(Arrays.asList("Ccode", "RRanl", "RGpart", "Co", "Nc", "Ip", "Pc", "Ta",
            "CagGuid"));

    private static final Pattern ODATA_DATE = Pattern.compile("^/Date\\((-?\\d+)([+-]\\d{4})?\\)/$");

    private final SapReportsClient client;
    private final String entitySet;

    public CollateralSecurityService(SapReportsClient client,
                                     @Value("${reports.collateral-securities.entity-set:CollateralSecurityReportSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        Selection sel = selection == null ? new Selection() : selection;
        String filter = filter(sel);
        List<JsonNode> results;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Collateral Securities Report", null, null)) {
            results = client.read(entitySet, filter);
        }
        Report report = toReport(results);
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The selection as an OData V2 filter expression (unencoded); every selection property is present. */
    static String filter(Selection sel) {
        List<String> terms = new ArrayList<>();
        String companyCode = sel.companyCode == null || sel.companyCode.trim().isEmpty() ? "PFS" : sel.companyCode.trim().toUpperCase();
        terms.add("Ccode eq '" + companyCode.replace("'", "''") + "'");
        terms.add(SelectOptions.filter("RRanl", sel.contract, Kind.TEXT, "Loan Number"));
        terms.add(SelectOptions.filter("RGpart", sel.partner, Kind.TEXT, "Loan Partner"));
        terms.add("Co eq " + sel.complied);
        terms.add("Nc eq " + sel.notComplied);
        terms.add("Ip eq " + sel.inProcess);
        terms.add("Pc eq " + sel.partiallyComplied);
        terms.add("Ta eq " + sel.timelineAvailable);
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
            return node.asBoolean() ? "Yes" : "No";            // Edm.Boolean (penal charges applicable)
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
