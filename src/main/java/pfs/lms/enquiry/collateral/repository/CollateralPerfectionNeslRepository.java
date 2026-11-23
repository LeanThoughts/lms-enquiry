package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionNesl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralPerfectionNeslRepository extends JpaRepository<CollateralPerfectionNesl, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralPerfectionNesl> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<CollateralPerfectionNesl> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralPerfectionNesl> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralPerfectionNesl> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralPerfectionNesl entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<CollateralPerfectionNesl> findByItem_Id(UUID itemId);
}