package pfs.lms.enquiry.action.projectproposal.shareholder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface ShareHolderRepository extends JpaRepository<ShareHolder, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<ShareHolder> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ShareHolder> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ShareHolder> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ShareHolder> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ShareHolder entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ShareHolder> findByProjectProposalId(UUID projectProposalId);
}
