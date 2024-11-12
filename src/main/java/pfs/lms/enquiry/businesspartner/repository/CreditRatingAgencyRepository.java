package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;

public interface CreditRatingAgencyRepository extends JpaRepository<CreditRatingAgency, Long> {

    CreditRatingAgency findByCode(String code);

}
