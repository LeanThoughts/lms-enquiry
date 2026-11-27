package pfs.lms.enquiry.reports.portfoliodebt;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Column;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Report;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Portfolio Debt Listing (Reports › Loans Management): the SAP Loans Debt Listing Report read through the gateway
 * entity set {@code PortfolioDebtListingReportSet} of ZPFS_LMS_REPORTS_SRV, one row per loan. The selection screen has
 * the loan contract range (Ranl, sent only when filled in) and the run date (RunDate, the filterable property; today
 * when empty). The company code of the SAP screen is fixed (PFS) and not a property of the entity.
 * <p>
 * The columns are the properties of the entity in business order with readable headings; a property that SAP sends
 * and that is not listed here is added at the end, so a new field of the entity shows without a portal change.
 */
@Service
public class PortfolioDebtService {

    /** The properties of PortfolioDebtListingReport in the order of the result table, with heading and kind. */
    static final List<Column> COLUMNS = Arrays.asList(
            new Column("SerialNo", "Serial no.", "number"),
            new Column("Ranl", "Contract number", "text"),
            new Column("FacilityNumber", "Facility number", "text"),
            new Column("IsFacility", "Is facility", "text"),
            new Column("BusinessPartnerId", "Business partner", "text"),
            new Column("ProjectName", "Project name", "text"),
            new Column("BusinessPartnerIdGroup", "Group company partner", "text"),
            new Column("GroupName", "Group company (sponsor)", "text"),
            new Column("BusinessPartnerIdPromoter", "Promoter partner", "text"),
            new Column("Promoter", "Promoter name", "text"),
            new Column("TypeOfLoan", "Loan term", "text"),
            new Column("SanctionDate", "Sanction date", "date"),
            new Column("SanctionValidityDate", "Sanction validity date", "date"),
            new Column("SanctionYear", "Sanction year", "text"),
            new Column("SanctionQuarter", "Sanction quarter", "text"),
            new Column("Status", "Compliance status", "text"),
            new Column("Location", "Location", "text"),
            new Column("Region", "Region", "text"),
            new Column("Sector", "Project nature (sector)", "text"),
            new Column("SubSector", "Sub sector (project type)", "text"),
            new Column("CorporateProject", "Corporate / project", "text"),
            new Column("Capacity", "Project capacity", "number"),
            new Column("BaseUnitOfMeasure", "Unit of measure", "text"),
            new Column("GrossSanction", "Gross sanction amount", "amount"),
            new Column("EffectiveSanction", "Effective sanction amount", "amount"),
            new Column("DownSoldAmt", "Change in sanction amount (down-sold)", "amount"),
            new Column("AmountDisbursed", "Amount disbursed", "amount"),
            new Column("BalanceCommitment", "Balance commitment", "amount"),
            new Column("AmountOsRptDate", "Outstanding on run date", "amount"),
            new Column("AmountOsPrevQRptDate", "Outstanding at previous quarter end", "amount"),
            new Column("DisbAmtUptoPrevFyear", "Disbursed up to previous FY", "amount"),
            new Column("AmountDisbQ1RptFy", "Disbursed in Q1", "amount"),
            new Column("AmountDisbQ2RptFy", "Disbursed in Q2", "amount"),
            new Column("AmountDisbQ3RptFy", "Disbursed in Q3", "amount"),
            new Column("AmountDisbQ4RptFy", "Disbursed in Q4", "amount"),
            new Column("DisbInRptFy", "Disbursed in FY", "amount"),
            new Column("InternalRiskGrading", "Internal risk grade", "text"),
            new Column("OriginalRiskGradeSanction", "Risk grade at sanction", "text"),
            new Column("RevisedCurrentRiskGrade", "Current risk grade", "text"),
            new Column("RunDate", "Run date", "date"));

    private static final Pattern ODATA_DATE = Pattern.compile("^/Date\\((-?\\d+)([+-]\\d{4})?\\)/$");

    private final SapReportsClient client;
    private final String entitySet;

    public PortfolioDebtService(SapReportsClient client,
                                @Value("${reports.portfolio-debt-listing.entity-set:PortfolioDebtListingReportSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        Selection sel = selection == null ? new Selection() : selection;
        LocalDate runDate = runDate(sel.runDate);
        String filter = filter(sel, runDate);
        List<JsonNode> results;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Portfolio Debt Listing", null, null)) {
            results = client.read(entitySet, filter);
        }
        Report report = toReport(results);
        report.setRunDate(runDate.toString());
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** "RunDate eq datetime'2026-10-31T00:00:00'", and the loan contract range when one is entered. */
    static String filter(Selection sel, LocalDate runDate) {
        List<String> terms = new ArrayList<>();
        if (sel.contract != null && !SelectOptions.normalize(sel.contract, Kind.TEXT, "Loan Contract").isEmpty()) {
            terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Loan Contract"));
        }
        terms.add("RunDate eq datetime'" + runDate + "T00:00:00'");
        return String.join(" and ", terms);
    }

    static LocalDate runDate(String value) {
        String text = value == null ? "" : value.trim();
        if (text.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException ex) {
            throw new LmsException("Run Date: " + text + " is not a date (yyyy-MM-dd).", HttpStatus.PRECONDITION_FAILED);
        }
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
                if (key.startsWith("__") || field.getValue().isObject() || field.getValue().isArray()) {
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

    /** A value as text: Edm.DateTime as yyyy-MM-dd, decimals without SAP's padding, texts trimmed; empty as null. */
    static String value(JsonNode node, String type) {
        if (node == null || node.isNull()) {
            return null;
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

    /** "AmountOsRptDate" → "Amount os rpt date" (for properties the portal does not know yet) */
    static String heading(String property) {
        String words = property.replaceAll("([a-z0-9])([A-Z])", "$1 $2").replace('_', ' ').trim().toLowerCase();
        return words.isEmpty() ? property : Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }
}
