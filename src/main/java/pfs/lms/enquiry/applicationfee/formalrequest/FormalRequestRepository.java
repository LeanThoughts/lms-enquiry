package pfs.lms.enquiry.applicationfee.formalrequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.READ;
import static pfs.lms.enquiry.applicationfee.ApplicationFeeRepositorySecurity.WRITE;

public interface FormalRequestRepository extends JpaRepository<FormalRequest, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<FormalRequest> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<FormalRequest> findAll();

    @Override
    @PreAuthorize(READ)
    Page<FormalRequest> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends FormalRequest> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(FormalRequest entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<FormalRequest> findByApplicationFeeIdOrderBySerialNumber(UUID applicationFeeId);
}