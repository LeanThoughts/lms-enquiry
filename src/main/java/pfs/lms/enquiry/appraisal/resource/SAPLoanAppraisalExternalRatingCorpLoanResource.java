package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.customerrejection.CustomerRejection;
import pfs.lms.enquiry.appraisal.riskrating.CorporateLoanRiskRating;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalExternalRatingCorpLoanResource implements Serializable {

    public SAPLoanAppraisalExternalRatingCorpLoanResource() {
        sapLoanAppraisalExternalRatingCorpLoanResourceDetails = new SAPLoanAppraisalExternalRatingCorpLoanResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalExternalRatingCorpLoanResourceDetails sapLoanAppraisalExternalRatingCorpLoanResourceDetails;


    public void setSapLoanAppraisalCustomerRejectionResourceDetails( SAPLoanAppraisalExternalRatingCorpLoanResourceDetails sapLoanAppraisalExternalRatingCorpLoanResourceDetails  ) {
        this.sapLoanAppraisalExternalRatingCorpLoanResourceDetails = sapLoanAppraisalExternalRatingCorpLoanResourceDetails;
    }

    public SAPLoanAppraisalExternalRatingCorpLoanResourceDetails
                                mapToSAP(CorporateLoanRiskRating corporateLoanRiskRating) throws ParseException {


        SAPLoanAppraisalExternalRatingCorpLoanResourceDetails detailsResource   = new SAPLoanAppraisalExternalRatingCorpLoanResourceDetails();
        detailsResource.setId(corporateLoanRiskRating.getId().toString());
        detailsResource.setAppraisalId(corporateLoanRiskRating.getLoanAppraisal().getId().toString());
        detailsResource.setRatingYear(corporateLoanRiskRating.getYear());
        detailsResource.setFinRatioAnalysis(corporateLoanRiskRating.getFinancialRatio());
        detailsResource.setPurposeOfLoan(corporateLoanRiskRating.getPurposeOfLoan());
        detailsResource.setFinStrucAndSecPackage(corporateLoanRiskRating.getFinancingStructure());
        detailsResource.setRepCapabBorrowerPromoter(corporateLoanRiskRating.getRepaymentCapability());
        detailsResource.setCorGoverToRepRisk(corporateLoanRiskRating.getCorporateGovernancePractice());
        detailsResource.setConOfLoBanksAndFis(corporateLoanRiskRating.getConductOfLoan());
        detailsResource.setDevOpPolicyGuide(corporateLoanRiskRating.getDeviationWithOperationalPolicy());
        detailsResource.setExposureConcentration(corporateLoanRiskRating.getExposure());

        return detailsResource;
    }



}
