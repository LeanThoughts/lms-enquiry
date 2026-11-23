package pfs.lms.enquiry.action.projectproposal.projectdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<ProjectDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ProjectDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ProjectDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ProjectDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ProjectDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    ProjectDetail findByProjectProposalId(UUID projectProposalId);
}
