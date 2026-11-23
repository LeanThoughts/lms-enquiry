package pfs.lms.enquiry.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import pfs.lms.enquiry.domain.LoanApplication;

import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.READ;
import static pfs.lms.enquiry.action.ProcessEnquiryRepositorySecurity.WRITE;

public interface EnquiryActionRepository extends JpaRepository<EnquiryAction, UUID> {

@Override
    @PreAuthorize(READ)
    Optional<EnquiryAction> findById(UUID id);

    @Override
    @PreAuthorize(READ)
    List<EnquiryAction> findAll();

    @Override
    @PreAuthorize(READ)
    Page<EnquiryAction> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends EnquiryAction> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(EnquiryAction entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(UUID id);

    @RestResource(exported = false)
    @PreAuthorize(READ)
    Optional<EnquiryAction> findByLoanApplication(LoanApplication loanApplication);
    @PreAuthorize(READ)
    EnquiryAction findByLoanApplicationId(UUID loanApplicationId);
}
