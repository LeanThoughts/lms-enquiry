package pfs.lms.enquiry.reports.disbursement;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Column;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Report;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Row;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Loans Disbursement - Periodwise and Comparison Report (Reports › Loans Management): the SAP report read through
 * the gateway entity set {@code DisbursementCompRptSet} of ZPFS_LMS_REPORTS_SRV. Every filterable property of the entity
 * is a field of the selection screen and is sent as a {@code $filter} term — empty when not filled in: Bukrs, Mode,
 * GjahrFrom, GjahrTo, PeriodType, PeriodFilter, Ranl, Partner (ranges), ProjectType, AllLoans and SuppressZero.
 * <p>
 * The result comes as cells (RowNo, Fieldlabel, Value). The portal turns it into a table: one row per RowNo, one column
 * per Fieldlabel (in the order they first appear), the Value in the cell.
 */
@Service
public class DisbursementService {

    private final SapReportsClient client;
    private final String entitySet;

    public DisbursementService(SapReportsClient client,
                               @Value("${reports.disbursement.entity-set:DisbursementCompRptSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        String filter = filter(selection);
        List<JsonNode> cells;
        Report report;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Loans Disbursement Report", null, null)) {
            cells = client.read(entitySet, filter);
        }
        report = pivot(cells);
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The selection as an OData V2 filter expression (unencoded); every field is present, empty when not filled. */
    static String filter(Selection s) {
        Selection sel = s == null ? new Selection() : s;
        List<String> terms = new ArrayList<>();
        terms.add("Bukrs eq " + literal(trim(sel.companyCode).isEmpty() ? "PFS" : trim(sel.companyCode).toUpperCase()));
        terms.add("Mode eq " + literal(trim(sel.mode).isEmpty() ? "R" : trim(sel.mode).toUpperCase()));
        terms.add("GjahrFrom eq " + literal(year(sel.fiscalYearFrom, "Fiscal Year")));
        terms.add("GjahrTo eq " + literal(year(sel.fiscalYearTo, "Fiscal Year to")));
        terms.add("PeriodType eq " + literal(trim(sel.periodType).toUpperCase()));
        terms.add("PeriodFilter eq " + literal(trim(sel.periodFilter).toUpperCase()));
        terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Loan Contract"));
        terms.add(SelectOptions.filter("Partner", sel.partner, Kind.TEXT, "Business Partner Id"));
        terms.add("ProjectType eq " + literal(trim(sel.projectType)));
        terms.add("AllLoans eq " + sel.allLoans);
        terms.add("SuppressZero eq " + sel.suppressZero);
        return String.join(" and ", terms);
    }

    private static String year(String value, String label) {
        String v = trim(value);
        if (!v.isEmpty() && !v.matches("\\d{4}")) {
            throw new LmsException(label + ": " + v + " is not a year (yyyy).", HttpStatus.PRECONDITION_FAILED);
        }
        return v;
    }

    private static String literal(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    // ------------------------------------------------------------------------------------------------ pivot

    /**
     * Cells to a table: rows by RowNo (ascending), columns by Fieldlabel in the order of first appearance (after the
     * cells are put in RowNo order); a cell sent twice keeps the last Value.
     */
    static Report pivot(List<JsonNode> cells) {
        TreeMap<Long, List<JsonNode>> byRow = new TreeMap<>();
        long fallback = Long.MAX_VALUE / 2;
        for (JsonNode cell : cells) {
            JsonNode no = cell.get("RowNo");
            long rowNo = no != null && no.canConvertToLong() ? no.asLong()
                    : no != null && no.asText().trim().matches("-?\\d+") ? Long.parseLong(no.asText().trim()) : fallback++;
            byRow.computeIfAbsent(rowNo, k -> new ArrayList<>()).add(cell);
        }
        Map<String, String> keyByLabel = new LinkedHashMap<>();
        List<Column> columns = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        for (List<JsonNode> rowCells : byRow.values()) {
            Row row = new Row();
            for (JsonNode cell : rowCells) {
                String label = text(cell, "Fieldlabel");
                label = label == null ? "" : label;
                String key = keyByLabel.get(label);
                if (key == null) {
                    key = "c" + columns.size();
                    keyByLabel.put(label, key);
                    columns.add(new Column(key, label));
                }
                row.put(key, text(cell, "Value"));
            }
            rows.add(row);
        }
        Report report = new Report();
        report.setColumns(columns);
        report.setRows(rows);
        report.setTotal(rows.size());
        return report;
    }

    private static String text(JsonNode node, String property) {
        JsonNode value = node.get(property);
        if (value == null || value.isNull()) {
            return null;
        }
        String text = value.asText().trim();
        return text.isEmpty() ? null : text;
    }
}
