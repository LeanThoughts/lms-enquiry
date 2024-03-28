package pfs.lms.enquiry.appraisal.riskrating;

import java.util.UUID;

public interface IExternalRatingService {

    ExternalRating createExternalRating(ExternalRatingResource externalRatingResource, String username);

    ExternalRating updateExternalRating(ExternalRatingResource externalRatingResource, String username) throws CloneNotSupportedException;

    ExternalRating deleteExternalRating(UUID externalRatingId, String username);
}
