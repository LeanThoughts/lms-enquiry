package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

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
public class BMCICCReasonForDelayService implements IBMCICCReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository BMCICCApprovalRepository;
    private final BMCICCReasonForDelayRepository BMCICCReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcIccReasonForDelay create(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmciccReasonForDelayResource.getLoanApplicationId());

        BmcIccApproval BMCICCApproval = BMCICCApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = BMCICCApprovalRepository.save(obj);

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

        BmcIccReasonForDelay BMCICCReasonForDelay = new BmcIccReasonForDelay();
        BMCICCReasonForDelay.setBmcICCApproval(BMCICCApproval);
        BMCICCReasonForDelay.setReasonForDelay(bmciccReasonForDelayResource.getReasonForDelay());
        BMCICCReasonForDelay.setDate(bmciccReasonForDelayResource.getDate());
        BMCICCReasonForDelay = BMCICCReasonForDelayRepository.save(BMCICCReasonForDelay);


        changeDocumentService.createChangeDocument(
                BMCICCReasonForDelay.getId(),
                BMCICCReasonForDelay.getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getLoanApplication().getLoanContractId(),
                null,
                BMCICCReasonForDelay,
                "Created",
                username,
                "BmcApprovalByIcc", "BmcIccReasonForDelay");

        return BMCICCReasonForDelay;
    }

    @Override
    public BmcIccReasonForDelay update(BMCICCReasonForDelayResource bmciccReasonForDelayResource, String username)
            throws CloneNotSupportedException {

        BmcIccReasonForDelay BMCICCReasonForDelay = BMCICCReasonForDelayRepository.findById(bmciccReasonForDelayResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmciccReasonForDelayResource.getId().toString()));

        Object oldBMCICCReasonForDelay = BMCICCReasonForDelay.clone();

        BMCICCReasonForDelay.setReasonForDelay(bmciccReasonForDelayResource.getReasonForDelay());
        BMCICCReasonForDelay.setDate(bmciccReasonForDelayResource.getDate());
        BMCICCReasonForDelay = BMCICCReasonForDelayRepository.save(BMCICCReasonForDelay);

        changeDocumentService.createChangeDocument(
                BMCICCReasonForDelay.getId(),
                BMCICCReasonForDelay.getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getLoanApplication().getLoanContractId(),
                oldBMCICCReasonForDelay,
                BMCICCReasonForDelay,
                "Updated",
                username,
                "BmcApprovalByIcc", "BmcIccReasonForDelay");

        return BMCICCReasonForDelay;
    }

    @Override
    public BmcIccReasonForDelay delete(UUID iccFurtherDetailId, String username) {
        BmcIccReasonForDelay BMCICCReasonForDelay = BMCICCReasonForDelayRepository.findById(iccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(iccFurtherDetailId.toString()));

        BmcIccApproval BMCICCApproval = BMCICCApprovalRepository.getOne(BMCICCReasonForDelay.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        BMCICCApprovalRepository.save(BMCICCApproval);

        BMCICCReasonForDelayRepository.delete(BMCICCReasonForDelay);

        changeDocumentService.createChangeDocument(
                BMCICCReasonForDelay.getId(),
                BMCICCReasonForDelay.getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getId().toString(),
                BMCICCReasonForDelay.getBmcICCApproval().getLoanApplication().getLoanContractId(),
                null,
                BMCICCReasonForDelay,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BmcIccReasonForDelay");
        return BMCICCReasonForDelay;
    }
}
