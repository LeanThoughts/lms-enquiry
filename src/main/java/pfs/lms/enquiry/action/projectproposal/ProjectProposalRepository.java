package pfs.lms.enquiry.action.projectproposal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, UUID> {

    List<ProjectProposal> findByEnquiryActionId(UUID enquiryActionId);
}
