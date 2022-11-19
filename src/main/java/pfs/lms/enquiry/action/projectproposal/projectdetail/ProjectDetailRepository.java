package pfs.lms.enquiry.action.projectproposal.projectdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, UUID> {

    ProjectDetail findByProjectProposalId(UUID projectProposalId);
}
