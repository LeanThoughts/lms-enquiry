package pfs.lms.enquiry.boardapproval.deferredbyboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface DeferredByBoardRepository extends JpaRepository<DeferredByBoard, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<DeferredByBoard> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<DeferredByBoard> findAll();

    @Override
    @PreAuthorize(READ)
    Page<DeferredByBoard> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends DeferredByBoard> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(DeferredByBoard entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<DeferredByBoard> findByBoardApprovalId(UUID boardApprovalId);
}