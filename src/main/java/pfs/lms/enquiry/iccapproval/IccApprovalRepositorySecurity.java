package pfs.lms.enquiry.iccapproval;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on ICC approval
 * Spring Data repositories.
 */
public final class IccApprovalRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.IccApproval.Display', 'LoanContracts.IccApproval.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.IccApproval.Edit')" + OR_INTERNAL;

    private IccApprovalRepositorySecurity() {
    }
}