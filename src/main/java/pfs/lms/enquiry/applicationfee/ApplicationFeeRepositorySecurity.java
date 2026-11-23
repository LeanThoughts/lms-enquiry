package pfs.lms.enquiry.applicationfee;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on
 * application-fee Spring Data repositories.
 */
public final class ApplicationFeeRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.ApplicationFee.Display', 'LoanContracts.ApplicationFee.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.ApplicationFee.Edit')" + OR_INTERNAL;

    private ApplicationFeeRepositorySecurity() {
    }
}