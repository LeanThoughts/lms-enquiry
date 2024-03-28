package pfs.lms.enquiry.appraisal.securitytrustee;

import java.util.List;
import java.util.UUID;

public interface ISecurityTrusteeReportAndFeeService
{
    SecurityTrusteeReportAndFee saveReportAndFee(SecurityTrusteeReportAndFeeResource resource, String username);

    SecurityTrusteeReportAndFee updateReportAndFee(SecurityTrusteeReportAndFeeResource resource, String username)
            throws CloneNotSupportedException;

    SecurityTrusteeReportAndFee deleteReportAndFee(UUID reportAndFeeId, String moduleName, String username);

    List<SecurityTrusteeReportAndFeeResource> getReportAndFees(String loanApplicationId, String name);
}
