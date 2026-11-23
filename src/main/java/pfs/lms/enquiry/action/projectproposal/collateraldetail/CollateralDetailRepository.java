package pfs.lms.enquiry.action.projectproposal.collateraldetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface CollateralDetailRepository extends JpaRepository<CollateralDetail, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<CollateralDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<CollateralDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<CollateralDetail> findByProjectProposalId(UUID projectProposalId);
}
