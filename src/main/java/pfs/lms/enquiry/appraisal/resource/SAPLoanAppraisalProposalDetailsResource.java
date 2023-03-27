package pfs.lms.enquiry.appraisal.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.proposaldetails.ProposalDetail;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPLoanAppraisalProposalDetailsResource implements Serializable {


    public SAPLoanAppraisalProposalDetailsResource() {
        sapLoanAppraisalProposalDetailsResourceDetails = new SAPLoanAppraisalProposalDetailsResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalProposalDetailsResourceDetails sapLoanAppraisalProposalDetailsResourceDetails;


    public void setsapLoanAppraisalProposalDetailsResourceDetails(SAPLoanAppraisalProposalDetailsResourceDetails sapLoanAppraisalHeaderResourceDetails) {
        this.sapLoanAppraisalProposalDetailsResourceDetails = sapLoanAppraisalHeaderResourceDetails;
    }


    public SAPLoanAppraisalProposalDetailsResourceDetails
    mapProposalToSAP(ProposalDetail proposalDetail) throws ParseException {

        SAPLoanAppraisalProposalDetailsResourceDetails detailsResource = new SAPLoanAppraisalProposalDetailsResourceDetails();

        detailsResource.setId(proposalDetail.getId().toString());
        detailsResource.setAppraisalId(proposalDetail.getLoanAppraisal().getId().toString());

        if (proposalDetail.getRateOfInterestPostCod() != null)
            detailsResource.setRateofinterestpostcod(proposalDetail.getRateOfInterestPostCod().toString());
        else
            detailsResource.setRateofinterestpostcod("0.00");

        if (proposalDetail.getRateOfInterestPreCod() != null)
            detailsResource.setRateofinterestprecod(proposalDetail.getRateOfInterestPreCod().toString());

        if (proposalDetail.getSpreadReset() != null)
            detailsResource.setSpreadreset(proposalDetail.getSpreadReset().toString());
        else
            detailsResource.setSpreadreset("");

        detailsResource.setSpreadresetunit(proposalDetail.getSpreadResetUnit());
        if (proposalDetail.getEffectiveRateOfInterest() != null)
            detailsResource.setEffectiverateofinterest(proposalDetail.getEffectiveRateOfInterest().toString());

        if (proposalDetail.getConstructionPeriod() != null)
            detailsResource.setConstructionperiod(proposalDetail.getConstructionPeriod().toString());
        else
            detailsResource.setConstructionperiod("");

        if (proposalDetail.getConstructionPeriodUnit() != null) {
            detailsResource.setConstructionperiodunit(proposalDetail.getConstructionPeriodUnit());
        } else
            detailsResource.setConstructionperiodunit("");

        if (proposalDetail.getMoratoriumPeriod() != null)
            detailsResource.setMoratoriumperiod(proposalDetail.getMoratoriumPeriod().toString());
        else
            detailsResource.setMoratoriumperiod("");

        if (proposalDetail.getMoratoriumPeriodUnit() != null)
            detailsResource.setMoratoriumperiodunit(proposalDetail.getMoratoriumPeriodUnit());
        else
            detailsResource.setMoratoriumperiodunit("");
        if (proposalDetail.getRepaymentPeriod() != null)
            detailsResource.setRepaymentperiod(proposalDetail.getRepaymentPeriod().toString());
        else
            detailsResource.setRepaymentperiod("");

        if (proposalDetail.getRepaymentPeriod() != null)
            detailsResource.setRepaymentperiodunit(proposalDetail.getRepaymentPeriodUnit());
        else
            detailsResource.setRepaymentperiodunit("");

        if (proposalDetail.getTenor() != null)
            detailsResource.setTenor(proposalDetail.getTenor().toString());
        else
            detailsResource.setTenor("");

        if (proposalDetail.getTenorUnit() != null)
            detailsResource.setTenorunit(proposalDetail.getTenorUnit());
        else
            detailsResource.setTenorunit("");

        if (proposalDetail.getAvailabilityPeriod() != null)
            detailsResource.setAvailabilityperiod(proposalDetail.getAvailabilityPeriod().toString());
        else
            detailsResource.setAvailabilityperiod("");

        if (proposalDetail.getAvailabilityPeriodUnit() != null)
            detailsResource.setAvailabilityperiodunit(proposalDetail.getAvailabilityPeriodUnit());
        else
            detailsResource.setAvailabilityperiodunit("");

        if (proposalDetail.getPrePaymentCharges() != null)
            detailsResource.setPrepaymentcharges(proposalDetail.getPrePaymentCharges().toString());
        else
            detailsResource.setPrepaymentcharges("");

        if (proposalDetail.getFeeDetailsSchedule() != null)
            detailsResource.setFeedetailsschedule(proposalDetail.getFeeDetailsSchedule().toString());
        else
            detailsResource.setFeedetailsschedule("");


        return detailsResource;
    }
}
