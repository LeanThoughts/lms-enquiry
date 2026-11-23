package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCRejectedByICCRepository extends JpaRepository<BmcRejectedByIcc, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcRejectedByIcc> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcRejectedByIcc> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcRejectedByIcc> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcRejectedByIcc> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcRejectedByIcc entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    BmcRejectedByIcc findByBmcICCApprovalId(UUID iccApprovalId);
}