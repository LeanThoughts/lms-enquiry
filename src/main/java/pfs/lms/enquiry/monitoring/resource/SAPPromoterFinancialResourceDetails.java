package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPPromoterFinancialResourceDetails {

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


}
