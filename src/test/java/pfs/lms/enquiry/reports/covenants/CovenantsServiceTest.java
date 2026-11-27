package pfs.lms.enquiry.reports.covenants;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.reports.SelectOption;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Column;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Report;
import pfs.lms.enquiry.reports.covenants.CovenantsDtos.Selection;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Covenants Report: the $filter, the result table and the Excel file. */
@DisplayName("Covenants Report")
class CovenantsServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    private static SelectOption line(String option, String low, String high) {
        SelectOption line = new SelectOption();
        line.setSign("I");
        line.setOption(option);
        line.setLow(low);
        line.setHigh(high);
        return line;
    }

    @Test
    @DisplayName("filter: company code always; the loan contract range only when entered")
    void filter() {
        Selection selection = new Selection();
        assertThat(CovenantsService.filter(selection)).isEqualTo("Bukrs eq 'PFS'");
        selection.companyCode = " pfs ";
        selection.contract = new ArrayList<>(Arrays.asList(line("BT", "0000010003814", "0000010003900")));
        assertThat(CovenantsService.filter(selection))
                .isEqualTo("Bukrs eq 'PFS' and Ranl ge '0000010003814' and Ranl le '0000010003900'");
    }

    @Test
    @DisplayName("result: business order, company code and covenant GUID hidden, dates, percentages, new properties last")
    void result() throws Exception {
        JsonNode rows = JSON.readTree("[{\"__metadata\":{\"id\":\"x\"},\"Bukrs\":\"PFS\",\"Ranl\":\"0000010003814\","
                + "\"ProjectName\":\"Solar Park \",\"MaxPenalCharges\":\"2.00\",\"SerialNo\":\"0001\",\"CovId\":\"AFBWi0x2Hu+TnwAAAAAAAQ==\","
                + "\"CovTypeDesc\":\"Financial\",\"ValidFrom\":\"/Date(1735689600000)/\",\"ValidTo\":null,"
                + "\"NextMonitoringDate\":\"/Date(1767225600000)/\",\"PenalChgPct\":\"0.50\",\"NewField\":\"A\"}]");
        List<JsonNode> list = new ArrayList<>();
        rows.forEach(list::add);
        Report report = CovenantsService.toReport(list);

        List<String> keys = report.getColumns().stream().map(Column::getKey).collect(Collectors.toList());
        assertThat(keys.subList(0, 3)).containsExactly("SerialNo", "Ranl", "ProjectName");
        assertThat(keys).doesNotContain("Bukrs", "CovId", "__metadata");
        assertThat(keys.get(keys.size() - 1)).isEqualTo("NewField");
        assertThat(report.getRows().get(0))
                .containsEntry("SerialNo", "1")
                .containsEntry("ProjectName", "Solar Park")
                .containsEntry("MaxPenalCharges", "2")
                .containsEntry("PenalChgPct", "0.5")
                .containsEntry("ValidFrom", "2025-01-01")
                .containsEntry("ValidTo", null)
                .containsEntry("NextMonitoringDate", "2026-01-01")
                .doesNotContainKey("CovId");
    }

    @Test
    @DisplayName("Excel: selection block, dates as dd.MM.yyyy")
    void excel() throws Exception {
        Selection selection = new Selection();
        selection.contract = new ArrayList<>(Arrays.asList(line("EQ", "0000010003814", "")));
        List<Column> columns = Arrays.asList(new Column("Ranl", "Contract number", "text"), new Column("ValidFrom", "Valid from", "date"));
        java.util.Map<String, String> row = new java.util.LinkedHashMap<>();
        row.put("Ranl", "0000010003814");
        row.put("ValidFrom", "2025-01-01");
        byte[] bytes = new CovenantsExcelWriter().write(selection, Arrays.asList(row), columns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertThat(sheet.getSheetName()).isEqualTo("Covenants Report");
            StringBuilder text = new StringBuilder();
            sheet.forEach(r -> r.forEach(c -> text.append(c.toString()).append('|')));
            assertThat(text.toString()).contains("Loan Contract").contains("0000010003814").contains("01.01.2025");
        }
    }
}
