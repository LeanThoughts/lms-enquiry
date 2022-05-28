package pfs.lms.enquiry.appraisal.resource;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.appraisal.reasonfordelay.ReasonForDelay;
import pfs.lms.enquiry.appraisal.syndicateconsortium.SyndicateConsortium;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)

public class SAPLoanAppraisalSyndicateConsortiumResource implements Serializable {

    DataConversionUtility dataConversionUtility = new DataConversionUtility();

    public SAPLoanAppraisalSyndicateConsortiumResource() {
        syndicateConsortiumResourceDetails = new SAPLoanAppraisalSyndicateConsortiumResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanAppraisalSyndicateConsortiumResourceDetails  syndicateConsortiumResourceDetails;


    public void setSAPLoanAppraisalSyndicateConsortiumResourceDetails(SAPLoanAppraisalSyndicateConsortiumResourceDetails syndicateConsortiumResourceDetails) {
        this.syndicateConsortiumResourceDetails = syndicateConsortiumResourceDetails;
    }

    public SAPLoanAppraisalSyndicateConsortiumResourceDetails
                                mapSyndicateConsortiumToSAP(SyndicateConsortium syndicateConsortium) throws ParseException {

        SAPLoanAppraisalSyndicateConsortiumResourceDetails detailsResource = new SAPLoanAppraisalSyndicateConsortiumResourceDetails();
        detailsResource.setId(syndicateConsortium.getId().toString());
        detailsResource.setAppraisalId(syndicateConsortium.getLoanAppraisal().getId().toString());
        if (detailsResource.getSerialnumber() != null)
            detailsResource.setSerialnumber(syndicateConsortium.getSerialNumber().toString());

        detailsResource.setBankkey(syndicateConsortium.getBankKey());
        detailsResource.setBankname(syndicateConsortium.getBankName());
        if (syndicateConsortium.getSanctionedAmount() != null)
            detailsResource.setSanctionedamount(syndicateConsortium.getSanctionedAmount().toString());
        else
            detailsResource.setSanctionedamount("0.00");

        if (syndicateConsortium.getDisbursedAmount() != null)
            detailsResource.setDisbursedamount(syndicateConsortium.getDisbursedAmount().toString());
        else
            detailsResource.setDisbursedamount("0.00");

        detailsResource.setCurrency(syndicateConsortium.getCurrency());
        detailsResource.setApprovalstatus(syndicateConsortium.getApprovalStatus());
        detailsResource.setDocumentstage(syndicateConsortium.getDocumentStage());
        detailsResource.setDisbursementstatus(syndicateConsortium.getDisbursementStatus());
        if (syndicateConsortium.isLeadBank())
            detailsResource.setLeadbank("X");
        else
            detailsResource.setLeadbank(" ");

        return detailsResource;
    }



}
