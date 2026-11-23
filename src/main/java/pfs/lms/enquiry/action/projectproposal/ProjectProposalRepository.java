package pfs.lms.enquiry.action.projectproposal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<ProjectProposal> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ProjectProposal> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ProjectProposal> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ProjectProposal> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ProjectProposal entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ProjectProposal> findByEnquiryActionIdOrderBySerialNumber(UUID enquiryActionId);
    @PreAuthorize(READ)
    List<ProjectProposal> findByEnquiryActionIdAndProposalStatus(UUID enquiryActionId, String proposalStatus);
}
