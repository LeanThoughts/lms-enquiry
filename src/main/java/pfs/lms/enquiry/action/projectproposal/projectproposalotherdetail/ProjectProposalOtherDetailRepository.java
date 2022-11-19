package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectProposalOtherDetailRepository extends JpaRepository<ProjectProposalOtherDetail, UUID> {

    ProjectProposalOtherDetail findByProjectProposalId(UUID projectProposalId);
}
