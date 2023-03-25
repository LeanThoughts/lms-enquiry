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
public class SAPLoanDocumentationResourceDetails {

    @JsonProperty (value = "Id")
    private String id;
    @JsonProperty (value = "MonitorId")
    private String  monitorId;
    @JsonProperty (value = "Serialnumber")
    private String  serialnumber;
    @JsonProperty (value = "Documentationtypecode")
    private String  documentationTypeCode;
    @JsonProperty (value = "Documentationtypedescription")
    private String  documentationTypeDescription;
    @JsonProperty (value = "Executiondate")
    private String  executionDate;
    @JsonProperty (value = "Approvaldate")
    private String  approvalDate;
    @JsonProperty (value = "Loandocumentationstatuscode")
    private String  documentationStatusCode;
    @JsonProperty (value = "Loandocumentationstatuscodedes")
    private String  documentationStatusCodeDescription;
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
        return "SAPLoanDocumentationResourceDetails{" +
                "id='" + id + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", documentationTypeCode='" + documentationTypeCode + '\'' +
                ", documentationTypeDescription='" + documentationTypeDescription + '\'' +
                ", executionDate='" + executionDate + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", documentationStatusCode='" + documentationStatusCode + '\'' +
                ", documentationStatusCodeDescription='" + documentationStatusCodeDescription + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentTitle='" + documentTitle + '\'' +
                ", fileReference='" + fileReference + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
