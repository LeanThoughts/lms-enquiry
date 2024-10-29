package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;

public interface IBusinessPartnerIndustryService {

    BusinessPartnerIndustry create(BusinessPartnerIndustryResource businessPartnerIndustryResource);

    BusinessPartnerIndustry update(BusinessPartnerIndustryResource businessPartnerIndustryResource);
}
