package pfs.lms.enquiry.boardapproval.reasonfordelay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface BoardApprovalReasonForDelayRepository extends JpaRepository<BoardApprovalReasonForDelay, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BoardApprovalReasonForDelay> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BoardApprovalReasonForDelay> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BoardApprovalReasonForDelay> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BoardApprovalReasonForDelay> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BoardApprovalReasonForDelay entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BoardApprovalReasonForDelay> findByBoardApprovalId(UUID boardApprovalId);
}