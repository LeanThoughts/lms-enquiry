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
import pfs.lms.enquiry.service.impl.LoanApplicationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnquiriesExcelUploadController {

    private final ExcelEnquiryRepository excelEnquiryRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PartnerRepository partnerRepository;

    private final ProjectTypeRepository projectTypeRepository;
    private final ProposalTypeRepository proposalTypeRepository;
    private final ICCReadinessStatusRepository iccReadinessStatusRepository;
    private final ICCStatusRepository iccStatusRepository;
    private final PresentedInICCRepository presentedInICCRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanApplicationService loanApplicationService;

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
                if (row.getCell(0) != null && !row.getCell(0).toString().equals("")) {
//                        && row.getCell(1) != null && !row.getCell(1).toString().equals("")) {

                    ExcelEnquiry enquiry = new ExcelEnquiry();
                    String comments = "";

                    enquiry.setSerialNumber(new Double(row.getCell(0).getNumericCellValue()).longValue());
                    if(enquiry.getSerialNumber() == null || enquiry.getSerialNumber() == 0)
                        comments += "Serial Number is missing.\n";

                    try {
                        enquiry.setSapEnquiryId(new Double(row.getCell(1).getNumericCellValue()).longValue());
                    }
                    catch (IllegalStateException ex) {
                        enquiry.setSapEnquiryId(0L);
                    }

                    enquiry.setBorrowerName(row.getCell(2).getStringCellValue());
                    if(enquiry.getBorrowerName() == null || enquiry.getBorrowerName().trim().equals(""))
                        comments += "Borrower Name is missing.\n";

                    enquiry.setGroupName(row.getCell(3).getStringCellValue());
                    if(enquiry.getGroupName() == null || enquiry.getGroupName().trim().equals(""))
                        comments += "Group Name is missing.\n";

                    enquiry.setProjectType(row.getCell(4).getStringCellValue().trim());
                    if(enquiry.getProjectType().trim().equals(""))
                        comments += "Project Type is missing.\n";
                    else if(projectTypeRepository.findByValue(enquiry.getProjectType()) == null)
                        comments += "Project Type is invalid. Provide valid input for Project Type.\n";

                    enquiry.setLoanType(row.getCell(5).getStringCellValue().trim());
                    if(enquiry.getLoanType().trim().equals(""))
                        comments += "Type of Loan is missing.\n";
                    else if(loanTypeRepository.getLoanTypeByValue(enquiry.getLoanType()) == null)
                        comments += "Loan Type is invalid. Provide valid input for Loan Type.\n";

                    enquiry.setProposalType(row.getCell(6).getStringCellValue().trim());
                    if(enquiry.getProposalType().trim().equals(""))
                        comments += "Proposal Type is missing.\n";
                    else if(proposalTypeRepository.findByValue(enquiry.getProposalType()) == null)
                        comments += "Proposal Type is invalid. Provide valid input for Proposal Type.\n";

                    enquiry.setIccReadinessStatus(row.getCell(10).getStringCellValue().trim());
                    if(!enquiry.getIccReadinessStatus().trim().equals("") &&
                            iccReadinessStatusRepository.findByValue(enquiry.getIccReadinessStatus()) == null)
                        comments += "Provide valid input for ICC Readiness Status.\n";

                    enquiry.setAmountRequested(row.getCell(8).getNumericCellValue());
                    if(enquiry.getAmountRequested() == null || enquiry.getAmountRequested() == 0)
                        comments += "Provide valid input for Requested Amount..\n";

                    enquiry.setIccStatus(row.getCell(13).getStringCellValue().trim());
                    if(!enquiry.getIccStatus().trim().equals("") &&
                            iccStatusRepository.findByValue(enquiry.getIccStatus()) == null)
                        comments += "Provide valid input for ICC Status.\n";

                    String enquiryDate = row.getCell(7).getStringCellValue();
                    if(enquiryDate.equals(""))
                        comments += "Provide valid input for Date of Lead Generation.\n";
                    else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        formatter = formatter.withLocale(Locale.UK);
                        enquiry.setDateOfLeadGeneration(LocalDate.parse(enquiryDate, formatter));
                    }

                    List<LoanApplication> loanApplications = loanApplicationRepository.
                            findByProjectTypeAndLoanTypeAndProposalTypeAndLoanContractAmountAndLoanEnquiryDate(
                                    projectTypeRepository.findByValue(enquiry.getProjectType()).getCode(),
                                    loanTypeRepository.getLoanTypeByValue(enquiry.getLoanType()).getCode(),
                                    proposalTypeRepository.findByValue(enquiry.getProposalType()).getCode(),
                                    enquiry.getAmountRequested(),
                                    enquiry.getDateOfLeadGeneration());
                    if(loanApplications.size() > 0 && !loanApplications.get(0).getEnquiryNo().getId().equals(enquiry
                            .getSapEnquiryId())) {
                        comments += "Similar loan applications exists (Project type, Assistance type, Proposal type, " +
                                " and Requested amount and Date of lead generation.\n";
                    }

                    enquiry.setReasonForIccStatus(row.getCell(14).getStringCellValue().trim());
                    enquiry.setBorrowerRequestedROI(row.getCell(9).getNumericCellValue());
                    enquiry.setRemarksOnIccReadiness(row.getCell(11).getStringCellValue().trim());
                    enquiry.setPresentedInIcc(row.getCell(12).getStringCellValue().trim());

                    String clearanceDate = row.getCell(15).getStringCellValue();
                    if(!clearanceDate.equals("")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        formatter = formatter.withLocale(Locale.UK);
                        enquiry.setIccClearanceDate(LocalDate.parse(clearanceDate, formatter));
                    }

                    enquiry.setIccMeetingNumber(row.getCell(16).getStringCellValue().trim());
                    enquiry.setAmountApproved(row.getCell(17).getNumericCellValue());
                    enquiry.setIccApprovedRoi(row.getCell(18).getNumericCellValue());
                    enquiry.setRemarksForIccApproval(row.getCell(19).getStringCellValue().trim());

                    enquiry.setComments(comments);
                    enquiries.add(enquiry);
                    System.out.println(enquiry.getBorrowerName());
                }
            }
        }
        catch (Exception e) {
            throw new Exception(e.getMessage() + "Upload failed due to file format errors !!");
        }

        // Save all enquiries
        enquiries = excelEnquiryRepository.saveAll(enquiries);

        return ResponseEntity.ok(enquiries);
    }

    @PostMapping("/api/createExcelEnquiries")
    public ResponseEntity<List<ExcelEnquiry>> createEnquiries(HttpServletRequest request) throws Exception {

        List<ExcelEnquiry> enquiries = excelEnquiryRepository.findAll().stream()
                .sorted(Comparator.comparing(ExcelEnquiry::getSerialNumber)).collect(Collectors.toList());

        final boolean[] errors = {false};
        enquiries.forEach((enquiry) -> {
            if (!enquiry.getComments().trim().equals(""))
                errors[0] = true;
        });
        if (errors[0])
            throw new Exception("Found errors in the excel sheet. Cannot proceed unless they are fixed.");

        enquiries.forEach((enquiry) -> {
            // LoanApplication loanApplication = loanApplicationRepository.findByLoanEnquiryId(enquiry.getSerialNumber());
            LoanApplication loanApplication = loanApplicationRepository
                    .findByEnquiryNo(new EnquiryNo(enquiry.getSapEnquiryId()));
            List<Partner> partners = partnerRepository.findBySearchString(enquiry.getBorrowerName());
            // Create new enquiry if loanApplication is null
            if (loanApplication == null) {
                loanApplication = new LoanApplication();
                loanApplication.setEnquiryNo(new EnquiryNo());
                loanApplication.setTechnicalStatus(1);
                loanApplication.setTechnicalStatusDescription("Created");
                loanApplication.setFunctionalStatus(1);
                loanApplication.setFunctionalStatusDescription("Enquiry Stage");
                loanApplication.setProjectCapacity(0.00);
                loanApplication.setProjectCapacityUnit("MW");
                loanApplication.created(null, request.getUserPrincipal().getName());
            }
            else {
                loanApplication.setTechnicalStatus(2);
                loanApplication.setTechnicalStatusDescription("Changed");
                loanApplication.modified(null, request.getUserPrincipal().getName());
            }

            loanApplication.setLoanEnquiryId(enquiry.getSerialNumber());
            loanApplication.setGroupCompany(enquiry.getGroupName());

            ProjectType pt = projectTypeRepository.findByValue(enquiry.getProjectType());
            loanApplication.setProjectType(pt == null? null: pt.getCode());

            LoanType lt = loanTypeRepository.getLoanTypeByValue(enquiry.getLoanType());
            loanApplication.setLoanType(lt == null? null: lt.getCode());

            ProposalType prt = proposalTypeRepository.findByValue(enquiry.getProposalType());
            loanApplication.setProposalType(prt == null? null: prt.getCode());
            loanApplication.setFinancingType(prt == null? null: prt.getCode());

            ICCReadinessStatus irs = iccReadinessStatusRepository.findByValue(enquiry.getIccReadinessStatus());
            loanApplication.setIccReadinessStatus(irs == null? null: irs.getCode());

            PresentedInICC pi = presentedInICCRepository.findByValue(enquiry.getPresentedInIcc());
            loanApplication.setPresentedInIcc(pi == null? null: pi.getCode());

            ICCStatus is = iccStatusRepository.findByValue(enquiry.getIccStatus());
            loanApplication.setICCStatus(is == null? null: is.getCode());

            loanApplication.setBorrowerRequestedROI(enquiry.getBorrowerRequestedROI());
            loanApplication.setRemarksOnIccReadiness(enquiry.getRemarksOnIccReadiness());
            loanApplication.setReasonForIccStatus(enquiry.getReasonForIccStatus());
            loanApplication.setICCMeetNumber(enquiry.getIccMeetingNumber());
            loanApplication.setICCRemarks(enquiry.getRemarksForIccApproval());
            loanApplication.setLoanEnquiryDate(enquiry.getDateOfLeadGeneration());
            loanApplication.setICCClearanceDate(enquiry.getIccClearanceDate());
            loanApplication.setLoanContractAmount(enquiry.getAmountRequested());
            loanApplication.setBorrowerRequestedROI(enquiry.getBorrowerRequestedROI() * 100);
            loanApplication.setAmountApproved(enquiry.getAmountApproved());
            loanApplication.setIccApprovedRoi(enquiry.getIccApprovedRoi() * 100);
            loanApplication.setExpectedInterestRate(enquiry.getBorrowerRequestedROI());
            loanApplication.setPfsDebtAmount(enquiry.getAmountRequested());

            loanApplication.setProjectName(enquiry.getBorrowerName());

            if (partners.size() == 0) {
                Partner partner = new Partner();
                partner.setPartyName1(enquiry.getBorrowerName());
                partner.setGroupCompany(enquiry.getGroupName());
                partner = partnerRepository.save(partner);
                loanApplication = loanApplication.applicant(partner);
            }
            else {
                loanApplication = loanApplication.applicant(partners.get(0));
            }

            loanApplicationRepository.save(loanApplication);

            if (enquiry.getSapEnquiryId() == 0) {
                enquiry.setSapEnquiryId(loanApplication.getEnquiryNo().getId());
                excelEnquiryRepository.save(enquiry);
            }
        });

        return ResponseEntity.ok(enquiries);
    }
}