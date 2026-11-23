package pfs.lms.enquiry.riskassessment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.riskassessment.RiskAssessmentRepositorySecurity.READ;
import static pfs.lms.enquiry.riskassessment.RiskAssessmentRepositorySecurity.WRITE;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<RiskAssessment> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RiskAssessment> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RiskAssessment> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RiskAssessment> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RiskAssessment entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<RiskAssessment> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    RiskAssessment findByLoanApplicationId(UUID loanApplicationId);
}