package pfs.lms.enquiry.boardapproval.approvalbyboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface ApprovalByBoardRepository extends JpaRepository<ApprovalByBoard, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ApprovalByBoard> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ApprovalByBoard> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ApprovalByBoard> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ApprovalByBoard> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ApprovalByBoard entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ApprovalByBoard> findByBoardApprovalId(UUID boardApprovalId);
}