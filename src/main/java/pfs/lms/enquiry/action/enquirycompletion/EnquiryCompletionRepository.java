package pfs.lms.enquiry.action.enquirycompletion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface EnquiryCompletionRepository extends JpaRepository<EnquiryCompletion, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<EnquiryCompletion> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<EnquiryCompletion> findAll();

    @Override
    @PreAuthorize(READ)
    Page<EnquiryCompletion> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends EnquiryCompletion> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(EnquiryCompletion entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    EnquiryCompletion findByEnquiryActionId(UUID enquiryActionId);
}
