package pfs.lms.enquiry.iccapproval.risknotification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface RiskNotificationRepository extends JpaRepository<RiskNotification, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<RiskNotification> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RiskNotification> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RiskNotification> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RiskNotification> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RiskNotification entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<RiskNotification> findByIccApprovalId(UUID iccApprovalId);
}