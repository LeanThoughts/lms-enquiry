package pfs.lms.enquiry.reports.disbursement;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.reports.SelectOptions;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Column;
import pfs.lms.enquiry.reports.disbursement.DisbursementDtos.Selection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Writes the Loans Disbursement Report as an Excel workbook (.xlsx): the selection in a header block, the SAP field
 * labels as column titles (frozen, filterable) and the rows as shown. A column whose values are all amounts (e.g.
 * "1,25,000.50" or "125.00-") is written as numbers; everything else as text, so contract numbers and partner ids
 * keep their leading zeros.
 */
@Component
public class DisbursementExcelWriter {

    /** An amount as SAP or the portal shows it; a leading zero before more digits (an id such as 0001000234) is not one. */
    private static final Pattern AMOUNT = Pattern.compile("^-?(0|[1-9][\\d,]*)(\\.\\d+)?-?$");

    public byte[] write(DisbursementDtos.Selection selection, List<Map<String, String>> rows, List<Column> columns) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Disbursement");

            Font bold = workbook.createFont();
            bold.setBold(true);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            CellStyle title = workbook.createCellStyle();
            title.setFont(titleFont);
            CellStyle label = workbook.createCellStyle();
            label.setFont(bold);
            CellStyle header = workbook.createCellStyle();
            header.setFont(bold);
            header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setBorderBottom(BorderStyle.THIN);
            CellStyle amount = workbook.createCellStyle();
            amount.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00#"));

            Selection s = selection == null ? new Selection() : selection;
            int r = 0;
            cell(sheet, r++, 0, "Loans Disbursement - Periodwise and Comparison Report", title);
            r = line(sheet, r, label, "Company Code", s.companyCode);
            r = line(sheet, r, label, "Type", "C".equalsIgnoreCase(s.mode) ? "Comparative" : "Report");
            r = line(sheet, r, label, "Fiscal Year", s.fiscalYearFrom);
            r = line(sheet, r, label, "Fiscal Year to", s.fiscalYearTo);
            r = line(sheet, r, label, "Abstract Report", s.periodType);
            r = line(sheet, r, label, "Period Filter", s.periodFilter);
            r = line(sheet, r, label, "Loan Contract", SelectOptions.describeOrNull(s.contract));
            r = line(sheet, r, label, "Business Partner Id", SelectOptions.describeOrNull(s.partner));
            r = line(sheet, r, label, "Project Type", s.projectType);
            r = line(sheet, r, label, "Fetch all loans", s.allLoans ? "Yes" : "No");
            r = line(sheet, r, label, "Filter nil disbursements", s.suppressZero ? "Yes" : "No");
            r = line(sheet, r, label, "Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            r = line(sheet, r, label, "Rows", String.valueOf(rows == null ? 0 : rows.size()));
            r++;

            int headerRow = r;
            Row head = sheet.createRow(r++);
            boolean[] numeric = new boolean[columns.size()];
            for (int c = 0; c < columns.size(); c++) {
                Cell cell = head.createCell(c);
                cell.setCellValue(columns.get(c).getLabel());
                cell.setCellStyle(header);
                numeric[c] = isAmountColumn(rows, columns.get(c).getKey());
            }
            if (rows != null) {
                for (Map<String, String> row : rows) {
                    Row lineRow = sheet.createRow(r++);
                    for (int c = 0; c < columns.size(); c++) {
                        String value = row.get(columns.get(c).getKey());
                        if (value == null || value.isEmpty()) {
                            continue;
                        }
                        Cell cell = lineRow.createCell(c);
                        if (numeric[c]) {
                            cell.setCellValue(parse(value).doubleValue());
                            cell.setCellStyle(amount);
                        } else {
                            cell.setCellValue(value);
                        }
                    }
                }
            }

            if (!columns.isEmpty()) {
                sheet.createFreezePane(0, headerRow + 1);
                sheet.setAutoFilter(new CellRangeAddress(headerRow, Math.max(headerRow, r - 1), 0, columns.size() - 1));
                for (int c = 0; c < columns.size(); c++) {
                    sheet.autoSizeColumn(c);
                    int width = sheet.getColumnWidth(c);
                    sheet.setColumnWidth(c, Math.min(Math.max(width + 512, 3000), 16000));
                }
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new LmsException("The Excel file could not be created: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** True when the column has values and every one is an amount. */
    static boolean isAmountColumn(List<Map<String, String>> rows, String key) {
        boolean any = false;
        if (rows != null) {
            for (Map<String, String> row : rows) {
                String value = row.get(key);
                if (value == null || value.isEmpty()) {
                    continue;
                }
                if (!AMOUNT.matcher(value.trim()).matches()) {
                    return false;
                }
                any = true;
            }
        }
        return any;
    }

    /** An amount with SAP's trailing minus or digit grouping as a number. */
    static BigDecimal parse(String value) {
        String n = value.trim().replace(",", "");
        boolean negative = n.endsWith("-");
        if (negative) {
            n = n.substring(0, n.length() - 1);
        }
        BigDecimal number = new BigDecimal(n);
        return negative ? number.negate() : number;
    }

    private static int line(Sheet sheet, int row, CellStyle labelStyle, String name, String value) {
        cell(sheet, row, 0, name, labelStyle);
        cell(sheet, row, 1, value == null || value.isEmpty() ? "(all)" : value, null);
        return row + 1;
    }

    private static void cell(Sheet sheet, int row, int col, String value, CellStyle style) {
        Row r = sheet.getRow(row) == null ? sheet.createRow(row) : sheet.getRow(row);
        Cell cell = r.createCell(col);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }
}
