package pfs.lms.enquiry.sanction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.READ;
import static pfs.lms.enquiry.sanction.SanctionRepositorySecurity.WRITE;

public interface SanctionRepository extends JpaRepository<Sanction, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<Sanction> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<Sanction> findAll();

    @Override
    @PreAuthorize(READ)
    Page<Sanction> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends Sanction> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(Sanction entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<Sanction> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    Sanction findByLoanApplicationId(UUID loanApplicationId);
}