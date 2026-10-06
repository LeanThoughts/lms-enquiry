package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionNesl;

import java.util.List;
import java.util.UUID;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralPerfectionNeslRepository extends JpaRepository<CollateralPerfectionNesl, UUID> {

    List<CollateralPerfectionNesl> findByItem_Id(UUID itemId);
}
