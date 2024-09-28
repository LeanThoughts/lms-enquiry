package pfs.lms.enquiry.iccapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ICCApprovalServiceImpl implements ICCApprovalService {

    private  final LoanApplicationRepository loanApplicationRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ICCApproval create(ICCApproval iccApproval, String username) throws Exception {
        return null;
    }

    @Override
    public ICCApproval update(ICCApproval iccApproval, String username) throws Exception {
        return null;
    }

    @Override
    public ICCApproval processApprovedICC(ICCApproval iccApproval, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = iccApproval.getLoanApplication();
        Object oldLoanApplication;
        oldLoanApplication = loanApplication.clone();


        loanApplication.setFunctionalStatus(2);
        loanApplication.setFunctionalStatusDescription("ICC In-Principle Approved");

        // Change Documents for Loan Application
        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                loanApplication,
                oldLoanApplication,
                "Updated",
                username,
                "LoanApplication", "LoanApplication" );

        loanApplicationRepository.save(loanApplication);

        return iccApproval;
    }
}