package pfs.lms.enquiry.appraisal.riskrating;

import java.util.UUID;

public interface ICorporateLoanRiskRatingService {

    CorporateLoanRiskRating createCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, String username);

    CorporateLoanRiskRating updateCorporateLoanRiskRating(CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, String username) throws CloneNotSupportedException;

    CorporateLoanRiskRating deleteCorporateLoanRiskRating(UUID corporateLoanRiskRatingId, String username);
}
