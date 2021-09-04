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
public class SAPTermsAndConditionsModificationDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "SerialNo")
    private Integer serialNo;

    @JsonProperty(value = "Documenttype")
    private String documenttype;

    @JsonProperty(value = "DocumentTitle")
    private String documentTitle;

    @JsonProperty(value = "Communication")
    private String communication;

    @JsonProperty(value = "Borrowerrequestletterdate")
    private String borrowerrequestletterdate;

    @JsonProperty(value = "Dateofissueofamendedsanctionle")
    private String dateofissueofamendedsanctionle;

    @JsonProperty(value = "Remarks")
    private String remarks;

    @JsonProperty(value = "AmendDocumentTitle")
    private String amendDocumentTitle;
    @JsonProperty(value = "AmendDocumentType")
    private String amendDocumentType;
    @JsonProperty(value = "AmendDocumentDate")
    private String amendDocumentDate;
    @JsonProperty(value = "AmendedDocumentRemarks")
    private String amendedDocumentRemarks;


}
