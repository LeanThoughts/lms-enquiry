package pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCICCReasonForDelayRepository extends JpaRepository<BmcIccReasonForDelay, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcIccReasonForDelay> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcIccReasonForDelay> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcIccReasonForDelay> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcIccReasonForDelay> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcIccReasonForDelay entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BmcIccReasonForDelay> findByBmcICCApprovalId(UUID iccApprovalId);
}