package pfs.lms.enquiry.appraisal.reasonfordelay;

public interface IReasonForDelayService {

    ReasonForDelay createReasonForDelay(ReasonForDelayResource reasonForDelayResource, String username);

    ReasonForDelay updateReasonForDelay(ReasonForDelayResource reasonForDelayResource, String username) throws CloneNotSupportedException;
}
