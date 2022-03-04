package pfs.lms.enquiry.appraisal.syndicateconsortium;

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
public class SyndicateConsortiumController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final SyndicateConsortiumRepository syndicateConsortiumRepository;

    @PostMapping("/syndicateConsortiums/create")
    public ResponseEntity<SyndicateConsortium> createSyndicateConsortium(
            @RequestBody SyndicateConsortiumResource syndicateConsortiumResource, HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(syndicateConsortiumResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        SyndicateConsortium syndicateConsortium = new SyndicateConsortium();
        syndicateConsortium.setLoanAppraisal(loanAppraisal);
        syndicateConsortium.setSerialNumber(syndicateConsortiumRepository.findByLoanAppraisalId(loanAppraisal.getId()).size() + 1);
        syndicateConsortium.setBankKey(syndicateConsortiumResource.getBankKey());
        syndicateConsortium.setBankName(syndicateConsortiumResource.getBankName());
        syndicateConsortium.setSanctionedAmount(syndicateConsortiumResource.getSanctionedAmount());
        syndicateConsortium.setCurrency(syndicateConsortiumResource.getCurrency());
        syndicateConsortium.setLeadBank(syndicateConsortiumResource.isLeadBank());
        syndicateConsortium.setApprovalStatus(syndicateConsortiumResource.getApprovalStatus());
        syndicateConsortium.setDocumentStage(syndicateConsortiumResource.getDocumentStage());
        syndicateConsortium.setDisbursedAmount(syndicateConsortiumResource.getDisbursedAmount());
        syndicateConsortium.setDisbursementStatus(syndicateConsortiumResource.getDisbursementStatus());
        syndicateConsortium = syndicateConsortiumRepository.save(syndicateConsortium);
        return ResponseEntity.ok(syndicateConsortium);
    }

    @PutMapping("/syndicateConsortiums/update")
    public ResponseEntity<SyndicateConsortium> updateSyndicateConsortium(
            @RequestBody SyndicateConsortiumResource syndicateConsortiumResource, HttpServletRequest request) {

        SyndicateConsortium syndicateConsortium = syndicateConsortiumRepository.findById(syndicateConsortiumResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(syndicateConsortiumResource.getId().toString()));
        syndicateConsortium.setBankKey(syndicateConsortiumResource.getBankKey());
        syndicateConsortium.setBankName(syndicateConsortiumResource.getBankName());
        syndicateConsortium.setSanctionedAmount(syndicateConsortiumResource.getSanctionedAmount());
        syndicateConsortium.setCurrency(syndicateConsortiumResource.getCurrency());
        syndicateConsortium.setLeadBank(syndicateConsortiumResource.isLeadBank());
        syndicateConsortium.setApprovalStatus(syndicateConsortiumResource.getApprovalStatus());
        syndicateConsortium.setDocumentStage(syndicateConsortiumResource.getDocumentStage());
        syndicateConsortium.setDisbursedAmount(syndicateConsortiumResource.getDisbursedAmount());
        syndicateConsortium.setDisbursementStatus(syndicateConsortiumResource.getDisbursementStatus());
        syndicateConsortium = syndicateConsortiumRepository.save(syndicateConsortium);
        return ResponseEntity.ok(syndicateConsortium);
    }
}
