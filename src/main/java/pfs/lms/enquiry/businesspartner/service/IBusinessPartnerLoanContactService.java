package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;

public interface IBusinessPartnerLoanContactService {

    BusinessPartnerLoanContact create(BusinessPartnerLoanContactResource businessPartnerLoanContactResource);

    BusinessPartnerLoanContact update(BusinessPartnerLoanContactResource businessPartnerLoanContactResource);
}
