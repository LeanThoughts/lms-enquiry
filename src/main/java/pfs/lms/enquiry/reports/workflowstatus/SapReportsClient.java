package pfs.lms.enquiry.reports.workflowstatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.sapintegrationservice.SapRestTemplates;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Reads entity sets of the SAP reports gateway service ({@code sap.reportsServiceUri}, default
 * {@code /sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV}) as JSON rows. Uses the SAP connection settings of the portal
 * ({@code sap.baseUrl}, {@code sap.client}, {@code sap.userName}, {@code sap.password}) and a logged RestTemplate,
 * so every report call is in the SAP Integration Log. An SAP error is turned into an {@link LmsException} with
 * SAP's message, so the user sees why the report did not run.
 */
@Component
public class SapReportsClient {

    private static final Logger log = LoggerFactory.getLogger(SapReportsClient.class);

    private final RestTemplate restTemplate = SapRestTemplates.logged();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;
    private final String client;
    private final String servicePath;
    private final String authorization;

    public SapReportsClient(@Value("${sap.baseUrl:}") String baseUrl,
                            @Value("${sap.client:}") String client,
                            @Value("${sap.userName:}") String userName,
                            @Value("${sap.password:}") String password,
                            @Value("${sap.reportsServiceUri:/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV}") String servicePath) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim().replaceAll("/+$", "");
        this.client = client == null ? "" : client.trim();
        this.servicePath = servicePath == null ? "" : servicePath.trim().replaceAll("/+$", "");
        String auth = (userName == null ? "" : userName) + ":" + (password == null ? "" : password);
        this.authorization = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.US_ASCII));
    }

    public boolean isConfigured() {
        return !baseUrl.isEmpty();
    }

    /** The full URL of a query on the service (for the report's reference). */
    public String url(String entitySet, String filter) {
        String url = baseUrl + servicePath + "/" + entitySet + "?$format=json";
        if (filter != null && !filter.isEmpty()) {
            url += "&$filter=" + percentEncode(filter);
        }
        if (!client.isEmpty()) {
            url += "&sap-client=" + client;
        }
        return url;
    }

    /**
     * GET the entity set with the given {@code $filter} (unencoded OData expression, or null) and return the rows of
     * {@code d.results}.
     */
    public List<JsonNode> read(String entitySet, String filter) {
        if (!isConfigured()) {
            throw new LmsException("The SAP connection is not configured (sap.baseUrl).", HttpStatus.SERVICE_UNAVAILABLE);
        }
        String url = url(entitySet, filter);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-Requested-With", "X");
        log.info("SAP reports: GET {}", url);
        String body;
        try {
            ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET,
                    new HttpEntity<>(headers), String.class);
            body = response.getBody();
        } catch (HttpStatusCodeException ex) {
            String message = sapError(ex.getResponseBodyAsString());
            log.error("SAP reports: GET {} -> HTTP {} {}: {}", url, ex.getRawStatusCode(), ex.getStatusText(), message);
            throw new LmsException("SAP answered HTTP " + ex.getRawStatusCode() + " " + ex.getStatusText()
                    + (message.isEmpty() ? "" : ": " + message), HttpStatus.BAD_GATEWAY);
        } catch (RestClientException ex) {
            log.error("SAP reports: GET {} failed: {}", url, ex.getMessage());
            throw new LmsException("SAP could not be reached: " + ex.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        return rows(body);
    }

    /** The rows of an OData V2 JSON answer: d.results (collection), d (single entity) or value (V4). */
    List<JsonNode> rows(String body) {
        List<JsonNode> rows = new ArrayList<>();
        if (body == null || body.trim().isEmpty()) {
            return rows;
        }
        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (IOException ex) {
            throw new LmsException("SAP's answer is not JSON (" + ex.getMessage() + ").", HttpStatus.BAD_GATEWAY);
        }
        JsonNode results = root.path("d").path("results");
        if (!results.isArray()) {
            results = root.path("d").isArray() ? root.path("d") : root.path("value");
        }
        if (results.isArray()) {
            results.forEach(rows::add);
        } else if (root.path("d").isObject()) {
            rows.add(root.path("d"));
        }
        return rows;
    }

    /** SAP's error text from its JSON error document, or an empty string. */
    private String sapError(String body) {
        if (body == null || body.isEmpty()) {
            return "";
        }
        try {
            JsonNode message = objectMapper.readTree(body).path("error").path("message");
            String text = message.isObject() ? message.path("value").asText("") : message.asText("");
            return text.replaceAll("\\s+", " ").trim();
        } catch (IOException ex) {
            return body.length() > 300 ? body.substring(0, 300) : body;
        }
    }

    /** RFC 3986 percent-encoding of a query value (space → %20, quote → %27); unreserved characters unchanged. */
    static String percentEncode(String value) {
        StringBuilder out = new StringBuilder();
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int c = b & 0xff;
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '.' || c == '_' || c == '~') {
                out.append((char) c);
            } else {
                out.append('%').append(String.format("%02X", c));
            }
        }
        return out.toString();
    }
}
