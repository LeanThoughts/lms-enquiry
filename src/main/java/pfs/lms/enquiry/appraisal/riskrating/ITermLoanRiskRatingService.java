package pfs.lms.enquiry.appraisal.riskrating;

import java.util.UUID;

public interface ITermLoanRiskRatingService {

    TermLoanRiskRating createTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource, String username);

    TermLoanRiskRating updateTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource, String username) throws CloneNotSupportedException;

    TermLoanRiskRating deleteTermLoanRiskRating(UUID termLoanRiskRatingId, String username);
}
