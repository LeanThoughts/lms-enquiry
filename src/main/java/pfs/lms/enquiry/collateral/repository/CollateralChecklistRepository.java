package pfs.lms.enquiry.collateral.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.collateral.domain.CollateralChecklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.READ;
import static pfs.lms.enquiry.collateral.CollateralRepositorySecurity.WRITE;

/** Not exported by Spring Data REST: access goes through the role-checked collateral controller only. */
@RepositoryRestResource(exported = false)
public interface CollateralChecklistRepository extends JpaRepository<CollateralChecklist, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<CollateralChecklist> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<CollateralChecklist> findAll();

    @Override
    @PreAuthorize(READ)
    Page<CollateralChecklist> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends CollateralChecklist> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(CollateralChecklist entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    Optional<CollateralChecklist> findByLoanApplication_Id(UUID loanApplicationId);
}