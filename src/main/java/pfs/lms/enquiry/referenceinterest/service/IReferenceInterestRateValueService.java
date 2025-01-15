package pfs.lms.enquiry.referenceinterest.service;

import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;
import pfs.lms.enquiry.referenceinterest.resource.ReferenceInterestValueResource;

public interface IReferenceInterestRateValueService {


    ReferenceInterestRateValue create(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception;

    ReferenceInterestRateValue update(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception;

    ReferenceInterestRateValue delete(ReferenceInterestValueResource referenceInterestValueResource, String username) throws Exception;


    ReferenceInterestRateValue processApprovedReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue,String username) throws CloneNotSupportedException;
    ReferenceInterestRateValue processRejectionReferenceInterestValue(ReferenceInterestRateValue referenceInterestValue, String username) throws CloneNotSupportedException;
}
