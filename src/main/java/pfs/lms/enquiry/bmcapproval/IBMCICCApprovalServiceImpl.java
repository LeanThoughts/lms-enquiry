package pfs.lms.enquiry.bmcapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class IBMCICCApprovalServiceImpl implements IBMCICCApprovalService {

    private  final LoanApplicationRepository loanApplicationRepository;
    private final IChangeDocumentService changeDocumentService;
    private final BMCICCApprovalRepository BMCICCApprovalRepository;

    @Override
    public BmcICCApproval create(BmcICCApproval bmcICCApproval, String username) throws Exception {
        return null;
    }

    @Override
    public BmcICCApproval update(BmcICCApproval bmcIccApproval, String username) throws Exception {
        return null;
    }

    @Override
    public BmcICCApproval processRejection(BmcICCApproval bmcICCApproval, String username) throws CloneNotSupportedException {
        Object oldIccApproval = bmcICCApproval.clone();
        bmcICCApproval.setWorkFlowStatusCode(04);
        bmcICCApproval.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                bmcICCApproval.getId(), bmcICCApproval.getId().toString(), null,
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                bmcICCApproval,
                oldIccApproval,
                "Updated",
                username,
                "ICC In-Principal Approval", "Header");
        BMCICCApprovalRepository.save(bmcICCApproval);

        return bmcICCApproval;
    }

    @Override
    public BmcICCApproval processApprovedICC(BmcICCApproval BMCICCApproval, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = BMCICCApproval.getLoanApplication();
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

        return BMCICCApproval;
    }
}
