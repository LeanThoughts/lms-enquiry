package pfs.lms.enquiry.collateral;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on collateral
 * Spring Data repositories.
 */
public final class CollateralRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.Collaterals.Display', 'LoanContracts.Collaterals.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.Collaterals.Edit')" + OR_INTERNAL;

    private CollateralRepositorySecurity() {
    }
}