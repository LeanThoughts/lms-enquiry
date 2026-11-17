package pfs.lms.enquiry.reports.workflowstatus;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.Report;
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.Row;
import pfs.lms.enquiry.reports.workflowstatus.WorkflowStatusDtos.Selection;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Workflow Status Report (Reports › Miscellaneous): the SAP report "Loans: Workflow Status Report" read through
 * the gateway entity set {@code WorkflowStatusSet} of ZPFS_LMS_REPORTS_SRV. The selection screen becomes an OData
 * {@code $filter} on the entity's filterable properties — Bukrs (company code), Relobj (release object), Partner and
 * Datum ranges — plus the Ranl (contract number) range; the rows come back with SAP's dates as ISO dates and times as
 * HH:mm:ss. Sorting is done by the user in the browser (every column).
 */
@Service
public class WorkflowStatusReportService {

    private static final Pattern ODATA_DATE = Pattern.compile("/Date\\((-?\\d+)(?:[+-]\\d+)?\\)/");
    private static final Pattern ODATA_TIME = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?");

    private final SapReportsClient client;
    private final String entitySet;

    public WorkflowStatusReportService(SapReportsClient client,
                                       @Value("${reports.workflow-status.entity-set:WorkflowStatusSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        String filter = filter(selection);
        Report report = new Report();
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        List<JsonNode> rows;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Workflow Status Report", null, null)) {
            rows = client.read(entitySet, filter);
        }
        report.setRows(rows.stream().map(WorkflowStatusReportService::row).collect(Collectors.toList()));
        report.setTotal(report.getRows().size());
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The selection as an OData V2 filter expression (unencoded), or null when nothing is selected. */
    static String filter(Selection s) {
        // Every selection field is always sent, empty when not filled, so SAP always receives the full filter.
        Selection sel = s == null ? new Selection() : s;
        List<String> terms = new ArrayList<>();
        terms.add("Bukrs eq " + literal(notBlank(sel.companyCode) ? sel.companyCode.trim().toUpperCase() : ""));
        terms.add("Relobj eq " + literal(notBlank(sel.releaseObject) ? sel.releaseObject.trim().toUpperCase() : ""));
        terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Contract Number"));
        terms.add(SelectOptions.filter("Partner", sel.partner, Kind.TEXT, "Business Partner"));
        // an empty date selection is the lowest .. highest date: an OData datetime literal cannot be empty
        terms.add(SelectOptions.filter("Datum", sel.date, Kind.DATE, "Date"));
        return String.join(" and ", terms);
    }

    private static String literal(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // ------------------------------------------------------------------------------------------------ rows

    static Row row(JsonNode n) {
        Row r = new Row();
        r.setRowId(text(n, "RowId"));
        r.setBukrs(text(n, "Bukrs"));
        r.setRelobj(text(n, "Relobj"));
        r.setPartner(text(n, "Partner"));
        r.setDatum(date(n, "Datum"));
        r.setRelObject(text(n, "RelObject"));
        r.setRanl(text(n, "Ranl"));
        r.setParnter(text(n, "Parnter"));
        r.setBoId(text(n, "BoId"));
        r.setInitiatorId(text(n, "InitiatorId"));
        r.setInitiatorPosition(text(n, "InitiatorPosition"));
        r.setInitiatorName(text(n, "InitiatorName"));
        r.setInitiationDate(date(n, "InitiationDate"));
        r.setInitiationTime(time(n, "InitiationTime"));
        r.setWfReferenceNo(text(n, "WfReferenceNo"));
        r.setTaskText(text(n, "TaskText"));
        r.setApproverId(text(n, "ApproverId"));
        r.setApproverPosition(text(n, "ApproverPosition"));
        r.setApproverName(text(n, "ApproverName"));
        r.setApprovalDate(date(n, "ApprovalDate"));
        r.setApprovalTime(time(n, "ApprovalTime"));
        r.setReleaseStatus(text(n, "ReleaseStatus"));
        r.setReleaseStatusDesc(text(n, "ReleaseStatusDesc"));
        r.setReleaseIndicator(text(n, "ReleaseIndicator"));
        r.setReleaseIndicatorDesc(text(n, "ReleaseIndicatorDesc"));
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

    /** An Edm.DateTime ({@code /Date(ms)/} or ISO) as an ISO date; SAP's initial date (0000-00-00 / 1970) as null. */
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
        return text.length() >= 10 ? text.substring(0, 10) : text;
    }

    /** An Edm.Time ({@code PT09H06M12S}) as HH:mm:ss; an initial time (PT00H00M00S) as null. */
    static String time(JsonNode node, String property) {
        String text = text(node, property);
        if (text == null) {
            return null;
        }
        Matcher m = ODATA_TIME.matcher(text);
        if (m.matches()) {
            int h = m.group(1) == null ? 0 : Integer.parseInt(m.group(1));
            int min = m.group(2) == null ? 0 : Integer.parseInt(m.group(2));
            int sec = m.group(3) == null ? 0 : Integer.parseInt(m.group(3));
            if (h == 0 && min == 0 && sec == 0) {
                return null;
            }
            return String.format("%02d:%02d:%02d", h, min, sec);
        }
        return text;
    }
}
