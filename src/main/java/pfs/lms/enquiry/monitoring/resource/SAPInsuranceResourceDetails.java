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
public class SAPInsuranceResourceDetails {

    @JsonProperty (value = "Id")
    private String id;
    @JsonProperty (value = "MonitorId")
    private String  monitorId;
    @JsonProperty (value = "Serialnumber")
    private String  serialnumber;

    @JsonProperty (value = "Validfrom")
    private String  validFrom;
    @JsonProperty (value = "Validto")
    private String  validTo;

    @JsonProperty (value = "Documenttype")
    private String  documentType;

    @JsonProperty (value = "Documenttitle")
    private String  documentTitle;
    @JsonProperty (value = "Filereference")
    private String  fileReference;

    @JsonProperty (value = "Remarks")
    private String  remarks;

    @Override
    public String toString() {
        return "SAPInsuranceResourceDetails{" +
                "id='" + id + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentTitle='" + documentTitle + '\'' +
                ", fileReference='" + fileReference + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
