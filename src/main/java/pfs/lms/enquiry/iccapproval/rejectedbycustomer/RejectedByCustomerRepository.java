package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface RejectedByCustomerRepository extends JpaRepository<RejectedByCustomer, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<RejectedByCustomer> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RejectedByCustomer> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RejectedByCustomer> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RejectedByCustomer> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RejectedByCustomer entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    RejectedByCustomer findByIccApprovalId(UUID iccApprovalId);
}