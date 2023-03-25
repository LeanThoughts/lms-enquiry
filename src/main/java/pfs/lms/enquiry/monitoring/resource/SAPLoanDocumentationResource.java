package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentation;
import pfs.lms.enquiry.monitoring.npa.NPA;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPLoanDocumentationResource implements Serializable   {


    public SAPLoanDocumentationResource() {
        sapLoanDocumentationResourceDetails = new SAPLoanDocumentationResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPLoanDocumentationResourceDetails sapLoanDocumentationResourceDetails;

    public SAPLoanDocumentationResourceDetails getSAPNPAResourceDetails() {
        return sapLoanDocumentationResourceDetails;
    }



    public void setSAPNPAResourceDetails(SAPLoanDocumentationResourceDetails sapLoanDocumentationResourceDetails) {
        this.sapLoanDocumentationResourceDetails = sapLoanDocumentationResourceDetails;
    }

    public SAPLoanDocumentationResourceDetails
                    mapToSAP(LoanDocumentation loanDocumentation ) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

        SAPLoanDocumentationResourceDetails detailsResource= new SAPLoanDocumentationResourceDetails();

        detailsResource.setId(loanDocumentation.getId().toString());
        
        detailsResource.setMonitorId(loanDocumentation.getLoanMonitor().getId().toString());

        detailsResource.setSerialnumber(loanDocumentation.getSerialNumber().toString());

        if (loanDocumentation.getExecutionDate() != null)
            detailsResource.setExecutionDate(dataConversionUtility.convertDateToSAPFormat(loanDocumentation.getExecutionDate()));
        else
            detailsResource.setExecutionDate(null);
        if (loanDocumentation.getApprovalDate() != null)
            detailsResource.setApprovalDate(dataConversionUtility.convertDateToSAPFormat(loanDocumentation.getApprovalDate()));
        else
            detailsResource.setApprovalDate(null);


        detailsResource.setDocumentationTypeCode(loanDocumentation.getDocumentationTypeCode() );
        detailsResource.setDocumentationTypeDescription(loanDocumentation.getDocumentationTypeDescription() );
        detailsResource.setDocumentationStatusCodeDescription(loanDocumentation.getLoanDocumentationStatusCodeDescription() );
        detailsResource.setDocumentationStatusCode(loanDocumentation.getLoanDocumentationStatusCode() );
        detailsResource.setDocumentType(loanDocumentation.getDocumentType() );
        detailsResource.setDocumentTitle(loanDocumentation.getDocumentTitle() );
        detailsResource.setFileReference(loanDocumentation.getFileReference() );
        detailsResource.setRemarks(loanDocumentation.getRemarks() );

        return detailsResource;
    }
}
