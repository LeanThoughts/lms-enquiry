package pfs.lms.enquiry.reports.iccapproval;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

/**
 * Writes the ICC Approval Status Report as an Excel workbook (.xlsx): the selection in a header block, the SAP
 * labels as column titles (frozen, filterable) and the rows in the order and with the filter of the screen. Dates
 * and amounts are real Excel dates / numbers; everything else is text (contract numbers keep leading zeros).
 */
@Component
public class IccApprovalExcelWriter {

    private static final class Column {
        final String title;
        final Function<IccApprovalDtos.Row, String> value;
        final char type; // 'd' date, 'n' number, 's' text

        Column(String title, char type, Function<IccApprovalDtos.Row, String> value) {
            this.title = title;
            this.type = type;
            this.value = value;
        }
    }

    /** The entity's properties in SEGW order, with the SAP labels. */
    private static final Column[] ALL_COLUMNS = {
            new Column("Fiscal Year", 's', IccApprovalDtos.Row::getFyear),
            new Column("Serial Number", 's', IccApprovalDtos.Row::getSerialNo),
            new Column("Quarter", 's', IccApprovalDtos.Row::getQuarter),
            new Column("Contract Number", 's', IccApprovalDtos.Row::getRanl),
            new Column("Status", 's', IccApprovalDtos.Row::getStatus),
            new Column("Busn. Partner", 's', IccApprovalDtos.Row::getBupaId),
            new Column("Borrower Name", 's', IccApprovalDtos.Row::getBorrowerName),
            new Column("Project Name", 's', IccApprovalDtos.Row::getProjectName),
            new Column("Sanctioned Amount", 'n', IccApprovalDtos.Row::getZzsanctionedAmt),
            new Column("Curr.Commitment Cap.", 'n', IccApprovalDtos.Row::getCommitCapital),
            new Column("Loan Type", 's', IccApprovalDtos.Row::getLoanType),
            new Column("Project Sector", 's', IccApprovalDtos.Row::getSector),
            new Column("Project Type", 's', IccApprovalDtos.Row::getProjectType),
            new Column("Group Company Name", 's', IccApprovalDtos.Row::getGrpCompName),
            new Column("ICC Approved Fee %", 'n', IccApprovalDtos.Row::getFeePct),
            new Column("Interest Rate %", 'n', IccApprovalDtos.Row::getRoi),
            new Column("BCM (ICC) Appr Status", 's', IccApprovalDtos.Row::getIccStatusDesc),
            new Column("ICC Clearance Date", 'd', IccApprovalDtos.Row::getIccApprDate),
            new Column("ICC Remark", 's', IccApprovalDtos.Row::getZiccRemarks),
            new Column("Board Approval", 'd', IccApprovalDtos.Row::getBodDate),
            new Column("BOD Status", 's', IccApprovalDtos.Row::getBodStatusDesc),
            new Column("Board App Remarks", 's', IccApprovalDtos.Row::getZboardAppRemarks)
    };


    /** The columns to write: those whose title is in {@code titles} (table order); all when none matches. */
    private static Column[] columnsFor(List<String> titles) {
        if (titles == null || titles.isEmpty()) {
            return ALL_COLUMNS;
        }
        List<Column> picked = new java.util.ArrayList<>();
        for (Column c : ALL_COLUMNS) {
            if (titles.contains(c.title)) {
                picked.add(c);
            }
        }
        return picked.isEmpty() ? ALL_COLUMNS : picked.toArray(new Column[0]);
    }

    public byte[] write(IccApprovalDtos.Selection selection, List<IccApprovalDtos.Row> rows) {
        return write(selection, rows, null);
    }

    /** @param visibleColumns titles of the columns to write, in table order; null / empty / unknown = all columns */
    public byte[] write(IccApprovalDtos.Selection selection, List<IccApprovalDtos.Row> rows, List<String> visibleColumns) {
        final Column[] COLUMNS = columnsFor(visibleColumns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ICC Approval Status");

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
            CellStyle number = workbook.createCellStyle();
            number.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            int r = 0;
            cell(sheet, r++, 0, "Loans : ICC Approval Status Report", title);
            r = line(sheet, r, label, "Company Code", selection == null ? null : selection.companyCode);
            r = line(sheet, r, label, "Fiscal Year", selection == null ? null : selection.fiscalYear);
            r = line(sheet, r, label, "Quarter", selection == null ? null : selection.quarter);
            r = line(sheet, r, label, "Loan Contract", SelectOptions.describeOrNull(selection == null ? null : selection.contract));
            r = line(sheet, r, label, "Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            r = line(sheet, r, label, "Rows", String.valueOf(rows == null ? 0 : rows.size()));
            r++;

            int headerRow = r;
            Row head = sheet.createRow(r++);
            for (int c = 0; c < COLUMNS.length; c++) {
                Cell cell = head.createCell(c);
                cell.setCellValue(COLUMNS[c].title);
                cell.setCellStyle(header);
            }

            if (rows != null) {
                for (IccApprovalDtos.Row row : rows) {
                    Row lineRow = sheet.createRow(r++);
                    for (int c = 0; c < COLUMNS.length; c++) {
                        String value = COLUMNS[c].value.apply(row);
                        if (value == null || value.isEmpty()) {
                            continue;
                        }
                        Cell cell = lineRow.createCell(c);
                        if (COLUMNS[c].type == 'd') {
                            LocalDate d = parseDate(value);
                            if (d != null) {
                                cell.setCellValue(d);
                                cell.setCellStyle(date);
                                continue;
                            }
                        } else if (COLUMNS[c].type == 'n') {
                            try {
                                cell.setCellValue(new BigDecimal(value).doubleValue());
                                cell.setCellStyle(number);
                                continue;
                            } catch (NumberFormatException ignored) {
                                // written as text below
                            }
                        }
                        cell.setCellValue(value);
                    }
                }
            }

            sheet.createFreezePane(0, headerRow + 1);
            sheet.setAutoFilter(new CellRangeAddress(headerRow, Math.max(headerRow, r - 1), 0, COLUMNS.length - 1));
            for (int c = 0; c < COLUMNS.length; c++) {
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
