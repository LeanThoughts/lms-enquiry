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

public class SAPLoanAppraisalProjectDataResourceDetails {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "AppraisalId")
    private String appraisalId;

    @JsonProperty(value = "Projectname")
    private String  projectname;

    @JsonProperty(value = "Typeoffunding")
    private String  typeoffunding;

    @JsonProperty(value = "Policyapplicable")
    private String  policyapplicable;
    @JsonProperty(value = "Technology")
    private String technology ;
    @JsonProperty(value = "Projectcapacityunitsize")
    private String  projectcapacityunitsize;
    @JsonProperty(value = "Projectcapacityunitmeasure")
    private String  projectcapacityunitmeasure;
    @JsonProperty(value = "Numberofunits")
    private String  numberofunits;
    @JsonProperty(value = "Designplfcuf")
    private String  designplfcuf;
    @JsonProperty(value = "Maincontractor")
    private String  maincontractor;
    @JsonProperty(value = "Epccontractor")
    private String  epccontractor;
    @JsonProperty(value = "Resourceassessmentagency")
    private String  resourceassessmentagency;

    @JsonProperty(value = "Oandmcontractor")
    private String  oandmcontractor;
    @JsonProperty(value = "Offtakevolume")
    private String  offtakevolume;
    @JsonProperty(value = "Salerate")
    private String  salerate;
    @JsonProperty(value = "Fuelmix")
    private String  fuelmix;
    @JsonProperty(value = "Fuelcost")
    private String  fuelcost;
    @JsonProperty(value = "Stationheatrate")
    private String  stationheatrate;
    @JsonProperty(value = "Oandmexpenses")
    private String  oandmexpenses;
    @JsonProperty(value = "Totalland")
    private String  totalland;
    @JsonProperty(value = "Projectcod")
    private String  projectcod;
    @JsonProperty(value = "Pparate")
    private String  pparate;
    @JsonProperty(value = "Offtakercompany")
    private String  offtakercompany;
    @JsonProperty(value = "Ipppercentage")
    private String  ipppercentage;
    @JsonProperty(value = "Groupcaptivepercentage")
    private String  groupcaptivepercentage;
    @JsonProperty(value = "Thirdpartypercentage")
    private String  thirdpartypercentage;
    @JsonProperty(value = "Marketpercentage")
    private String  marketpercentage;
    @JsonProperty(value = "Epccost")
    private String  epccost;
    @JsonProperty(value = "Overallprojectcost")
    private String  overallprojectcost;

    @JsonProperty(value = "Debtequityratio")
    private String  debtequityratio;
    @JsonProperty(value = "Totaldebt")
    private String  totaldebt;
    @JsonProperty(value = "Roiprecod")
    private String  roiprecod;
    @JsonProperty(value = "Roipostcod")
    private String roipostcod;
    @JsonProperty(value = "Constructionperiod")
    private String  constructionperiod;
    @JsonProperty(value = "Constructionperiodunit")
    private String  constructionperiodunit;
    @JsonProperty(value = "Moratoriumperiod")
    private String  moratoriumperiod;
    @JsonProperty(value = "Moratoriumperiodunit")
    private String  moratoriumperiodunit;
    @JsonProperty(value = "Tenorperiod")
    private String  tenorperiod;
    @JsonProperty(value = "Tenorunit")
    private String  tenorunit;
    @JsonProperty(value = "Repaymentschedule")
    private String  repaymentschedule;
    @JsonProperty(value = "Dscrminimum")
    private String  dscrminimum;
    @JsonProperty(value = "Dscraverage")
    private String  dscraverage;
    @JsonProperty(value = "Levcosttotal")
    private String  levcosttotal;
    @JsonProperty(value = "Levcostfixed")
    private String levcostfixed ;
    @JsonProperty(value = "Levcostvariable")
    private String  levcostvariable;
    @JsonProperty(value = "Workingcapitalcycle")
    private String  workingcapitalcycle;
    @JsonProperty(value = "Workingcapitalunit")
    private String  workingcapitalunit;

}
