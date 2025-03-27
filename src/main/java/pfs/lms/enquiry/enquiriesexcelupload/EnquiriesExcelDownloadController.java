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
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.repository.*;
import pfs.lms.enquiry.service.impl.LoanApplicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        List<ExcelEnquiry> excelEnquiryList = searchLoanApplications(  request, pageable);

        EnquiryListBDExcel enquiryListBDExcel = new EnquiryListBDExcel(excelEnquiryList);

        SXSSFWorkbook sxssfWorkbook = enquiryListBDExcel.exportSXSSWorkBook(response);

    }

    private List<ExcelEnquiry> searchLoanApplications(  HttpServletRequest request,
                                                        @PageableDefault(sort = "loanEnquiryDate", size = 999999, direction = Sort.Direction.DESC) Pageable pageable) {

        List<LoanApplication> loanApplicationList = loanApplicationService.getLoanEnquiries(request,pageable);
        List<ExcelEnquiry> excelEnquiryList = new ArrayList<>();

        for (LoanApplication loanApplication:loanApplicationList) {
            ExcelEnquiry excelEnquiry = new ExcelEnquiry();
            excelEnquiry.setSerialNumber( loanApplication.getLoanEnquiryId());
            excelEnquiry.setSapEnquiryId(loanApplication.getEnquiryNo().getId());
            excelEnquiry.setFunctionalStatus(loanApplication.getFunctionalStatus());
            excelEnquiry.setFunctionalStatusDescription(loanApplication.getFunctionalStatusDescription());
            if (loanApplication.getbusPartnerNumber() != null) {
                Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber()));
                if (partner != null) {
                    excelEnquiry.setBorrowerName((partner.getPartyName()));
                    excelEnquiry.setGroupName(partner.getGroupCompany());
                }
                else {
                    excelEnquiry.setGroupName(loanApplication.getGroupCompany());
                }
            }
            ProjectType projectType = projectTypeRepository.findByCode(loanApplication.getProjectType());
            LoanType loanType = loanTypeRepository.getLoanTypeByCode(loanApplication.getLoanType());
            FinancingType financingType = financingTypeRepository.findByCode(loanApplication.getProposalType());

            if ( projectType != null){
                excelEnquiry.setProjectType(projectType.getValue());
            }
            if (loanType != null) {
                excelEnquiry.setLoanType(loanType.getValue());
            }
            if (financingType != null) {
                excelEnquiry.setProposalType(financingType.getValue());
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
                    Double approvedAmountInCR = loanApplication.getAmountApproved() ; // 10000000;
                    excelEnquiry.setAmountRequested(approvedAmountInCR);
                }
            }

            excelEnquiry.setBorrowerRequestedROI(loanApplication.getBorrowerRequestedROI());
            excelEnquiry.setIccApprovedRoi(loanApplication.getIccApprovedRoi());
            excelEnquiry.setComments(loanApplication.getEnquiryRemarks());

            //Business Development Officer
            LoanPartner loanPartner = loanPartnerRepository.findByLoanApplicationAndBusinessPartnerIdAndRoleType(loanApplication,loanApplication.getbusPartnerNumber(),"ZLM034");
            if (loanPartner != null){
                Partner nodalOfficeBDPartner = partnerRepository.findByPartyNumber( Integer.parseInt(loanPartner.getBusinessPartnerId()));
                if (nodalOfficeBDPartner != null)
                excelEnquiry.setNodalOfficerBD(nodalOfficeBDPartner.getPartyName1() + " " + nodalOfficeBDPartner.getPartyName2());
            }
            excelEnquiryList.add(excelEnquiry);


        }

        return excelEnquiryList;
    }

}