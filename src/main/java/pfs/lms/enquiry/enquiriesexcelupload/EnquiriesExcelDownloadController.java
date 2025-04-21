package pfs.lms.enquiry.enquiriesexcelupload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetail;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetailRepository;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelay;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelayRepository;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICC;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICCRepository;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.service.impl.LoanApplicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnquiriesExcelDownloadController {
    private final LoanPartnerRepository loanPartnerRepository;
    private final FinancingTypeRepository financingTypeRepository;

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
    private final FunctionalStatusRepository functionalStatusRepository;

    private final EnquiryActionRepository enquiryActionRepository;

    private final ICCApprovalRepository iccApprovalRepository;
    private final ApprovalByICCRepository approvalByICCRepository;
    private final RejectedByICCRepository rejectedByICCRepository;
    private final ICCFurtherDetailRepository iccFurtherDetailRepository;
    private final ICCReasonForDelayRepository iccReasonForDelayRepository;

    @GetMapping(value = "/api/enquiriesExcelDownload")
    public void searchAndGenerateExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String enquiryDateFrom,
            @RequestParam(required = false) String enquiryDateTo ,
            @PageableDefault(sort = "loanEnquiryDate", size = 99999, direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) throws IOException, ParseException {

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=LoanEnquiryListReport_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFrom = null;
        LocalDate dateTo = null;
        if (enquiryDateFrom != null && !enquiryDateFrom.isEmpty()) {
            dateFrom = LocalDate.parse(enquiryDateFrom, formatter);
        }
        if (enquiryDateTo != null && !enquiryDateTo.isEmpty()) {
            dateTo = LocalDate.parse(enquiryDateTo, formatter);
        }
        List<ExcelEnquiry> excelEnquiryList = searchLoanApplications(dateFrom, dateTo, request, pageable);

        EnquiryListBDExcel enquiryListBDExcel = new EnquiryListBDExcel(excelEnquiryList);

        SXSSFWorkbook sxssfWorkbook = enquiryListBDExcel.exportSXSSWorkBook(response);

    }

    private List<ExcelEnquiry> searchLoanApplications(LocalDate enquiryDateForm,
                                                      LocalDate enquiryDateTo,
                                                      HttpServletRequest request,
                                                      @PageableDefault(sort = "loanEnquiryDate", size = 999999, direction = Sort.Direction.DESC) Pageable pageable) {

        List<LoanApplication> loanApplicationList = loanApplicationService.getLoanEnquiries(enquiryDateForm, enquiryDateTo, request, pageable);
        List<ExcelEnquiry> excelEnquiryList = new ArrayList<>();

        for (LoanApplication loanApplication:loanApplicationList) {
            try {
                ExcelEnquiry excelEnquiry = new ExcelEnquiry();
                excelEnquiry.setSerialNumber(loanApplication.getLoanEnquiryId());
                excelEnquiry.setSapEnquiryId(loanApplication.getEnquiryNo().getId());
                excelEnquiry.setFunctionalStatus(loanApplication.getFunctionalStatus());

                if (loanApplication.getFunctionalStatus() == null)
                    excelEnquiry.setFunctionalStatusDescription("--");
                else {
                    FunctionalStatus functionalStatus = functionalStatusRepository.findByCode(loanApplication.getFunctionalStatus());
                    if (functionalStatus == null)
                        excelEnquiry.setFunctionalStatusDescription(loanApplication.getFunctionalStatus().toString());
                    else
                        excelEnquiry.setFunctionalStatusDescription(functionalStatus.getValue());
                }

                if (loanApplication.getbusPartnerNumber() != null) {
                    Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber()));
                    if (partner != null) {
                        excelEnquiry.setBorrowerName((partner.getPartyName()));
                        excelEnquiry.setGroupName(partner.getGroupCompany());
                    }
                    else {
                        excelEnquiry.setBorrowerName(loanApplication.getProjectName());
                        excelEnquiry.setGroupName("---");
                    }
                }
                else {
                    excelEnquiry.setBorrowerName(loanApplication.getProjectName());
                    excelEnquiry.setGroupName("--");
                }

                if (loanApplication.getProjectType() == null)
                    excelEnquiry.setProjectType("--");
                else {
                    ProjectType projectType = projectTypeRepository.findByCode(loanApplication.getProjectType());
                    if (projectType != null)
                        excelEnquiry.setProjectType(projectType.getValue());
                    else
                        excelEnquiry.setProjectType(loanApplication.getProjectType());
                }

                if (loanApplication.getLoanType() == null)
                    excelEnquiry.setLoanType("--");
                else {
                    LoanType loanType = loanTypeRepository.getLoanTypeByCode(loanApplication.getLoanType());
                    if (loanType != null)
                        excelEnquiry.setLoanType(loanType.getValue());
                    else
                        excelEnquiry.setLoanType(loanApplication.getLoanType());
                }

                if (loanApplication.getProposalType() == null)
                    excelEnquiry.setProposalType("--");
                else {
                    FinancingType financingType = financingTypeRepository.findByCode(loanApplication.getProposalType());
                    if (financingType != null)
                        excelEnquiry.setProposalType(financingType.getValue());
                    else
                        excelEnquiry.setProposalType(loanApplication.getProposalType());
                }

                excelEnquiry.setIccReadinessStatus(loanApplication.getIccReadinessStatus());
                excelEnquiry.setRemarksOnIccReadiness(loanApplication.getRemarksOnIccReadiness());
                excelEnquiry.setPresentedInIcc(loanApplication.getPresentedInIcc());
                excelEnquiry.setIccStatus(loanApplication.getiCCStatus());
                excelEnquiry.setIccMeetingNumber(loanApplication.getiCCMeetNumber());
                excelEnquiry.setReasonForIccStatus(loanApplication.getReasonForIccStatus());
                excelEnquiry.setRemarksForIccApproval(loanApplication.getiCCRemarks());

                excelEnquiry.setDateOfLeadGeneration(loanApplication.getLoanEnquiryDate());
                excelEnquiry.setIccClearanceDate(loanApplication.getiCCClearanceDate());

                if (loanApplication.getPfsDebtAmount() != null) {
                    if (loanApplication.getPfsDebtAmount() > 0) {
                        Double requestAmountInCR = loanApplication.getPfsDebtAmount();  // 10000000;
                        excelEnquiry.setAmountRequested(requestAmountInCR);
                    }
                }

                if (loanApplication.getAmountApproved() != null) {

                    if (loanApplication.getAmountApproved() > 0) {
                        Double approvedAmountInCR = loanApplication.getAmountApproved(); // 10000000;
                        excelEnquiry.setAmountRequested(approvedAmountInCR);
                    }
                }

                excelEnquiry.setBorrowerRequestedROI(loanApplication.getBorrowerRequestedROI());


                excelEnquiry.setIccReadinessStatus("Under Process");
                EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplicationId(loanApplication.getId());
                if (enquiryAction != null && enquiryAction.getWorkFlowStatusCode() == 3)
                    excelEnquiry.setIccReadinessStatus("Ready for ICC-In Principle");

                excelEnquiry.setPresentedInIcc("No");
                if (enquiryAction != null && enquiryAction.getWorkFlowStatusCode() == 3) {
                    ICCApproval iccApproval = iccApprovalRepository.findByLoanApplicationId(loanApplication.getId());
                    if (iccApproval != null) {
                        ApprovalByICC approvalByICC = approvalByICCRepository.findByIccApprovalId(iccApproval.getId());
                        if (approvalByICC != null) {
                            excelEnquiry.setPresentedInIcc("Yes");
                            excelEnquiry.setIccStatus("Cleared");
                            excelEnquiry.setReasonForIccStatus(approvalByICC.getRemarks());
                            excelEnquiry.setIccClearanceDate(approvalByICC.getMeetingDate());
                            excelEnquiry.setIccMeetingNumber(approvalByICC.getMeetingNumber());
                        }
                        else {
                            RejectedByICC rejectedByICC = rejectedByICCRepository.findByIccApprovalId(iccApproval.getId());
                            if (rejectedByICC != null) {
                                excelEnquiry.setPresentedInIcc("Yes");
                                excelEnquiry.setIccStatus("Dropped");
                                excelEnquiry.setReasonForIccStatus(rejectedByICC.getReasonForRejection());
                                excelEnquiry.setIccMeetingNumber(rejectedByICC.getMeetingNumber());
                            }
                        }
                    }
                    if (!(excelEnquiry.getIccStatus().equals("Cleared") || excelEnquiry.getIccStatus().equals("Dropped"))) {
                        List<ICCFurtherDetail> furtherDetails = iccFurtherDetailRepository.findByIccApprovalId(iccApproval.getId());
                        if (furtherDetails != null && furtherDetails.size() > 0)
                            excelEnquiry.setIccStatus("Deferred");
                        else {
                            List<ICCReasonForDelay> reasonForDelays = iccReasonForDelayRepository.findByIccApprovalId(iccApproval.getId());
                            if (reasonForDelays != null && reasonForDelays.size() > 0)
                                excelEnquiry.setIccStatus("On Hold");
                            else
                                excelEnquiry.setIccStatus("Ready for ICC-In Principle");
                        }
                    }
                }

//                excelEnquiry.setIccApprovedRoi(loanApplication.getIccApprovedRoi());
//                excelEnquiry.setComments(loanApplication.getEnquiryRemarks());

                //Business Development Officer
                LoanPartner loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerIdAndRoleType(loanApplication, loanApplication.getbusPartnerNumber(), "ZLM034");
                if (loanPartner != null) {
                    Partner nodalOfficeBDPartner = partnerRepository.findByPartyNumber(Integer.parseInt(loanPartner.getBusinessPartnerId()));
                    if (nodalOfficeBDPartner != null)
                        excelEnquiry.setNodalOfficerBD(nodalOfficeBDPartner.getPartyName1() + " " + nodalOfficeBDPartner.getPartyName2());
                }
                else
                    excelEnquiry.setNodalOfficerBD("--");

                excelEnquiryList.add(excelEnquiry);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return excelEnquiryList;
    }

}