package pfs.lms.enquiry.enquiriesexcelupload;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class EnquiryListBDExcel {

    private SXSSFWorkbook sxssfWorkbook;
    private SXSSFSheet sxssfSheet;


    private List<ExcelEnquiry> resources;

    public EnquiryListBDExcel(List<ExcelEnquiry> resources) {
         this.resources = resources;
         sxssfWorkbook = new SXSSFWorkbook();
    }


    public  SXSSFWorkbook exportSXSSWorkBook  (HttpServletResponse response) throws IOException, ParseException {


        writeHeaderLineSXSS();
        writeDataLinesSXSS();

        ServletOutputStream outputStream = response.getOutputStream();
        sxssfWorkbook.write(outputStream);
        sxssfWorkbook.close();

        outputStream.close();
        return sxssfWorkbook;

    }
    private void writeHeaderLineSXSS() {
        if ( sxssfWorkbook.getSheet("Loan Enquiries") !=null ) {
            //sxssfSheet =  sxssfWorkbook.createSheet("Loan Enquiries");
        }
        else {
            sxssfSheet =  sxssfWorkbook.createSheet("Loan Enquiries");
        }

        sxssfSheet.setRandomAccessWindowSize(50);
        short fontHeight = 300;
        Row row = sxssfSheet.createRow(0);

        CellStyle style = sxssfWorkbook.createCellStyle();

        Font font = sxssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontHeight);
        style.setFont(font);


        createSXSSCell(row, 0, "Serial Number", style);
        createSXSSCell(row, 1, "SAP Enquiry ID", style);
        createSXSSCell(row, 2, "Status", style);
        createSXSSCell(row, 3, "Status Description", style);
        createSXSSCell(row, 4, "Borrower Name", style);
        createSXSSCell(row, 5, "Group Name", style);
        createSXSSCell(row, 6, "Project Type", style);
        createSXSSCell(row, 7, "Type of Loan", style);
        createSXSSCell(row, 8, "Financing Type", style);
        createSXSSCell(row, 9, "Date of Lead Generation", style);
        createSXSSCell(row, 10, "Amount Requested", style);
        createSXSSCell(row, 11, "Borrower Requested ROI", style);
        createSXSSCell(row, 12, "ICC Readiness Status", style);
        createSXSSCell(row, 13, "Remarks on ICC Readiness", style);
        createSXSSCell(row, 14, "Presented in ICC", style);
        createSXSSCell(row, 15, "ICC Status", style);
        createSXSSCell(row, 16, "Reason for ICC Status", style);
        createSXSSCell(row, 17, "ICC Clearance Date", style);
        createSXSSCell(row, 18, "ICC Meeting Number", style);
        createSXSSCell(row, 19, "Amount Approved (Cr)", style);
        createSXSSCell(row, 20, "ICC Approved ROI", style);
        createSXSSCell(row, 21, "ICC Approved Fee %", style);
        createSXSSCell(row, 22, "Remarks for ICC Approval / Rejection", style);
        createSXSSCell(row, 23, "Dealing/Nodal Officer BD", style);
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

        private void writeDataLinesSXSS() throws ParseException {
            int rowCount = 1;
            int serialNo = 0;
            short fontHeight = 300;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            CellStyle style = sxssfWorkbook.createCellStyle();
            Font font = sxssfWorkbook.createFont();
            font.setFontHeight(fontHeight);
            style.setFont(font);

            for (ExcelEnquiry excelEnquiry : resources) {
                log.info("Serial No: " + serialNo + " Excel Output Row for Loan :" + excelEnquiry.getSapEnquiryId());
                Row row = sxssfSheet.createRow(rowCount++);
                int columnCount = 0;


                //Enquiry ID
                createSXSSCell(row, columnCount++, excelEnquiry.getSerialNumber(), style);

                //SAP Enquiry ID
                if (excelEnquiry.getSapEnquiryId() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getSapEnquiryId().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                // Functional Status
                if (excelEnquiry.getFunctionalStatus() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getFunctionalStatus().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                // Functional Status Description
                if (excelEnquiry.getFunctionalStatusDescription() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getFunctionalStatusDescription(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Borrower Name
                if (excelEnquiry.getBorrowerName() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getBorrowerName().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Group Name
                if (excelEnquiry.getGroupName() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getGroupName().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Project Type
                if (excelEnquiry.getProjectType() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getProjectType().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Loan Type
                if (excelEnquiry.getLoanType() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getLoanType().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Financing Type
                if (excelEnquiry.getProposalType() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getProposalType().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Date of Lead Generation
                if (excelEnquiry.getDateOfLeadGeneration() != null) {
                    String formattedString = excelEnquiry.getDateOfLeadGeneration().format(formatter);
                    createSXSSCell(row, columnCount++, formattedString, style);

                 }
                    else
                    createSXSSCell(row, columnCount++, "", style);

                //Amount Requested
                if (excelEnquiry.getAmountRequested() != null) {
                    String amountRequested = new DecimalFormat("#.00#").format(excelEnquiry.getAmountRequested()); // rounded to 2 decimal places
                    createSXSSCell(row, columnCount++, amountRequested, style);
                }
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Borrower Requested ROI
                if (excelEnquiry.getBorrowerRequestedROI() != null) {
                    String borrowerRequestedRoi = new DecimalFormat("#.00#").format(excelEnquiry.getBorrowerRequestedROI()); // rounded to 2 decimal places
                    createSXSSCell(row, columnCount++, borrowerRequestedRoi, style);
                }
                else
                    createSXSSCell(row, columnCount++, "", style);


                //ICC Readiness Status
                if (excelEnquiry.getIccReadinessStatus() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getIccReadinessStatus().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Remarks on ICC Readiness
                if (excelEnquiry.getRemarksOnIccReadiness() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getRemarksOnIccReadiness(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Presented in ICC
                if (excelEnquiry.getPresentedInIcc() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getPresentedInIcc(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //ICC Status
                if (excelEnquiry.getIccStatus() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getIccStatus(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Reason for ICC Status
                if (excelEnquiry.getReasonForIccStatus() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getReasonForIccStatus(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //ICC Clearance Date
                if (excelEnquiry.getIccClearanceDate() != null) {
                     String formattedString = excelEnquiry.getIccClearanceDate().format(formatter);
                    createSXSSCell(row, columnCount++, formattedString, style);
                }
                    else
                    createSXSSCell(row, columnCount++, "", style);

                // ICC Meeting Number
                if (excelEnquiry.getIccMeetingNumber() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getIccMeetingNumber(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                // Amount Approved (Cr)
                if (excelEnquiry.getAmountApproved() != null) {
                    if (excelEnquiry.getAmountApproved() > 0) {
                        String amountApproved = new DecimalFormat("#.00#").format(excelEnquiry.getAmountApproved()); // rounded to 2 decimal places
                        createSXSSCell(row, columnCount++, amountApproved, style);
                    }
                    else {
                        createSXSSCell(row, columnCount++, "", style);
                    }
                }
                else
                    createSXSSCell(row, columnCount++, "", style);

                // ICC Approved ROI
                if (excelEnquiry.getIccApprovedRoi() != null) {
                    if ((excelEnquiry.getIccApprovedRoi()) > 0) {
                        String iccApprovedRoi = new DecimalFormat("#.00#").format(excelEnquiry.getIccApprovedRoi()); // rounded to 2 decimal places
                        createSXSSCell(row, columnCount++, iccApprovedRoi, style);
                    }else {
                        createSXSSCell(row, columnCount++, "", style);
                    }
                }
                 else
                    createSXSSCell(row, columnCount++, "", style);

                // ICC Approved ROI
                if (excelEnquiry.getIccApprovedFeePct() != null) {
                    if ((excelEnquiry.getIccApprovedFeePct()) > 0) {
                        String iccApprovedFeePct = new DecimalFormat("#.00#").format(excelEnquiry.getIccApprovedFeePct()); // rounded to 2 decimal places
                        createSXSSCell(row, columnCount++, iccApprovedFeePct, style);
                    }else {
                        createSXSSCell(row, columnCount++, "", style);
                    }
                }
                else
                    createSXSSCell(row, columnCount++, "", style);

                //Remarks for ICC Approval / Rejection
                if (excelEnquiry.getRemarksForIccApproval() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getRemarksForIccApproval(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);


                //Dealing/Nodal Officer BD
                if (excelEnquiry.getNodalOfficerBD() != null)
                    createSXSSCell(row, columnCount++, excelEnquiry.getNodalOfficerBD(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);


            }

    }

}
