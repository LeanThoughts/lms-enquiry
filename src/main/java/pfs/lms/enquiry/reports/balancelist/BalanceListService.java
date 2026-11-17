package pfs.lms.enquiry.reports.balancelist;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.SelectOptions.Kind;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Report;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Row;
import pfs.lms.enquiry.reports.balancelist.BalanceListDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Loans Balance List (Reports › Loans Management): the SAP report "Balance List" (FNR7) read through the
 * gateway entity set {@code ZFNR7ReportSet} of ZPFS_LMS_REPORTS_SRV. Every field of the selection screen is sent as a
 * {@code $filter} term — empty when not filled in — on the filterable properties: Bukrs (fixed PFS), Ranl, Gsart,
 * Stitel, Santwhr, Rrefkont, Rbear (ranges), PaInteb, PaStich, PaAnzw, FlgSald, PaAll and PaSumme. The ALV / Classic
 * list switches and the layout / variant of the SAP screen are not used: PPrNew is sent true, PPrOld false.
 */
@Service
public class BalanceListService {

    /** Output properties of the entity (SEGW names), in the order of the result table. */
    static final String[] OUTPUT = {
            "Ranl", "Rantrag", "Bukrs", "Gsart", "Santwhr", "Rrefkont", "Moratorium", "Status", "ProjectName", "Anzwhr",
            "SanctionedAmt", "Bzusage", "Nomkap", "Effkap", "Tilgng", "Zinsen", "Versch", "Ueberz", "Briwr", "Bvzwr",
            "Bvkwr", "Rbear"
    };

    private static final List<String> BOOLEANS = java.util.Collections.singletonList("Moratorium");

    private final SapReportsClient client;
    private final String entitySet;
    /** The entity has no property named like "Loan Type"; the property it is matched to is configurable. */
    private final String productTypeProperty;
    private final String loanTypeProperty;

    public BalanceListService(SapReportsClient client,
                              @Value("${reports.balance-list.entity-set:ZFNR7ReportSet}") String entitySet,
                              @Value("${reports.balance-list.product-type-property:Gsart}") String productTypeProperty,
                              @Value("${reports.balance-list.loan-type-property:Stitel}") String loanTypeProperty) {
        this.client = client;
        this.entitySet = entitySet;
        this.productTypeProperty = productTypeProperty;
        this.loanTypeProperty = loanTypeProperty;
    }

    public Report run(Selection selection) {
        String filter = filter(selection, productTypeProperty, loanTypeProperty);
        Report report = new Report();
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        List<JsonNode> rows;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Loans Balance List", null, null)) {
            rows = client.read(entitySet, filter);
        }
        report.setRows(rows.stream().map(BalanceListService::row).collect(Collectors.toList()));
        report.setTotal(report.getRows().size());
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    static String filter(Selection s) {
        return filter(s, "Gsart", "Stitel");
    }

    /** The selection as an OData V2 filter expression (unencoded); every field is present, empty when not filled. */
    static String filter(Selection s, String productTypeProperty, String loanTypeProperty) {
        Selection sel = s == null ? new Selection() : s;
        List<String> terms = new ArrayList<>();
        terms.add("Bukrs eq 'PFS'");
        terms.add(SelectOptions.filter("Ranl", sel.contract, Kind.TEXT, "Contract Number"));
        terms.add(SelectOptions.filter(productTypeProperty, sel.productType, Kind.TEXT, "Product Type"));
        terms.add(SelectOptions.filter(loanTypeProperty, sel.loanType, Kind.TEXT, "Loan Type"));
        terms.add(SelectOptions.filter("Santwhr", sel.currency, Kind.TEXT, "Contract Currency"));
        terms.add(SelectOptions.filter("Rrefkont", sel.acctRef, Kind.TEXT, "Acct Assignment Ref."));
        terms.add(SelectOptions.filter("Rbear", sel.changedBy, Kind.TEXT, "Last Changed by"));
        terms.add("PaInteb eq " + sel.evaluateCompleted);
        LocalDate key = date(sel.keyDate, "Key date");
        terms.add("PaStich eq datetime'" + (key != null ? key : LocalDate.now()) + "T00:00:00'");
        terms.add("PaAnzw eq " + literal(trim(sel.displayCurrency).toUpperCase()));
        terms.add("FlgSald eq " + sel.nonZeroOnly);
        terms.add("PaAll eq " + sel.totalsForZero);
        terms.add("PaSumme eq " + sel.totalsOnly);
        terms.add("PPrNew eq true");
        terms.add("PPrOld eq false");
        return String.join(" and ", terms);
    }

    private static String literal(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static LocalDate date(String iso, String label) {
        if (trim(iso).isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(iso.trim());
        } catch (DateTimeParseException ex) {
            throw new LmsException(label + ": invalid date " + iso + " (expected yyyy-MM-dd).", HttpStatus.PRECONDITION_FAILED);
        }
    }

    // ------------------------------------------------------------------------------------------------ rows

    /** A row with the output properties under their lower-camel-case names (Ranl → ranl). */
    static Row row(JsonNode n) {
        Row r = new Row();
        for (String property : OUTPUT) {
            String key = Character.toLowerCase(property.charAt(0)) + property.substring(1);
            r.put(key, BOOLEANS.contains(property) ? flag(n, property) : text(n, property));
        }
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

    /** An Edm.Boolean as "X" when set, null otherwise (like an ABAP checkbox). */
    private static String flag(JsonNode node, String property) {
        JsonNode value = node.get(property);
        boolean set = value != null && !value.isNull()
                && (value.isBoolean() ? value.asBoolean() : "true".equalsIgnoreCase(value.asText()) || "X".equalsIgnoreCase(value.asText()));
        return set ? "X" : null;
    }
}
