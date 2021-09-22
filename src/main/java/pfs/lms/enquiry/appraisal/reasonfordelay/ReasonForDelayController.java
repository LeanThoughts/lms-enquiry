package pfs.lms.enquiry.appraisal.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ReasonForDelayController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ReasonForDelayRepository reasonForDelayRepository;

    @PostMapping("/reasonForDelays/create")
    public ResponseEntity<ReasonForDelay> createProjectAppraisalCompletion(
                @RequestBody ReasonForDelayResource reasonForDelayResource,
                HttpServletRequest request) {

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
        return ResponseEntity.ok(reasonForDelay);
    }

    @PutMapping("/reasonForDelays/update")
    public ResponseEntity<ReasonForDelay> updateProjectAppraisalCompletion(
                @RequestBody ReasonForDelayResource reasonForDelayResource,
                HttpServletRequest request) {

        ReasonForDelay reasonForDelay =
                reasonForDelayRepository.findById(reasonForDelayResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(reasonForDelayResource.getId().toString()));
        reasonForDelay.setDate(reasonForDelayResource.getDate());
        reasonForDelay.setHeldBy(reasonForDelayResource.getHeldBy());
        reasonForDelay.setStatus(reasonForDelayResource.getStatus());
        reasonForDelay.setStatusOfProposal(reasonForDelayResource.getStatusOfProposal());
        reasonForDelay = reasonForDelayRepository.save(reasonForDelay);
        return ResponseEntity.ok(reasonForDelay);
    }
}
