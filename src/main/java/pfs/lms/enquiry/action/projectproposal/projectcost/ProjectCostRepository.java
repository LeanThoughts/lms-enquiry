package pfs.lms.enquiry.action.projectproposal.projectcost;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectCostRepository extends JpaRepository<ProjectCost, UUID> {

    ProjectCost findByProjectProposalId(UUID projectProposalId);
}
