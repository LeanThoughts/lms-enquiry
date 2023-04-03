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
public class SAPEndUseCertificateResourceDetails {


    @JsonProperty (value = "EndUseCertId")
    private String id;

    @JsonProperty (value = "MonitorId")
    private String monitorId;


    @JsonProperty (value = "SerialNo")
    private String serialNumber;


    @JsonProperty (value = "Endusecertificatedate")
    private String endusecertificatedate;

    @JsonProperty (value = "Eventdate")
    private String eventdate;

    @JsonProperty (value = "Endusecertificateduedate")
    private String endusecertificateduedate;

    @JsonProperty (value = "Documenttype")
    private String documenttype;

    @JsonProperty (value = "Documenttitle")
    private String documentTitle;

    @JsonProperty (value = "Filereference")
    private String filereference;

    @JsonProperty (value = "Remarks")
    private String remarks;




    public SAPEndUseCertificateResourceDetails() {
    }


}
