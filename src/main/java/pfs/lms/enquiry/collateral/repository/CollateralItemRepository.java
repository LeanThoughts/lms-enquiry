package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralItemRepository extends JpaRepository<CollateralItem, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralItem> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<CollateralItem> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralItem> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralItem> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralItem entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<CollateralItem> findByChecklist_Id(UUID checklistId);

    @PreAuthorize(READ)
    long countByChecklist_Id(UUID checklistId);

    @PreAuthorize(READ)
    boolean existsByChecklistIdNo(Long checklistIdNo);

    @PreAuthorize(READ)
    Optional<CollateralItem> findByChecklistIdNo(Long checklistIdNo);

    @PreAuthorize(READ)
    @Query("select max(i.checklistIdNo) from CollateralItem i")
    Long findHighestChecklistIdNo();

    @PreAuthorize(READ)
    @Query("select max(i.checklistIdNo) from CollateralItem i where i.sourceOfEntry = :sourceOfEntry")
    Long findHighestChecklistIdNo(@Param("sourceOfEntry") String sourceOfEntry);

    /** Collaterals created before Checklist IDs were generated. */
    @PreAuthorize(READ)
    List<CollateralItem> findByChecklistIdNoIsNull();
}