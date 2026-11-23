package pfs.lms.enquiry.referenceinterest;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on
 * reference-interest Spring Data repositories.
 */
public final class ReferenceInterestRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('ReferenceInterestRates.Display', 'ReferenceInterestRates.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('ReferenceInterestRates.Edit')" + OR_INTERNAL;

    private ReferenceInterestRepositorySecurity() {
    }
}