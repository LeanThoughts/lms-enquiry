package pfs.lms.enquiry.applicationfee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface ApplicationFeeRepository extends JpaRepository<ApplicationFee, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ApplicationFee> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ApplicationFee> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ApplicationFee> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ApplicationFee> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ApplicationFee entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<ApplicationFee> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    ApplicationFee findByLoanApplicationId(UUID loanApplicationId);
}