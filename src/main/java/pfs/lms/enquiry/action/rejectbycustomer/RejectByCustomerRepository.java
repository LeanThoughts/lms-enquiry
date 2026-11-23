package pfs.lms.enquiry.action.rejectbycustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface RejectByCustomerRepository extends JpaRepository<RejectByCustomer, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<RejectByCustomer> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RejectByCustomer> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RejectByCustomer> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RejectByCustomer> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RejectByCustomer entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    RejectByCustomer findByEnquiryActionId(UUID enquiryActionId);
}
