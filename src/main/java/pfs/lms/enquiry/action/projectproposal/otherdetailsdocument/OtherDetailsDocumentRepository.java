package pfs.lms.enquiry.action.projectproposal.otherdetailsdocument;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OtherDetailsDocumentRepository extends JpaRepository<OtherDetailsDocument, UUID> {

    List<OtherDetailsDocument> findByProjectProposalId(UUID projectProposalId);
}
