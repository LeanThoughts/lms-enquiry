package pfs.lms.enquiry.sanction.reasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.sanction.Sanction;
import pfs.lms.enquiry.sanction.SanctionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SanctionReasonForDelayService implements ISanctionReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final SanctionRepository sanctionRepository;
    private final SanctionReasonForDelayRepository sanctionReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public SanctionReasonForDelay createReasonForDelay(SanctionReasonForDelayResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Sanction sanction = sanctionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Sanction obj = new Sanction();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = sanctionRepository.save(obj);
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
        SanctionReasonForDelay sanctionReasonForDelay = new SanctionReasonForDelay();
        sanctionReasonForDelay.setSanction(sanction);
        sanctionReasonForDelay.setSerialNumber(sanctionReasonForDelayRepository.findBySanctionId(sanction.getId()).size() + 1);
        sanctionReasonForDelay.setDate(resource.getDate());
        sanctionReasonForDelay.setReason(resource.getReason());
        sanctionReasonForDelay.setDeleteFlag(false);
        sanctionReasonForDelay = sanctionReasonForDelayRepository.save(sanctionReasonForDelay);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                null,
//                boardApprovalReasonForDelay,
//                "Created",
//                username,
//                "Appraisal", "Reason For Delay" );

        return sanctionReasonForDelay;
    }

    @Override
    public SanctionReasonForDelay updateReasonForDelay(SanctionReasonForDelayResource resource, String username) throws CloneNotSupportedException {
        SanctionReasonForDelay sanctionReasonForDelay =
                sanctionReasonForDelayRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = sanctionReasonForDelay.clone();

        sanctionReasonForDelay.setDate(resource.getDate());
        sanctionReasonForDelay.setReason(resource.getReason());
        sanctionReasonForDelay = sanctionReasonForDelayRepository.save(sanctionReasonForDelay);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                oldReasonForDelay,
//                boardApprovalReasonForDelay,
//                "Updated",
//                username,
//                "Appraisal", "Reason For Delay" );

        return sanctionReasonForDelay;
    }

    @Override
    public SanctionReasonForDelay deleteReasonForDelay(UUID sanctionReasonForDelayId) throws CloneNotSupportedException {
        SanctionReasonForDelay sanctionReasonForDelay = sanctionReasonForDelayRepository.findById(sanctionReasonForDelayId).
                orElseThrow(() -> new EntityNotFoundException(sanctionReasonForDelayId.toString()));
        sanctionReasonForDelay.setDeleteFlag(true);
        sanctionReasonForDelay = sanctionReasonForDelayRepository.save(sanctionReasonForDelay);
        return sanctionReasonForDelay;
    }
}
