package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SAPTermsAndConditionsModificationResourceDetails {


    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "MonitorId")
    private String monitorId;

    @JsonProperty(value = "SerialNumber")
    private Integer serialNumber;

    @JsonProperty(value = "DocumentType")
    private String documentType;

    @JsonProperty(value = "DocumentTitle")
    private String documentTitle;

    @JsonProperty(value = "Communication")
    private String communication;

    @JsonProperty(value = "BorrowerRequestLetterDate")
    private String borrowerRequestLetterDate;

    @JsonProperty(value = "DateOfIssueOfAmendedSanctionLetter")
    private String dateOfIssueOfAmendedSanctionLetter;

    @JsonProperty(value = "Remarks")
    private String remarks;
    @JsonProperty(value = "ReasonForAmend")
    private String reasonForAmend;
    @JsonProperty(value = "Filereference")
    private String fileReference;



    @JsonProperty(value = "AmendedDocumentTitle")
    private String amendedDocumentTitle;
    @JsonProperty(value = "AmendedDocumentType")
    private String amendedDocumentType;
    @JsonProperty(value = "DateOfIssueOfAmendedDocument")
    private String dateOfIssueOfAmendedDocument;
    @JsonProperty(value = "AmendedDocumentRemarks")
    private String amendedDocumentRemarks;
    @JsonProperty(value = "Amendeddocfilereference")
    private String amendeddocfilereference;
    @JsonProperty(value = "AmendDocReason")
    private String amendDocReason;





    @JsonProperty(value = "DateOfInternalDocument")
    private String dateOfInternalDocument;
    @JsonProperty(value = "InternalDocumentRemarks")
    private String internalDocumentRemarks;
    @JsonProperty(value = "InternalDocumentTitle")
    private String internalDocumentTitle;
    @JsonProperty(value = "InternalDocumentType")
    private String internalDocumentType;
    @JsonProperty(value = "Internaldocfilereference")
    private String internaldocfilereference;

    @JsonProperty(value = "LeadBankerDocumentTitle")
    private String leadBankerDocumentTitle;
    @JsonProperty(value = "LeadBankerDocumentType")
    private String leadBankerDocumentType;
    @JsonProperty(value = "Lbdocfilereference")
    private String lbdocfilereference;



}
