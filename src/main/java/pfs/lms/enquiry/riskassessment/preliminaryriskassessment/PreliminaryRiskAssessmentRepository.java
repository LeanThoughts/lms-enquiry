package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreliminaryRiskAssessmentRepository extends JpaRepository<PreliminaryRiskAssessment, UUID> {

    PreliminaryRiskAssessment findByRiskAssessmentId(UUID riskAssessmentId);
}
