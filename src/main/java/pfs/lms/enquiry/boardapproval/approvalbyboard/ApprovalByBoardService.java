package pfs.lms.enquiry.boardapproval.approvalbyboard;

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
public class ApprovalByBoardService implements IApprovalByBoardService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BoardApprovalRepository boardApprovalRepository;
    private final ApprovalByBoardRepository approvalByBoardRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ApprovalByBoard create(ApprovalByBoardResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        BoardApproval boardApproval = boardApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BoardApproval obj = new BoardApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = boardApprovalRepository.save(obj);
                    // Change Documents for Board Approval Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),
                            obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Board Approval", "Header");
                    return obj;
                });
        ApprovalByBoard approvalByBoard = new ApprovalByBoard();
        approvalByBoard.setBoardApproval(boardApproval);
        approvalByBoard.setMeetingDate(resource.getMeetingDate());
        approvalByBoard.setMeetingNumber(resource.getMeetingNumber());
        approvalByBoard.setDetails(resource.getDetails());
        approvalByBoard = approvalByBoardRepository.save(approvalByBoard);

        // Change Documents for ApprovalByBoard
        changeDocumentService.createChangeDocument(
                approvalByBoard.getBoardApproval().getId(),
                approvalByBoard.getId().toString(),
                approvalByBoard.getBoardApproval().getId().toString(),
                approvalByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                null,
                approvalByBoard,
                "Created",
                username,
                "Board Approval", "ApprovalByBoard" );

        return approvalByBoard;
    }

    @Override
    public ApprovalByBoard update(ApprovalByBoardResource resource, String username) throws CloneNotSupportedException {
        ApprovalByBoard approvalByBoard = approvalByBoardRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldApprovalByBoard = approvalByBoard.clone();

        approvalByBoard.setMeetingNumber(resource.getMeetingNumber());
        approvalByBoard.setMeetingDate(resource.getMeetingDate());
        approvalByBoard.setDetails(resource.getDetails());
        approvalByBoard = approvalByBoardRepository.save(approvalByBoard);

        // Change Documents for ApprovalByBoard
        changeDocumentService.createChangeDocument(
                approvalByBoard.getBoardApproval().getId(),
                approvalByBoard.getId().toString(),
                approvalByBoard.getBoardApproval().getId().toString(),
                approvalByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                oldApprovalByBoard,
                approvalByBoard,
                "Updated",
                username,
                "Board Approval", "ApprovalByBoard" );

        return approvalByBoard;
    }

    @Override
    public ApprovalByBoard delete(UUID deferredByBoardId, String username) throws CloneNotSupportedException {
        ApprovalByBoard approvalByBoard = approvalByBoardRepository.findById(deferredByBoardId)
                .orElseThrow(() -> new EntityNotFoundException(deferredByBoardId.toString()));
        approvalByBoardRepository.delete(approvalByBoard);
        // Change Documents for ApprovalByBoard
        changeDocumentService.createChangeDocument(
                approvalByBoard.getBoardApproval().getId(),
                approvalByBoard.getId().toString(),
                approvalByBoard.getBoardApproval().getId().toString(),
                approvalByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                null,
                approvalByBoard,
                "Deleted",
                username,
                "Board Approval", "ApprovalByBoard" );
        return approvalByBoard;
    }
}
