package pfs.lms.enquiry.documentation.reasonfordelay;

import java.util.UUID;

public interface IDocumentationReasonForDelayService {

    DocumentationReasonForDelay createReasonForDelay(DocumentationReasonForDelayResource resource, String username);

    DocumentationReasonForDelay updateReasonForDelay(DocumentationReasonForDelayResource resource, String username)
            throws CloneNotSupportedException;

    DocumentationReasonForDelay deleteReasonForDelay(UUID documentationReasonForDelayId, String username)
            throws CloneNotSupportedException;
}
