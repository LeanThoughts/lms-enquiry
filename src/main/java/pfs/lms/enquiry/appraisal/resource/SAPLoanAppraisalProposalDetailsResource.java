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
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLoanAppraisalProposalDetailsResource implements Serializable   {



    public SAPLoanAppraisalProposalDetailsResource() {
        sapLoanAppraisalProposalDetailsResourceDetails = new SAPLoanAppraisalProposalDetailsResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalProposalDetailsResourceDetails sapLoanAppraisalProposalDetailsResourceDetails;



    public void setsapLoanAppraisalProposalDetailsResourceDetails (SAPLoanAppraisalProposalDetailsResourceDetails sapLoanAppraisalHeaderResourceDetails) {
        this.sapLoanAppraisalProposalDetailsResourceDetails = sapLoanAppraisalHeaderResourceDetails;
    }


    public SAPLoanAppraisalProposalDetailsResourceDetails
                    mapProposalToSAP(ProposalDetail proposalDetail ) throws ParseException {

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


        detailsResource.setConstructionperiod(proposalDetail.getConstructionPeriod().toString());
        detailsResource.setConstructionperiodunit(proposalDetail.getConstructionPeriodUnit());
        detailsResource.setMoratoriumperiod(proposalDetail.getMoratoriumPeriod().toString());
        detailsResource.setMoratoriumperiodunit(proposalDetail.getMoratoriumPeriodUnit());
        detailsResource.setRepaymentperiod(proposalDetail.getRepaymentPeriod().toString());
        detailsResource.setRepaymentperiodunit(proposalDetail.getRepaymentPeriodUnit());
        detailsResource.setTenor(proposalDetail.getTenor().toString());
        detailsResource.setTenorunit(proposalDetail.getTenorUnit());
        detailsResource.setAvailabilityperiod(proposalDetail.getAvailabilityPeriod().toString());
        detailsResource.setAvailabilityperiodunit(proposalDetail.getAvailabilityPeriodUnit());
        if (proposalDetail.getPrePaymentCharges() !=null)
            detailsResource.setPrepaymentcharges(proposalDetail.getPrePaymentCharges().toString());
        if (proposalDetail.getFeeDetailsSchedule() != null)
        detailsResource.setFeedetailsschedule(proposalDetail.getFeeDetailsSchedule().toString());

        return detailsResource;
    }
}
