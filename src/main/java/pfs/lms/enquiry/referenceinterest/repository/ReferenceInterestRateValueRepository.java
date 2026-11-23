package pfs.lms.enquiry.referenceinterest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.referenceinterest.ReferenceInterestRepositorySecurity.READ;
import static pfs.lms.enquiry.referenceinterest.ReferenceInterestRepositorySecurity.WRITE;

public interface ReferenceInterestRateValueRepository extends JpaRepository<ReferenceInterestRateValue, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ReferenceInterestRateValue> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ReferenceInterestRateValue> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ReferenceInterestRateValue> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ReferenceInterestRateValue> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ReferenceInterestRateValue entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ReferenceInterestRateValue> findByReferenceInterestRateIdOrderByValidFromDate(Long referenceInterestRateId);

    @PreAuthorize(READ)
    ReferenceInterestRateValue findByReferenceInterestRateIdAndValidFromDate(Long id, LocalDate validFromDate);
}