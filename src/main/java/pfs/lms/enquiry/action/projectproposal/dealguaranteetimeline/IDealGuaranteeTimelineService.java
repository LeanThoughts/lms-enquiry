package pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline;

public interface IDealGuaranteeTimelineService {

    DealGuaranteeTimeline create(DealGuaranteeTimelineResource resource, String username);

    DealGuaranteeTimeline update(DealGuaranteeTimelineResource resource, String username) throws CloneNotSupportedException;
}