package pfs.lms.enquiry.managementdashboard;

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
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;

/**
 * Reads the Management Dashboard from the SAP gateway service {@code ZPFS_LMS_DASHBOARD_SRV}
 * ({@code sap.dashboardServiceUri}, default {@code /sap/opu/odata/sap/ZPFS_LMS_DASHBOARD_SRV}) in one call:
 * <pre>
 * GET …/DashboardSet(RunDate=datetime'2026-11-28T00:00:00',AmtInCr=true)
 *     ?$expand=BudgetActual,InterestRates,Ratings,SmaOverdue,EclOverdue,LoanBook,CashFlow,CollEfficiency,Liquidity,Proposals
 *     &amp;$format=json
 * </pre>
 * The service's DPC_EXT fills all ten navigations in GET_EXPANDED_ENTITY, so one request returns the whole
 * dashboard. Uses the portal's SAP connection settings ({@code sap.baseUrl}, {@code sap.client}, {@code sap.userName},
 * {@code sap.password}) and the logged RestTemplate, so the call appears in the SAP Integration Log. SAP's
 * "No dashboard data found for …" business error is passed on as HTTP 404 so the screen can say that the chosen run
 * date has no snapshot; other SAP errors become HTTP 502 with SAP's message.
 */
@Component
public class ManagementDashboardSapClient {

    private static final Logger log = LoggerFactory.getLogger(ManagementDashboardSapClient.class);

    static final String ENTITY_SET = "DashboardSet";
    static final String EXPAND = "BudgetActual,InterestRates,Ratings,SmaOverdue,EclOverdue,LoanBook,CashFlow,"
            + "CollEfficiency,Liquidity,Proposals";

    private final RestTemplate restTemplate = SapRestTemplates.logged();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;
    private final String client;
    private final String servicePath;
    private final String authorization;

    public ManagementDashboardSapClient(@Value("${sap.baseUrl:}") String baseUrl,
                                        @Value("${sap.client:}") String client,
                                        @Value("${sap.userName:}") String userName,
                                        @Value("${sap.password:}") String password,
                                        @Value("${sap.dashboardServiceUri:/sap/opu/odata/sap/ZPFS_LMS_DASHBOARD_SRV}")
                                        String servicePath) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim().replaceAll("/+$", "");
        this.client = client == null ? "" : client.trim();
        this.servicePath = servicePath == null ? "" : servicePath.trim().replaceAll("/+$", "");
        String auth = (userName == null ? "" : userName) + ":" + (password == null ? "" : password);
        this.authorization = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.US_ASCII));
    }

    public boolean isConfigured() {
        return !baseUrl.isEmpty();
    }

    /** The full URL of the dashboard read for a run date and amount unit (shown on the screen as the source). */
    public String url(LocalDate runDate, boolean amountInCrore) {
        String url = baseUrl + servicePath + "/" + ENTITY_SET
                + "(RunDate=datetime'" + runDate + "T00:00:00',AmtInCr=" + amountInCrore + ")"
                + "?$expand=" + EXPAND + "&$format=json";
        if (!client.isEmpty()) {
            url += "&sap-client=" + client;
        }
        return url;
    }

    /** GET the dashboard entity with all navigations expanded and return its JSON object ({@code d}). */
    public JsonNode read(LocalDate runDate, boolean amountInCrore) {
        if (!isConfigured()) {
            throw new LmsException("The SAP connection is not configured (sap.baseUrl).", HttpStatus.SERVICE_UNAVAILABLE);
        }
        String url = url(runDate, amountInCrore);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-Requested-With", "X");
        log.info("Management dashboard: GET {}", url);
        String body;
        long started = System.nanoTime();
        try {
            ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET,
                    new HttpEntity<>(headers), String.class);
            body = response.getBody();
            log.info("Management dashboard: GET {} -> {} bytes in {} ms", ENTITY_SET, body == null ? 0 : body.length(),
                    Math.round((System.nanoTime() - started) / 1_000_000d));
        } catch (HttpStatusCodeException ex) {
            String message = sapError(ex.getResponseBodyAsString());
            log.error("Management dashboard: GET {} -> HTTP {} {}: {}", url, ex.getRawStatusCode(), ex.getStatusText(),
                    message);
            if (isNoData(ex, message)) {
                throw new LmsException(message.isEmpty() ? "No dashboard data found for " + runDate + "." : message,
                        HttpStatus.NOT_FOUND);
            }
            throw new LmsException("SAP answered HTTP " + ex.getRawStatusCode() + " " + ex.getStatusText()
                    + (message.isEmpty() ? "" : ": " + message), HttpStatus.BAD_GATEWAY);
        } catch (RestClientException ex) {
            log.error("Management dashboard: GET {} failed: {}", url, ex.getMessage());
            throw new LmsException("SAP could not be reached: " + ex.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        return entity(body);
    }

    /** SAP's business error for a run date without snapshot (DPC_EXT raises it as 400/404 with that text). */
    private static boolean isNoData(HttpStatusCodeException ex, String message) {
        return ex.getStatusCode() == HttpStatus.NOT_FOUND
                || message.toLowerCase().startsWith("no dashboard data found");
    }

    /** The entity of an OData V2 JSON answer: {@code d} (single entity) or the first of {@code d.results}. */
    JsonNode entity(String body) {
        if (body == null || body.trim().isEmpty()) {
            throw new LmsException("SAP returned an empty answer.", HttpStatus.BAD_GATEWAY);
        }
        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (IOException ex) {
            throw new LmsException("SAP's answer is not JSON (" + ex.getMessage() + ").", HttpStatus.BAD_GATEWAY);
        }
        JsonNode d = root.path("d");
        if (d.path("results").isArray()) {
            if (d.path("results").size() == 0) {
                throw new LmsException("No dashboard data found.", HttpStatus.NOT_FOUND);
            }
            return d.path("results").get(0);
        }
        if (!d.isObject()) {
            throw new LmsException("SAP's answer has no dashboard entity.", HttpStatus.BAD_GATEWAY);
        }
        return d;
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
}
