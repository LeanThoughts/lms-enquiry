package pfs.lms.enquiry.boardapproval;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on board-approval
 * Spring Data repositories.
 */
public final class BoardApprovalRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.BoardApproval.Display', 'LoanContracts.BoardApproval.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.BoardApproval.Edit')" + OR_INTERNAL;

    private BoardApprovalRepositorySecurity() {
    }
}