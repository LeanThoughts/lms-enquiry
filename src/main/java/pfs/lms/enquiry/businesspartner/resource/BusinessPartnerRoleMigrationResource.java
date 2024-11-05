package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.domain.Partner;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerRoleMigrationResource {

    private String partnerId;

    private String roleType ;

    private String differentiationType;
    private String allPartnerRoles;
    private LocalDate validFromDate;
    private LocalDate validToDate;

//    UUID businessPartnerId;
//    Long roleTypeId;
    
    String defaultRole;
}
