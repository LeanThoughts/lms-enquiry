package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.riskrating.TermLoanRiskRating;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalExternalRatingTermLoanResource implements Serializable {

    public SAPLoanAppraisalExternalRatingTermLoanResource() {
        sapLoanAppraisalExternalRatingTermLoanResourceDetails = new SAPLoanAppraisalExternalRatingTermLoanResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalExternalRatingTermLoanResourceDetails sapLoanAppraisalExternalRatingTermLoanResourceDetails;


    public void setSapLoanAppraisalCustomerRejectionResourceDetails(SAPLoanAppraisalExternalRatingTermLoanResourceDetails sapLoanAppraisalExternalRatingTermLoanResourceDetails) {
        this.sapLoanAppraisalExternalRatingTermLoanResourceDetails = sapLoanAppraisalExternalRatingTermLoanResourceDetails;
    }

    public SAPLoanAppraisalExternalRatingTermLoanResourceDetails
                                mapToSAP(TermLoanRiskRating termLoanRiskRating) throws ParseException {

        SAPLoanAppraisalExternalRatingTermLoanResourceDetails detailsResource = new SAPLoanAppraisalExternalRatingTermLoanResourceDetails();
        detailsResource.setId(termLoanRiskRating.getId().toString());
        detailsResource.setAppraisalId(termLoanRiskRating.getLoanAppraisal().getId().toString());
        detailsResource.setRatingYear(termLoanRiskRating.getYear());
        detailsResource.setFinRatioStochProb(termLoanRiskRating.getFinancialRatio());
        detailsResource.setApprovalRisk(termLoanRiskRating.getApprovalRisk());
        detailsResource.setOffTakeRelRisk(termLoanRiskRating.getOffTakeRisk());
        detailsResource.setFuelAnalogRisk(termLoanRiskRating.getFuelRisk());
        detailsResource.setSignIssLeadRepRisk(termLoanRiskRating.getReputationRisk());
        detailsResource.setFinancingStructure(termLoanRiskRating.getFinancingStructure());
        detailsResource.setSponsorSupport(termLoanRiskRating.getSponsorSupport());
        detailsResource.setSecurityPackage(termLoanRiskRating.getSecurityPackage());
        detailsResource.setConstrRelRisk(termLoanRiskRating.getConstructionRisk());
        detailsResource.setExposureConcentration(termLoanRiskRating.getExposure());
        detailsResource.setDesignTechRelRisk(termLoanRiskRating.getTechnologyRisk());
        detailsResource.setOverallRiskRating(termLoanRiskRating.getOverallRisk());

        return detailsResource;
    }



}
