package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gguptha on 09/11/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPNPAResourceDetails {


    @JsonProperty (value = "Id")
    private String id;

    @JsonProperty (value = "MonitorId")
    private String  monitorId;
    @JsonProperty (value = "NpaDeclDate")
    private String  npaDeclDate;
    @JsonProperty (value = "AssetClassification")
    private String  assetClassification;
    @JsonProperty (value = "TotalLoanAsset")
    private String  totalLoanAsset;
    @JsonProperty (value = "SecLoanAsset")
    private String  secLoanAsset;
    @JsonProperty (value = "NonSecLoanAsset")
    private String  nonSecLoanAsset;
    @JsonProperty (value = "RestructringType")
    private String  restructringType;
    @JsonProperty (value = "SmaCategory")
    private String  smaCategory;

    @JsonProperty (value = "FraudDate")
    private String  fraudDate;
    @JsonProperty (value = "ImpairmentReserve")
    private String  impairmentReserve;
    @JsonProperty (value = "ProvisionAmount")
    private String  provisionAmount;

}
