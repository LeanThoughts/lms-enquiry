package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralValue;

import java.util.List;
import java.util.Optional;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: the value lists are served by the collateral controller. */
@RepositoryRestResource(exported = false)
public interface CollateralValueRepository extends JpaRepository<CollateralValue, Long> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralValue> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<CollateralValue> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralValue> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralValue> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralValue entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);

    @PreAuthorize(READ)
    List<CollateralValue> findByListName(String listName);
}