package pfs.lms.enquiry.boardapproval.rejectedbyboard;

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
public class RejectedByBoardService implements IRejectedByBoardService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BoardApprovalRepository boardApprovalRepository;
    private final RejectedByBoardRepository rejectedByBoardRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public RejectedByBoard create(RejectedByBoardResource resource, String username) {
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
        RejectedByBoard rejectedByBoard = new RejectedByBoard();
        rejectedByBoard.setBoardApproval(boardApproval);
        rejectedByBoard.setMeetingDate(resource.getMeetingDate());
        rejectedByBoard.setMeetingNumber(resource.getMeetingNumber());
        rejectedByBoard.setDetails(resource.getDetails());
        rejectedByBoard = rejectedByBoardRepository.save(rejectedByBoard);

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

        return rejectedByBoard;
    }

    @Override
    public RejectedByBoard update(RejectedByBoardResource resource, String username) throws CloneNotSupportedException {
        RejectedByBoard rejectedByBoard = rejectedByBoardRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = rejectedByBoard.clone();

        rejectedByBoard.setMeetingNumber(resource.getMeetingNumber());
        rejectedByBoard.setMeetingDate(resource.getMeetingDate());
        rejectedByBoard.setDetails(resource.getDetails());
        rejectedByBoard = rejectedByBoardRepository.save(rejectedByBoard);

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

        return rejectedByBoard;
    }

    @Override
    public RejectedByBoard delete(UUID deferredByBoardId) throws CloneNotSupportedException {
        RejectedByBoard rejectedByBoard = rejectedByBoardRepository.findById(deferredByBoardId)
                .orElseThrow(() -> new EntityNotFoundException(deferredByBoardId.toString()));
        rejectedByBoardRepository.delete(rejectedByBoard);
        return rejectedByBoard;
    }
}
