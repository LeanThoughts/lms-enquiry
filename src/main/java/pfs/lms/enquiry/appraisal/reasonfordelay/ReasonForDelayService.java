package pfs.lms.enquiry.appraisal.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReasonForDelayService implements IReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ReasonForDelayRepository reasonForDelayRepository;

    @Override
    public ReasonForDelay createReasonForDelay(ReasonForDelayResource reasonForDelayResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(reasonForDelayResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        ReasonForDelay reasonForDelay = new ReasonForDelay();
        reasonForDelay.setLoanAppraisal(loanAppraisal);
        reasonForDelay.setDate(reasonForDelayResource.getDate());
        reasonForDelay.setHeldBy(reasonForDelayResource.getHeldBy());
        reasonForDelay.setStatus(reasonForDelayResource.getStatus());
        reasonForDelay.setStatusOfProposal(reasonForDelayResource.getStatusOfProposal());
        reasonForDelay = reasonForDelayRepository.save(reasonForDelay);
        return reasonForDelay;

    }

    @Override
    public ReasonForDelay updateReasonForDelay(ReasonForDelayResource reasonForDelayResource) {
        ReasonForDelay reasonForDelay =
                reasonForDelayRepository.findById(reasonForDelayResource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(reasonForDelayResource.getId().toString()));
        reasonForDelay.setDate(reasonForDelayResource.getDate());
        reasonForDelay.setHeldBy(reasonForDelayResource.getHeldBy());
        reasonForDelay.setStatus(reasonForDelayResource.getStatus());
        reasonForDelay.setStatusOfProposal(reasonForDelayResource.getStatusOfProposal());
        reasonForDelay = reasonForDelayRepository.save(reasonForDelay);
        return reasonForDelay;
    }
}
