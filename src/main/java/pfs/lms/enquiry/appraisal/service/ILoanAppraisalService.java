package pfs.lms.enquiry.appraisal.service;

import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;

public interface ILoanAppraisalService {
    public LoanAppraisal createLoanAppraisal(LoanApplication loanApplication, String username);
    public LoanAppraisal processRejection(LoanAppraisal loanAppraisal, String username) throws CloneNotSupportedException;

}
