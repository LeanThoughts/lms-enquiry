package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;

import java.util.List;
import java.util.UUID;

public interface IBusinessPartnerIndustryService {

    BusinessPartnerIndustry create(BusinessPartnerIndustryResource businessPartnerIndustryResource, String username);

    BusinessPartnerIndustry update(BusinessPartnerIndustryResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException;
    BusinessPartnerIndustry migrate(BusinessPartnerIndustryMigrationResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException;

    List<BusinessPartnerIndustryResource> findByPartnerId(UUID partnerId);
}
