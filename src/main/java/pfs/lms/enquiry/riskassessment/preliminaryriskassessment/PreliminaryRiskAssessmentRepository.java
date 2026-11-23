package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.riskassessment.RiskAssessmentRepositorySecurity.READ;
import static pfs.lms.enquiry.riskassessment.RiskAssessmentRepositorySecurity.WRITE;

public interface PreliminaryRiskAssessmentRepository extends JpaRepository<PreliminaryRiskAssessment, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<PreliminaryRiskAssessment> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<PreliminaryRiskAssessment> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PreliminaryRiskAssessment> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PreliminaryRiskAssessment> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PreliminaryRiskAssessment entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    PreliminaryRiskAssessment findByRiskAssessmentId(UUID riskAssessmentId);
}