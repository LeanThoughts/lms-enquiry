package pfs.lms.enquiry.iccapproval.rejectedbyicc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface RejectedByICCRepository extends JpaRepository<RejectedByICC, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<RejectedByICC> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RejectedByICC> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RejectedByICC> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RejectedByICC> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RejectedByICC entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    RejectedByICC findByIccApprovalId(UUID iccApprovalId);
}