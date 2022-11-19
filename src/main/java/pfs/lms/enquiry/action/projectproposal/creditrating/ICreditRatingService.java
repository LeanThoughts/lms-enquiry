package pfs.lms.enquiry.action.projectproposal.creditrating;

public interface ICreditRatingService {

    CreditRating create(CreditRatingResource resource, String username);

    CreditRating update(CreditRatingResource resource, String username) throws CloneNotSupportedException;
}
