package pfs.lms.enquiry.reports;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.resource.LoanApplicationResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Slf4j
public class EnquiryReportExcelV1 {

    private SXSSFWorkbook sxssfWorkbook;
    private SXSSFSheet sxssfSheet;


    private List<LoanApplicationResource> resources;

    public EnquiryReportExcelV1(List<LoanApplicationResource> resources) {
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
        createSXSSCell(row, 1, "Process Status", style);
        createSXSSCell(row, 2, "Status", style);
        createSXSSCell(row, 3, "Enquiry Date", style);
        createSXSSCell(row, 4, "Enquiry Number", style);
        createSXSSCell(row, 5, "Loan Contract Number", style);
        createSXSSCell(row, 6, "Busines Partner Number", style);
        createSXSSCell(row, 7, "Business Partner Name", style);
        createSXSSCell(row, 8, "Project Name", style);
        createSXSSCell(row, 9, "Group Name", style);
        createSXSSCell(row, 10, "Loan Class", style);
        createSXSSCell(row, 11, "Project Type", style);
        createSXSSCell(row, 12, "Project Capacity", style);
        createSXSSCell(row, 13, "Loan Type", style);
        createSXSSCell(row, 14, "Project Cost (InCR)", style);
        createSXSSCell(row, 15, "Loan Amount Request", style);
        createSXSSCell(row, 16, "ROI Requested By", style);
        createSXSSCell(row, 17, "Created By", style);
        createSXSSCell(row, 18, "Created At", style);
        createSXSSCell(row, 19, "Approved By", style);
        createSXSSCell(row, 20, "Approval Date", style);
        createSXSSCell(row, 21, "ICC Clearance Status", style);
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

            CellStyle style = sxssfWorkbook.createCellStyle();
            Font font = sxssfWorkbook.createFont();
            font.setFontHeight(fontHeight);
            style.setFont(font);

            for (LoanApplicationResource loanApplicationResource : resources) {
                log.info("Serial No: " + serialNo + " Excel Output Row for Loan :" + loanApplicationResource.getLoanApplication().getEnquiryNo().getId());
                Row row = sxssfSheet.createRow(rowCount++);
                int columnCount = 0;

                LoanApplication loanApplication = loanApplicationResource.getLoanApplication();
                Partner partner = loanApplicationResource.getPartner();
                serialNo++;

                //serialNumber
                createSXSSCell(row, columnCount++, serialNo, style);

                //processStatus
                if (loanApplication.getFinalDecisionStatus() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getFinalDecisionStatus().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //status
                if (loanApplication.getTechnicalStatusDescription() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getTechnicalStatusDescription().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //enquiryDate
                if (loanApplication.getLoanEnquiryDate() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getLoanEnquiryDate().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //enquiryNumber
                if (loanApplication.getEnquiryNo().getId() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getEnquiryNo().getId(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //loanContractNumber
                if (loanApplication.getLoanContractId() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getLoanContractId().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //businesPartnerNumber
                if (loanApplication.getbusPartnerNumber() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getbusPartnerNumber() , style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //businessPartnerName
                String partnerName = partner.getPartyName1() + " "  + partner.getPartyName2();
                if (partnerName != null)
                    createSXSSCell(row, columnCount++, partnerName , style);
                else
                    createSXSSCell(row, columnCount++, "", style);


                //projectName
                if (loanApplication.getProjectName() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getProjectName(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                // groupName
                if (loanApplication.getGroupCompany() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getGroupCompany(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //loanClass
                if (loanApplication.getLoanClass() != null)
                    createSXSSCell(row, columnCount++, loanApplicationResource.getLoanClassDesc(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //projectType
                if (loanApplication.getProjectType() != null)
                    createSXSSCell(row, columnCount++, loanApplicationResource.getProjectTypeDesc(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //projectCapacity
                String projectCapacity = "";
                if (loanApplication.getProjectCapacityUnit() != null){
                    if (loanApplication.getProjectCapacity() != null) {
                        projectCapacity = loanApplication.getProjectCapacity().toString() + " " + loanApplication.getProjectCapacityUnit();
                    }
                } else if (loanApplication.getProjectCapacity() != null){
                    projectCapacity =  loanApplication.getProjectCapacity().toString();
                }
                if (loanApplication.getProjectCapacity() != null)
                    createSXSSCell(row, columnCount++,  projectCapacity, style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //loanType
                if (loanApplication.getFinancingType() != null)
                    createSXSSCell(row, columnCount++, loanApplicationResource.getFinancingTypeDesc(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //projectCostInCR
                if (loanApplication.getProjectCost() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getProjectCost().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //loanAmountRequest
                if (loanApplication.getLoanContractAmount() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getLoanContractAmount(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //roiRequestedBy
                if (loanApplication.getCreatedByUserName() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getCreatedByUserName(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //createdBy
                if (loanApplication.getCreatedByUserName() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getCreatedByUserName(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //createdAt
                if (loanApplication.getCreatedAt() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getCreatedAt().toString(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);
                //approvedBy
                if (loanApplication.getChangedByUserName() != null)
                    createSXSSCell(row, columnCount++, loanApplication.getChangedByUserName(), style);
                else
                    createSXSSCell(row, columnCount++, "", style);

                //approvalDate
            if (loanApplication.getChangedOn() != null)
                createSXSSCell(row, columnCount++, loanApplication.getChangedOn(), style);
            else
                createSXSSCell(row, columnCount++, "", style);
            //iccClearanceStatus

            }



    }

}
