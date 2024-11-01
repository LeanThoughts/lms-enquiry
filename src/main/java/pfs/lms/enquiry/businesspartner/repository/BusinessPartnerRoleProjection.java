package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.rest.core.config.Projection;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import java.util.UUID;

@Projection(name = "businessPartnerRoleProjection", types = { BusinessPartnerRole.class })
public interface BusinessPartnerRoleProjection {

    UUID getId();

    // PartnerId getPartner();
    
    BusinessPartnerRoleType getRoleType() ;

    // interface PartnerId {
    //     UUID getId();
    // }
}