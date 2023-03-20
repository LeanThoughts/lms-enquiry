package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class SAPPromoterDetailsItemsResourceDetails {

    @JsonProperty(value = "Id")
    private String id;
    @JsonProperty(value = "PromDtlId")
    private String promDtlId;
    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "Serialno")
    private Integer serialNo;

    @JsonProperty(value = "Shareholdingcompany")
    private String shareholdingcompany;

    @JsonProperty(value = "Sanctionedpaidupce")
    private Double sanctionedpaidupce;

    @JsonProperty(value = "Currentpaidupce")
    private Double currentpaidupce;

    @JsonProperty(value = "Sanctionedequitylinkinstrument")
    private Double sanctionedequitylinkinstrument;

    @JsonProperty(value = "Currentequitylinkinstrument")
    private Double currentequitylinkinstrument;

    @JsonProperty(value = "BupaId")
    private String bupaId;

    @JsonProperty(value = "Dateofchange")
    private String dateofchange;

    @JsonProperty(value = "Groupexposure")
    private Double groupexposure;


}
