package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface BupaRoleEntityFieldStatusRepository extends JpaRepository<BupaRoleEntityFieldStatus, String> {
@Override
    @PreAuthorize(READ)
    Optional<BupaRoleEntityFieldStatus> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<BupaRoleEntityFieldStatus> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BupaRoleEntityFieldStatus> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BupaRoleEntityFieldStatus> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BupaRoleEntityFieldStatus entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   List<BupaRoleEntityFieldStatus> findByBupaRoleCode(String code);
   @PreAuthorize(READ)
   BupaRoleEntityFieldStatus findByBupaRoleCodeAndEntityAndAndFieldName( String role, String entity, String fieldName);

}
