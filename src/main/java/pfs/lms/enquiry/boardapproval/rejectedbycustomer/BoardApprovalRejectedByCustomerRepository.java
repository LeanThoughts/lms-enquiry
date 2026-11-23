package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface BoardApprovalRejectedByCustomerRepository extends JpaRepository<BoardApprovalRejectedByCustomer, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BoardApprovalRejectedByCustomer> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BoardApprovalRejectedByCustomer> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BoardApprovalRejectedByCustomer> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BoardApprovalRejectedByCustomer> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BoardApprovalRejectedByCustomer entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BoardApprovalRejectedByCustomer> findByBoardApprovalId(UUID boardApprovalId);
}