package pfs.lms.enquiry.sapintegrationservice;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import pfs.lms.enquiry.monitoring.resource.SAPLIEReportAndFeeResource;
import pfs.lms.enquiry.monitoring.resource.SAPLIEResource;

public interface ISAPLoanProcessesIntegrationService {

    String fetchCSRFToken();

    Object postResource(SAPLIEResource saplieResource);

    Object postResourceToSAP(Object resource, String serviceUri, HttpMethod httpMethod, MediaType mediaType);

    Object deleteResourceFromSAP(String serviceUri, String objectId,   MediaType mediaType);

}
