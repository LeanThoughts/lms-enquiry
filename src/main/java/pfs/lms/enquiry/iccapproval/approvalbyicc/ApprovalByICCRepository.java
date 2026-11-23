package pfs.lms.enquiry.iccapproval.approvalbyicc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface ApprovalByICCRepository extends JpaRepository<ApprovalByICC, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ApprovalByICC> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ApprovalByICC> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ApprovalByICC> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ApprovalByICC> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ApprovalByICC entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    ApprovalByICC findByIccApprovalId(UUID iccApprovalId);
}