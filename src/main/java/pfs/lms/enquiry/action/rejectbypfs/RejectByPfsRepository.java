package pfs.lms.enquiry.action.rejectbypfs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface RejectByPfsRepository extends JpaRepository<RejectByPfs, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<RejectByPfs> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<RejectByPfs> findAll();

    @Override
    @PreAuthorize(READ)
    Page<RejectByPfs> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends RejectByPfs> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(RejectByPfs entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    RejectByPfs findByEnquiryActionId(UUID enquiryActionId);
}
