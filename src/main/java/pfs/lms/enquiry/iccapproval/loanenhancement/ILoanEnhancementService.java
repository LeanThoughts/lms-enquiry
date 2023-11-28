package pfs.lms.enquiry.iccapproval.loanenhancement;

import java.util.UUID;

public interface ILoanEnhancementService {

    LoanEnhancement create(LoanEnhancementResource loanEnhancementResource, String username);

    LoanEnhancement update(LoanEnhancementResource loanEnhancementResource, String username) throws CloneNotSupportedException;

    LoanEnhancement delete(UUID loanEnhancementId, String username);
}
