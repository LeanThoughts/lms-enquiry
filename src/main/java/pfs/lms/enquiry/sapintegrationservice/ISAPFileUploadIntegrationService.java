package pfs.lms.enquiry.sapintegrationservice;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import pfs.lms.enquiry.monitoring.resource.SAPLIEResource;

import java.io.IOException;

public interface ISAPFileUploadIntegrationService {



    Object fileUploadToSAP(String fileName, byte[] documentContent, String serviceUri , MediaType mediaType  ) throws IOException;

    Object fileUpload(byte[] documentConent, String serviceUrl) throws IOException;

    Object fileUploadToSAP(String serviceUrl, String filePath);


 }
