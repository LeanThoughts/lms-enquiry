package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPTRAStatementResourceDetails {


    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "TraId")
    private String traId;

    @JsonProperty(value = "SerialNo")
    private Integer serialNo;

    @JsonProperty(value = "Viewrights")
    private String viewrights;
    @JsonProperty(value = "Remarks")
    private String remarks;
    @JsonProperty(value = "Traaccountnumber")
    private String traaccountnumber;

    @JsonProperty(value = "PeriodQuarter")
    private String periodQuarter;
    @JsonProperty(value = "PeriodYear")
    private String periodYear;
    @JsonProperty(value = "Documenttype")
    private String documenttype;
    @JsonProperty(value = "Documenttitle")
    private String documenttitle;
    @JsonProperty(value = "FileReference")
    private String fileReference;



}
