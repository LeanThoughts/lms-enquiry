package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.domain.Partner;

public interface IBusinessPartnerService {

    Partner updatePartnerAfterApproval(Partner partner, String username) throws CloneNotSupportedException;
    Partner updatePartnerAfterRejection(Partner partner, String username) throws CloneNotSupportedException;
}
