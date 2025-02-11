package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
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
    public BmcLoanEnhancement create(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmcLoanEnhancementResource.getLoanApplicationId());

        BmcIccApproval BMCICCApproval = bmcIccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmcIccApprovalRepository.save(obj);

                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "BmcIccApproval", "Header");

                    return obj;
                });

        BmcLoanEnhancement bmcLoanEnhancement = new BmcLoanEnhancement();
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

        changeDocumentService.createChangeDocument(
                bmcLoanEnhancement.getId(),
                bmcLoanEnhancement.getId().toString(),
                bmcLoanEnhancement.getBmcICCApproval().getId().toString(),
                BMCICCApproval.getLoanApplication().getLoanContractId(),
                null,
                bmcLoanEnhancement,
                "Created",
                username,
                "BmcApprovalByIcc", "BmcLoanEnhancement");

        return bmcLoanEnhancement;
    }

    @Override
    public BmcLoanEnhancement update(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username)
            throws CloneNotSupportedException {

        BmcLoanEnhancement bmcLoanEnhancement = bmcLoanEnhancementRepository.findById(bmcLoanEnhancementResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcLoanEnhancementResource.getId().toString()));

        Object oldBmcLoanEnhancement = bmcLoanEnhancement.clone();

        bmcLoanEnhancement.setIccMeetingNumber(bmcLoanEnhancementResource.getIccMeetingNumber());
        bmcLoanEnhancement.setIccClearanceDate(bmcLoanEnhancementResource.getIccClearanceDate());
        bmcLoanEnhancement.setRevisedProjectCost(bmcLoanEnhancementResource.getRevisedProjectCost());
        bmcLoanEnhancement.setRevisedEquity(bmcLoanEnhancementResource.getRevisedEquity());
        bmcLoanEnhancement.setRevisedContractAmount(bmcLoanEnhancementResource.getRevisedContractAmount());
        bmcLoanEnhancement.setRevisedCommercialOperationsDate(bmcLoanEnhancementResource.getRevisedCommercialOperationsDate());
        bmcLoanEnhancement.setReviseRepaymentStartDate(bmcLoanEnhancementResource.getReviseRepaymentStartDate());
        bmcLoanEnhancement.setRemarks(bmcLoanEnhancementResource.getRemarks());
        bmcLoanEnhancement = bmcLoanEnhancementRepository.save(bmcLoanEnhancement);

        BmcIccApproval bmcICCApproval = bmcIccApprovalRepository.getOne(bmcLoanEnhancement.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcIccApprovalRepository.save(bmcICCApproval);

        changeDocumentService.createChangeDocument(
                bmcLoanEnhancement.getId(),
                bmcLoanEnhancement.getId().toString(),
                bmcLoanEnhancement.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                oldBmcLoanEnhancement,
                bmcLoanEnhancement,
                "Updated",
                username,
                "BmcApprovalByIcc", "BmcLoanEnhancement");
        return bmcLoanEnhancement;
    }

    @Override
    public BmcLoanEnhancement delete(UUID bmcLoanEnhancementId, String username) {
        BmcLoanEnhancement bmcLoanEnhancement = bmcLoanEnhancementRepository.findById(bmcLoanEnhancementId)
                .orElseThrow(() -> new EntityNotFoundException(bmcLoanEnhancementId.toString()));

        BmcIccApproval bmcICCApproval = bmcIccApprovalRepository.getOne(bmcLoanEnhancement.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcIccApprovalRepository.save(bmcICCApproval);

        bmcLoanEnhancementRepository.delete(bmcLoanEnhancement);

        changeDocumentService.createChangeDocument(
                bmcLoanEnhancement.getId(),
                bmcLoanEnhancement.getId().toString(),
                bmcLoanEnhancement.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                null,
                bmcLoanEnhancement,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BmcLoanEnhancement");
        return bmcLoanEnhancement;
    }
}
