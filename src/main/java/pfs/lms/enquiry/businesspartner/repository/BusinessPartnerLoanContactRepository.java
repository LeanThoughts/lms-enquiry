package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.domain.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

public interface BusinessPartnerLoanContactRepository extends JpaRepository<BusinessPartnerLoanContact, UUID> {
@Override
    @PreAuthorize(READ)
    Optional<BusinessPartnerLoanContact> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BusinessPartnerLoanContact> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BusinessPartnerLoanContact> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BusinessPartnerLoanContact> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BusinessPartnerLoanContact entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);


    @PreAuthorize(READ)
    List<BusinessPartnerLoanContact> findByPartnerIdOrderBySerialNumberDesc(UUID partnerId);
    
    @PreAuthorize(READ)
    Optional<BusinessPartnerLoanContact> findFirstByPartnerOrderBySerialNumberDesc(Partner partner);

    @PreAuthorize(READ)
    List<BusinessPartnerLoanContact> findByLoanNumber(String loanNumber);
}
