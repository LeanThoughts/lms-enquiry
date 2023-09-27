package pfs.lms.enquiry.boardapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardApprovalService implements IBoardApprovalService {

    @Autowired
    private  final  LoanApplicationRepository loanApplicationRepository;



    @Override
    public BoardApproval processApprovedBoardApproval(BoardApproval boardApproval, String username) {

        LoanApplication loanApplication = boardApproval.getLoanApplication();
        loanApplication.setChangedByUserName(username);

        loanApplication.setFunctionalStatus(4); //Enquiry Stage
        loanApplication.setTechnicalStatus(4);  // Approved/Taken Up for processing
        loanApplication.setPostedInSAP(0);
        loanApplication.setFinalDecisionStatus(0); //

        loanApplicationRepository.save(loanApplication);
        return boardApproval;
    }
}

