package pfs.lms.enquiry.reports.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
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

    private  final ILoanApplicationService loanApplicationService;


    @Override
    public LoanEnquiryDashboardDTO getLoanEnquiryDashboardData(LocalDate reportDate, HttpServletRequest request, Pageable pageable) {

        LocalDate dateFrom = LocalDate.of(2000, 01, 01);

        LoanEnquiryDashboardDTO loanEnquiryDashboardDTO = new LoanEnquiryDashboardDTO();

        // Get Applications by Enquiry Date
        List<LoanApplication> loanApplications = loanApplicationService.getLoanEnquiries(dateFrom, reportDate,request,pageable);

        for (LoanApplication loanApplication: loanApplications
             ) {
            switch ( loanApplication.getFunctionalStatus()) {
                case 1: //Enquiry Stage
                    loanEnquiryDashboardDTO.setEnquiryPendingICCCount(loanEnquiryDashboardDTO.getEnquiryPendingICCCount() + 1 );
                    loanEnquiryDashboardDTO.setEnquiryPendingICCAmount(loanEnquiryDashboardDTO.getEnquiryPendingICCAmount() +
                            loanApplication.getPfsDebtAmount()
                            );
                    break;
                case 2: //ICC In-Principle Approved
                    loanEnquiryDashboardDTO.setEnquiryClearedByICCCount(loanEnquiryDashboardDTO.getEnquiryClearedByICCCount() + 1 );
                    loanEnquiryDashboardDTO.setEnquiryClearedByICCAmount(loanEnquiryDashboardDTO.getEnquiryClearedByICCAmount() +
                            loanApplication.getPfsDebtAmount()
                    );
                case 11: //Application Fee
                    loanEnquiryDashboardDTO.setEnquiryApprovedByBoardCount(loanEnquiryDashboardDTO.getEnquiryApprovedByBoardCount() + 1 );
                    loanEnquiryDashboardDTO.setEnquiryApprovedByBoardAmount(loanEnquiryDashboardDTO.getEnquiryApprovedByBoardAmount() +
                            loanApplication.getPfsDebtAmount()
                    );
            }
        }

        return loanEnquiryDashboardDTO;
    }
}
