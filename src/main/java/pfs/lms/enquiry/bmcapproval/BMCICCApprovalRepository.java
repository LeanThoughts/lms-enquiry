package pfs.lms.enquiry.bmcapproval;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCICCApprovalRepository extends JpaRepository<BmcIccApproval, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcIccApproval> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcIccApproval> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcIccApproval> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcIccApproval> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcIccApproval entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<BmcIccApproval> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    BmcIccApproval findByLoanApplicationId(UUID loanApplicationId);
}