package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralNumberRange;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST. */
@RepositoryRestResource(exported = false)
public interface CollateralNumberRangeRepository extends JpaRepository<CollateralNumberRange, String> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralNumberRange> findById(String id);

    @Override
    @PreAuthorize(READ)
    List<CollateralNumberRange> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralNumberRange> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralNumberRange> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralNumberRange entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(String id);

    /** Reads the range and locks its row until the transaction ends (SELECT ... FOR UPDATE). */
    @PreAuthorize(WRITE)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from CollateralNumberRange r where r.name = :name")
    Optional<CollateralNumberRange> findForUpdate(@Param("name") String name);
}