package pfs.lms.enquiry.iccapproval;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface ICCApprovalRepository extends JpaRepository<ICCApproval, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ICCApproval> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ICCApproval> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ICCApproval> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ICCApproval> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ICCApproval entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<ICCApproval> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    ICCApproval findByLoanApplicationId(UUID loanApplicationId);
}