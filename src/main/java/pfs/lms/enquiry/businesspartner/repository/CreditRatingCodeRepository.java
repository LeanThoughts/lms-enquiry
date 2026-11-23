package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface CreditRatingCodeRepository extends JpaRepository<CreditRatingCode, Long> {
@Override
    @PreAuthorize(READ)
    Optional<CreditRatingCode> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<CreditRatingCode> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CreditRatingCode> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CreditRatingCode> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CreditRatingCode entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    CreditRatingCode findByCode(String code);

}
