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
public class SAPSiteVisitResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value ="AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "SerialNo")
    private Integer serialNo;

    @JsonProperty(value = "FiscalYear")
    private String fiscalYear;

    @JsonProperty(value = "Actualcod")
    private String actualcod;

    @JsonProperty(value = "Dateofsitevisit")
    private String dateofsitevisit;

    @JsonProperty(value = "Dateoflendersmeet")
    private String dateoflendersmeet;

}
