package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralAttributesValueList;

import java.util.List;
import java.util.Optional;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: the value lists are served by the collateral controller. */
@RepositoryRestResource(exported = false)
public interface CollateralAttributesValueListRepository extends JpaRepository<CollateralAttributesValueList, Long> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralAttributesValueList> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<CollateralAttributesValueList> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralAttributesValueList> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralAttributesValueList> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralAttributesValueList entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);

    @PreAuthorize(READ)
    List<CollateralAttributesValueList> findByListName(String listName);
}