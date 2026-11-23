package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.DunningProcedure;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface DunningProcedureRepository extends JpaRepository<DunningProcedure, Long> {
@Override
    @PreAuthorize(READ)
    Optional<DunningProcedure> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<DunningProcedure> findAll();

    @Override
    @PreAuthorize(READ)
    Page<DunningProcedure> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends DunningProcedure> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(DunningProcedure entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    DunningProcedure findById(String id);

}
