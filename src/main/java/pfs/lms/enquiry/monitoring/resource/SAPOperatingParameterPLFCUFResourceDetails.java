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
public class SAPOperatingParameterPLFCUFResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "ItemNo")
    private String serialNo;

    @JsonProperty(value = "Period")
    private String period;

    @JsonProperty(value = "MusGenDuringYear")
    private String musGenDuringYear;

    @JsonProperty(value = "DesignPlfCuf")
    private String designPlfCuf;

    @JsonProperty(value = "ActualPlfCuf")
    private String actualPlfCuf;

    @JsonProperty(value = "Remark")
    private String remark;


}
