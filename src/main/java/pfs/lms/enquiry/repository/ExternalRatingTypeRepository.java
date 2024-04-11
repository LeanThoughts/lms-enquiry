package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.appraisal.riskrating.ExternalRating;
import pfs.lms.enquiry.domain.ExternalRatingType;
import pfs.lms.enquiry.domain.LoanType;

import java.util.UUID;

public interface ExternalRatingTypeRepository extends JpaRepository<ExternalRatingType, UUID> {

    ExternalRatingType  getExternalRatingTypeByCode (String code);
}
