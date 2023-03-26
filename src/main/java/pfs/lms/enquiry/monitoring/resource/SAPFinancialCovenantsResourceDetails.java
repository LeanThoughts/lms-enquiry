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
public class SAPFinancialCovenantsResourceDetails {



    @JsonProperty(value = "FinCovId")
    private String finCovId;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "SerialNo")
    private Integer serialNo;

    @JsonProperty(value = "Financialcovenanttype")
    private String financialcovenanttype;

    @JsonProperty(value = "Financialyear")
    private String financialyear;

    @JsonProperty(value = "Debtequityratio")
    private String debtequityratio;

    @JsonProperty(value = "Dscr")
    private String dscr;

    @JsonProperty(value = "Toltnw")
    private String toltnw;

    @JsonProperty(value = "Remarksfordeviation")
    private String remarksfordeviation;

    @JsonProperty(value = "FileReference")
    private String fileReference;


}
