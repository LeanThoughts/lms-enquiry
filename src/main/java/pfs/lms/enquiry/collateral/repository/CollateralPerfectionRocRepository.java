package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralPerfectionRoc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralPerfectionRocRepository extends JpaRepository<CollateralPerfectionRoc, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralPerfectionRoc> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<CollateralPerfectionRoc> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralPerfectionRoc> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralPerfectionRoc> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralPerfectionRoc entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<CollateralPerfectionRoc> findByItem_Id(UUID itemId);
}