package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.READ;
import static pfs.lms.enquiry.iccapproval.IccApprovalRepositorySecurity.WRITE;

public interface ICCFurtherDetailRepository extends JpaRepository<ICCFurtherDetail, UUID> {

    @Override
    @PreAuthorize(READ)
    Optional<ICCFurtherDetail> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<ICCFurtherDetail> findAll();

    @Override
    @PreAuthorize(READ)
    Page<ICCFurtherDetail> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends ICCFurtherDetail> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(ICCFurtherDetail entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @PreAuthorize(READ)
    List<ICCFurtherDetail> findByIccApprovalId(UUID iccApprovalId);
}