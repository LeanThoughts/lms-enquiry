package pfs.lms.enquiry.referenceinterest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SAPReferenceInterestRateValueResourceDetail {

    @JsonProperty(value = "Referenz")
    private String referenz;



    @JsonProperty(value = "Datab")
    private String datab;

    @JsonProperty(value = "Zsoll")
    private String zsoll;

}
