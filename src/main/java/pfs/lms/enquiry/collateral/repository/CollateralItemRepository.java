package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralItemRepository extends JpaRepository<CollateralItem, UUID> {

    List<CollateralItem> findByChecklist_Id(UUID checklistId);

    long countByChecklist_Id(UUID checklistId);

    boolean existsByChecklistIdNo(Long checklistIdNo);

    Optional<CollateralItem> findByChecklistIdNo(Long checklistIdNo);

    @Query("select max(i.checklistIdNo) from CollateralItem i")
    Long findHighestChecklistIdNo();

    @Query("select max(i.checklistIdNo) from CollateralItem i where i.sourceOfEntry = :sourceOfEntry")
    Long findHighestChecklistIdNo(@Param("sourceOfEntry") String sourceOfEntry);

    /** Collaterals created before Checklist IDs were generated. */
    List<CollateralItem> findByChecklistIdNoIsNull();
}
