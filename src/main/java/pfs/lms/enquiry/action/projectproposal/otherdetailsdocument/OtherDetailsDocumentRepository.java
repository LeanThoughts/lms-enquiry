package pfs.lms.enquiry.action.projectproposal.otherdetailsdocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface OtherDetailsDocumentRepository extends JpaRepository<OtherDetailsDocument, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<OtherDetailsDocument> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<OtherDetailsDocument> findAll();

    @Override
    @PreAuthorize(READ)
    Page<OtherDetailsDocument> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends OtherDetailsDocument> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(OtherDetailsDocument entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<OtherDetailsDocument> findByProjectProposalId(UUID projectProposalId);
}
