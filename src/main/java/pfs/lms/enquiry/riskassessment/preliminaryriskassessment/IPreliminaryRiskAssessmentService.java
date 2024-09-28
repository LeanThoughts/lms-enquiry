package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

import java.util.UUID;

public interface IPreliminaryRiskAssessmentService {

    PreliminaryRiskAssessment create(PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource, String username);

    PreliminaryRiskAssessment update(PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource, String username) throws CloneNotSupportedException;

    PreliminaryRiskAssessment delete(UUID preliminaryRiskAssessmentId, String username);
}
