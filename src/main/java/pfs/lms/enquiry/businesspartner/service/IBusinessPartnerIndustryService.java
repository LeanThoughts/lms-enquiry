package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;

public interface IBusinessPartnerIndustryService {

    BusinessPartnerIndustry create(BusinessPartnerIndustryResource businessPartnerIndustryResource, String username);

    BusinessPartnerIndustry update(BusinessPartnerIndustryResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException;
    BusinessPartnerIndustry migrate(BusinessPartnerIndustryMigrationResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException;


}
