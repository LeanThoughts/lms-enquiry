package pfs.lms.enquiry.riskassessment;

import static pfs.lms.enquiry.security.LmsSystemSecurity.OR_INTERNAL;

/**
 * SpEL expressions for {@link org.springframework.security.access.prepost.PreAuthorize} on risk-assessment
 * Spring Data repositories.
 */
public final class RiskAssessmentRepositorySecurity {

    public static final String READ =
            "hasAnyAuthority('LoanContracts.RiskAssessment.Display', 'LoanContracts.RiskAssessment.Edit')" + OR_INTERNAL;
    public static final String WRITE = "hasAuthority('LoanContracts.RiskAssessment.Edit')" + OR_INTERNAL;

    private RiskAssessmentRepositorySecurity() {
    }
}