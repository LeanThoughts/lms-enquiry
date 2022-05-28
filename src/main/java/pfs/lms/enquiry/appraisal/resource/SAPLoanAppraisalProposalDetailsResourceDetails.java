package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SAPLoanAppraisalProposalDetailsResourceDetails {
    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;
    @JsonProperty(value = "Rateofinterestprecod")
    private String  rateofinterestprecod;
    @JsonProperty(value = "Rateofinterestpostcod")
    private String  rateofinterestpostcod;
    @JsonProperty(value = "Spreadreset")
    private String  spreadreset;
    @JsonProperty(value = "Spreadresetunit")
    private String  spreadresetunit;
    @JsonProperty(value = "Effectiverateofinterest")
    private String  effectiverateofinterest;
    @JsonProperty(value = "Constructionperiod")
    private String  constructionperiod;
    @JsonProperty(value = "Constructionperiodunit")
    private String  constructionperiodunit;
    @JsonProperty(value = "Moratoriumperiod")
    private String  moratoriumperiod;
    @JsonProperty(value = "Moratoriumperiodunit")
    private String  moratoriumperiodunit;
    @JsonProperty(value = "Repaymentperiod")
    private String  repaymentperiod;
    @JsonProperty(value = "Repaymentperiodunit")
    private String  repaymentperiodunit;
    @JsonProperty(value = "Tenor")
    private String  tenor;
    @JsonProperty(value = "Tenorunit")
    private String  tenorunit;
    @JsonProperty(value = "Availabilityperiod")
    private String  availabilityperiod;
    @JsonProperty(value = "Availabilityperiodunit")
    private String  availabilityperiodunit;
    @JsonProperty(value = "Prepaymentcharges")
    private String  prepaymentcharges;
    @JsonProperty(value = "Feedetailsschedule")
    private String  feedetailsschedule;



}
