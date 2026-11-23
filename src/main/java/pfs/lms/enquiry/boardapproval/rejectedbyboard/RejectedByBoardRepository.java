package pfs.lms.enquiry.boardapproval.rejectedbyboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface RejectedByBoardRepository extends JpaRepository<RejectedByBoard, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<RejectedByBoard> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RejectedByBoard> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RejectedByBoard> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RejectedByBoard> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RejectedByBoard entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<RejectedByBoard> findByBoardApprovalId(UUID boardApprovalId);
}