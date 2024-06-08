package pfs.lms.enquiry.enquiriesexcelupload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnquiriesExcelUploadController {

    private final ExcelEnquiryRepository excelEnquiryRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PartnerRepository partnerRepository;

    private final ProjectTypeRepository projectTypeRepository;
    private final AssistanceTypeRepository assistanceTypeRepository;
    private final ProposalTypeRepository proposalTypeRepository;
    private final ICCReadinessStatusRepository iccReadinessStatusRepository;
    private final ICCStatusRepository iccStatusRepository;
    private final PresentedInICCRepository presentedInICCRepository;

    @PostMapping("/api/enquiriesExcelUpload")
    public ResponseEntity<List<ExcelEnquiry>> processExcelFile(@RequestParam(value = "file") MultipartFile file)
            throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        List<ExcelEnquiry> enquiries = new ArrayList<>();

        try {
            // Delete all previously processed enquiries from the excel sheet
            excelEnquiryRepository.deleteAll();
            // Add all rows to the enquiries list
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                if (row.getCell(0) != null && !row.getCell(0).toString().equals("")
                        && row.getCell(1) != null && !row.getCell(1).toString().equals("")) {

                    ExcelEnquiry enquiry = new ExcelEnquiry();
                    String comments = "";
                    enquiry.setSerialNumber(new Double(row.getCell(0).getNumericCellValue()).longValue());

                    enquiry.setBorrowerName(row.getCell(1).getStringCellValue().trim());
                    if(enquiry.getBorrowerName().trim().equals(""))
                        comments += "Borrower Name is empty\n";

                    enquiry.setGroupName(row.getCell(2).getStringCellValue().trim());
                    if(enquiry.getGroupName().trim().equals(""))
                        comments += "Group Name is empty\n";

                    enquiry.setProjectType(row.getCell(3).getStringCellValue().trim());
                    if(enquiry.getProjectType().trim().equals(""))
                        comments += "Project Type is empty\n";

                    enquiry.setTypeOfAssistance(row.getCell(4).getStringCellValue().trim());
                    if(enquiry.getTypeOfAssistance().trim().equals(""))
                        comments += "Type of Assistance is empty\n";

                    enquiry.setProposalType(row.getCell(5).getStringCellValue().trim());
                    if(enquiry.getProposalType().trim().equals(""))
                        comments += "Reason for ICC Status is empty\n";

                    enquiry.setIccReadinessStatus(row.getCell(9).getStringCellValue().trim());
                    if(enquiry.getIccReadinessStatus().trim().equals(""))
                        comments += "ICC Readiness Status is empty\n";

                    enquiry.setReasonForIccStatus(row.getCell(13).getStringCellValue().trim());
                    if(enquiry.getReasonForIccStatus().trim().equals(""))
                        comments += "Reason for ICC Status is empty\n";

                    //enquiry.setDateOfLeadGeneration(row.getCell(6).getDateCellValue());
                    enquiry.setAmountRequested(row.getCell(7).getNumericCellValue());
                    enquiry.setBorrowerRequestedROI(row.getCell(8).getNumericCellValue());
                    enquiry.setRemarksOnIccReadiness(row.getCell(10).getStringCellValue().trim());
                    enquiry.setPresentedInIcc(row.getCell(11).getStringCellValue().trim());
                    enquiry.setIccStatus(row.getCell(12).getStringCellValue().trim());
                    //enquiry.setIccClearanceDate(row.getCell(14).getDateCellValue());
                    enquiry.setIccMeetingNumber(row.getCell(15).getStringCellValue().trim());
                    enquiry.setAmountApproved(row.getCell(16).getNumericCellValue());
                    enquiry.setIccApprovedRoi(row.getCell(17).getNumericCellValue());
                    enquiry.setRemarksForIccApproval(row.getCell(18).getStringCellValue().trim());
                    enquiry.setComments(comments);
                    enquiries.add(enquiry);
                    System.out.println(enquiry.getBorrowerName());
                }
            }
        } catch (Exception e) {
            System.out.println("Errors occurred on row, ignoring");
        }

        // Save all enquiries
        enquiries = excelEnquiryRepository.saveAll(enquiries);

        return ResponseEntity.ok(enquiries);
    }

    @PostMapping("/api/createExcelEnquiries")
    public ResponseEntity<List<ExcelEnquiry>> createEnquiries() throws Exception {

        List<ExcelEnquiry> enquiries = excelEnquiryRepository.findAll();
        enquiries.forEach((enquiry) -> {
            LoanApplication loanApplication = loanApplicationRepository.findByLoanEnquiryId(enquiry.getSerialNumber());
            List<Partner> partners = partnerRepository.findBySearchString(enquiry.getBorrowerName());
            if (loanApplication == null) {
                loanApplication = new LoanApplication();
                // loanApplication.setEnquiryNo(enquiryNo); // TODO
                loanApplication.setLoanEnquiryId(enquiry.getSerialNumber());

                ProjectType pt = projectTypeRepository.findByValue(enquiry.getProjectType());
                loanApplication.setProjectType(pt == null? null: pt.getCode());

                AssistanceType at = assistanceTypeRepository.findByValue(enquiry.getTypeOfAssistance());
                loanApplication.setAssistanceType(at == null? null: at.getCode());

                ProposalType prt = proposalTypeRepository.findByValue(enquiry.getProposalType());
                loanApplication.setProposalType(prt == null? null: prt.getCode());

                ICCReadinessStatus irs = iccReadinessStatusRepository.findByValue(enquiry.getIccReadinessStatus());
                loanApplication.setIccReadinessStatus(irs == null? null: irs.getCode());

                loanApplication.setRemarksOnIccReadiness(enquiry.getRemarksOnIccReadiness());

                PresentedInICC pi = presentedInICCRepository.findByValue(enquiry.getPresentedInIcc());
                loanApplication.setPresentedInIcc(pi == null? null: pi.getCode());

                ICCStatus is = iccStatusRepository.findByValue(enquiry.getIccStatus());
                loanApplication.setICCStatus(is == null? null: is.getCode());

                loanApplication.setReasonForIccStatus(enquiry.getReasonForIccStatus());
                loanApplication.setICCMeetNumber(enquiry.getIccMeetingNumber());
                loanApplication.setICCRemarks(enquiry.getRemarksForIccApproval());
                loanApplication.setLoanEnquiryDate(enquiry.getDateOfLeadGeneration());
                loanApplication.setICCClearanceDate(enquiry.getIccClearanceDate());
                loanApplication.setLoanContractAmount(enquiry.getAmountRequested());
                // loanApplication.setBorrowerRequestedROI(); // TODO
                // loanApplication.setAmountApproved() // TODO
                loanApplication.setIccApprovedRoi(enquiry.getIccApprovedRoi());

                if (partners.size() == 0) {
                    Partner partner = new Partner();
                    partner.setPartyName1(enquiry.getBorrowerName());
                    partner.setGroupCompany(enquiry.getGroupName());
                    partner = partnerRepository.save(partner);
                    loanApplication = loanApplication.applicant(partner);
                }
                else if (partners.size() > 0) {
                    loanApplication = loanApplication.applicant(partners.get(0));
                }
                loanApplicationRepository.save(loanApplication);
            }
        });

        return ResponseEntity.ok(enquiries);
    }
}