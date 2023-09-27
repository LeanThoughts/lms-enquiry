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
                    // Change Documents for Board Approval Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Board Approval", "Header");
                    return obj;

                });
        DeferredByBoard deferredByBoard = new DeferredByBoard();
        deferredByBoard.setBoardApproval(boardApproval);
        deferredByBoard.setMeetingDate(resource.getMeetingDate());
        deferredByBoard.setMeetingNumber(resource.getMeetingNumber());
        deferredByBoard.setDetails(resource.getDetails());
        deferredByBoard = deferredByBoardRepository.save(deferredByBoard);

        // Change Documents for DeferredByBoard
        changeDocumentService.createChangeDocument(
                deferredByBoard.getBoardApproval().getId(),
                deferredByBoard.getId().toString(),
                deferredByBoard.getBoardApproval().getId().toString(),
                deferredByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                null,
                deferredByBoard,
                "Created",
                username,
                "Board Approval", "DeferredByBoard");

        return deferredByBoard;
    }

    @Override
    public DeferredByBoard update(DeferredByBoardResource resource, String username) throws CloneNotSupportedException {
        DeferredByBoard deferredByBoard = deferredByBoardRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldDeferredByBoard = deferredByBoard.clone();

        deferredByBoard.setMeetingNumber(resource.getMeetingNumber());
        deferredByBoard.setMeetingDate(resource.getMeetingDate());
        deferredByBoard.setDetails(resource.getDetails());
        deferredByBoard = deferredByBoardRepository.save(deferredByBoard);

        // Change Documents for DeferredByBoard
        changeDocumentService.createChangeDocument(
                deferredByBoard.getBoardApproval().getId(),
                deferredByBoard.getId().toString(),
                deferredByBoard.getBoardApproval().getId().toString(),
                deferredByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                oldDeferredByBoard,
                deferredByBoard,
                "Updated",
                username,
        "Board Approval", "DeferredByBoard");

        return deferredByBoard;
    }

    @Override
    public DeferredByBoard delete(UUID deferredByBoardId, String username) throws CloneNotSupportedException {
        DeferredByBoard deferredByBoard = deferredByBoardRepository.findById(deferredByBoardId)
                .orElseThrow(() -> new EntityNotFoundException(deferredByBoardId.toString()));
        deferredByBoardRepository.delete(deferredByBoard);

        // Change Documents for DeferredByBoard
        changeDocumentService.createChangeDocument(
                deferredByBoard.getBoardApproval().getId(),
                deferredByBoard.getId().toString(),
                deferredByBoard.getBoardApproval().getId().toString(),
                deferredByBoard.getBoardApproval().getLoanApplication().getLoanContractId(),
                null,
                deferredByBoard,
                "Deleted",
                username,
                "Board Approval", "DeferredByBoard");

        return deferredByBoard;
    }
}
