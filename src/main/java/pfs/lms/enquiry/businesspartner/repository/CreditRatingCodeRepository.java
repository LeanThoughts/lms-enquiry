package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;

public interface CreditRatingCodeRepository extends JpaRepository<CreditRatingCode, Long> {

    CreditRatingCode findByCode(String code);

}
