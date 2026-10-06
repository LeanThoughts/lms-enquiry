package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.CollateralValue;

import java.util.List;

/** Not exported by Spring Data REST: the value lists are served by the collateral controller. */
@RepositoryRestResource(exported = false)
public interface CollateralValueRepository extends JpaRepository<CollateralValue, Long> {

    List<CollateralValue> findByListName(String listName);
}
