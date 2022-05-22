package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalLoanPartnerResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;
    @JsonProperty(value = "Roletype")
    private String roleType;
    @JsonProperty(value = "SerialNumber")
    private String serialNumber;
    @JsonProperty(value = "Businesspartnerid")
    private String businessPartnerId;
    @JsonProperty(value = "Businesspartnername")
    private String businessPartnerName;

    @JsonProperty(value = "Roledescription")
    private String roleDescription;
    @JsonProperty(value = "Kycstatus")
    private String kycStatus;
    @JsonProperty(value = "Kycrequired")
    private String kycRequired;
    @JsonProperty(value = "Startdate")
    private String startDate;
    @JsonProperty(value = "Enddate")
    private String endDate;




}
