package pfs.lms.enquiry.appraisal.loanpartner;

import java.util.UUID;

public interface ILoanPartnerService {

    LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource);

    LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource);

    LoanPartner deleteLoanPartner(UUID loanPartnerId);
}
