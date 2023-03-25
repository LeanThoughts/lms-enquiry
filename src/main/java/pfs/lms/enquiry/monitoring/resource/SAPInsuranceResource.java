package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.insurance.Insurance;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPInsuranceResource implements Serializable   {


    public SAPInsuranceResource() {
        sapInsuranceResourceDetails = new SAPInsuranceResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPInsuranceResourceDetails sapInsuranceResourceDetails;

    public SAPInsuranceResourceDetails getSAPNPAResourceDetails() {
        return sapInsuranceResourceDetails;
    }



    public void setSAPNPAResourceDetails(SAPInsuranceResourceDetails sapInsuranceResourceDetails) {
        this.sapInsuranceResourceDetails = sapInsuranceResourceDetails;
    }

    public SAPInsuranceResourceDetails
                    mapToSAP(Insurance insurance ) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

        SAPInsuranceResourceDetails detailsResource= new SAPInsuranceResourceDetails();

        detailsResource.setId(insurance.getId().toString());
        
        detailsResource.setMonitorId(insurance.getLoanMonitor().getId().toString());

        detailsResource.setSerialnumber(insurance.getSerialNumber().toString());

        if (insurance.getValidFrom() != null)
            detailsResource.setValidFrom(dataConversionUtility.convertDateToSAPFormat(insurance.getValidFrom()));
        else
            detailsResource.setValidFrom(null);

        if (insurance.getValidTo() != null)
            detailsResource.setValidTo(dataConversionUtility.convertDateToSAPFormat(insurance.getValidTo()));
        else
            detailsResource.setValidTo(null);

        detailsResource.setDocumentType(insurance.getDocumentType() );
        detailsResource.setDocumentTitle(insurance.getDocumentTitle() );
        detailsResource.setFileReference(insurance.getFileReference() );
        //detailsResource.setRemarks(insurance.getRemarks() );

        return detailsResource;
    }
}
