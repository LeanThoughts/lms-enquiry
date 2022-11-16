package pfs.lms.enquiry.action.projectproposal.shareholder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShareHolderRepository extends JpaRepository<ShareHolder, UUID> {

    List<ShareHolder> findByProjectProposalId(UUID projectProposalId);
}
