package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleResource;

public interface IBusinessPartnerRoleService {

    BusinessPartnerRole create(BusinessPartnerRoleResource businessPartnerRoleResource,String username);
    BusinessPartnerRole migrate(BusinessPartnerRoleMigrationResource businessPartnerRoleResource,String username);


}
