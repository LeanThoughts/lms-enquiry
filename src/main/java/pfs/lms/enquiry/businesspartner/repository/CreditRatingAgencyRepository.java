package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface CreditRatingAgencyRepository extends JpaRepository<CreditRatingAgency, Long> {
@Override
    @PreAuthorize(READ)
    Optional<CreditRatingAgency> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<CreditRatingAgency> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CreditRatingAgency> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CreditRatingAgency> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CreditRatingAgency entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    CreditRatingAgency findByCode(String code);

}
