package pfs.lms.enquiry.appraisal.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReasonForDelayService implements IReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ReasonForDelayRepository reasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ReasonForDelay createReasonForDelay(ReasonForDelayResource reasonForDelayResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(reasonForDelayResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),null,
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");
                    return obj;
                });
        ReasonForDelay reasonForDelay = new ReasonForDelay();
        reasonForDelay.setLoanAppraisal(loanAppraisal);
        reasonForDelay.setDate(reasonForDelayResource.getDate());
        reasonForDelay.setHeldBy(reasonForDelayResource.getHeldBy());
        reasonForDelay.setStatus(reasonForDelayResource.getStatus());
        reasonForDelay.setStatusOfProposal(reasonForDelayResource.getStatusOfProposal());
        reasonForDelay = reasonForDelayRepository.save(reasonForDelay);

        // Change Documents for Reason Delay
        changeDocumentService.createChangeDocument(
                reasonForDelay.getLoanAppraisal().getId(),
                reasonForDelay.getId().toString(),
                null,
                reasonForDelay.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                reasonForDelay,
                "Created",
                username,
                "Appraisal", "Reason For Delay" );

        return reasonForDelay;

    }

    @Override
    public ReasonForDelay updateReasonForDelay(ReasonForDelayResource reasonForDelayResource, String username) throws CloneNotSupportedException {
        ReasonForDelay reasonForDelay =
                reasonForDelayRepository.findById(reasonForDelayResource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(reasonForDelayResource.getId().toString()));

        Object oldReasonForDelay = reasonForDelay.clone();

        reasonForDelay.setDate(reasonForDelayResource.getDate());
        reasonForDelay.setHeldBy(reasonForDelayResource.getHeldBy());
        reasonForDelay.setStatus(reasonForDelayResource.getStatus());
        reasonForDelay.setStatusOfProposal(reasonForDelayResource.getStatusOfProposal());
        reasonForDelay = reasonForDelayRepository.save(reasonForDelay);

        // Change Documents for Reason Delay
        changeDocumentService.createChangeDocument(
                reasonForDelay.getLoanAppraisal().getId(),
                reasonForDelay.getId().toString(),
                null,
                reasonForDelay.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldReasonForDelay,
                reasonForDelay,
                "Updated",
                username,
                "Appraisal", "Reason For Delay" );

        return reasonForDelay;
    }
}
