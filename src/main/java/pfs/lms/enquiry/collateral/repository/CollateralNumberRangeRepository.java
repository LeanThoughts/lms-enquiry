package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.CollateralNumberRange;

import javax.persistence.LockModeType;
import java.util.Optional;

/** Not exported by Spring Data REST. */
@RepositoryRestResource(exported = false)
public interface CollateralNumberRangeRepository extends JpaRepository<CollateralNumberRange, String> {

    /** Reads the range and locks its row until the transaction ends (SELECT ... FOR UPDATE). */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from CollateralNumberRange r where r.name = :name")
    Optional<CollateralNumberRange> findForUpdate(@Param("name") String name);
}
