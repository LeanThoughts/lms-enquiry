package pfs.lms.enquiry.sanction;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on sanction
 * Spring Data repositories.
 */
public final class SanctionRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.Sanction.Display', 'LoanContracts.Sanction.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.Sanction.Edit')" + OR_INTERNAL;

    private SanctionRepositorySecurity() {
    }
}