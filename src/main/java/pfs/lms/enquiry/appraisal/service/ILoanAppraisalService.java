package pfs.lms.enquiry.appraisal.service;

import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.LoanApplication;

public interface ILoanAppraisalService {
    public LoanAppraisal createLoanAppraisal(LoanApplication loanApplication, String username);

}
