package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
@Override
    @PreAuthorize(READ)
    Optional<PaymentMethod> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<PaymentMethod> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PaymentMethod> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PaymentMethod> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PaymentMethod entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    PaymentMethod findById(String id);

}
