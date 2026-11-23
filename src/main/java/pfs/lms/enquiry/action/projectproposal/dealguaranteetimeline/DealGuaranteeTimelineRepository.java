package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface DealGuaranteeTimelineRepository extends JpaRepository<DealGuaranteeTimeline, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<DealGuaranteeTimeline> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<DealGuaranteeTimeline> findAll();

    @Override
    @PreAuthorize(READ)
    Page<DealGuaranteeTimeline> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends DealGuaranteeTimeline> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(DealGuaranteeTimeline entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    DealGuaranteeTimeline findByProjectProposalId(UUID projectProposalId);
}
