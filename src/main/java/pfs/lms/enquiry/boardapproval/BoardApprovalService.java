package pfs.lms.enquiry.boardapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoard;
import pfs.lms.enquiry.boardapproval.approvalbyboard.ApprovalByBoardRepository;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoard;
import pfs.lms.enquiry.boardapproval.deferredbyboard.DeferredByBoardRepository;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelay;
import pfs.lms.enquiry.boardapproval.reasonfordelay.BoardApprovalReasonForDelayRepository;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoard;
import pfs.lms.enquiry.boardapproval.rejectedbyboard.RejectedByBoardRepository;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomer;
import pfs.lms.enquiry.boardapproval.rejectedbycustomer.BoardApprovalRejectedByCustomerRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardApprovalService implements IBoardApprovalService {
    @Autowired
    private BoardApprovalRepository boardApprovalRepository;

    @Autowired
    private final LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private final ApprovalByBoardRepository approvalByBoardRepository;
    @Autowired
    private final DeferredByBoardRepository deferredByBoardRepository;
    @Autowired
    private final BoardApprovalReasonForDelayRepository boardApprovalReasonForDelayRepository;
    @Autowired
    private final RejectedByBoardRepository rejectedByBoardRepository;
    @Autowired
    private final BoardApprovalRejectedByCustomerRepository boardApprovalRejectedByCustomerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BoardApproval processRejection(BoardApproval boardApproval, String username) throws CloneNotSupportedException {
        Object oldBoardApproval = boardApproval.clone();
        boardApproval.setWorkFlowStatusCode(04);
        boardApproval.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                boardApproval.getId(), boardApproval.getId().toString(), null,
                boardApproval.getLoanApplication().getLoanContractId(),
                oldBoardApproval,
                boardApproval,
                "Updated",
                username,
                "Board Approval", "Header");
        boardApprovalRepository.save(boardApproval);

        return boardApproval;
    }

    @Override
    public BoardApproval processApprovedBoardApproval(BoardApproval boardApproval, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = boardApproval.getLoanApplication();
        Object oldLoanApplication;
        oldLoanApplication = loanApplication.clone();

        loanApplication.setChangedByUserName(username);

        loanApplication = mapBoardApprovalToLoanApplication(boardApproval, loanApplication);

        loanApplication.setFunctionalStatus(4); // Baord Approval
        loanApplication.setFunctionalStatusDescription("Board Approval Stage");
        loanApplication.setTechnicalStatus(4);  // Approved/Taken Up for processing
        loanApplication.setPostedInSAP(0);
        loanApplication.setFinalDecisionStatus(0); //

        // Change Documents for Loan Application
        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                loanApplication,
                oldLoanApplication,
                "Updated",
                username,
                "LoanApplication", "LoanApplication" );

        loanApplicationRepository.save(loanApplication);
        return boardApproval;
    }

    private LoanApplication mapBoardApprovalToLoanApplication(BoardApproval boardApproval, LoanApplication loanApplication) {

        ApprovalByBoard approvalByBoard = new ApprovalByBoard();
        DeferredByBoard deferredByBoard = new DeferredByBoard();
        BoardApprovalReasonForDelay boardApprovalReasonForDelay = new BoardApprovalReasonForDelay();
        RejectedByBoard rejectedByBoard = new RejectedByBoard();
        BoardApprovalRejectedByCustomer boardApprovalRejectedByCustomer = new BoardApprovalRejectedByCustomer();

        List<ApprovalByBoard> approvalByBoardList = approvalByBoardRepository.findByBoardApprovalId(boardApproval.getId());
        //TODO - There will only be one ApprovalByBoard
        //if (approvalByBoardList.size() > 0) {
        approvalByBoard = approvalByBoardList.get(0);
        List<DeferredByBoard> deferredByBoardList = deferredByBoardRepository.findByBoardApprovalId(boardApproval.getId());
        List<BoardApprovalReasonForDelay> boardApprovalReasonForDelayList = boardApprovalReasonForDelayRepository.findByBoardApprovalId(boardApproval.getId());
        List<RejectedByBoard> rejectedByBoardRepositoryList = rejectedByBoardRepository.findByBoardApprovalId(boardApproval.getId());
        List<BoardApprovalRejectedByCustomer> boardApprovalRejectedByCustomerList = boardApprovalRejectedByCustomerRepository.findByBoardApprovalId(boardApproval.getId());


        if (approvalByBoardList.size() > 0)
            approvalByBoard = approvalByBoardList.get(0);
        if (rejectedByBoardRepositoryList.size() > 0)
            rejectedByBoard = rejectedByBoardRepositoryList.get(0);
        if (approvalByBoardList.size() > 0)
            approvalByBoard = approvalByBoardList.get(0);
        if (boardApprovalRejectedByCustomerList.size() > 0)
            boardApprovalRejectedByCustomer = boardApprovalRejectedByCustomerList.get(0);

        if (approvalByBoard != null) {
            loanApplication.setBoardMeetingNumber(approvalByBoard.getMeetingNumber().toString());
            if (approvalByBoard.getMeetingDate() != null)
                loanApplication.setBoardApprovalDate(approvalByBoard.getMeetingDate());
            if (approvalByBoard.getDetails() != null)
                loanApplication.setBoardApprovalRemarks(approvalByBoard.getDetails());
            loanApplication.setBODStatus("4"); //Approved by Board
            return loanApplication;
        }
        if (rejectedByBoard != null) {
            loanApplication.setBoardMeetingNumber(rejectedByBoard.getMeetingNumber().toString());
            if (rejectedByBoard.getMeetingDate() != null)
                loanApplication.setBoardApprovalDate(rejectedByBoard.getMeetingDate());
            if (rejectedByBoard.getDetails() != null)
                loanApplication.setBoardApprovalRemarks(rejectedByBoard.getDetails());
            loanApplication.setBODStatus("3"); //Rejected by Board
        }
        if (boardApprovalRejectedByCustomer != null) {
            loanApplication.setBODStatus("5"); //Rejected by Customer
            return loanApplication;
        }

        // Use the Latest DeferredByBoard Item
        if (deferredByBoardList != null) {

            // Sort by Board Meeting Date
            Comparator<DeferredByBoard> comparator = (d1, d2) -> {
                return d1.getMeetingDate().compareTo(d2.getMeetingDate());
            };
            Collections.sort(deferredByBoardList, comparator);

            deferredByBoard = deferredByBoardList.get(0);

            loanApplication.setBoardMeetingNumber(deferredByBoard.getMeetingNumber().toString());
            if (deferredByBoard.getMeetingDate() != null)
                loanApplication.setBoardApprovalDate(deferredByBoard.getMeetingDate());
            else
                loanApplication.setBoardApprovalDate(null);

            if (deferredByBoard.getDetails() != null)
                loanApplication.setBoardApprovalRemarks(deferredByBoard.getDetails());
            loanApplication.setBODStatus("1"); //Deferred by Board
            return loanApplication;
        }
        return loanApplication;
    }


}

