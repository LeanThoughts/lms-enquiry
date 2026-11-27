package pfs.lms.enquiry.reports.portfoliodebt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOption;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Column;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Report;
import pfs.lms.enquiry.reports.portfoliodebt.PortfolioDebtDtos.Selection;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Portfolio Debt Listing: the $filter, the result table and the Excel file. */
@DisplayName("Portfolio Debt Listing")
class PortfolioDebtServiceTest {

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
    @DisplayName("filter: run date as Edm.DateTime; the loan contract range only when entered")
    void filter() {
        Selection selection = new Selection();
        assertThat(PortfolioDebtService.filter(selection, LocalDate.of(2026, 10, 31)))
                .isEqualTo("RunDate eq datetime'2026-10-31T00:00:00'");
        selection.contract = new ArrayList<>(Arrays.asList(line("BT", "0000010003814", "0000010003900")));
        assertThat(PortfolioDebtService.filter(selection, LocalDate.of(2026, 10, 31)))
                .isEqualTo("Ranl ge '0000010003814' and Ranl le '0000010003900' and RunDate eq datetime'2026-10-31T00:00:00'");
        assertThat(PortfolioDebtService.runDate("")).isEqualTo(LocalDate.now());
        assertThatThrownBy(() -> PortfolioDebtService.runDate("31.10.2026")).isInstanceOf(LmsException.class)
                .hasMessageContaining("not a date");
    }

    @Test
    @DisplayName("result: business column order, dates, amounts, and properties SAP adds later at the end")
    void result() throws Exception {
        JsonNode rows = JSON.readTree("[{\"__metadata\":{\"id\":\"x\"},\"SerialNo\":\"0001\",\"Ranl\":\"0000010003814\","
                + "\"ProjectName\":\"Solar Park \",\"SanctionDate\":\"/Date(1735689600000)/\",\"SanctionValidityDate\":null,"
                + "\"GrossSanction\":\"1250000000.000\",\"AmountDisbursed\":\"0.000\",\"Capacity\":\"100.00\","
                + "\"RunDate\":\"/Date(1793404800000)/\",\"NewField\":\"A\"}]");
        List<JsonNode> results = new ArrayList<>();
        rows.forEach(results::add);
        Report report = PortfolioDebtService.toReport(results);

        List<String> keys = report.getColumns().stream().map(Column::getKey).collect(Collectors.toList());
        assertThat(keys.subList(0, 3)).containsExactly("SerialNo", "Ranl", "FacilityNumber");
        assertThat(keys.get(keys.size() - 1)).isEqualTo("NewField");
        assertThat(report.getColumns().get(keys.size() - 1).getLabel()).isEqualTo("New field");
        assertThat(keys).doesNotContain("__metadata");
        assertThat(report.getRows().get(0))
                .containsEntry("SerialNo", "1")
                .containsEntry("ProjectName", "Solar Park")
                .containsEntry("SanctionDate", "2025-01-01")
                .containsEntry("SanctionValidityDate", null)
                .containsEntry("GrossSanction", "1250000000.00")
                .containsEntry("AmountDisbursed", "0.00")
                .containsEntry("Capacity", "100")
                .containsEntry("RunDate", "2026-10-31");
        assertThat(report.getTotal()).isEqualTo(1);
    }

    @Test
    @DisplayName("Excel: selection block, amounts as numbers, dates as dd.MM.yyyy, ids keep their zeros")
    void excel() throws Exception {
        Selection selection = new Selection();
        selection.runDate = "2026-10-31";
        List<Column> columns = Arrays.asList(new Column("Ranl", "Contract number", "text"),
                new Column("SanctionDate", "Sanction date", "date"), new Column("GrossSanction", "Gross sanction amount", "amount"));
        java.util.Map<String, String> row = new java.util.LinkedHashMap<>();
        row.put("Ranl", "0000010003814");
        row.put("SanctionDate", "2025-01-01");
        row.put("GrossSanction", "1250.50");
        byte[] bytes = new PortfolioDebtExcelWriter().write(selection, Arrays.asList(row), columns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertThat(sheet.getRow(0).getCell(0).getStringCellValue()).isEqualTo("Portfolio Debt Listing");
            assertThat(sheet.getRow(3).getCell(1).getStringCellValue()).isEqualTo("31.10.2026");
            int header = sheet.getLastRowNum() - 1;
            assertThat(sheet.getRow(header).getCell(2).getStringCellValue()).isEqualTo("Gross sanction amount");
            assertThat(sheet.getRow(header + 1).getCell(0).getStringCellValue()).isEqualTo("0000010003814");
            assertThat(sheet.getRow(header + 1).getCell(1).getStringCellValue()).isEqualTo("01.01.2025");
            assertThat(sheet.getRow(header + 1).getCell(2).getNumericCellValue()).isEqualTo(1250.5);
        }
    }
}
