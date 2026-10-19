package pfs.lms.enquiry.reports.disbursement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Report;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DisbursementServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    private static JsonNode cell(int rowNo, String label, String value) throws Exception {
        return JSON.readTree("{\"RowNo\":" + rowNo + ",\"Fieldlabel\":\"" + label + "\",\"Value\":" + (value == null ? "null" : "\"" + value + "\"") + "}");
    }

    @Test
    void everyParameterIsSent() {
        Selection s = new Selection();
        s.companyCode = "pfs";
        s.fiscalYearFrom = "2026";
        s.contract = SelectOptions.parse("[{\"sign\":\"I\",\"option\":\"BT\",\"low\":\"1000\",\"high\":\"2000\"}]");
        s.projectType = "Solar";
        s.suppressZero = false;
        assertThat(DisbursementService.filter(s)).isEqualTo("Bukrs eq 'PFS' and Mode eq 'R' and GjahrFrom eq '2026' and GjahrTo eq '' "
                + "and PeriodType eq 'M' and PeriodFilter eq '' and Ranl ge '1000' and Ranl le '2000' and Partner eq '' "
                + "and ProjectType eq 'Solar' and AllLoans eq true and SuppressZero eq false");
    }

    @Test
    void comparativeAndYearRange() {
        Selection s = new Selection();
        s.mode = "c";
        s.fiscalYearFrom = "2025";
        s.fiscalYearTo = "2026";
        assertThat(DisbursementService.filter(s)).contains("Mode eq 'C'", "GjahrFrom eq '2025'", "GjahrTo eq '2026'");
    }

    @Test
    void invalidYearIsRejected() {
        Selection s = new Selection();
        s.fiscalYearFrom = "26";
        assertThatThrownBy(() -> DisbursementService.filter(s)).isInstanceOf(LmsException.class).hasMessageContaining("Fiscal Year");
    }

    @Test
    void cellsBecomeOneRowPerRowNoAndOneColumnPerFieldlabel() throws Exception {
        List<JsonNode> cells = new ArrayList<>(Arrays.asList(
                cell(2, "Loan Contract", "0000010003900"), cell(1, "Loan Contract", "0000010003814"),
                cell(1, "Apr-2026", "1,000.00"), cell(1, "May-2026", "250.00"),
                cell(2, "May-2026", "75.50"), cell(2, "Apr-2026", null)));
        Report r = DisbursementService.pivot(cells);
        assertThat(r.getTotal()).isEqualTo(2);
        assertThat(r.getColumns()).extracting(DisbursementDtos.Column::getLabel).containsExactly("Loan Contract", "Apr-2026", "May-2026");
        assertThat(r.getColumns()).extracting(DisbursementDtos.Column::getKey).containsExactly("c0", "c1", "c2");
        assertThat(r.getRows().get(0).get("c0")).isEqualTo("0000010003814");     // RowNo 1 first, whatever the order sent
        assertThat(r.getRows().get(0).get("c2")).isEqualTo("250.00");
        assertThat(r.getRows().get(1).get("c0")).isEqualTo("0000010003900");
        assertThat(r.getRows().get(1).get("c1")).isNull();
        assertThat(r.getRows().get(1).get("c2")).isEqualTo("75.50");
    }

    @Test
    void amountColumnsAreRecognised() {
        List<java.util.Map<String, String>> rows = new ArrayList<>();
        rows.add(java.util.Collections.singletonMap("a", "1,25,000.50"));
        rows.add(java.util.Collections.singletonMap("a", "12.50-"));
        assertThat(DisbursementExcelWriter.isAmountColumn(rows, "a")).isTrue();
        assertThat(DisbursementExcelWriter.parse("12.50-").toPlainString()).isEqualTo("-12.50");
        rows.add(java.util.Collections.singletonMap("b", "0000010003814"));
        assertThat(DisbursementExcelWriter.isAmountColumn(rows, "b")).isFalse();
    }

    @Test
    void emptyResultIsAnEmptyTable() {
        Report r = DisbursementService.pivot(new ArrayList<>());
        assertThat(r.getRows()).isEmpty();
        assertThat(r.getColumns()).isEmpty();
    }
}
