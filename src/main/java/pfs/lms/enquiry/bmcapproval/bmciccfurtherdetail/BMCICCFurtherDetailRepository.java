package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.bmcapproval.BmcApprovalRepositorySecurity.WRITE;

public interface BMCICCFurtherDetailRepository extends JpaRepository<BmcIccFurtherDetail, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<BmcIccFurtherDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<BmcIccFurtherDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<BmcIccFurtherDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends BmcIccFurtherDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(BmcIccFurtherDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<BmcIccFurtherDetail> findByBmcICCApprovalId(UUID iccApprovalId);
}