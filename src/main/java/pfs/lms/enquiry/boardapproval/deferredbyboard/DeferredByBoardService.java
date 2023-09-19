package pfs.lms.enquiry.boardapproval.deferredbyboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.BoardApprovalRepository;
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
public class DeferredByBoardService implements IDeferredByBoardService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BoardApprovalRepository boardApprovalRepository;
    private final DeferredByBoardRepository deferredByBoardRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public DeferredByBoard create(DeferredByBoardResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        BoardApproval boardApproval = boardApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BoardApproval obj = new BoardApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = boardApprovalRepository.save(obj);
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
        DeferredByBoard deferredByBoard = new DeferredByBoard();
        deferredByBoard.setBoardApproval(boardApproval);
        deferredByBoard.setMeetingDate(resource.getMeetingDate());
        deferredByBoard.setMeetingNumber(resource.getMeetingNumber());
        deferredByBoard.setDetails(resource.getDetails());
        deferredByBoard = deferredByBoardRepository.save(deferredByBoard);

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

        return deferredByBoard;
    }

    @Override
    public DeferredByBoard update(DeferredByBoardResource resource, String username) throws CloneNotSupportedException {
        DeferredByBoard deferredByBoard = deferredByBoardRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = deferredByBoard.clone();

        deferredByBoard.setMeetingNumber(resource.getMeetingNumber());
        deferredByBoard.setMeetingDate(resource.getMeetingDate());
        deferredByBoard.setDetails(resource.getDetails());
        deferredByBoard = deferredByBoardRepository.save(deferredByBoard);

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

        return deferredByBoard;
    }

    @Override
    public DeferredByBoard delete(UUID deferredByBoardId) throws CloneNotSupportedException {
        DeferredByBoard deferredByBoard = deferredByBoardRepository.findById(deferredByBoardId)
                .orElseThrow(() -> new EntityNotFoundException(deferredByBoardId.toString()));
        deferredByBoardRepository.delete(deferredByBoard);
        return deferredByBoard;
    }
}
