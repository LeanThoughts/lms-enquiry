package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.monitoring.tra.TrustRetentionAccountStatement;
import pfs.lms.enquiry.utils.DataConversionUtility;

/**
 * Created by sajeev on 28-Jun-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Component
public class SAPTRAStatementResource {

    SAPTRAStatementResourceDetails saptraStatementResourceDetails = new SAPTRAStatementResourceDetails();

    public SAPTRAStatementResourceDetails mapToSAP(TrustRetentionAccountStatement  traStatement) {

        DataConversionUtility dataConversionUtility = new DataConversionUtility();

        SAPTRAStatementResourceDetails detailedResource = new SAPTRAStatementResourceDetails();

        detailedResource.setId(traStatement.getId());
        detailedResource.setTraId(traStatement.getTrustRetentionAccount().getId().toString());

        detailedResource.setSerialNo(traStatement.getSerialNumber());

        detailedResource.setViewrights(traStatement.getViewRights());
        detailedResource.setRemarks(traStatement.getRemarks());

        detailedResource.setTraaccountnumber(traStatement.getTrustRetentionAccount().getAccountNumber());

        detailedResource.setPeriodQuarter(traStatement.getPeriodQuarter());
        detailedResource.setPeriodYear(traStatement.getPeriodYear());
        detailedResource.setDocumenttype(traStatement.getDocumentType());
        detailedResource.setDocumenttitle(traStatement.getDocumentTitle());
        detailedResource.setFileReference(traStatement.getFileReference());
        return detailedResource;
    }

}
