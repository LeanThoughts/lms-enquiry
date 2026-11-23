package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PaymentTerms;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface PaymentTermsRepository extends JpaRepository<PaymentTerms, Long> {
@Override
    @PreAuthorize(READ)
    Optional<PaymentTerms> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<PaymentTerms> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PaymentTerms> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PaymentTerms> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PaymentTerms entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    PaymentTerms findById(String id);

}
