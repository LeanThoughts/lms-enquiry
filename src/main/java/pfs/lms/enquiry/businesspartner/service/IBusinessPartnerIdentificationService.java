package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;

public interface IBusinessPartnerIdentificationService {

    BusinessPartnerIdentification create(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username);

    BusinessPartnerIdentification update(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username) throws CloneNotSupportedException;
}
