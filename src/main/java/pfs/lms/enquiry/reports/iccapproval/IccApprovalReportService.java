package pfs.lms.enquiry.reports.iccapproval;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.Report;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.Row;
import pfs.lms.enquiry.reports.iccapproval.IccApprovalDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The ICC Approval Status Report (Reports › Loans Management): the SAP report "Loans : ICC Approval Status Report"
 * read through the gateway entity set {@code MISIccApprovalStatRptSet} (entity type MISIccApprovalStatRpt) of ZPFS_LMS_REPORTS_SRV. Its filterable
 * properties are Fyear (fiscal year), Quarter and Ranl (contract number); like the Workflow Status Report every
 * one of them is always sent, empty when not filled in, so SAP always receives the complete filter. The company
 * code of the selection screen is not a property of the entity and is not sent.
 */
@Service
public class IccApprovalReportService {

    private static final Pattern ODATA_DATE = Pattern.compile("/Date\\((-?\\d+)(?:[+-]\\d+)?\\)/");

    private final SapReportsClient client;
    private final String entitySet;

    public IccApprovalReportService(SapReportsClient client,
                                    @Value("${reports.icc-approval.entity-set:MISIccApprovalStatRptSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        String filter = filter(selection);
        Report report = new Report();
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        List<JsonNode> rows;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "ICC Approval Status Report", null, null)) {
            rows = client.read(entitySet, filter);
        }
        report.setRows(rows.stream().map(IccApprovalReportService::row).collect(Collectors.toList()));
        report.setTotal(report.getRows().size());
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The selection as an OData V2 filter expression (unencoded); every field is present, empty when not filled. */
    static String filter(Selection s) {
        Selection sel = s == null ? new Selection() : s;
        List<String> terms = new ArrayList<>();
        terms.add("Fyear eq " + literal(trim(sel.fiscalYear)));
        terms.add("Quarter eq " + literal(trim(sel.quarter)));
        terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Loan Contract"));
        return String.join(" and ", terms);
    }


    private static String literal(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    // ------------------------------------------------------------------------------------------------ rows

    static Row row(JsonNode n) {
        Row r = new Row();
        r.setFyear(text(n, "Fyear"));
        r.setSerialNo(text(n, "SerialNo"));
        r.setQuarter(text(n, "Quarter"));
        r.setRanl(text(n, "Ranl"));
        r.setStatus(text(n, "Status"));
        r.setBupaId(text(n, "BupaId"));
        r.setBorrowerName(text(n, "BorrowerName"));
        r.setProjectName(text(n, "ProjectName"));
        r.setZzsanctionedAmt(decimal(n, "ZzsanctionedAmt"));
        r.setCommitCapital(decimal(n, "CommitCapital"));
        r.setLoanType(text(n, "LoanType"));
        r.setSector(text(n, "Sector"));
        r.setProjectType(text(n, "ProjectType"));
        r.setGrpCompName(text(n, "GrpCompName"));
        r.setFeePct(decimal(n, "FeePct"));
        r.setRoi(decimal(n, "Roi"));
        r.setIccStatusDesc(text(n, "IccStatusDesc"));
        r.setIccApprDate(date(n, "IccApprDate"));
        r.setZiccRemarks(text(n, "ZiccRemarks"));
        r.setBodDate(date(n, "BodDate"));
        r.setBodStatusDesc(text(n, "BodStatusDesc"));
        r.setZboardAppRemarks(text(n, "ZboardAppRemarks"));
        return r;
    }

    private static String text(JsonNode node, String property) {
        JsonNode value = node.get(property);
        if (value == null || value.isNull()) {
            return null;
        }
        String text = value.asText().trim();
        return text.isEmpty() ? null : text;
    }

    /** An Edm.Decimal (SAP sends it as a string) with two decimals; null when absent. */
    static String decimal(JsonNode node, String property) {
        String text = text(node, property);
        if (text == null) {
            return null;
        }
        try {
            return new BigDecimal(text).setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (NumberFormatException ex) {
            return text;
        }
    }

    /** An Edm.DateTime ({@code /Date(ms)/} or ISO) as an ISO date; SAP's initial date as null. */
    static String date(JsonNode node, String property) {
        String text = text(node, property);
        if (text == null) {
            return null;
        }
        Matcher m = ODATA_DATE.matcher(text);
        if (m.matches()) {
            long millis = Long.parseLong(m.group(1));
            if (millis <= 0) {
                return null;
            }
            return LocalDateTime.ofEpochSecond(millis / 1000, 0, ZoneOffset.UTC).toLocalDate().toString();
        }
        String iso = text.length() >= 10 ? text.substring(0, 10) : text;
        return iso.startsWith("0000") || iso.startsWith("0001") ? null : iso;
    }
}
