package pfs.lms.enquiry.action.otherdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface OtherDetailRepository extends JpaRepository<OtherDetail, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<OtherDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<OtherDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<OtherDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends OtherDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(OtherDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    OtherDetail findByEnquiryActionId(UUID enquiryActionId);
}
