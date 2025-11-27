package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerKYCDetail;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerKYCDetailResource;

import java.util.List;
import java.util.UUID;

public interface IBusinessPartnerKYCDetailService {

    BusinessPartnerKYCDetail create(BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource, String username);
    BusinessPartnerKYCDetail update(BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource, String username)
            throws CloneNotSupportedException;
}
