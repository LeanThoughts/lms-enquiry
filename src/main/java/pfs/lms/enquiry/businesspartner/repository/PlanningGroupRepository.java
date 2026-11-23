package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PlanningGroup;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.List;
import java.util.Optional;

public interface PlanningGroupRepository extends JpaRepository<PlanningGroup, Long> {
@Override
    @PreAuthorize(READ)
    Optional<PlanningGroup> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<PlanningGroup> findAll();

    @Override
    @PreAuthorize(READ)
    Page<PlanningGroup> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends PlanningGroup> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(PlanningGroup entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    PlanningGroup findById(String id);

}
