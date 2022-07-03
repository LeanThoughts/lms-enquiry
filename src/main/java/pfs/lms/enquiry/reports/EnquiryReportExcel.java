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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EnquiryReportExcel {


    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<LoanApplicationResource> resources;

    public EnquiryReportExcel(List<LoanApplicationResource> resources) {
        this.resources = resources;
        workbook = new XSSFWorkbook();
     }

    public XSSFWorkbook export(HttpServletResponse response) throws IOException  {


        writeHeaderLine();
        try {
            writeDataLines();
        } catch (ParseException p){
            System.out.println(p.toString());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

        return  workbook;

    }





    private void writeHeaderLine() {
        sheet = workbook.createSheet("Loan Enquiries");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Serial Number", style);
        createCell(row, 1, "Process Status", style);
        createCell(row, 2, "Status", style);
        createCell(row, 3, "Enquiry Date", style);
        createCell(row, 4, "Enquiry Number", style);
        createCell(row, 5, "Loan Contract Number", style);
        createCell(row, 6, "Busines Partner Number", style);
        createCell(row, 7, "Business Partner Name", style);
        createCell(row, 8, "Project Name", style);
        createCell(row, 9, "Group Name", style);
        createCell(row, 10, "Loan Class", style);
        createCell(row, 11, "Project Type", style);
        createCell(row, 12, "Project Capacity", style);
        createCell(row, 13, "Loan Type", style);
        createCell(row, 14, "Project Cost (InCR)", style);
        createCell(row, 15, "Loan Amount Request", style);
        createCell(row, 16, "ROI Requested By", style);
        createCell(row, 17, "Created By", style);
        createCell(row, 18, "Created At", style);
        createCell(row, 19, "Approved By", style);
        createCell(row, 20, "Approval Date", style);
        createCell(row, 22, "ICC Clearance Status", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
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

    private void writeDataLines() throws ParseException {
        int rowCount = 1;
        int serialNo = 0;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (LoanApplicationResource loanApplicationResource : resources) {
            log.info("Serial No: " + serialNo + " Excel Output Row for Loan :" + loanApplicationResource.getLoanApplication().getEnquiryNo().getId());
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            LoanApplication loanApplication = loanApplicationResource.getLoanApplication();
            Partner partner = loanApplicationResource.getPartner();
            serialNo++;

            //serialNumber
            createCell(row, columnCount++, serialNo, style);

            //processStatus
            if (loanApplication.getFinalDecisionStatus() != null)
                createCell(row, columnCount++, loanApplication.getFinalDecisionStatus().toString(), style);
            else
                createCell(row, columnCount++, "", style);

            //status
            if (loanApplication.getTechnicalStatusDescription() != null)
                createCell(row, columnCount++, loanApplication.getTechnicalStatusDescription().toString(), style);
            else
                createCell(row, columnCount++, "", style);

            //enquiryDate
            if (loanApplication.getLoanEnquiryDate() != null)
                createCell(row, columnCount++, loanApplication.getLoanEnquiryDate().toString(), style);
            else
                createCell(row, columnCount++, "", style);

            //enquiryNumber
            if (loanApplication.getEnquiryNo().getId() != null)
                createCell(row, columnCount++, loanApplication.getEnquiryNo().getId(), style);
            else
                createCell(row, columnCount++, "", style);

            //loanContractNumber
            if (loanApplication.getLoanContractId() != null)
                createCell(row, columnCount++, loanApplication.getLoanContractId().toString(), style);
            else
                createCell(row, columnCount++, "", style);

            //businesPartnerNumber
            if (loanApplication.getbusPartnerNumber() != null)
                createCell(row, columnCount++, loanApplication.getbusPartnerNumber(), style);
            else
                createCell(row, columnCount++, "", style);

            //businessPartnerName
            String partnerName = partner.getPartyName1() + " " + partner.getPartyName2();
            if (partnerName != null)
                createCell(row, columnCount++, partnerName, style);
            else
                createCell(row, columnCount++, "", style);


            //projectName
            if (loanApplication.getProjectName() != null)
                createCell(row, columnCount++, loanApplication.getProjectName(), style);
            else
                createCell(row, columnCount++, "", style);

            // groupName
            if (loanApplication.getGroupCompany() != null)
                createCell(row, columnCount++, loanApplication.getGroupCompany(), style);
            else
                createCell(row, columnCount++, "", style);

            //loanClass
            if (loanApplication.getLoanClass() != null)
                createCell(row, columnCount++, loanApplicationResource.getLoanClassDesc(), style);
            else
                createCell(row, columnCount++, "", style);

            //projectType
            if (loanApplication.getProjectType() != null)
                createCell(row, columnCount++, loanApplicationResource.getProjectTypeDesc(), style);
            else
                createCell(row, columnCount++, "", style);
            //projectCapacity
            if (loanApplication.getProjectCapacity() != null)
                createCell(row, columnCount++, loanApplication.getProjectCapacity().toString() + " " + loanApplication.getProjectCapacityUnit(), style);
            else
                createCell(row, columnCount++, "", style);
            //loanType
            if (loanApplication.getFinancingType() != null)
                createCell(row, columnCount++, loanApplicationResource.getFinancingTypeDesc(), style);
            else
                createCell(row, columnCount++, "", style);

            //projectCostInCR
            if (loanApplication.getProjectCost() != null)
                createCell(row, columnCount++, loanApplication.getProjectCost().toString(), style);
            else
                createCell(row, columnCount++, "", style);
            //loanAmountRequest
            if (loanApplication.getLoanContractAmount() != null)
                createCell(row, columnCount++, loanApplication.getLoanContractAmount(), style);
            else
                createCell(row, columnCount++, "", style);
            //roiRequestedBy

            //createdBy
            if (loanApplication.getCreatedByUserName() != null)
                createCell(row, columnCount++, loanApplication.getCreatedByUserName(), style);
            else
                createCell(row, columnCount++, "", style);
            //createdAt
            if (loanApplication.getCreatedAt() != null)
                createCell(row, columnCount++, loanApplication.getCreatedAt().toString(), style);
            else
                createCell(row, columnCount++, "", style);
            //approvedBy

            //approvalDate
//            if (loanApplication.getChangedOn() != null)
//                createCell(row, columnCount++, loanApplication.getCreatedAt(), style);
//            else
//                createCell(row, columnCount++, "", style);
//            //iccClearanceStatus

        }
    }




}
