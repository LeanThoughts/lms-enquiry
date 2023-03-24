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
public class SAPNPADetailResourceDetails {


    @JsonProperty (value = "Id")
    private String id;
    @JsonProperty (value = "MonitorId")
    private String  monitorId;

    @JsonProperty (value = "LoanNumber")
    private String  loanNumber;
    @JsonProperty (value = "SerialNumber")
    private String serialNumber;

    @JsonProperty (value = "NpaDeclDate")
    private String  npaDeclDate;
    @JsonProperty (value = "AssetClassification")
    private String  assetClassification;

    @JsonProperty (value = "ProvisionDate")
    private String  provisionDate;

    @JsonProperty (value = "PercentageSec")
    private String  percentageSec;
    @JsonProperty (value = "PercentageUns")
    private String  percentageUns;


    @JsonProperty (value = "AbsValue")
    private String  absValue;

    @JsonProperty (value = "EffecCap")
    private String  effecCap;
    @JsonProperty (value = "SecLoanAsset")
    private String  secLoanAsset;
    @JsonProperty (value = "NonSecLoanAsset")
    private String  nonSecLoanAsset;

    @JsonProperty (value = "ProvnAmt")
    private String  provnAmt;

    @JsonProperty (value = "NetAsset")
    private String  netAsset;

    @JsonProperty (value = "Remarks")
    private String  remarks;

    @Override
    public String toString() {
        return "SAPNPADetailResourceDetails{" +
                "id='" + id + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", loanNumber='" + loanNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", npaDeclDate='" + npaDeclDate + '\'' +
                ", assetClassification='" + assetClassification + '\'' +
                ", provisionDate='" + provisionDate + '\'' +
                ", percentageSec='" + percentageSec + '\'' +
                ", percentageUns='" + percentageUns + '\'' +
                ", absValue='" + absValue + '\'' +
                ", effecCap='" + effecCap + '\'' +
                ", secLoanAsset='" + secLoanAsset + '\'' +
                ", nonSecLoanAsset='" + nonSecLoanAsset + '\'' +
                ", provnAmt='" + provnAmt + '\'' +
                ", netAsset='" + netAsset + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
