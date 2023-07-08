package pfs.lms.enquiry.action.projectproposal.creditrating;

import java.util.UUID;

public interface ICreditRatingService {

    CreditRating create(CreditRatingResource resource, String username);

    CreditRating update(CreditRatingResource resource, String username) throws CloneNotSupportedException;

    CreditRating delete(UUID creditRatingId, String username);
}
