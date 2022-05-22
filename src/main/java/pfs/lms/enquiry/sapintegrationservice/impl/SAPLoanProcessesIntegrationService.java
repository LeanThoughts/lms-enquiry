package pfs.lms.enquiry.sapintegrationservice.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import pfs.lms.enquiry.monitoring.resource.SAPLIEResourceDetails;
import pfs.lms.enquiry.monitoring.resource.SAPLIEResource;
import pfs.lms.enquiry.sapintegrationservice.ISAPLoanProcessesIntegrationService;

import javax.xml.ws.http.HTTPException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class SAPLoanProcessesIntegrationService implements ISAPLoanProcessesIntegrationService {


    @Value("http://192.168.1.205:8000//sap/opu/odata/sap/ZPFS_LMS_MONITOR_SRV/")
    private String postURL;

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
    public SAPLoanProcessesIntegrationService(RestTemplateBuilder restTemplateBuilder) {
         RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    public String fetchCSRFToken() {


        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
                add("Cookie", "sap-usercontext=sap-client="+client);

            }
        };
        headers.add("X-Csrf-Token", "Fetch ");
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        System.out.println("THE REQUEST : "+requestEntity.toString());

        URI uri = null;
        try
        {
            uri = new URI(postURL+"$metadata" );
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        System.out.println("THE URI : "+uri.toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        System.out.println("Headers: " + responseEntity.getHeaders());
        System.out.println("Result - status ("+ responseEntity.getStatusCode() + ") has body: " + responseEntity.hasBody());
                HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("THE STATUS CODE: "+statusCode);

       // System.out.println(responseEntity.getHeaders().get("x-csrf-token").get(0));

        return responseEntity.getHeaders().get("X-Csrf-Token").get(0);
    }

    @Override
    public Object postResource(SAPLIEResource saplieResource) {

       // String csrfToken = this.fetchCSRFToken();

        String postURL = baseUrl + "/sap/opu/odata/sap/ZPFS_LMS_MONITOR_SRV/LendersIndependentEngineerSet?sap-client=300";

        System.out.println("THE URI : "+postURL.toString());

        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
                setContentType(MediaType.APPLICATION_JSON);
                add("X-Requested-With", "X");
               // add("X-CSRF-Token", csrfToken);

                //add("Cookie", "sap-usercontext=sap-client="+client);
            }
        };

        RestTemplate restTemplate = new RestTemplate();
        SAPLIEResourceDetails createdLIE = null;

        HttpEntity<SAPLIEResource> requestToPost = new HttpEntity<SAPLIEResource>(saplieResource, headers);

        System.out.println("THE REQUEST : " + requestToPost.toString());
        System.out.println("THE PAYLOAD : " + saplieResource.getSaplieResourceDetails().toString());


        ResponseEntity responseEntity; // = new ResponseEntity();

        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter());



        try {
            //SAPLIEResource saplieResource1 = restTemplate.postForObject(postURL, requestToPost, SAPLIEResource.class);

             responseEntity =
                    restTemplate.exchange(postURL, HttpMethod.POST, requestToPost, Object.class);

             createdLIE = (SAPLIEResourceDetails) responseEntity.getBody();

        } catch (HttpClientErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post LIE to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;

        }
        catch (HttpServerErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post LIE to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }
        catch (UnknownHttpStatusCodeException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post LIE to SAP");
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }

        catch (HTTPException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post LIE to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;
        }
        catch (Exception ex) {



            System.out.println("General EXCEPTION ----------------------- Post LIE to SAP");
            System.out.println("Exception Message :" +ex.getMessage());
            return null;

        }

        SAPLIEResourceDetails saplieResourceDetails = createdLIE;
         return createdLIE;

     }



    @Override
    public Object postResourceToSAP(Object resource, String serviceUri, HttpMethod httpMethod , MediaType mediaType) {

        String postURL = baseUrl + serviceUri+ "?sap-client=" + client;

        System.out.println("THE URI : "+postURL.toString());

        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
                setContentType(mediaType);
                add("X-Requested-With", "X");
            }
        };

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> requestToPost = new HttpEntity<Object>(resource, headers);

        System.out.println("THE REQUEST : " + requestToPost.toString());
        System.out.println("THE PAYLOAD : " + resource.toString());

        ResponseEntity responseEntity; // = new ResponseEntity();

            try{

                responseEntity =
                        restTemplate.exchange(postURL, httpMethod, requestToPost, Object.class);
                log.info("HTTP Code :" + responseEntity.getStatusCodeValue());

        } catch (HttpClientErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;

        }
        catch (HttpServerErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }
        catch (UnknownHttpStatusCodeException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }

        catch (HTTPException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;
        }
        catch (Exception ex) {
            System.out.println("HTTP EXCEPTION ----------------------- Post " + serviceUri + "  to SAP");
            System.out.println("Exception Message :" +ex.getMessage());
            return null;

        }


        return responseEntity;

    }



    @Override
    public Object deleteResourceFromSAP(String serviceUri, String objectId,    MediaType mediaType) {


        String deleteURL = baseUrl + serviceUri+ "('" +  objectId + "')" + "?sap-client=" + client;
        System.out.println("THE URI : "+deleteURL.toString());

        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
                setContentType(mediaType);
                add("X-Requested-With", "X");
                }
        };
        headers.add("X-Csrf-Token", "Fetch ");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> requestToPost = new HttpEntity<Object>(null, headers);

        restTemplate = new RestTemplate();

        System.out.println("OBJECT ID For Deletion : " + objectId);

        try {

            ResponseEntity responseEntity =
                    restTemplate.exchange(deleteURL, HttpMethod.DELETE, requestToPost, Object.class);
        } catch (Exception ex) {
            System.out.println("Exception Deleting from SAP------------------------- " +ex.getMessage());
        }

        try{
            restTemplate.delete(deleteURL,headers);

        } catch (HttpClientErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Delete " + deleteURL + "  from SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;

        }
        catch (HttpServerErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Delete " + deleteURL + "  from SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }
        catch (UnknownHttpStatusCodeException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Delete " + deleteURL + "  from SAP");
            System.out.println("HTTP Message :" + ex.getMessage());
            return  null;
        }

        catch (HTTPException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Delete " + deleteURL + "  from SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            return  null;
        }
        catch (Exception ex) {
            System.out.println("HTTP EXCEPTION ----------------------- Delete " + deleteURL + "  from SAP");
            System.out.println("Exception Message :" +ex.getMessage());
            return null;

        }


        return "Deleted";

    }


    public void getLoanApplication(String loanApplicationId) {

        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        System.out.println("THE REQUEST : "+requestEntity.toString());

        URI uri = null;

        try
        {
            uri  = new URI(baseUrl + serviceUri + "(" + loanApplicationId + ")?sap-client=" +client +"$format=json"   );
            //uri = new URI("http://192.168.1.203:8000/sap/opu/odata/sap/ZPFS_LOAN_ENQ_PORTAL_SRV/LoanApplicationSet('0000010003115')?sap-client=300&$format=json" );
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        log.info(responseEntity.getBody());
    }
}

