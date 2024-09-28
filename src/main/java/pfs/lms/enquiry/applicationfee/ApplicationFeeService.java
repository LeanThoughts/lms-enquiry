package pfs.lms.enquiry.applicationfee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalService;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationFeeService implements IApplicationFeeService {

    private  final LoanApplicationRepository loanApplicationRepository;
    private final IChangeDocumentService changeDocumentService;
    private final ApplicationFeeRepository applicationFeeRepository;

    @Override
    public ApplicationFee processRejection(ApplicationFee applicationFee, String username) throws CloneNotSupportedException {
        Object oldEnquiryAction = applicationFee.clone();
        applicationFee.setWorkFlowStatusCode(04);
        applicationFee.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                applicationFee.getId(), applicationFee.getId().toString(), null,
                applicationFee.getLoanApplication().getLoanContractId(),
                oldEnquiryAction,
                applicationFee,
                "Updated",
                username,
                "Application Fee", "Header");
        applicationFeeRepository.save(applicationFee);

        return applicationFee;
    }

    @Override
    public ApplicationFee processApprovedApplicationFee(ApplicationFee applicationFee, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = applicationFee.getLoanApplication();
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

        return applicationFee;
    }

    @Override
    public ApplicationFee create(ApplicationFee applicationFee, String username) throws Exception {
        return null;
    }

    @Override
    public ApplicationFee update(ApplicationFee applicationFee, String username) throws Exception {
        return null;
    }


}
