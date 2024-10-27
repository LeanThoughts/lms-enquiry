package pfs.lms.enquiry.businesspartner.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SAPBusinessPartnerRoleResourceDetail {

    @JsonProperty(value = "Businesspartner")
    private String busPartnerNumber;

    @JsonProperty(value = "Partnerrole")
    private String  roleType;

    @JsonProperty(value = "Difftypevalue")
    private String  differentiationType;

    @JsonProperty(value = "Partnerrolecategory")
    private String  partnerRoleCategory;

    @JsonProperty(value = "AllPartnerroles")
    private String  allPartneRoles;

    @JsonProperty(value = "ValidFrom")
    private String validFromDate ;

    @JsonProperty(value = "ValidFrom")
    private String validToDate ;

}
