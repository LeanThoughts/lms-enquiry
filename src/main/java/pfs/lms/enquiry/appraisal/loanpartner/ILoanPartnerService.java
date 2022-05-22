package pfs.lms.enquiry.appraisal.loanpartner;

import org.apache.http.HttpRequest;

import java.util.UUID;

public interface ILoanPartnerService {

    LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource, String username);

    LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource, String username) throws CloneNotSupportedException;

    LoanPartner deleteLoanPartner(UUID loanPartnerId, String username);
}
