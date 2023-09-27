package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DealGuaranteeTimelineRepository extends JpaRepository<DealGuaranteeTimeline, UUID> {

    DealGuaranteeTimeline findByProjectProposalId(UUID projectProposalId);
}