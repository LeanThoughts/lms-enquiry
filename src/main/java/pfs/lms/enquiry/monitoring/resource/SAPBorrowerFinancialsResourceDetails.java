package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPBorrowerFinancialsResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "SerialNo")
    private Integer serialNo;

    @JsonProperty(value = "Fiscalyear")
    private String fiscalyear;

    @JsonProperty(value = "Turnover")
    private String turnover;

    @JsonProperty(value = "Pat")
    private String pat;

    @JsonProperty(value = "Networth")
    private String networth;

    @JsonProperty(value = "Dateofexternalrating")
    private String dateofexternalrating;

    @JsonProperty(value = "Nextduedateofexternalrating")
    private String nextduedateofexternalrating;

    @JsonProperty(value = "Overallrating")
    private String overallrating;

    @JsonProperty(value = "Remarks")
    private String remarks;

    @JsonProperty(value = "Annualrptfilereference")
    private String annualrptfilereference;

    @JsonProperty(value = "Ratingfilereference")
    private String ratingfilereference;




    @Override
    public String toString() {
        return "SAPBorrowerFinancialsResourceDetails{" +
                "id='" + id + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", fiscalyear='" + fiscalyear + '\'' +
                ", turnover='" + turnover + '\'' +
                ", pat='" + pat + '\'' +
                ", networth='" + networth + '\'' +
                ", dateofexternalrating='" + dateofexternalrating + '\'' +
                ", nextduedateofexternalrating='" + nextduedateofexternalrating + '\'' +
                ", overallrating='" + overallrating + '\'' +
                '}';
    }
}
