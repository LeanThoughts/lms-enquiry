package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerIndustryMigrationResource {

    private String id;

    private String partnerId;

    private Integer serialNumber;

    private String industrySystemId;
    private String industryTypeId;
}
