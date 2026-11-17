package pfs.lms.enquiry.reports.kyc;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.kyc.KycReportDtos.Report;
import pfs.lms.enquiry.reports.kyc.KycReportDtos.Row;
import pfs.lms.enquiry.reports.kyc.KycReportDtos.Selection;
import pfs.lms.enquiry.reports.workflowstatus.SapReportsClient;
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
 * The Loans KYC Report (Reports › Loans Management): the SAP report "Loans KYC Report" read through the gateway
 * entity set {@code KycReportSet} of ZPFS_LMS_REPORTS_SRV. Every field of the selection screen is sent as a
 * {@code $filter} term — empty when not filled in — on the entity's filterable properties: ContractNo, PartnerId,
 * Status (ranges), RunDate, IncludeAuthSign, NonZeroEffCapOnly and DateKycCompl (range). The company code is not a
 * property of the entity (the ABAP uses PFS); it is only shown on the screen.
 */
@Service
public class KycReportService {

    private static final Pattern ODATA_DATE = Pattern.compile("/Date\\((-?\\d+)(?:[+-]\\d+)?\\)/");

    /** Output properties of the entity, in the order of the result table (SEGW names, incl. "Complaince"). */
    static final String[] OUTPUT = {
            "ContractNo", "Status", "SerialNo", "PartnerId", "PartnerName", "Role", "ProjectName", "LoanAgmtDate",
            "KycStatus", "KycComplainceDate", "ReKycComplainceDate", "CkycComplDate", "CkycRefNo", "Pan", "PanNumber",
            "Coi", "Moa", "Aoa", "Cin", "Lei", "AddrProof", "BoardRes", "AuthSignatory", "KycRiskCategoryDesc",
            "ReKycRiskCategoryDesc"
    };

    /** The dates among the output properties. */
    private static final List<String> DATES = java.util.Arrays.asList(
            "LoanAgmtDate", "KycComplainceDate", "ReKycComplainceDate", "CkycComplDate");

    private final SapReportsClient client;
    private final String entitySet;

    public KycReportService(SapReportsClient client,
                            @Value("${reports.kyc.entity-set:KycReportSet}") String entitySet) {
        this.client = client;
        this.entitySet = entitySet;
    }

    public Report run(Selection selection) {
        String filter = filter(selection);
        Report report = new Report();
        report.setGeneratedOn(Instant.now().toString());
        report.setQuery(client.url(entitySet, filter));
        List<JsonNode> rows;
        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Reports", "Loans KYC Report", null, null)) {
            rows = client.read(entitySet, filter);
        }
        report.setRows(rows.stream().map(KycReportService::row).collect(Collectors.toList()));
        report.setTotal(report.getRows().size());
        return report;
    }

    // ------------------------------------------------------------------------------------------------ $filter

    /** The selection as an OData V2 filter expression (unencoded); every field is present, empty when not filled. */
    static String filter(Selection s) {
        Selection sel = s == null ? new Selection() : s;
        List<String> terms = new ArrayList<>();
        terms.add(SelectOptions.filter("ContractNo", sel.contract, Kind.TEXT, "Loan Contract"));
        terms.add(SelectOptions.filter("PartnerId", sel.partner, Kind.TEXT, "Bus. Partner Id"));
        terms.add(SelectOptions.filter("Status", sel.status, Kind.TEXT, "Loan Contract Status"));
        LocalDate run = date(sel.runDate, "Run Date");
        terms.add("RunDate eq datetime'" + (run != null ? run : LocalDate.now()) + "T00:00:00'");
        terms.add("IncludeAuthSign eq " + sel.includeAuthSignatories);
        terms.add("NonZeroEffCapOnly eq " + sel.effCapOnly);
        // an empty date selection is the lowest .. highest date: an OData datetime literal cannot be empty
        terms.add(SelectOptions.filter("DateKycCompl", sel.kycDate, Kind.DATE, "KYC/Re-KYC Compliance Date"));
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

    /** A row with the output properties under their lower-camel-case names (ContractNo → contractNo). */
    static Row row(JsonNode n) {
        Row r = new Row();
        for (String property : OUTPUT) {
            // SEGW spells the two compliance dates "Complaince"; the portal keys use the correct spelling.
            String key = Character.toLowerCase(property.charAt(0)) + property.substring(1).replace("Complaince", "Compliance");
            r.put(key, DATES.contains(property) ? isoDate(n, property) : text(n, property));
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

    /** An Edm.DateTime ({@code /Date(ms)/} or ISO) as an ISO date; SAP's initial date as null. */
    static String isoDate(JsonNode node, String property) {
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
