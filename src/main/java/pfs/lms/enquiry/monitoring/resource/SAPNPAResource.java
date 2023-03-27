package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.llc.LendersLegalCouncil;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPNPAResource implements Serializable   {


    public SAPNPAResource() {
        sapnpaResourceDetails = new SAPNPAResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPNPAResourceDetails sapnpaResourceDetails;

    public SAPNPAResourceDetails getSAPNPAResourceDetails() {
        return sapnpaResourceDetails;
    }



    public void setSAPNPAResourceDetails(SAPNPAResourceDetails sapnpaResourceDetails) {
        this.sapnpaResourceDetails = sapnpaResourceDetails;
    }

    public SAPNPAResourceDetails
                    mapToSAP(NPA npa ) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPNPAResourceDetails detailsResource= new SAPNPAResourceDetails();

        detailsResource.setId(npa.getId().toString());
        
        detailsResource.setMonitorId(npa.getLoanMonitor().getId().toString());
        detailsResource.setAssetClassification(npa.getAssetClass());
        if (npa.getNpaDeclarationDate() != null)
            detailsResource.setNpaDeclDate(dataConversionUtility.convertDateToSAPFormat(npa.getNpaDeclarationDate()));
        else
            detailsResource.setNpaDeclDate(null);

        if (npa.getTotalLoanAsset() != null)
            detailsResource.setTotalLoanAsset(npa.getTotalLoanAsset().toString());
        if (npa.getSecuredLoanAsset() != null)
            detailsResource.setSecLoanAsset(npa.getSecuredLoanAsset().toString());
        if (npa.getUnSecuredLoanAsset() != null)
            detailsResource.setNonSecLoanAsset(npa.getUnSecuredLoanAsset().toString());

        if (npa.getFraudDate() != null)
            detailsResource.setFraudDate(dataConversionUtility.convertDateToSAPFormat(npa.getFraudDate()));
        else
            detailsResource.setFraudDate(null);
        if (npa.getImpairmentReserve() != null)
            detailsResource.setImpairmentReserve(npa.getImpairmentReserve().toString());
        if (npa.getProvisionAmount() != null)
            detailsResource.setProvisionAmount(npa.getProvisionAmount().toString());

        detailsResource.setRestructringType(npa.getRestructuringType() );
        detailsResource.setSmaCategory(npa.getSmaCategory() );


        return detailsResource;
    }
}
