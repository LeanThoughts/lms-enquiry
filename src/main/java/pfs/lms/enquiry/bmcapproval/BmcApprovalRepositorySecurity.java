package pfs.lms.enquiry.bmcapproval;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on BMC approval
 * Spring Data repositories.
 */
public final class BmcApprovalRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.BmcApproval.Display', 'LoanContracts.BmcApproval.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.BmcApproval.Edit')" + OR_INTERNAL;

    private BmcApprovalRepositorySecurity() {
    }
}