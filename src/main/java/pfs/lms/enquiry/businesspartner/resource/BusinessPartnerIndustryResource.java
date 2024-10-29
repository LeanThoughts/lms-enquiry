package pfs.lms.enquiry.businesspartner.resource;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerIndustryResource {

    private UUID id;

    private UUID partnerId;

    private Integer serialNumber;

    private UUID industrySystemId;    
    private UUID industryTypeId;
}
