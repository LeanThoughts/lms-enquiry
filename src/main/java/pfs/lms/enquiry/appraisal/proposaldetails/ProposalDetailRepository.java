package pfs.lms.enquiry.appraisal.proposaldetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProposalDetailRepository extends JpaRepository<ProposalDetail, UUID> {

    ProposalDetail findByLoanAppraisalId(UUID loanAppraisalId);
}
