package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;

public interface IBusinessPartnerLoanContactService {

    BusinessPartnerLoanContact create(BusinessPartnerLoanContactResource businessPartnerLoanContactResource, String username);

    BusinessPartnerLoanContact update(BusinessPartnerLoanContactResource businessPartnerLoanContactResource, String username) throws CloneNotSupportedException;

    BusinessPartnerLoanContact migrate(BusinessPartnerLoanContactMigrationResource businessPartnerLoanContactResource, String username) throws CloneNotSupportedException;

}
