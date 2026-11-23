package pfs.lms.enquiry.action.projectproposal.projectcost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface ProjectCostRepository extends JpaRepository<ProjectCost, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<ProjectCost> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ProjectCost> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ProjectCost> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ProjectCost> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ProjectCost entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    ProjectCost findByProjectProposalId(UUID projectProposalId);
}
