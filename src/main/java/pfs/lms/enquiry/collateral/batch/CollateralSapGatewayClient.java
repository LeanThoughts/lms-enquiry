package pfs.lms.enquiry.collateral.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pfs.lms.enquiry.sapintegrationservice.SapRestTemplates;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

/**
 * HTTP client for the collateral gateway service (ZPFS_LMS_COLLATERAL_SRV). It uses the same SAP connection settings as
 * the other batches ({@code sap.baseUrl}, {@code sap.client}, {@code sap.userName}, {@code sap.password}) but differs
 * from {@code SAPLoanProcessesIntegrationService} in two ways that matter for this service:
 * <ul>
 * <li>It sends {@code Accept: application/json} and reads the body as text. Without an Accept header SAP Gateway
 * answers a successful POST with the created entity ({@code er_entity}) as Atom XML, which the shared service cannot
 * deserialize; that exception is swallowed there and the caller gets {@code null} for a POST that actually succeeded.
 * Here the real HTTP status decides success, and the response body (the created entity, or SAP's error text) is
 * returned to the caller.</li>
 * <li>Update and delete take the complete entity path including the OData key predicate, e.g.
 * {@code CoverageSet(ZidNo='0000000912',ItemId=guid'…')}, so composite keys work. The shared delete wraps its argument
 * in {@code ('…')}, which only fits a single simple key.</li>
 * </ul>
 */
@Component
public class CollateralSapGatewayClient {

    private static final Logger log = LoggerFactory.getLogger(CollateralSapGatewayClient.class);

    /** Outcome of one gateway call: the HTTP status, the raw body (entity JSON or SAP error text) and success. */
    public static final class Result {
        private final int status;
        private final String body;
        private final boolean success;
        private final String error;

        Result(int status, String body, boolean success, String error) {
            this.status = status;
            this.body = body;
            this.success = success;
            this.error = error;
        }

        public int getStatus() { return status; }
        public String getBody() { return body; }
        public boolean isSuccess() { return success; }
        /** Short description of the failure (HTTP status text or transport error); null when successful. */
        public String getError() { return error; }

        @Override
        public String toString() {
            return success ? "HTTP " + status : "HTTP " + status + " " + error;
        }
    }

    /** Logged (SAP HTTP log) and buffering, so every collateral call is in the Administration report. */
    private final RestTemplate restTemplate = SapRestTemplates.logged();
    private final String baseUrl;
    private final String client;
    private final String authorization;

    public CollateralSapGatewayClient(@Value("${sap.baseUrl:}") String baseUrl,
                                      @Value("${sap.client:}") String client,
                                      @Value("${sap.userName:}") String userName,
                                      @Value("${sap.password:}") String password) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim();
        this.client = client == null ? "" : client.trim();
        String auth = (userName == null ? "" : userName) + ":" + (password == null ? "" : password);
        this.authorization = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * GET a collection or entity, e.g. {@code …/CollateralObjectTypeAgreementTypeSet?$filter=…}. The path must already
     * be percent-encoded (spaces as %20, quotes as %27); the JSON body is in {@link Result#getBody()}.
     */
    public Result get(String path) {
        return exchange(HttpMethod.GET, path, null);
    }

    /** True when {@code sap.baseUrl} is configured; without it every call fails fast with status 0. */
    public boolean isConfigured() {
        return !baseUrl.isEmpty();
    }

    /** POST the entity to a collection, e.g. {@code /sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/ChecklistHeaderSet}. */
    public Result post(String collectionPath, Object payload) {
        return exchange(HttpMethod.POST, collectionPath, payload);
    }

    /** PUT the entity to its keyed path, e.g. {@code …/CoverageSet(ZidNo='0000000912',ItemId=guid'…')}. */
    public Result put(String entityPath, Object payload) {
        return exchange(HttpMethod.PUT, entityPath, payload);
    }

    /** DELETE the entity at its keyed path. */
    public Result delete(String entityPath) {
        return exchange(HttpMethod.DELETE, entityPath, null);
    }

    /**
     * Sends a file as the media stream of a keyed entity: {@code PUT <entityPath>/$value} with the raw bytes as body,
     * the file's content type and the {@code Slug} header. On the gateway this is {@code UPDATE_STREAM}: the key from
     * the URL arrives in {@code it_key_tab}, the bytes in {@code is_media_resource}, and the Slug — like any request
     * header — is read through the data-provider facade ({@code get_request_header}).
     *
     * @param entityPath  the keyed entity, e.g. {@code …/DocumentAttachmentSet('<id>')}
     * @param content     the file bytes
     * @param contentType the file's MIME type, e.g. {@code application/pdf}
     * @param slug        the Slug header value (the entity's fields, or just the file name)
     */
    public Result putStream(String entityPath, byte[] content, String contentType, String slug) {
        return stream(HttpMethod.PUT, entityPath + "/$value", content, contentType, slug);
    }

    private Result stream(HttpMethod method, String path, byte[] content, String contentType, String slug) {
        HttpHeaders extra = new HttpHeaders();
        extra.setContentType(MediaType.parseMediaType(contentType));
        if (slug != null && !slug.isEmpty()) {
            extra.set("Slug", slug);
        }
        return exchange(method, path, content, extra);
    }

    private Result exchange(HttpMethod method, String path, Object payload) {
        return exchange(method, path, payload, null);
    }

    private Result exchange(HttpMethod method, String path, Object payload, HttpHeaders extra) {
        if (baseUrl.isEmpty()) {
            log.warn("Collateral SAP gateway: sap.baseUrl is not configured; {} {} not sent", method, path);
            return new Result(0, null, false, "sap.baseUrl is not configured");
        }
        String url = url(path);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-Requested-With", "X");
        if (payload != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        if (extra != null) {
            headers.putAll(extra); // e.g. the file's content type and Slug for a media stream
        }
        log.info("Collateral SAP gateway: {} {}", method, url);
        log.info("Collateral Payload: {} ", payload == null ? "(none)"
                : payload instanceof byte[] ? ((byte[]) payload).length + " bytes" : payload.toString());

        try {
            // the path is already a valid, percent-encoded URI (key predicates encode their values); passing a URI
            // rather than a String stops RestTemplate from encoding it a second time (%20 -> %2520)
            ResponseEntity<String> response = restTemplate.exchange(URI.create(url), method,
                    new HttpEntity<>(payload, headers),
                    String.class);
            int status = response.getStatusCodeValue();
            boolean ok = response.getStatusCode().is2xxSuccessful();
            log.info("Collateral SAP gateway: {} {} -> HTTP {}", method, path, status);
            if (log.isDebugEnabled() && response.getBody() != null) {
                log.debug("Collateral SAP gateway response body: {}", response.getBody());
            }
            return new Result(status, response.getBody(), ok, ok ? null : response.getStatusCode().toString());
        } catch (HttpStatusCodeException ex) {
            // 4xx / 5xx: SAP's error document is in the body; keep it for the log and the caller
            String body = ex.getResponseBodyAsString();
            log.error("Collateral SAP gateway: {} {} -> HTTP {} {}: {}", method, path, ex.getRawStatusCode(),
                    ex.getStatusText(), abbreviate(body));
            return new Result(ex.getRawStatusCode(), body, false, ex.getStatusCode() + " " + ex.getStatusText());
        } catch (RestClientException ex) {
            // connection refused, timeout, unreadable response, …
            log.error("Collateral SAP gateway: {} {} failed: {}", method, path, ex.getMessage());
            return new Result(0, null, false, ex.getMessage());
        }
    }

    private String url(String path) {
        String p = path == null ? "" : path;
        String base = baseUrl;
        if (base.endsWith("/") && p.startsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String url = base + p;
        if (!client.isEmpty()) {
            url += (url.contains("?") ? "&" : "?") + "sap-client=" + client;
        }
        return url;
    }

    private static String abbreviate(String text) {
        if (text == null) {
            return "";
        }
        String oneLine = text.replaceAll("\\s+", " ").trim();
        return oneLine.length() <= 600 ? oneLine : oneLine.substring(0, 600) + "…";
    }
}
