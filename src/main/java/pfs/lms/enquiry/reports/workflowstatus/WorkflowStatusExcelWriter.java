package pfs.lms.enquiry.reports.workflowstatus;

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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

/**
 * Writes the Workflow Status Report as an Excel workbook (.xlsx): one sheet with the selection in a header block,
 * the SAP labels as column titles (frozen, filterable) and the rows as given — i.e. in the order and with the
 * filter the user applied on the screen. Dates and times are real Excel dates/times, so they sort and filter in
 * Excel; everything else is text (contract numbers keep their leading zeros).
 */
@Component
public class WorkflowStatusExcelWriter {

    /** A column: title, value of a row, and whether it is a date, a time or text. */
    private static final class Column {
        final String title;
        final Function<WorkflowStatusDtos.Row, String> value;
        final char type; // 'd' date, 't' time, 's' text

        Column(String title, char type, Function<WorkflowStatusDtos.Row, String> value) {
            this.title = title;
            this.type = type;
            this.value = value;
        }
    }

    /** The entity's properties in SEGW order, with the SAP labels. */
    private static final Column[] ALL_COLUMNS = {
            new Column("Row", 's', WorkflowStatusDtos.Row::getRowId),
            new Column("Company Code", 's', WorkflowStatusDtos.Row::getBukrs),
            new Column("Release Object", 's', WorkflowStatusDtos.Row::getRelobj),
            new Column("Business Partner", 's', WorkflowStatusDtos.Row::getPartner),
            new Column("Date", 'd', WorkflowStatusDtos.Row::getDatum),
            new Column("Release Object (Text)", 's', WorkflowStatusDtos.Row::getRelObject),
            new Column("Contract Number", 's', WorkflowStatusDtos.Row::getRanl),
            new Column("Partner", 's', WorkflowStatusDtos.Row::getParnter),
            new Column("Bus. Operation No.", 's', WorkflowStatusDtos.Row::getBoId),
            new Column("Initiator User Id", 's', WorkflowStatusDtos.Row::getInitiatorId),
            new Column("Initiator Position", 's', WorkflowStatusDtos.Row::getInitiatorPosition),
            new Column("Initiator Name", 's', WorkflowStatusDtos.Row::getInitiatorName),
            new Column("Initiation Date", 'd', WorkflowStatusDtos.Row::getInitiationDate),
            new Column("Initiation Time", 't', WorkflowStatusDtos.Row::getInitiationTime),
            new Column("WF Reference No.", 's', WorkflowStatusDtos.Row::getWfReferenceNo),
            new Column("Work Item", 's', WorkflowStatusDtos.Row::getTaskText),
            new Column("Approver User Id", 's', WorkflowStatusDtos.Row::getApproverId),
            new Column("Approver Position", 's', WorkflowStatusDtos.Row::getApproverPosition),
            new Column("Approver Name", 's', WorkflowStatusDtos.Row::getApproverName),
            new Column("Approval Date", 'd', WorkflowStatusDtos.Row::getApprovalDate),
            new Column("Approval Time", 't', WorkflowStatusDtos.Row::getApprovalTime),
            new Column("Rel. St.", 's', WorkflowStatusDtos.Row::getReleaseStatus),
            new Column("Release Status", 's', WorkflowStatusDtos.Row::getReleaseStatusDesc),
            new Column("Rel. Ind.", 's', WorkflowStatusDtos.Row::getReleaseIndicator),
            new Column("Approval Status", 's', WorkflowStatusDtos.Row::getReleaseIndicatorDesc)
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

    public byte[] write(WorkflowStatusDtos.Selection selection, List<WorkflowStatusDtos.Row> rows) {
        return write(selection, rows, null);
    }

    /** @param visibleColumns titles of the columns to write, in table order; null / empty / unknown = all columns */
    public byte[] write(WorkflowStatusDtos.Selection selection, List<WorkflowStatusDtos.Row> rows, List<String> visibleColumns) {
        final Column[] COLUMNS = columnsFor(visibleColumns);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Workflow Status");

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
            CellStyle time = workbook.createCellStyle();
            time.setDataFormat(workbook.createDataFormat().getFormat("hh:mm:ss"));
            time.setAlignment(HorizontalAlignment.LEFT);

            // Title and selection block
            int r = 0;
            cell(sheet, r++, 0, "Loans: Workflow Status Report", title);
            r = selectionLine(sheet, r, label, "Company Code", selection == null ? null : selection.companyCode);
            r = selectionLine(sheet, r, label, "Release Object", selection == null ? null : selection.releaseObject);
            r = selectionLine(sheet, r, label, "Contract Number", SelectOptions.describeOrNull(selection == null ? null : selection.contract));
            r = selectionLine(sheet, r, label, "Business Partner", SelectOptions.describeOrNull(selection == null ? null : selection.partner));
            r = selectionLine(sheet, r, label, "Date Range", SelectOptions.describeOrNull(selection == null ? null : selection.date));
            r = selectionLine(sheet, r, label, "Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            r = selectionLine(sheet, r, label, "Rows", String.valueOf(rows == null ? 0 : rows.size()));
            r++;

            // Column titles
            int headerRow = r;
            Row head = sheet.createRow(r++);
            for (int c = 0; c < COLUMNS.length; c++) {
                Cell cell = head.createCell(c);
                cell.setCellValue(COLUMNS[c].title);
                cell.setCellStyle(header);
            }

            // Rows
            if (rows != null) {
                for (WorkflowStatusDtos.Row row : rows) {
                    Row line = sheet.createRow(r++);
                    for (int c = 0; c < COLUMNS.length; c++) {
                        String value = COLUMNS[c].value.apply(row);
                        if (value == null || value.isEmpty()) {
                            continue;
                        }
                        Cell cell = line.createCell(c);
                        if (COLUMNS[c].type == 'd') {
                            LocalDate d = parseDate(value);
                            if (d != null) {
                                cell.setCellValue(d);
                                cell.setCellStyle(date);
                                continue;
                            }
                        } else if (COLUMNS[c].type == 't') {
                            LocalTime t = parseTime(value);
                            if (t != null) {
                                cell.setCellValue(LocalDateTime.of(LocalDate.of(1900, 1, 1), t));
                                cell.setCellStyle(time);
                                continue;
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

    private static int selectionLine(Sheet sheet, int row, CellStyle labelStyle, String name, String value) {
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

    private static LocalTime parseTime(String text) {
        try {
            return LocalTime.parse(text);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
