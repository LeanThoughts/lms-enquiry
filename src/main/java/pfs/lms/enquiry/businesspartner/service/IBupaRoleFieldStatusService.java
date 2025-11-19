package pfs.lms.enquiry.businesspartner.service;

import pfs.lms.enquiry.businesspartner.resource.BupaRoleFieldStatusResource;

public interface IBupaRoleFieldStatusService {

    BupaRoleFieldStatusResource getFieldStatusByBupaRole(String businessPartnerRole);

}
