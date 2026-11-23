package pfs.lms.enquiry.referenceinterest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;

import java.util.List;
import java.util.Optional;

import static pfs.lms.enquiry.referenceinterest.ReferenceInterestRepositorySecurity.READ;
import static pfs.lms.enquiry.referenceinterest.ReferenceInterestRepositorySecurity.WRITE;

public interface ReferenceInterestRateRepository extends JpaRepository<ReferenceInterestRate, Long> {

    @Override
    @PreAuthorize(READ)
    Optional<ReferenceInterestRate> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<ReferenceInterestRate> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ReferenceInterestRate> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ReferenceInterestRate> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ReferenceInterestRate entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);

    @PreAuthorize(READ)
    ReferenceInterestRate findByCode(String code);
}