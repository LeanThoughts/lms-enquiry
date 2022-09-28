package pfs.lms.enquiry.action.projectproposal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, UUID> {

    ProjectProposal findByEnquiryActionId(UUID enquiryActionId);
}
