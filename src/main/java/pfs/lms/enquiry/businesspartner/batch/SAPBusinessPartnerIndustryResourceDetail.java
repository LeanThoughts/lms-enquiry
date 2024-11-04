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
public class SAPBusinessPartnerIndustryResourceDetail {

    @JsonProperty(value = "Businesspartner")
    private String busPartnerNumber;

    @JsonProperty(value = "Industrysectorkeysystem")
    private String industrysectorkeysystem;

    @JsonProperty(value = "Industrysector")
    private String industrysector;

    @JsonProperty(value = "Defaultindustrysector")
    private String defaultIndustrySector;

    @JsonProperty(value = "EntityId")
    private String entityId;
}
