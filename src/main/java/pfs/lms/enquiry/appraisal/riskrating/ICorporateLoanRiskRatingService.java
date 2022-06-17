package pfs.lms.enquiry.appraisal.riskrating;

import java.util.UUID;

public interface ICorporateLoanRiskRatingService {

    CorporateLoanRiskRating createCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource);

    CorporateLoanRiskRating updateCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource);

    CorporateLoanRiskRating deleteCorporateLoanRiskRating(UUID corporateLoanRiskRatingId);
}
