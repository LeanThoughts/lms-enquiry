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
public class SAPBusinessPartnerIdentificationResourceDetail {

    @JsonProperty(value = "Businesspartner")
    private String busPartnerNumber;

    @JsonProperty(value = "Identificationcategory")
    private String identificationCategory;

    @JsonProperty(value = "Identificationnumber")
    private String identificationNumber;
    @JsonProperty(value = "Idinstitute")
    private String idInstitute;
    @JsonProperty(value = "Identrydate")
    private String idEntryDate;
    @JsonProperty(value = "Idvalidfromdate")
    private String idValidFromDate;
    @JsonProperty(value = "Idvalidtodate")
    private String idValidToDate;
    @JsonProperty(value = "Country")
    private String country;
    @JsonProperty(value = "Countryiso")
    private String countryIso;
    @JsonProperty(value = "Region")
    private String region;

}
