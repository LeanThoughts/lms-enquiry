package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface ProjectProposalOtherDetailRepository extends JpaRepository<ProjectProposalOtherDetail, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<ProjectProposalOtherDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ProjectProposalOtherDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ProjectProposalOtherDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ProjectProposalOtherDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ProjectProposalOtherDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    ProjectProposalOtherDetail findByProjectProposalId(UUID projectProposalId);
}
