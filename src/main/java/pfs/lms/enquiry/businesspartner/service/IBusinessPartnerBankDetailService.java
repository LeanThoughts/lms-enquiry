package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;

public interface IBusinessPartnerBankDetailService {

    BusinessPartnerBankDetail create(BusinessPartnerBankDetailResource businessPartnerBankDetailResource);

    BusinessPartnerBankDetail update(BusinessPartnerBankDetailResource businessPartnerBankDetailResource);
}
