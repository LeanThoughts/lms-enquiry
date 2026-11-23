package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCApprovalByICCRepository extends JpaRepository<BmcApprovalByIcc, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcApprovalByIcc> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcApprovalByIcc> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcApprovalByIcc> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcApprovalByIcc> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcApprovalByIcc entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    BmcApprovalByIcc findByBmcICCApprovalId(UUID iccApprovalId);
}