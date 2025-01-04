package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;

public interface AmendmentReasonRepository extends JpaRepository<AmendmentReason, String> {

   AmendmentReason findByCode(String code);
}
