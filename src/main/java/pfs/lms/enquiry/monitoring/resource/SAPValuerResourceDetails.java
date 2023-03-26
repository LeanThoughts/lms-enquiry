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
public class SAPValuerResourceDetails {


    @JsonProperty (value = "ValuerId")
    private String id;

    @JsonProperty (value = "MonitorId")
    private String monitorId;

    @JsonProperty (value = "AppraisalId")
    private String appraisalId;

    @JsonProperty (value = "SerialNo")
    private Integer serialNumber;

//    @JsonProperty (value = "Bpcode")
//    private String advisor;

    @JsonProperty (value = "Bpcode")
    private String bpCode;

    @JsonProperty (value = "Name")
    private String name;

    @JsonProperty (value = "Dateofappointment")
    private String dateOfAppointment;

    @JsonProperty (value = "Contractperiodfrom")
    private String contractPeriodFrom;

    @JsonProperty (value = "Contractperiodto")
    private String contractPeriodTo;

    @JsonProperty (value = "Contactperson")
    private String contactPerson;

    @JsonProperty (value = "Contactnumber")
    private String contactNumber;

    @JsonProperty (value = "Email")
    private String email;

    public SAPValuerResourceDetails() {
    }

    @Override
    public String toString() {
        return "SAPLIEResourceDetails{" +
                "id='" + id + '\'' +
                ", serialNumber=" + serialNumber +
                ", bpCode='" + bpCode + '\'' +
                ", name='" + name + '\'' +
                ", dateOfAppointment=" + dateOfAppointment +
                ", contractPeriodFrom=" + contractPeriodFrom +
                ", contractPeriodTo=" + contractPeriodTo +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
