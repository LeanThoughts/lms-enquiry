package pfs.lms.enquiry.action.projectproposal.financials;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface PromoterBorrowerFinancialRepository extends JpaRepository<PromoterBorrowerFinancial, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<PromoterBorrowerFinancial> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<PromoterBorrowerFinancial> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PromoterBorrowerFinancial> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PromoterBorrowerFinancial> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PromoterBorrowerFinancial entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<PromoterBorrowerFinancial> findByProjectProposalIdOrderByFiscalPeriod(UUID projectProposalId);
    @PreAuthorize(READ)
    List<PromoterBorrowerFinancial> findByProjectProposalId(UUID projectProposalId);
    @PreAuthorize(READ)
    List<PromoterBorrowerFinancial> findByProjectProposalIdAndFiscalPeriod(UUID projectProposalId, String fiscalPeriod);
}
