package pfs.lms.enquiry.boardapproval;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.boardapproval.BoardApprovalRepositorySecurity.WRITE;

public interface BoardApprovalRepository extends JpaRepository<BoardApproval, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BoardApproval> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BoardApproval> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BoardApproval> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BoardApproval> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BoardApproval entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    @RestResource(exported = false)
    Optional<BoardApproval> findByLoanApplication(LoanApplication loanApplication);

    @PreAuthorize(READ)
    BoardApproval findByLoanApplicationId(UUID loanApplicationId);
}