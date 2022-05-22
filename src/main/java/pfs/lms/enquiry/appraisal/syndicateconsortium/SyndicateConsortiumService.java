package pfs.lms.enquiry.appraisal.syndicateconsortium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyndicateConsortiumService implements ISyndicateConsortiumService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final SyndicateConsortiumRepository syndicateConsortiumRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public SyndicateConsortium createSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(syndicateConsortiumResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

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

        if (syndicateConsortium.isLeadBank()) {
            List<SyndicateConsortium> syndicateConsortiums = syndicateConsortiumRepository.
                    findByLoanAppraisalId(loanAppraisal.getId());
            for(SyndicateConsortium sc : syndicateConsortiums) {
                if (sc.isLeadBank() && sc.getId() != syndicateConsortium.getId()) {
                    sc.setLeadBank(false);
                    syndicateConsortiumRepository.save(sc);
                }
            }
        }

        // Change Documents for Syndicate Consortium
        changeDocumentService.createChangeDocument(
                syndicateConsortium.getLoanAppraisal().getId(),
                syndicateConsortium.getId().toString(),
                syndicateConsortium.getLoanAppraisal().getId().toString(),
                syndicateConsortium.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                syndicateConsortium,
                "Created",
                username,
                "Appraisal", "Syndicate Consortium" );

        return syndicateConsortium;
    }

    @Override
    public SyndicateConsortium updateSyndicateConsortium(SyndicateConsortiumResource syndicateConsortiumResource, String username) throws CloneNotSupportedException {
        SyndicateConsortium syndicateConsortium = syndicateConsortiumRepository.findById(syndicateConsortiumResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(syndicateConsortiumResource.getId().toString()));
        Object oldSyndicateConsortium = syndicateConsortium.clone();

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

        if (syndicateConsortium.isLeadBank()) {
            List<SyndicateConsortium> syndicateConsortiums = syndicateConsortiumRepository.
                    findByLoanAppraisalId(syndicateConsortium.getLoanAppraisal().getId());
            for(SyndicateConsortium sc : syndicateConsortiums) {
                if (sc.isLeadBank() && sc.getId() != syndicateConsortium.getId()) {
                    sc.setLeadBank(false);
                    syndicateConsortiumRepository.save(sc);
                }
            }
        }

        // Change Documents for Syndicate Consortium
        changeDocumentService.createChangeDocument(
                syndicateConsortium.getLoanAppraisal().getId(),
                syndicateConsortium.getId().toString(),
                syndicateConsortium.getLoanAppraisal().getId().toString(),
                syndicateConsortium.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldSyndicateConsortium,
                syndicateConsortium,
                "Updated",
                username,
                "Appraisal", "Syndicate Consortium" );

        return syndicateConsortium;
    }

    @Override
    public SyndicateConsortium deleteSyndicateConsortium(UUID bankId, String username) {
        SyndicateConsortium syndicateConsortium = syndicateConsortiumRepository.findById(bankId)
                .orElseThrow(() -> new EntityNotFoundException(bankId.toString()));
        syndicateConsortiumRepository.deleteById(bankId);

        // Change Documents for Syndicate Consortium
        changeDocumentService.createChangeDocument(
                syndicateConsortium.getLoanAppraisal().getId(),
                syndicateConsortium.getId().toString(),
                null,
                syndicateConsortium.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                syndicateConsortium,
                "Deleted",
                username,
                "Appraisal", "Syndicate Consortium" );

        return syndicateConsortium;
    }
}
