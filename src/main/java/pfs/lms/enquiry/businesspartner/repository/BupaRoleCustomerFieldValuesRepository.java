package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface BupaRoleCustomerFieldValuesRepository extends JpaRepository<BupaRoleCustomerFieldValues, String> {
@Override
    @PreAuthorize(READ)
    Optional<BupaRoleCustomerFieldValues> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<BupaRoleCustomerFieldValues> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BupaRoleCustomerFieldValues> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BupaRoleCustomerFieldValues> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BupaRoleCustomerFieldValues entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);



   @PreAuthorize(READ)
   BupaRoleCustomerFieldValues findByBupaRoleCode(String code);
   @PreAuthorize(READ)
   BupaRoleCustomerFieldValues findByBupaRoleCodeAndPartnerGroup(String roleType, String partnerGroup);

}
