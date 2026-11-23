package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface AmendmentReasonRepository extends JpaRepository<AmendmentReason, String> {
@Override
    @PreAuthorize(READ)
    Optional<AmendmentReason> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<AmendmentReason> findAll();

    @Override
    @PreAuthorize(READ)
    Page<AmendmentReason> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends AmendmentReason> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(AmendmentReason entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);


   @PreAuthorize(READ)
   AmendmentReason findByCode(String code);
}
