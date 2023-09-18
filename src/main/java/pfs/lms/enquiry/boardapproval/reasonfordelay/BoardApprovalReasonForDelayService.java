package pfs.lms.enquiry.boardapproval.reasonfordelay;

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
public class BoardApprovalReasonForDelayService implements IBoardApprovalReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BoardApprovalRepository boardApprovalRepository;
    private final BoardApprovalReasonForDelayRepository repository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BoardApprovalReasonForDelay createReasonForDelay(BoardApprovalReasonForDelayResource resource, String username) {
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
        BoardApprovalReasonForDelay boardApprovalReasonForDelay = new BoardApprovalReasonForDelay();
        boardApprovalReasonForDelay.setBoardApproval(boardApproval);
        boardApprovalReasonForDelay.setSerialNumber(repository.findByBoardApprovalId(boardApproval.getId()).size() + 1);
        boardApprovalReasonForDelay.setDate(resource.getDate());
        boardApprovalReasonForDelay.setReason(resource.getReason());
        boardApprovalReasonForDelay.setDeleteFlag(false);
        boardApprovalReasonForDelay = repository.save(boardApprovalReasonForDelay);

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

        return boardApprovalReasonForDelay;
    }

    @Override
    public BoardApprovalReasonForDelay updateReasonForDelay(BoardApprovalReasonForDelayResource resource, String username) throws CloneNotSupportedException {
        BoardApprovalReasonForDelay boardApprovalReasonForDelay =
                repository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = boardApprovalReasonForDelay.clone();

        boardApprovalReasonForDelay.setDate(resource.getDate());
        boardApprovalReasonForDelay.setReason(resource.getReason());
        boardApprovalReasonForDelay = repository.save(boardApprovalReasonForDelay);

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

        return boardApprovalReasonForDelay;
    }

    @Override
    public BoardApprovalReasonForDelay deleteReasonForDelay(UUID boardApprovalReasonForDelayId) throws CloneNotSupportedException {
        BoardApprovalReasonForDelay boardApprovalReasonForDelay = repository.findById(boardApprovalReasonForDelayId).
                orElseThrow(() -> new EntityNotFoundException(boardApprovalReasonForDelayId.toString()));
        boardApprovalReasonForDelay.setDeleteFlag(true);
        boardApprovalReasonForDelay = repository.save(boardApprovalReasonForDelay);
        return boardApprovalReasonForDelay;
    }
}
