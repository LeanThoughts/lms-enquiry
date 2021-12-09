package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import pfs.lms.enquiry.resource.SAPLoanApplicationResource;
import pfs.lms.enquiry.service.ISAPIntegrationService;

import javax.xml.ws.http.HTTPException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;


@Slf4j
@Service
@RequiredArgsConstructor
public class SAPIntegrationService implements ISAPIntegrationService {


    @Value("${sap.postUrl}")
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


    @Override
    public String fetchCSRFToken() {



        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
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

        System.out.println(responseEntity.getHeaders().get("x-csrf-token").get(0));

        return responseEntity.getHeaders().get("x-csrf-token").get(0);
    }


    @Override
    public SAPLoanApplicationResource postLoanApplication(SAPLoanApplicationResource sapLoanApplicationResource) {

        HttpHeaders headers = new HttpHeaders() {
            {
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
                setContentType(MediaType.APPLICATION_JSON);
                add("X-Requested-With", "X");
            }
        };

        RestTemplate restTemplate = new RestTemplate();
        SAPLoanApplicationResource createdEnquiry = null;
        HttpEntity<SAPLoanApplicationResource> requestToPost = new HttpEntity<SAPLoanApplicationResource>(sapLoanApplicationResource, headers);

        ResponseEntity responseEntity; // = new ResponseEntity();

        try {
            responseEntity = restTemplate.exchange(postURL, HttpMethod.POST, requestToPost, SAPLoanApplicationResource.class);
            //Object o =   responseEntity.getBody();
           // createdEnquiry = (ResponseEntity<SAPLoanApplicationResource>) responseEntity.getBody();

             createdEnquiry = (SAPLoanApplicationResource)responseEntity.getBody();

        } catch (HttpClientErrorException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post Loan to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            System.out.println("Payload " + sapLoanApplicationResource.getSapLoanApplicationDetailsResource().toString());
            return  null;

        }
        catch (HttpServerErrorException ex) {

            System.out.println("HttpServerErrorException ----------------------- Post Loan to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" + ex.getMessage());
            System.out.println("Payload " + sapLoanApplicationResource.getSapLoanApplicationDetailsResource().toString());
            return  null;
        }
        catch (UnknownHttpStatusCodeException ex) {

            System.out.println("UnknownHttpStatusCodeException ----------------------- Post Loan to SAP");
            System.out.println("HTTP Message :" + ex.getMessage());
            System.out.println("Payload " + sapLoanApplicationResource.getSapLoanApplicationDetailsResource().toString());
            return  null;
        }

        catch (HTTPException ex) {

            System.out.println("HTTP EXCEPTION ----------------------- Post Loan to SAP");
            System.out.println("HTTP Code    :" + ex.getStatusCode());
            System.out.println("HTTP Message :" +ex.getMessage());
            System.out.println("Payload " + sapLoanApplicationResource.getSapLoanApplicationDetailsResource().toString());
            return  null;
        }
        catch (Exception ex) {

            System.out.println("General EXCEPTION ----------------------- Post Loan to SAP");
            System.out.println("Exception Message :" +ex.getMessage());
            System.out.println("Payload " + sapLoanApplicationResource.getSapLoanApplicationDetailsResource().toString());
            return null;

        }

        //HttpStatus statusCode = createdEnquiry.getStatusCode();
        SAPLoanApplicationResource sapLoanApplicationResourceResponse = createdEnquiry;
        return sapLoanApplicationResourceResponse;
    }

    @Override
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

