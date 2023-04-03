package pfs.lms.enquiry.monitoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.monitoring.endusecertificate.EndUseCertificate;
import pfs.lms.enquiry.monitoring.llc.LendersLegalCouncil;
import pfs.lms.enquiry.utils.DataConversionUtility;

import java.io.Serializable;
import java.text.ParseException;

@Component
@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties (ignoreUnknown = true)
public class SAPEndUseCertificateResource implements Serializable   {


    public SAPEndUseCertificateResource() {
        sapEndUseCertificateResourceDetails = new SAPEndUseCertificateResourceDetails();
    }

    @JsonProperty(value = "d")
    private SAPEndUseCertificateResourceDetails sapEndUseCertificateResourceDetails;

    public SAPEndUseCertificateResourceDetails getSapEndUseCertificateResourceDetails() {
        return sapEndUseCertificateResourceDetails;
    }



    public void setSapEndUseCertificateResourceDetails(SAPEndUseCertificateResourceDetails sapEndUseCertificateResourceDetails) {
        this.sapEndUseCertificateResourceDetails = sapEndUseCertificateResourceDetails;
    }

    public SAPEndUseCertificateResourceDetails
                    mapToSAP(EndUseCertificate endUseCertificate, User lastProcessedBy) throws ParseException {
        DataConversionUtility dataConversionUtility =  new DataConversionUtility();

       SAPEndUseCertificateResourceDetails detailsResource= new SAPEndUseCertificateResourceDetails();

        detailsResource.setId(endUseCertificate.getId().toString());
        detailsResource.setMonitorId(endUseCertificate.getLoanMonitor().getId().toString());
        detailsResource.setSerialNumber(endUseCertificate.getSerialNumber().toString());

        if (endUseCertificate.getEndUseCertificateDate() != null)
            detailsResource.setEndusecertificatedate(dataConversionUtility.convertDateToSAPFormat(endUseCertificate.getEndUseCertificateDate()));
        else
            endUseCertificate.setEndUseCertificateDate(null);

        if (endUseCertificate.getEventDate() != null)
            detailsResource.setEventdate(dataConversionUtility.convertDateToSAPFormat(endUseCertificate.getEventDate()));
        else
            endUseCertificate.setEventDate(null);

        if (endUseCertificate.getEndUseCertificateDueDate() != null)
            detailsResource.setEndusecertificateduedate(dataConversionUtility.convertDateToSAPFormat(endUseCertificate.getEndUseCertificateDueDate()));
        else
            endUseCertificate.setEndUseCertificateDueDate(null);

        detailsResource.setDocumenttype(endUseCertificate.getDocumentType());
        detailsResource.setDocumentTitle(endUseCertificate.getDocumentTitle());
        detailsResource.setFilereference(endUseCertificate.getFileReference());
        detailsResource.setRemarks(endUseCertificate.getRemarks());

        return detailsResource;
    }
}
