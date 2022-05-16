package pfs.lms.enquiry.sapintegrationservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pfs.lms.enquiry.sapintegrationservice.ISAPFileUploadIntegrationService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SAPFileUploadIntegrationService implements ISAPFileUploadIntegrationService {


//    @Value("http://192.168.1.205:8000//sap/opu/odata/sap/ZPFS_LMS_MONITOR_SRV/")
//    private String postURL;

    @Value("${sap.userName}")
    private String userName;

    @Value("${sap.password}")
    private String password;

    @Value("${sap.baseUrl}")
    private String baseUrl;

    @Value("${sap.serviceUri}")
    private String serviceUri;

    @Value("${sap.client}")
    private String client;

    private RestTemplate restTemplate;

    @Autowired
    public SAPFileUploadIntegrationService(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();

//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
//
//        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//
//
//        restTemplate.getMessageConverters().add(converter);
//        restTemplate.getMessageConverters().add(stringHttpMessageConverter);
//
//        List<MediaType> supportedApplicationTypes = new ArrayList<>();
//        MediaType pdfApplication = new MediaType("application","pdf");
//        supportedApplicationTypes.add(pdfApplication);
//
//        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
//
//        byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedApplicationTypes);
//        supportedApplicationTypes.add(pdfApplication);
//        restTemplate.getMessageConverters().add(byteArrayHttpMessageConverter);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);


        this.restTemplate = restTemplate;
    }

    private HttpHeaders prepareHttpHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                setContentType(mediaType);
                add("X-Requested-With", "X");

            }
        };

        return headers;
    }

    @Override
    public Object fileUploadToSAP(String fileName, byte[] documentContent, String serviceUri, MediaType mediaType) throws IOException {


        String url = baseUrl + serviceUri + "?sap-client=" + client;
        log.info("THE URI : " + url.toString());

        log.info("UPLOADING FILE TO SAP ................. THE URI : " + url.toString());


        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                setContentType(mediaType);
                add("X-Requested-With", "X");

            }
        };

        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(fileName)
                .build();
        bodyMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());



         HttpEntity<Object> requestEntity = new HttpEntity<Object>(documentContent, headers);

        try {
            ResponseEntity<String> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class);
            log.info("File Upload Successful : " + response.getStatusCode());

            return response;

        } catch (HttpClientErrorException ex) {

            log.info("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            log.info("HTTP Code    :" + ex.getStatusCode());
            log.info("HTTP Message :" + ex.getMessage());
            return null;
        }


    }

    @Override
    public Object fileUploadToSAP(String serviceUrl, String filePath) {

        String url = baseUrl + serviceUrl + "?sap-client=" + client;
        log.info("THE URI : " + url.toString());

        log.info("TEST UPLOADING FILE TO SAP ................. THE URI : " + url.toString());


        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(new File(filePath));
        map.add("file", value);
        HttpHeaders headers = prepareHttpHeaders(MediaType.MULTIPART_FORM_DATA);

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
         HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        Object response = new Object();
            try {
                 response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

                System.out.println( "FILE UPLOAD RESPONSE SUCESSS:" + response.toString());

            } catch (Exception exception) {
                System.out.println( "FILE UPLOAD FAILED :" + exception.toString());

            }



        return response;

    }

    public Object fileUpload(byte[] documentContent, String serviceUri) throws IOException {

        String url = baseUrl + serviceUri + "?sap-client=" + client;
        log.info("THE URI : " + url.toString());

        log.info("UPLOADING FILE TO SAP ................. THE URI : " + url.toString());

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("user-file", getUserFileResource(documentContent));

        HttpHeaders headers = prepareHttpHeaders(MediaType.APPLICATION_PDF);

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MediaType mediaType = new MediaType("application", "pdf", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        ResponseEntity<String> response = new ResponseEntity<String>(null);
        try {
            //RestTemplate restTemplate = new RestTemplate();
            response = this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);


            System.out.println("response status: " + response.getStatusCode());
            System.out.println("response body: " + response.getBody());
            return  response;
        }
        catch ( Exception ex ) {

            System.out.println("response status: " + response.getStatusCode());
            System.out.println("response body: " + response.getBody());
            return null;
        }
    }

    public static Resource getUserFileResource( byte[] documentContent) throws IOException {
        //todo replace tempFile with a real file
        Path tempFile = Files.createTempFile("upload-test-file", ".txt");
        Files.write(tempFile, documentContent);
        System.out.println("uploading: " + tempFile);
        File file = tempFile.toFile();
        //to upload in-memory bytes use ByteArrayResource instead
        return new FileSystemResource(file);
    }

}

