package pfs.lms.enquiry.riskassessment;

import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletion;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletionResource;

public interface IRiskAssessmentService {

    RiskAssessment processRiskAssessmentApproval( RiskAssessment riskAssessment, String username) throws Exception;
    RiskAssessment processRejection( RiskAssessment riskAssessment, String username) throws CloneNotSupportedException;

}
