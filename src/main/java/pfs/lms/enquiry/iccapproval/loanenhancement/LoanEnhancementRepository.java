package pfs.lms.enquiry.iccapproval.loanenhancement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface LoanEnhancementRepository extends JpaRepository<LoanEnhancement, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<LoanEnhancement> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<LoanEnhancement> findAll();

    @Override
    @PreAuthorize(READ)
    Page<LoanEnhancement> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends LoanEnhancement> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(LoanEnhancement entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<LoanEnhancement> findByIccApprovalId(UUID iccApprovalId);
}