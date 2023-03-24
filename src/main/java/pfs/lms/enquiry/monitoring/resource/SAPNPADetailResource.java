package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.npa.NPADetail;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPNPADetailResource implements Serializable   {


    public SAPNPADetailResource() {
        sapnpaResourceDetails = new SAPNPADetailResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPNPADetailResourceDetails sapnpaResourceDetails;

    public SAPNPADetailResourceDetails getSAPNPADetailResourceDetails() {
        return sapnpaResourceDetails;
    }



    public void setSAPNPADetailResourceDetails(SAPNPADetailResourceDetails sapnpaResourceDetails) {
        this.sapnpaResourceDetails = sapnpaResourceDetails;
    }

    public SAPNPADetailResourceDetails
                    mapToSAP(NPADetail npaDetail) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPNPADetailResourceDetails detailsResource= new SAPNPADetailResourceDetails();

        detailsResource.setId(npaDetail.getId().toString());

        detailsResource.setSerialNumber(npaDetail.getLineItemNumber().toString());

        detailsResource.setMonitorId(npaDetail.getNpa().getLoanMonitor().getId().toString());
        detailsResource.setLoanNumber(npaDetail.getLoanNumber());

        detailsResource.setAssetClassification(npaDetail.getNpaAssetClass());


        if (npaDetail.getProvisionDate() != null)
            detailsResource.setProvisionDate(dataConversionUtility.convertDateToSAPFormat(npaDetail.getProvisionDate()));
        else
            detailsResource.setProvisionDate(null);
        if(npaDetail.getAssetClassificationChangeDate() != null)
            detailsResource.setNpaDeclDate(dataConversionUtility.convertDateToSAPFormat(npaDetail.getAssetClassificationChangeDate()));
        else
            detailsResource.setNpaDeclDate(null);

        if (npaDetail.getLoanAssetValue() != null)
            detailsResource.setEffecCap(npaDetail.getLoanAssetValue().toString());

        if (npaDetail.getPercentageSecured() != null)
            detailsResource.setPercentageSec(npaDetail.getPercentageSecured().toString());

        if (npaDetail.getPercentageUnsecured() != null)
            detailsResource.setPercentageUns(npaDetail.getPercentageUnsecured().toString());

        if (npaDetail.getAbsoluteValue() != null)
            detailsResource.setAbsValue(npaDetail.getAbsoluteValue().toString());

        if (npaDetail.getNetAssetValue() != null)
            detailsResource.setNetAsset(npaDetail.getNetAssetValue().toString());

        if (npaDetail.getSecuredLoanAsset() != null)
            detailsResource.setSecLoanAsset(npaDetail.getSecuredLoanAsset().toString());

        if (npaDetail.getUnsecuredLoanAsset() != null)
            detailsResource.setNonSecLoanAsset(npaDetail.getUnsecuredLoanAsset().toString());

        if (npaDetail.getNpaProvisionValue() != null)
            detailsResource.setProvnAmt(npaDetail.getNpaProvisionValue().toString());

        if (npaDetail.getNetAssetValue() != null)
            detailsResource.setNetAsset(npaDetail.getNetAssetValue().toString());


        detailsResource.setRemarks(npaDetail.getRemarks() );


        return detailsResource;
    }
}
