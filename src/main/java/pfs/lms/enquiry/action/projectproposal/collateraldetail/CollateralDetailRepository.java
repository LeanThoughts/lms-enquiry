package pfs.lms.enquiry.action.projectproposal.collateraldetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CollateralDetailRepository extends JpaRepository<CollateralDetail, UUID> {

    List<CollateralDetail> findByProjectProposalId(UUID projectProposalId);
}
