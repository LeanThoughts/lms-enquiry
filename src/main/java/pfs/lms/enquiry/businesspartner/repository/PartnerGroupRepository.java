package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.PartnerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface PartnerGroupRepository extends JpaRepository<PartnerGroup, String> {
@Override
    @PreAuthorize(READ)
    Optional<PartnerGroup> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<PartnerGroup> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PartnerGroup> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PartnerGroup> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PartnerGroup entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   PartnerGroup findByCode(String code);
}
