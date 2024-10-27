package pfs.lms.enquiry.businesspartner.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.resource.SAPDocumentAttachmentResourceDetails;

import java.io.Serializable;

/**
 * Created by sajeev on 24-Jun-21.
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPBusinessPartnerDocumentAttachmentResource implements Serializable {



    @JsonProperty(value = "d")
    private SAPBusinessPartnerDocumentAttachmentResourceDetails sapDocumentAttachmentResourceDetails;




    public void setSapDocumentAttachmentResourceDetails(SAPBusinessPartnerDocumentAttachmentResourceDetails sapDocumentAttachmentResourceDetails) {
        this.sapDocumentAttachmentResourceDetails = sapDocumentAttachmentResourceDetails;
    }

    public SAPBusinessPartnerDocumentAttachmentResourceDetails mapToSAP(
                                                         String businessPartnerId,
                                                         String id,
                                                         String entityId,
                                                         String entityName,
                                                         String documentContent,
                                                         String mimeType,
                                                         String fileName,
                                                         String fileReference,
                                                         String documentType) {

        SAPBusinessPartnerDocumentAttachmentResourceDetails documentAttachmentDetailsResource = new SAPBusinessPartnerDocumentAttachmentResourceDetails();

        documentAttachmentDetailsResource.setBusinessPartnerId(businessPartnerId);
        documentAttachmentDetailsResource.setDocumentcontent(documentContent);
        documentAttachmentDetailsResource.setId(id);
        documentAttachmentDetailsResource.setMimeType(mimeType);
        documentAttachmentDetailsResource.setEntityId(entityId);
        documentAttachmentDetailsResource.setEntityName(entityName);
        documentAttachmentDetailsResource.setFilename(fileName);
        documentAttachmentDetailsResource.setFileReference(fileReference);
        documentAttachmentDetailsResource.setDocumentType(documentType);

        return documentAttachmentDetailsResource;
    }
}
