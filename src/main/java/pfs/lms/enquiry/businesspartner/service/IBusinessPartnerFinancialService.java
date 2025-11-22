package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerFinancialResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;

import java.util.List;
import java.util.UUID;

public interface IBusinessPartnerFinancialService {

    BusinessPartnerFinancial create(BusinessPartnerFinancialResource businessPartnerFinancialResource, String username);
    BusinessPartnerFinancial update(BusinessPartnerFinancialResource businessPartnerFinancialResource, String username) throws CloneNotSupportedException;
}
