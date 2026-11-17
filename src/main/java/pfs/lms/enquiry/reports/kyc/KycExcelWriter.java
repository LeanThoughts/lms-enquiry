package pfs.lms.enquiry.reports.kyc;

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
 * Writes the Loans KYC Report as an Excel workbook (.xlsx): the selection in a header block, the SAP labels as
 * column titles (frozen, filterable) and the rows in the order and with the filter of the screen. Only the visible
 * columns are written when the browser names them. Dates are real Excel dates; everything else is text (contract
 * numbers and partner ids keep their leading zeros).
 */
@Component
public class KycExcelWriter {

    private static final class Column {
        final String key;
        final String title;
        final boolean date;

        Column(String key, String title, boolean date) {
            this.key = key;
            this.title = title;
            this.date = date;
        }
    }

    /** The output columns in table order, with the SAP labels (same as the screen). */
    private static final Column[] ALL_COLUMNS = {
            new Column("contractNo", "Contract Number", false),
            new Column("status", "Status", false),
            new Column("serialNo", "Serial Number", false),
            new Column("partnerId", "Busn. Partner", false),
            new Column("partnerName", "Borrower Name", false),
            new Column("role", "Partner Role", false),
            new Column("projectName", "Project Name", false),
            new Column("loanAgmtDate", "Date of Loan Agrmt", true),
            new Column("kycStatus", "KYC Status", false),
            new Column("kycComplianceDate", "KYC Compliance Date", true),
            new Column("reKycComplianceDate", "Re-KYC Compliance Dt", true),
            new Column("ckycComplDate", "CKYC Compliance Date", true),
            new Column("ckycRefNo", "CKYC Ref. Num", false),
            new Column("pan", "PAN", false),
            new Column("panNumber", "PAN Number", false),
            new Column("coi", "COI", false),
            new Column("moa", "MOA", false),
            new Column("aoa", "AOA", false),
            new Column("cin", "CIN", false),
            new Column("lei", "LEI No", false),
            new Column("addrProof", "Address Proof", false),
            new Column("boardRes", "Board Resolution", false),
            new Column("authSignatory", "Authorized Signatory", false),
            new Column("kycRiskCategoryDesc", "KYC Risk Cat. Desc", false),
            new Column("reKycRiskCategoryDesc", "Re-KYC Risk Cat.Desc", false)
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

    public byte[] write(KycReportDtos.Selection selection, List<Map<String, String>> rows) {
        return write(selection, rows, null);
    }

    /** @param visibleColumns titles of the columns to write; null / empty / unknown = all columns */
    public byte[] write(KycReportDtos.Selection selection, List<Map<String, String>> rows, List<String> visibleColumns) {
        final Column[] columns = columnsFor(visibleColumns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Loans KYC");

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
            CellStyle date = workbook.createCellStyle();
            date.setDataFormat(workbook.createDataFormat().getFormat("dd.mm.yyyy"));
            date.setAlignment(HorizontalAlignment.LEFT);

            KycReportDtos.Selection s = selection == null ? new KycReportDtos.Selection() : selection;
            int r = 0;
            cell(sheet, r++, 0, "Loans KYC Report", title);
            r = line(sheet, r, label, "Company Code", "PFS");
            r = line(sheet, r, label, "Loan Contract", SelectOptions.describeOrNull(s.contract));
            r = line(sheet, r, label, "Bus. Partner Id", SelectOptions.describeOrNull(s.partner));
            r = line(sheet, r, label, "Loan Contract Status", SelectOptions.describeOrNull(s.status));
            r = line(sheet, r, label, "Include Authorized Signatories", s.includeAuthSignatories ? "Yes" : "No");
            r = line(sheet, r, label, "Run Date", s.runDate);
            r = line(sheet, r, label, "Select Loans having Eff Cap", s.effCapOnly ? "Yes" : "No");
            r = line(sheet, r, label, "KYC/Re-KYC Compliance Date", SelectOptions.describeOrNull(s.kycDate));
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
                        if (columns[c].date) {
                            LocalDate d = parseDate(value);
                            if (d != null) {
                                cell.setCellValue(d);
                                cell.setCellStyle(date);
                                continue;
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

    private static LocalDate parseDate(String iso) {
        try {
            return LocalDate.parse(iso.length() > 10 ? iso.substring(0, 10) : iso);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
