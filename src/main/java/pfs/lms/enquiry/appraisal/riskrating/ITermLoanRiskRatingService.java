package pfs.lms.enquiry.appraisal.riskrating;

import java.util.UUID;

public interface ITermLoanRiskRatingService {

    TermLoanRiskRating createTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource);

    TermLoanRiskRating updateTermLoanRiskRating(TermLoanRiskRatingResource termLoanRiskRatingResource);

    TermLoanRiskRating deleteTermLoanRiskRating(UUID termLoanRiskRatingId);
}
