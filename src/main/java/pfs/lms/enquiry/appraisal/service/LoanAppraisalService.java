package pfs.lms.enquiry.appraisal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.service.ILoanAppraisalService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanAppraisalService implements ILoanAppraisalService {

    @Autowired
    LoanAppraisalRepository loanAppraisalRepository;
    IChangeDocumentService changeDocumentService;

    @Override
    public LoanAppraisal createLoanAppraisal(LoanApplication loanApplication, String username) {
        LoanAppraisal loanAppraisal  = loanAppraisalRepository.findByLoanApplication(loanApplication).get();

        if (loanAppraisal == null) {
            loanAppraisal = new LoanAppraisal();
            loanAppraisal.setLoanApplication(loanApplication);
            loanAppraisal.setWorkFlowStatusCode(01); loanAppraisal.setWorkFlowStatusDescription("Created");
            loanAppraisal = loanAppraisalRepository.save(loanAppraisal);

            // Change Documents for Monitoring Header
            changeDocumentService.createChangeDocument(
                    loanAppraisal.getId(), loanAppraisal.getId().toString(), null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanAppraisal,
                    "Created",
                    username,
                    "Appraisal", "Header");
        }

        return loanAppraisal;
    }
}
