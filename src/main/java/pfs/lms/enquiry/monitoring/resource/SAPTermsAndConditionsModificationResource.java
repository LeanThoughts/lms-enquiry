package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.TermsAndConditionsModification;
import pfs.lms.enquiry.monitoring.borrowerfinancials.BorrowerFinancials;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by sajeev on 28-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
 public class SAPTermsAndConditionsModificationResource implements Serializable {

    @JsonProperty(value = "d")
    private SAPTermsAndConditionsModificationDetails sapTermsAndConditionsModificationDetails;

    public SAPTermsAndConditionsModificationResource() {
        sapTermsAndConditionsModificationDetails = new SAPTermsAndConditionsModificationDetails();
    }

    public void setSAPTermsAndConditionsModificationDetails(SAPTermsAndConditionsModificationDetails sapTermsAndConditionsModificationDetails) {
        this.sapTermsAndConditionsModificationDetails = sapTermsAndConditionsModificationDetails;
    }



    public SAPTermsAndConditionsModificationDetails mapToSAP(TermsAndConditionsModification termsAndConditionsModification) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPTermsAndConditionsModificationDetails detailedResource = new SAPTermsAndConditionsModificationDetails();

        detailedResource.setId(termsAndConditionsModification.getId());
        detailedResource.setMonitorId(termsAndConditionsModification.getLoanMonitor().getId().toString());
        detailedResource.setSerialNo(termsAndConditionsModification.getSerialNumber());

        if (termsAndConditionsModification.getBorrowerRequestLetterDate() != null)
             detailedResource.setBorrowerrequestletterdate(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getBorrowerRequestLetterDate()));
        else
            detailedResource.setBorrowerrequestletterdate(null);

        if(termsAndConditionsModification.getDateofIssueofAmendedSanctionLetter() != null) {
            detailedResource.setDateofissueofamendedsanctionle(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getDateofIssueofAmendedSanctionLetter()));
        } else
            detailedResource.setDateofissueofamendedsanctionle(null);

        detailedResource.setDocumenttype(termsAndConditionsModification.getDocumentType());
        detailedResource.setDocumentTitle(termsAndConditionsModification.getDocumentTitle());
        detailedResource.setCommunication(termsAndConditionsModification.getCommunication());
        detailedResource.setRemarks(termsAndConditionsModification.getRemarks());
 
        

        return detailedResource;


    }

}
