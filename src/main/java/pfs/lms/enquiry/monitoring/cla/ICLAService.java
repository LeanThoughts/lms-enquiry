package pfs.lms.enquiry.monitoring.cla;

import java.util.List;

public interface ICLAService {

    CommonLoanAgreement saveCLA(CLAResource resource, String username) throws CloneNotSupportedException;

    CommonLoanAgreement updateCLA(CLAResource resource, String username) throws CloneNotSupportedException;

    List<CLAResource> getCommonLoanAgreements(String loanApplicationId);

    CLAReportAndFee saveCLAReportAndFee(CLAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    CLAReportAndFee updateCLAReportAndFee(CLAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<CLAReportAndFeeResource> getCLAReportAndFee(String loanApplicationId);
}
