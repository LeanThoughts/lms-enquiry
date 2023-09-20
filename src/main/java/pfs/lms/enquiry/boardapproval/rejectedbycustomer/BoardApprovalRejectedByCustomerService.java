package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.BoardApprovalRepository;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoardRepository;
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
public class BoardApprovalRejectedByCustomerService implements IBoardApprovalRejectedByCustomerService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BoardApprovalRepository boardApprovalRepository;
    private final BoardApprovalRejectedByCustomerRepository boardApprovalRejectedByCustomerRepository;
    private final ApprovalByBoardRepository approvalByBoardRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BoardApprovalRejectedByCustomer create(BoardApprovalRejectedByCustomerResource resource, String username) {
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
        BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = new BoardApprovalRejectedByCustomer();
        boardApprovalRejectedByCustomer.setBoardApproval(boardApproval);
        boardApprovalRejectedByCustomer.setApprovalByBoardMeetingNumber(resource.getApprovalByBoardMeetingNumber());
        boardApprovalRejectedByCustomer.setMeetingDate(resource.getMeetingDate());
        boardApprovalRejectedByCustomer.setRejectionCategory(resource.getRejectionCategory());
        boardApprovalRejectedByCustomer.setDetails(resource.getDetails());
        boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerRepository.save(boardApprovalRejectedByCustomer);

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

        return boardApprovalRejectedByCustomer;
    }

    @Override
    public BoardApprovalRejectedByCustomer update(BoardApprovalRejectedByCustomerResource resource, String username) throws CloneNotSupportedException {
        BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = boardApprovalRejectedByCustomer.clone();

        boardApprovalRejectedByCustomer.setApprovalByBoardMeetingNumber(resource.getApprovalByBoardMeetingNumber());
        boardApprovalRejectedByCustomer.setMeetingDate(resource.getMeetingDate());
        boardApprovalRejectedByCustomer.setRejectionCategory(resource.getRejectionCategory());
        boardApprovalRejectedByCustomer.setDetails(resource.getDetails());
        boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerRepository.save(boardApprovalRejectedByCustomer);

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

        return boardApprovalRejectedByCustomer;
    }

    @Override
    public BoardApprovalRejectedByCustomer delete(UUID boardApprovalRejectedByCustomerId) throws CloneNotSupportedException {
        BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerRepository
                .findById(boardApprovalRejectedByCustomerId)
                .orElseThrow(() -> new EntityNotFoundException(boardApprovalRejectedByCustomerId.toString()));
        boardApprovalRejectedByCustomerRepository.delete(boardApprovalRejectedByCustomer);
        return boardApprovalRejectedByCustomer;
    }
}
