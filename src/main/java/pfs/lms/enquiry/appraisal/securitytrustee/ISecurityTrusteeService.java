package pfs.lms.enquiry.appraisal.securitytrustee;

import java.util.List;
import java.util.UUID;

public interface ISecurityTrusteeService
{
    SecurityTrustee saveSecurityTrustee(SecurityTrusteeResource resource, String username)
            throws CloneNotSupportedException;

    SecurityTrustee updateSecurityTrustee(SecurityTrusteeResource resource, String username)
            throws CloneNotSupportedException;

    SecurityTrustee deleteSecurityTrustee(UUID securityTrusteeId, String moduleName, String username);

    List<SecurityTrusteeResource> getSecurityTrustees(String loanApplicationId, String name);
}
