package pfs.lms.enquiry.monitoring.lia;

import java.util.List;

public interface ILIAService {

    LendersInsuranceAdvisor saveLIA(LIAResource resource, String username) throws CloneNotSupportedException;

    LendersInsuranceAdvisor updateLIA(LIAResource resource, String username) throws CloneNotSupportedException;

    List<LIAResource> getLendersInsuranceAdvisors(String loanApplicationId);

    LIAReportAndFee saveLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    LIAReportAndFee updateLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<LIAReportAndFeeResource> getLIAReportAndFee(String loanApplicationId);
}
