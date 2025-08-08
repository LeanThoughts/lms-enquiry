package pfs.lms.enquiry.reports.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BMCICCApprovalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.approvalbyicc.IApprovalByICCService;
import pfs.lms.enquiry.reports.enquiry.dto.LoanEnquiryDashboardDTO;
import pfs.lms.enquiry.reports.enquiry.service.IEnquiryDashboardService;
import pfs.lms.enquiry.service.ILoanApplicationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnquiryDashBoardService implements IEnquiryDashboardService {

    private final ILoanApplicationService loanApplicationService;
    private final IApprovalByICCService approvalByICCService;
    private final ApprovalByICCRepository approvalByICCRepository;
    private final ICCApprovalRepository iccApprovalRepository;


    @Override
    public LoanEnquiryDashboardDTO getLoanEnquiryDashboardData(LocalDate reportDate,
                                                               LocalDate enquiryCutOffDate,
                                                               LocalDate fYearStartDate,
                                                               LocalDate fYearEndDate,
                                                               HttpServletRequest request, Pageable pageable) {
        Double loanAmount = 0D;


        LoanEnquiryDashboardDTO loanEnquiryDashboardDTO = new LoanEnquiryDashboardDTO();

        log.info("EnquiryDashBoardService : Cutoff Date :" + enquiryCutOffDate.toString());
        log.info("EnquiryDashBoardService : Report Date :" + reportDate.toString());



        // Get Applications by Enquiry Date
        List<LoanApplication> loanApplications = loanApplicationService.getLoanEnquiries(enquiryCutOffDate,reportDate, request,pageable);

        log.info("Total Loan Applications Found : ", loanApplications.size());

        for (LoanApplication loanApplication: loanApplications
             ) {
            if (loanApplication.getPfsDebtAmount() != null)
                loanAmount = loanApplication.getPfsDebtAmount();

            log.info("Loan Enquiry No   : " + loanApplication.getEnquiryNo().getId());
            log.info("Functional Status : " + loanApplication.getFunctionalStatus());

            switch ( loanApplication.getFunctionalStatus()) {
                case 1: //Enquiry Stage
                    log.info( "Pending ICC Count : " + loanEnquiryDashboardDTO.getEnquiryPendingICCCount().toString() );

                    loanEnquiryDashboardDTO.setEnquiryPendingICCCount(loanEnquiryDashboardDTO.getEnquiryPendingICCCount() + 1 );
                    loanEnquiryDashboardDTO.setEnquiryPendingICCAmount(loanEnquiryDashboardDTO.getEnquiryPendingICCAmount() + loanAmount );

                    log.info( "Pending ICC Count : " + loanEnquiryDashboardDTO.getEnquiryPendingICCCount().toString() );
                    log.info( "Pending ICC Amount : " + loanEnquiryDashboardDTO.getEnquiryPendingICCCount().toString() );
                    break;
                case 12: //BMC Approval
//                    loanEnquiryDashboardDTO.setEnquiryApprovedByBoardCount(loanEnquiryDashboardDTO.getEnquiryApprovedByBoardCount() + 1 );
//                    loanEnquiryDashboardDTO.setEnquiryApprovedByBoardAmount(loanEnquiryDashboardDTO.getEnquiryApprovedByBoardAmount() + loanAmount );
                    break;

                    default:
                    log.info("Functional Status Others : " + loanApplication.getFunctionalStatus());

            }

            //ICC Approval
            ICCApproval iccApproval = iccApprovalRepository.findByLoanApplicationId(loanApplication.getId());
            if ( iccApproval != null) {

                ApprovalByICC approvalByICC = approvalByICCRepository.findByIccApprovalId(iccApproval.getId());
                if (approvalByICC != null){
                    if (approvalByICC.getMeetingDate() != null) {
                        if ((approvalByICC.getMeetingDate().isEqual(fYearStartDate) || approvalByICC.getMeetingDate().isAfter(fYearStartDate))
                                &&
                                (approvalByICC.getMeetingDate().isEqual(fYearEndDate) || approvalByICC.getMeetingDate().isBefore(fYearEndDate))
                        ) {

                            loanEnquiryDashboardDTO.setEnquiryClearedByICCCount(loanEnquiryDashboardDTO.getEnquiryClearedByICCCount() + 1);

                            if (approvalByICC.getAmountApproved() != null) {
                                loanEnquiryDashboardDTO.setEnquiryClearedByICCAmount(loanEnquiryDashboardDTO.getEnquiryClearedByICCAmount() + approvalByICC.getAmountApproved());
                            }
                        }
                    }
                }
            }

        }

        return loanEnquiryDashboardDTO;
    }
}
