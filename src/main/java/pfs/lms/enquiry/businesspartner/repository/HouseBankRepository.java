package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.HouseBank;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface HouseBankRepository extends JpaRepository<HouseBank, String> {
@Override
    @PreAuthorize(READ)
    Optional<HouseBank> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<HouseBank> findAll();

    @Override
    @PreAuthorize(READ)
    Page<HouseBank> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends HouseBank> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(HouseBank entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   HouseBank findByHouseBankId(String code);
}
