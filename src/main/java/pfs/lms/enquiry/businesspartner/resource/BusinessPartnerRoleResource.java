package pfs.lms.enquiry.businesspartner.resource;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.domain.Partner;
import java.util.UUID;
import java.lang.Long;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerRoleResource {
    Partner partner;

    private BusinessPartnerRoleType roleType ;

    private String differentiationType;
    private String allPartnerRoles;
    private LocalDate validFromDate;
    private LocalDate validToDate;

    UUID businessPartnerId;
    Long roleTypeId;
    
    boolean defaultRole;
}
