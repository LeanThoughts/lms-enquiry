package pfs.lms.enquiry.action;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on process-enquiry
 * Spring Data repositories.
 */
public final class ProcessEnquiryRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.ProcessEnquiry.Display', 'LoanContracts.ProcessEnquiry.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.ProcessEnquiry.Edit')" + OR_INTERNAL;

    private ProcessEnquiryRepositorySecurity() {
    }
}