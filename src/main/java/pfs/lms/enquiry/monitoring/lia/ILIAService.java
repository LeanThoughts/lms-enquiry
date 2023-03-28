package pfs.lms.enquiry.monitoring.lia;

import java.util.List;
import java.util.UUID;

public interface ILIAService {

    LendersInsuranceAdvisor saveLIA(LIAResource resource, String username) throws CloneNotSupportedException;

    LendersInsuranceAdvisor updateLIA(LIAResource resource, String username) throws CloneNotSupportedException;

    LendersInsuranceAdvisor deleteLIA(UUID liaId, String moduleName, String username);

    List<LIAResource> getLendersInsuranceAdvisors(String loanApplicationId);

    LIAReportAndFee saveLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    LIAReportAndFee updateLIAReportAndFee(LIAReportAndFeeResource resource, String username) throws CloneNotSupportedException;

    List<LIAReportAndFeeResource> getLIAReportAndFee(String loanApplicationId);

    LIAReportAndFee deleteLIAReportAndFee(UUID liaReportAndFeeId, String moduleName, String username);
}
