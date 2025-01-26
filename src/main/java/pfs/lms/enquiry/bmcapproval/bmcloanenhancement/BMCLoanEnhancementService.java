package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BMCICCApproval;
import pfs.lms.enquiry.bmcapproval.BMCICCApprovalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BMCLoanEnhancementService implements IBMCLoanEnhancementService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository bmcIccApprovalRepository;
    private final BMCLoanEnhancementRepository bmcLoanEnhancementRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BMCLoanEnhancement create(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmcLoanEnhancementResource.getLoanApplicationId());

        BMCICCApproval BMCICCApproval = bmcIccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BMCICCApproval obj = new BMCICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmcIccApprovalRepository.save(obj);

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

        BMCLoanEnhancement bmcLoanEnhancement = new BMCLoanEnhancement();
        bmcLoanEnhancement.setBmcICCApproval(BMCICCApproval);
        bmcLoanEnhancement.setSerialNumber(bmcLoanEnhancementRepository.findByBmcICCApprovalId(BMCICCApproval.getId()).size() + 1);
        bmcLoanEnhancement.setIccMeetingNumber(bmcLoanEnhancementResource.getIccMeetingNumber());
        bmcLoanEnhancement.setIccClearanceDate(bmcLoanEnhancementResource.getIccClearanceDate());
        bmcLoanEnhancement.setRevisedProjectCost(bmcLoanEnhancementResource.getRevisedProjectCost());
        bmcLoanEnhancement.setRevisedEquity(bmcLoanEnhancementResource.getRevisedEquity());
        bmcLoanEnhancement.setRevisedContractAmount(bmcLoanEnhancementResource.getRevisedContractAmount());
        bmcLoanEnhancement.setRevisedCommercialOperationsDate(bmcLoanEnhancementResource.getRevisedCommercialOperationsDate());
        bmcLoanEnhancement.setReviseRepaymentStartDate(bmcLoanEnhancementResource.getReviseRepaymentStartDate());
        bmcLoanEnhancement.setRemarks(bmcLoanEnhancementResource.getRemarks());
        bmcLoanEnhancement = bmcLoanEnhancementRepository.save(bmcLoanEnhancement);
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

        return bmcLoanEnhancement;
    }

    @Override
    public BMCLoanEnhancement update(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username)
            throws CloneNotSupportedException {

        BMCLoanEnhancement bmcLoanEnhancement = bmcLoanEnhancementRepository.findById(bmcLoanEnhancementResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcLoanEnhancementResource.getId().toString()));

        Object oldICCFurtherDetail = bmcLoanEnhancement.clone();

        bmcLoanEnhancement.setIccMeetingNumber(bmcLoanEnhancementResource.getIccMeetingNumber());
        bmcLoanEnhancement.setIccClearanceDate(bmcLoanEnhancementResource.getIccClearanceDate());
        bmcLoanEnhancement.setRevisedProjectCost(bmcLoanEnhancementResource.getRevisedProjectCost());
        bmcLoanEnhancement.setRevisedEquity(bmcLoanEnhancementResource.getRevisedEquity());
        bmcLoanEnhancement.setRevisedContractAmount(bmcLoanEnhancementResource.getRevisedContractAmount());
        bmcLoanEnhancement.setRevisedCommercialOperationsDate(bmcLoanEnhancementResource.getRevisedCommercialOperationsDate());
        bmcLoanEnhancement.setReviseRepaymentStartDate(bmcLoanEnhancementResource.getReviseRepaymentStartDate());
        bmcLoanEnhancement.setRemarks(bmcLoanEnhancementResource.getRemarks());
        bmcLoanEnhancement = bmcLoanEnhancementRepository.save(bmcLoanEnhancement);

        BMCICCApproval bmcICCApproval = bmcIccApprovalRepository.getOne(bmcLoanEnhancement.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcIccApprovalRepository.save(bmcICCApproval);

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

        return bmcLoanEnhancement;
    }

    @Override
    public BMCLoanEnhancement delete(UUID bmcLoanEnhancementId, String username) {
        BMCLoanEnhancement bmcLoanEnhancement = bmcLoanEnhancementRepository.findById(bmcLoanEnhancementId)
                .orElseThrow(() -> new EntityNotFoundException(bmcLoanEnhancementId.toString()));

        BMCICCApproval bmcICCApproval = bmcIccApprovalRepository.getOne(bmcLoanEnhancement.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcIccApprovalRepository.save(bmcICCApproval);

        bmcLoanEnhancementRepository.delete(bmcLoanEnhancement);
        return bmcLoanEnhancement;
    }
}
