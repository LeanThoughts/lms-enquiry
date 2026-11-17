package pfs.lms.enquiry.reports.balancelist;

import pfs.lms.enquiry.reports.SelectOptions;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.exception.LmsException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Writes the Loans Balance List as an Excel workbook (.xlsx): the selection in a header block, the SAP labels as
 * column titles (frozen, filterable) and the rows in the order and with the filter of the screen. Only the visible
 * columns are written when the browser names them. Dates are real Excel dates; everything else is text (contract
 * numbers and partner ids keep their leading zeros).
 */
@Component
public class BalanceListExcelWriter {

    private static final class Column {
        final String key;
        final String title;
        final boolean amount;

        Column(String key, String title, boolean amount) {
            this.key = key;
            this.title = title;
            this.amount = amount;
        }
    }

    /** The output columns in table order, with the same titles as the screen. */
    private static final Column[] ALL_COLUMNS = {
            new Column("ranl", "Contract Number", false),
            new Column("rantrag", "Application No.", false),
            new Column("bukrs", "Company Code", false),
            new Column("gsart", "Business Type", false),
            new Column("santwhr", "Contract Currency", false),
            new Column("rrefkont", "Acct Assignment Ref.", false),
            new Column("moratorium", "Moratorium", false),
            new Column("status", "Status", false),
            new Column("projectName", "Project Name", false),
            new Column("anzwhr", "Display Currency", false),
            new Column("sanctionedAmt", "Sanctioned Amount", true),
            new Column("bzusage", "Commitment Capital", true),
            new Column("nomkap", "Nominal Capital", true),
            new Column("effkap", "Effective Capital", true),
            new Column("tilgng", "Repayment", true),
            new Column("zinsen", "Interest", true),
            new Column("versch", "Versch.", true),
            new Column("ueberz", "Ueberz.", true),
            new Column("briwr", "Briwr", true),
            new Column("bvzwr", "Bvzwr", true),
            new Column("bvkwr", "Bvkwr", true),
            new Column("rbear", "Last Changed by", false)
    };

    /** The columns to write: those whose title is in {@code titles} (table order); all when none matches. */
    private static Column[] columnsFor(List<String> titles) {
        if (titles == null || titles.isEmpty()) {
            return ALL_COLUMNS;
        }
        List<Column> picked = new ArrayList<>();
        for (Column c : ALL_COLUMNS) {
            if (titles.contains(c.title)) {
                picked.add(c);
            }
        }
        return picked.isEmpty() ? ALL_COLUMNS : picked.toArray(new Column[0]);
    }

    public byte[] write(BalanceListDtos.Selection selection, List<Map<String, String>> rows) {
        return write(selection, rows, null);
    }

    /** @param visibleColumns titles of the columns to write; null / empty / unknown = all columns */
    public byte[] write(BalanceListDtos.Selection selection, List<Map<String, String>> rows, List<String> visibleColumns) {
        final Column[] columns = columnsFor(visibleColumns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Balance List");

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

            BalanceListDtos.Selection s = selection == null ? new BalanceListDtos.Selection() : selection;
            int r = 0;
            cell(sheet, r++, 0, "Loans Balance List", title);
            r = line(sheet, r, label, "Company Code", "PFS");
            r = line(sheet, r, label, "Contract Number", SelectOptions.describeOrNull(s.contract));
            r = line(sheet, r, label, "Product Type", SelectOptions.describeOrNull(s.productType));
            r = line(sheet, r, label, "Loan Type", SelectOptions.describeOrNull(s.loanType));
            r = line(sheet, r, label, "Contract Currency", SelectOptions.describeOrNull(s.currency));
            r = line(sheet, r, label, "Acct Assignment Ref.", SelectOptions.describeOrNull(s.acctRef));
            r = line(sheet, r, label, "Last Changed by", SelectOptions.describeOrNull(s.changedBy));
            r = line(sheet, r, label, "Evaluate completed contracts?", s.evaluateCompleted ? "Yes" : "No");
            r = line(sheet, r, label, "Key date", s.keyDate);
            r = line(sheet, r, label, "Display currency", s.displayCurrency);
            r = line(sheet, r, label, "Only display balances <> 0?", s.nonZeroOnly ? "Yes" : "No");
            r = line(sheet, r, label, "Also totals for balances = 0?", s.totalsForZero ? "Yes" : "No");
            r = line(sheet, r, label, "Only display totals?", s.totalsOnly ? "Yes" : "No");
            r = line(sheet, r, label, "Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            r = line(sheet, r, label, "Rows", String.valueOf(rows == null ? 0 : rows.size()));
            r++;

            int headerRow = r;
            Row head = sheet.createRow(r++);
            for (int c = 0; c < columns.length; c++) {
                Cell cell = head.createCell(c);
                cell.setCellValue(columns[c].title);
                cell.setCellStyle(header);
            }

            if (rows != null) {
                for (Map<String, String> row : rows) {
                    Row lineRow = sheet.createRow(r++);
                    for (int c = 0; c < columns.length; c++) {
                        String value = row.get(columns[c].key);
                        if (value == null || value.isEmpty()) {
                            continue;
                        }
                        Cell cell = lineRow.createCell(c);
                        if (columns[c].amount) {
                            try {
                                cell.setCellValue(new java.math.BigDecimal(value.trim()).doubleValue());
                                cell.setCellStyle(amount);
                                continue;
                            } catch (NumberFormatException ex) {
                                // not a number: written as text below
                            }
                        }
                        cell.setCellValue(value);
                    }
                }
            }

            sheet.createFreezePane(0, headerRow + 1);
            sheet.setAutoFilter(new CellRangeAddress(headerRow, Math.max(headerRow, r - 1), 0, columns.length - 1));
            for (int c = 0; c < columns.length; c++) {
                sheet.autoSizeColumn(c);
                int width = sheet.getColumnWidth(c);
                sheet.setColumnWidth(c, Math.min(Math.max(width + 512, 3000), 16000));
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new LmsException("The Excel file could not be created: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
