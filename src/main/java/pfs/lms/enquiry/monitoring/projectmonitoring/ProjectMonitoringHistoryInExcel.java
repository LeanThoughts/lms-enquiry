package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorItemResource;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorResource;
import pfs.lms.enquiry.resource.LoanApplicationResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class ProjectMonitoringHistoryInExcel {

    private SXSSFWorkbook sxssfWorkbook;
    private SXSSFSheet sxssfSheet;

    int rowCount;

    private ProjectMonitorResource resource;

    public ProjectMonitoringHistoryInExcel( ProjectMonitorResource resource) {
        this.resource = resource;
        sxssfWorkbook = new SXSSFWorkbook();
    }


    public  SXSSFWorkbook exportSXSSWorkBook  (HttpServletResponse response) throws IOException, ParseException {


        writeHeaderLineSXSS();
        writeHeader();
        writeDataLinesSXSS();


        ServletOutputStream outputStream = response.getOutputStream();
        sxssfWorkbook.write(outputStream);
        sxssfWorkbook.close();

        outputStream.close();
        return sxssfWorkbook;

    }
    private void writeHeaderLineSXSS() {
        if ( sxssfWorkbook.getSheet("History") !=null ) {
            //sxssfSheet =  sxssfWorkbook.createSheet("Loan Enquiries");
        }
        else {
            sxssfSheet =  sxssfWorkbook.createSheet("History");
        }

        sxssfSheet.setRandomAccessWindowSize(50);
        short fontHeight = 400;

        CellStyle style = sxssfWorkbook.createCellStyle();

        Font font = sxssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontHeight);
        style.setFont(font);

        rowCount = 0;

        Row row = sxssfSheet.createRow(rowCount);
        createSXSSCell(row, 0, "Project Monitoring Data History", style);

    }

    private void writeHeader() throws ParseException {
        sxssfSheet.setRandomAccessWindowSize(50);
        short fontHeight = 300;

        CellStyle style = sxssfWorkbook.createCellStyle();

        Font font = sxssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontHeight);
        style.setFont(font);

        rowCount++;

        Row row = sxssfSheet.createRow(rowCount);
        createSXSSCell(row, 0, "Loan Contract Id", style);
        createSXSSCell(row, 1, resource.getLoanNumber(), style);

        rowCount++;
        Row projectNameRow = sxssfSheet.createRow(rowCount);
        createSXSSCell(projectNameRow, 0, "Project Name", style);
        createSXSSCell(projectNameRow, 1, resource.getProjectName(), style);
        rowCount++;

        Row downloadDateRow = sxssfSheet.createRow(rowCount);
        createSXSSCell(downloadDateRow, 0, "Date of download", style);
        createSXSSCell(downloadDateRow, 1, resource.getDownloadDate(), style);
        rowCount++;

        Row headerRow = sxssfSheet.createRow(rowCount);
        createSXSSCell(headerRow, 0, "Serial Number", style);
        createSXSSCell(headerRow, 1, "Date of Entry", style);
        createSXSSCell(headerRow, 2, "Particulars", style);
        createSXSSCell(headerRow, 3, "Description", style);
        createSXSSCell(headerRow, 4, "Original Data", style);
        createSXSSCell(headerRow, 5, "Remarks", style);
        rowCount++;

    }
        private void writeDataLinesSXSS() throws ParseException {

         short fontHeight = 200;


        CellStyle style = sxssfWorkbook.createCellStyle();
        Font font = sxssfWorkbook.createFont();
        font.setFontHeight(fontHeight);
        style.setFont(font);

        Integer serialNumber = 1;
        for (ProjectMonitorItemResource item : resource.getProjectMonitorItemResourceList()) {

            log.info("Serial No: Excel Output Row for Project Monitor :" + item.getSerialNumber());

            Row row = sxssfSheet.createRow(rowCount++);
            int columnCount = 0;

            createSXSSCell(row, columnCount++, serialNumber, style);
            if ( item.getDateOfEntry() != null)
            createSXSSCell(row, columnCount++, item.getDateOfEntry().toString(), style);
            else
                createSXSSCell(row, columnCount++, "", style);

            createSXSSCell(row, columnCount++, item.getParticulars(), style);
            createSXSSCell(row, columnCount++, item.getDescription(), style);
            createSXSSCell(row, columnCount++, item.getOriginalData(), style);
            createSXSSCell(row, columnCount++, item.getRemarks(), style);
            serialNumber++;

        }

    }


    private void createSXSSCell(Row row, int columnCount, Object value, CellStyle style) {

        sxssfSheet.trackAllColumnsForAutoSizing();


        sxssfSheet.autoSizeColumn(columnCount);

        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long){
            cell.setCellValue((Long)value);
        } else if (value instanceof Double){
            cell.setCellValue((Double)value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


}
