package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCLoanEnhancementRepository extends JpaRepository<BmcLoanEnhancement, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcLoanEnhancement> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcLoanEnhancement> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcLoanEnhancement> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcLoanEnhancement> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcLoanEnhancement entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BmcLoanEnhancement> findByBmcICCApprovalId(UUID iccApprovalId);
}