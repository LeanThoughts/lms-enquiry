package pfs.lms.enquiry.appraisal.loanpartner;

public interface ILoanPartnerService {

    LoanPartner createLoanPartner(LoanPartnerResource loanPartnerResource);

    LoanPartner updateLoanPartner(LoanPartnerResource loanPartnerResource);
}
