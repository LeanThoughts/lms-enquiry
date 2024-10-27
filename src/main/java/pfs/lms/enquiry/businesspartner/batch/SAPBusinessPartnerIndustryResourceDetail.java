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
    private String industrySystem;

    @JsonProperty(value = "Industrysector")
    private String industryType;

    @JsonProperty(value = "Defaultindustrysector")
    private String defaultIndustrySector;


}
