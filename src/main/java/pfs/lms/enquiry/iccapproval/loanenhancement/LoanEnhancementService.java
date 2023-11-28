package pfs.lms.enquiry.iccapproval.loanenhancement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanEnhancementService implements ILoanEnhancementService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final LoanEnhancementRepository loanEnhancementRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public LoanEnhancement create(LoanEnhancementResource loanEnhancementResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(loanEnhancementResource.getLoanApplicationId());

        ICCApproval iccApproval = iccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ICCApproval obj = new ICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = iccApprovalRepository.save(obj);

                    // Change Documents for Appraisal Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Appraisal", "Header");

                    return obj;
                });

        LoanEnhancement loanEnhancement = new LoanEnhancement();
        loanEnhancement.setIccApproval(iccApproval);
        loanEnhancement.setSerialNumber(loanEnhancementRepository.findByIccApprovalId(iccApproval.getId()).size() + 1);
        loanEnhancement.setIccMeetingNumber(loanEnhancementResource.getIccMeetingNumber());
        loanEnhancement.setIccClearanceDate(loanEnhancementResource.getIccClearanceDate());
        loanEnhancement.setRevisedProjectCost(loanEnhancementResource.getRevisedProjectCost());
        loanEnhancement.setRevisedEquity(loanEnhancementResource.getRevisedEquity());
        loanEnhancement.setRevisedContractAmount(loanEnhancementResource.getRevisedContractAmount());
        loanEnhancement.setRevisedCommercialOperationsDate(loanEnhancementResource.getRevisedCommercialOperationsDate());
        loanEnhancement.setReviseRepaymentStartDate(loanEnhancementResource.getReviseRepaymentStartDate());
        loanEnhancement.setRemarks(loanEnhancementResource.getRemarks());
        loanEnhancement = loanEnhancementRepository.save(loanEnhancement);
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanApplication.getLoanContractId(),
//                null,
//                loanPartner,
//                "Created",
//                username,
//                "Appraisal", "Loan Partner");

        return loanEnhancement;
    }

    @Override
    public LoanEnhancement update(LoanEnhancementResource loanEnhancementResource, String username)
            throws CloneNotSupportedException {

        LoanEnhancement loanEnhancement = loanEnhancementRepository.findById(loanEnhancementResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(loanEnhancementResource.getId().toString()));

        Object oldICCFurtherDetail = loanEnhancement.clone();

        loanEnhancement.setIccMeetingNumber(loanEnhancementResource.getIccMeetingNumber());
        loanEnhancement.setIccClearanceDate(loanEnhancementResource.getIccClearanceDate());
        loanEnhancement.setRevisedProjectCost(loanEnhancementResource.getRevisedProjectCost());
        loanEnhancement.setRevisedEquity(loanEnhancementResource.getRevisedEquity());
        loanEnhancement.setRevisedContractAmount(loanEnhancementResource.getRevisedContractAmount());
        loanEnhancement.setRevisedCommercialOperationsDate(loanEnhancementResource.getRevisedCommercialOperationsDate());
        loanEnhancement.setReviseRepaymentStartDate(loanEnhancementResource.getReviseRepaymentStartDate());
        loanEnhancement.setRemarks(loanEnhancementResource.getRemarks());
        loanEnhancement = loanEnhancementRepository.save(loanEnhancement);

        // Change Documents for  Loan Partner
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanPartner.getLoanApplication().getLoanContractId(),
//                oldLoanPartner,
//                loanPartner,
//                "Updated",
//                username,
//                "Appraisal", "Loan Partner");

        return loanEnhancement;
    }

    @Override
    public LoanEnhancement delete(UUID loanEnhancementId, String username) {
        LoanEnhancement loanEnhancement = loanEnhancementRepository.findById(loanEnhancementId)
                .orElseThrow(() -> new EntityNotFoundException(loanEnhancementId.toString()));
        loanEnhancementRepository.delete(loanEnhancement);
        return loanEnhancement;
    }
}
