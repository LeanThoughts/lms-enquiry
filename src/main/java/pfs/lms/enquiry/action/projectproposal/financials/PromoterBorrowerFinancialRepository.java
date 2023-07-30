package pfs.lms.enquiry.action.projectproposal.financials;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PromoterBorrowerFinancialRepository extends JpaRepository<PromoterBorrowerFinancial, UUID> {

    List<PromoterBorrowerFinancial> findByProjectProposalIdOrderByFiscalPeriod(UUID projectProposalId);
    List<PromoterBorrowerFinancial> findByProjectProposalId(UUID projectProposalId);
}
