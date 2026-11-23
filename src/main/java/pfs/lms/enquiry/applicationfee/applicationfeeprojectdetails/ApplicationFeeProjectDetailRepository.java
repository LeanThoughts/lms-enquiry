package pfs.lms.enquiry.applicationfee.applicationfeeprojectdetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface ApplicationFeeProjectDetailRepository extends JpaRepository<ApplicationFeeProjectDetail, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ApplicationFeeProjectDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ApplicationFeeProjectDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ApplicationFeeProjectDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ApplicationFeeProjectDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ApplicationFeeProjectDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    ApplicationFeeProjectDetail findByApplicationFeeId(UUID applicationFeeId);
}