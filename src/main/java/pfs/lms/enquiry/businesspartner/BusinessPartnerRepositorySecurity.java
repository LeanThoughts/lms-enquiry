package pfs.lms.enquiry.businesspartner;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on
 * business-partner Spring Data repositories.
 */
public final class BusinessPartnerRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('BusinessPartners.Display', 'BusinessPartners.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('BusinessPartners.Edit')" + OR_INTERNAL;

    private BusinessPartnerRepositorySecurity() {
    }
}