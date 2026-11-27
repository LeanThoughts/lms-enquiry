package pfs.lms.enquiry.reports.collateralsecurity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pfs.lms.enquiry.reports.SelectOption;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Column;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Report;
import pfs.lms.enquiry.reports.collateralsecurity.CollateralSecurityDtos.Selection;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Collateral Securities Report: the $filter, the result table and the Excel file. */
@DisplayName("Collateral Securities Report")
class CollateralSecurityServiceTest {

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
    @DisplayName("filter: company code, loan number / partner select-options (empty = all) and the five checkboxes")
    void filter() {
        Selection selection = new Selection();
        assertThat(CollateralSecurityService.filter(selection)).isEqualTo(
                "Ccode eq 'PFS' and RRanl eq '' and RGpart eq '' and Co eq true and Nc eq true and Ip eq true"
                        + " and Pc eq true and Ta eq true");

        selection.companyCode = " pfs ";
        selection.contract = new ArrayList<>(Arrays.asList(line("BT", "0000010003814", "0000010003900")));
        selection.partner = new ArrayList<>(Arrays.asList(line("EQ", "1000000123", "")));
        selection.notComplied = false;
        selection.timelineAvailable = false;
        assertThat(CollateralSecurityService.filter(selection)).isEqualTo(
                "Ccode eq 'PFS' and RRanl ge '0000010003814' and RRanl le '0000010003900' and RGpart eq '1000000123'"
                        + " and Co eq true and Nc eq false and Ip eq true and Pc eq true and Ta eq false");
    }

    @Test
    @DisplayName("result: business order, selection properties and the GUID hidden, dates, decimals, booleans, new properties last")
    void result() throws Exception {
        JsonNode rows = JSON.readTree("[{\"__metadata\":{\"id\":\"x\"},\"ZslNo\":\"0001\",\"Ranl\":\"0000010003814\","
                + "\"ZloanAcName\":\"Solar Park \",\"ZcomplianceDate\":\"/Date(1735689600000)/\",\"WaiverDate\":null,"
                + "\"PenalChgAppl\":true,\"PenalChgPct\":\"2.00\",\"ZsecValue\":\"1250000.000\",\"ZsecNoOfUnits\":\"100.00000\","
                + "\"CagGuid\":\"0050568b-1a2b-1ee1-9a9c-000000000001\",\"Ccode\":\"PFS\",\"RRanl\":\"\",\"RGpart\":\"\","
                + "\"Co\":true,\"Nc\":true,\"Ip\":true,\"Pc\":true,\"Ta\":true,\"ZnewField\":\"A\"}]");
        List<JsonNode> list = new ArrayList<>();
        rows.forEach(list::add);
        Report report = CollateralSecurityService.toReport(list);

        List<String> keys = report.getColumns().stream().map(Column::getKey).collect(Collectors.toList());
        assertThat(keys.subList(0, 3)).containsExactly("ZslNo", "Ranl", "ZloanAcName");
        assertThat(keys).doesNotContain("Ccode", "RRanl", "RGpart", "Co", "Nc", "Ip", "Pc", "Ta", "CagGuid", "__metadata");
        assertThat(keys.get(keys.size() - 1)).isEqualTo("ZnewField");
        assertThat(report.getColumns().get(keys.size() - 1).getLabel()).isEqualTo("Znew field");
        assertThat(report.getTotal()).isEqualTo(1);

        assertThat(report.getRows().get(0))
                .containsEntry("ZslNo", "1")
                .containsEntry("ZloanAcName", "Solar Park")
                .containsEntry("ZcomplianceDate", "2025-01-01")
                .containsEntry("WaiverDate", null)
                .containsEntry("PenalChgAppl", "Yes")
                .containsEntry("PenalChgPct", "2")
                .containsEntry("ZsecValue", "1250000.00")
                .containsEntry("ZsecNoOfUnits", "100")
                .doesNotContainKey("CagGuid");
    }

    @Test
    @DisplayName("Excel: selection block with the checked filters, amounts as numbers, dates as dd.MM.yyyy")
    void excel() throws Exception {
        Selection selection = new Selection();
        selection.inProcess = false;
        selection.contract = new ArrayList<>(Arrays.asList(line("EQ", "0000010003814", "")));
        List<Column> columns = Arrays.asList(new Column("Ranl", "Contract number", "text"),
                new Column("ZcomplianceDate", "Compliance date", "date"), new Column("ZsecValue", "Nominal value", "amount"));
        java.util.Map<String, String> row = new java.util.LinkedHashMap<>();
        row.put("Ranl", "0000010003814");
        row.put("ZcomplianceDate", "2025-01-01");
        row.put("ZsecValue", "1250000.00");
        byte[] bytes = new CollateralSecurityExcelWriter().write(selection, Arrays.asList(row), columns);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertThat(sheet.getSheetName()).isEqualTo("Collateral Securities Report");
            StringBuilder text = new StringBuilder();
            sheet.forEach(r -> r.forEach(c -> text.append(c.toString()).append('|')));
            assertThat(text.toString()).contains("Loan Number").contains("0000010003814")
                    .contains("Complied, Not Complied, Partially Complied, Timeline Available").contains("01.01.2025");
            int last = sheet.getLastRowNum();
            assertThat(sheet.getRow(last).getCell(2).getNumericCellValue()).isEqualTo(1250000.00);
        }
    }
}
