package pfs.lms.enquiry.sanction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.boardapproval.BoardApproval;
import pfs.lms.enquiry.boardapproval.IBoardApprovalService;
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
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetter;
import pfs.lms.enquiry.sanction.sanctionletter.SanctionLetterRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SanctionService implements ISanctionService {

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
    @Autowired
    private final SanctionLetterRepository sanctionLetterRepository;

    @Override
    public BoardApproval processApprovedSanction(Sanction sanction, String username) {

        LoanApplication loanApplication = sanction.getLoanApplication();
        loanApplication.setFunctionalStatus(5); //Sanction Stage
        loanApplicationRepository.save(loanApplication);

        List<SanctionLetter> sanctionLetterList = sanctionLetterRepository.findBySanctionId(sanction.getId());
        for (SanctionLetter sanctionLetter: sanctionLetterList){
            if (sanctionLetter.getPostedInSAP() == 5 || sanctionLetter.getPostedInSAP() == 6){
                sanctionLetter.setPostedInSAP(0);
                sanctionLetterRepository.save(sanctionLetter);
            }
        }


        return null;
    }
}

