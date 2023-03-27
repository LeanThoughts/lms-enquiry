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

    @JsonProperty(value = "DocumentType")
    private String  documentType;
    @JsonProperty(value = "DocumentTitle")
    private String  documentTitle;
    @JsonProperty(value = "InitialSCOD")
    private String  initialSCOD;
    @JsonProperty(value = "RevisedSCOD1")
    private String  revisedSCOD1;
    @JsonProperty(value = "RevisedSCOD2")
    private String  revisedSCOD2;
    @JsonProperty(value = "Businesspartnerid")
    private String  businesspartnerid;
    @JsonProperty(value = "Businesspartnername")
    private String  businesspartnername;


    @JsonProperty(value = "FileReference")
    private String fileReference;


}
