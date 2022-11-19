package pfs.lms.enquiry.action.projectproposal.creditrating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CreditRatingRepository extends JpaRepository<CreditRating, UUID> {

    List<CreditRating> findByProjectProposalId(UUID projectProposalId);
}
