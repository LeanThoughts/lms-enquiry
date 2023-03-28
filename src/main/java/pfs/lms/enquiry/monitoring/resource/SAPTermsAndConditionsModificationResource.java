package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.domain.TermsAndConditionsModification;
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
    private SAPTermsAndConditionsModificationResourceDetails sapTermsAndConditionsModificationResourceDetails;

    public SAPTermsAndConditionsModificationResource() {
        sapTermsAndConditionsModificationResourceDetails = new SAPTermsAndConditionsModificationResourceDetails();
    }

    public void setSAPTermsAndConditionsModificationDetails(SAPTermsAndConditionsModificationResourceDetails sapTermsAndConditionsModificationResourceDetails) {
        this.sapTermsAndConditionsModificationResourceDetails = sapTermsAndConditionsModificationResourceDetails;
    }



    public SAPTermsAndConditionsModificationResourceDetails mapToSAP(TermsAndConditionsModification termsAndConditionsModification) throws ParseException {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPTermsAndConditionsModificationResourceDetails detailedResource = new SAPTermsAndConditionsModificationResourceDetails();

        detailedResource.setId(termsAndConditionsModification.getId());
        detailedResource.setMonitorId(termsAndConditionsModification.getLoanMonitor().getId().toString());
        detailedResource.setSerialNumber(termsAndConditionsModification.getSerialNumber());

        if (termsAndConditionsModification.getBorrowerRequestLetterDate() != null)
             detailedResource.setBorrowerRequestLetterDate(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getBorrowerRequestLetterDate()));
        else
            detailedResource.setBorrowerRequestLetterDate(null);

        if(termsAndConditionsModification.getDateOfIssueOfAmendedSanctionLetter() != null) {
            detailedResource.setDateOfIssueOfAmendedSanctionLetter(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getDateOfIssueOfAmendedSanctionLetter()));
        } else
            detailedResource.setDateOfIssueOfAmendedSanctionLetter(null);

        detailedResource.setDocumentType(termsAndConditionsModification.getDocumentType());
        detailedResource.setDocumentTitle(termsAndConditionsModification.getDocumentTitle());
        detailedResource.setCommunication(termsAndConditionsModification.getCommunication());
        detailedResource.setRemarks(termsAndConditionsModification.getRemarks());
        detailedResource.setReasonForAmend(termsAndConditionsModification.getBrlReasonsForAmendment());
        detailedResource.setFileReference(termsAndConditionsModification.getFileReference());

        //Amended Doc
        detailedResource.setAmendedDocumentType(termsAndConditionsModification.getAmendedDocumentType().toString());
        detailedResource.setAmendedDocumentTitle(termsAndConditionsModification.getAmendedDocumentTitle());
        detailedResource.setAmendedDocumentRemarks(termsAndConditionsModification.getAmendedDocumentRemarks());
        if(termsAndConditionsModification.getDateOfIssueOfAmendedDocument() != null) {
            detailedResource.setDateOfIssueOfAmendedDocument(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getDateOfIssueOfAmendedDocument()));
        } else
            detailedResource.setDateOfIssueOfAmendedDocument(null);
        detailedResource.setAmendDocReason(termsAndConditionsModification.getReasonsForAmendment());
        detailedResource.setAmendeddocfilereference(termsAndConditionsModification.getAmendedDocumentFileReference());

        // Internal Document
        if(termsAndConditionsModification.getDateOfInternalDocument() != null) {
            detailedResource.setDateOfInternalDocument(dataConversionUtility.convertDateToSAPFormat(termsAndConditionsModification.getDateOfInternalDocument()));
        } else
            detailedResource.setDateOfInternalDocument(null);
        detailedResource.setInternalDocumentRemarks(termsAndConditionsModification.getInternalDocumentRemarks());
        detailedResource.setInternalDocumentTitle(termsAndConditionsModification.getInternalDocumentTitle());
        detailedResource.setInternalDocumentType(termsAndConditionsModification.getInternalDocumentType());
        detailedResource.setInternaldocfilereference(termsAndConditionsModification.getInternalDocumentFileReference());

        //Lead Banker Document
        detailedResource.setLeadBankerDocumentTitle(termsAndConditionsModification.getLeadBankerDocumentTitle());
        detailedResource.setLeadBankerDocumentType(termsAndConditionsModification.getLeadBankerDocumentType());
        detailedResource.setLbdocfilereference(termsAndConditionsModification.getLeadBankerDocumentFileReference());


        return detailedResource;


    }

}
