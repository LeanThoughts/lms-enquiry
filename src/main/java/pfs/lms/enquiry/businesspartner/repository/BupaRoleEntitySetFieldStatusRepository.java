package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface BupaRoleEntitySetFieldStatusRepository extends JpaRepository<BupaRoleEntitySetFieldStatus, String> {
@Override
    @PreAuthorize(READ)
    Optional<BupaRoleEntitySetFieldStatus> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<BupaRoleEntitySetFieldStatus> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BupaRoleEntitySetFieldStatus> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BupaRoleEntitySetFieldStatus> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BupaRoleEntitySetFieldStatus entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   List<BupaRoleEntitySetFieldStatus> findByBupaRoleCode(String code);
   @PreAuthorize(READ)
   BupaRoleEntitySetFieldStatus findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue( String role, String entity, String fieldName, String keyFieldValue);


}
