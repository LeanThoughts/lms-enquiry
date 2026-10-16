package pfs.lms.enquiry.sapintegrationservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Records every HTTP request a {@link SapRestTemplates logged RestTemplate} sends to SAP as a {@link SapHttpLog}:
 * method, URL, headers (Authorization masked), body, the response status and body (or the transport failure), the
 * duration, the business context of the thread ({@link SapHttpLogContext}) and the calling portal class. Logging
 * never changes the outcome of the call: a failure to write the log is logged and swallowed, and exceptions of the
 * call itself are recorded and rethrown unchanged.
 * <p>
 * The response body can be read here and again by the caller because the logged RestTemplate uses a buffering
 * request factory.
 */
@Component
public class SapHttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SapHttpLoggingInterceptor.class);

    /** /sap/opu/odata/sap/<service>/<entity set>(…) — the OData service and collection in the path. */
    private static final Pattern ODATA_PATH = Pattern.compile("/odata/(?:sap/)?([^/]+)/([A-Za-z0-9_]+)");
    private static final Pattern SERVICE_ONLY = Pattern.compile("/odata/(?:sap/)?([^/?]+)");

    private final SapHttpLogService logService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SapHttpLoggingInterceptor(SapHttpLogService logService) {
        this.logService = logService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (!logService.isEnabled()) {
            log.debug("SAP HTTP log: disabled (sap.http-log.enabled=false), {} {} not logged", request.getMethodValue(),
                    request.getURI());
            return execution.execute(request, body);
        }
        log.debug("SAP HTTP log: recording {} {}", request.getMethodValue(), request.getURI());
        SapHttpLog entry = new SapHttpLog();
        entry.setRequestedAt(LocalDateTime.now());
        long started = System.nanoTime();
        try {
            describeRequest(entry, request, body);
        } catch (RuntimeException ex) {
            log.warn("SAP HTTP log: could not describe the request: {}", ex.getMessage());
        }
        try {
            ClientHttpResponse response = execution.execute(request, body);
            entry.setDurationMs((System.nanoTime() - started) / 1_000_000);
            try {
                describeResponse(entry, response);
            } catch (RuntimeException ex) {
                log.warn("SAP HTTP log: could not describe the response: {}", ex.getMessage());
            }
            logService.record(entry);
            return response;
        } catch (IOException | RuntimeException ex) {
            entry.setDurationMs((System.nanoTime() - started) / 1_000_000);
            entry.setResponseStatus(0);
            entry.setSuccess(false);
            entry.setErrorMessage(abbreviate(ex.getClass().getSimpleName() + ": " + ex.getMessage(), 2000));
            logService.record(entry);
            throw ex;
        }
    }

    // ------------------------------------------------------------------------------------------ request

    private void describeRequest(SapHttpLog entry, HttpRequest request, byte[] body) {
        URI uri = request.getURI();
        entry.setHttpMethod(request.getMethodValue());
        entry.setUrl(abbreviate(uri.toString(), 2000));
        entry.setHost(uri.getHost() == null ? null : uri.getHost() + (uri.getPort() > 0 ? ":" + uri.getPort() : ""));
        String path = uri.getRawPath() == null ? "" : uri.getRawPath();
        Matcher m = ODATA_PATH.matcher(path);
        if (m.find()) {
            entry.setServiceName(abbreviate(m.group(1), 120));
            entry.setEntitySet(abbreviate(m.group(2), 120));
        } else {
            Matcher s = SERVICE_ONLY.matcher(path);
            if (s.find()) {
                entry.setServiceName(abbreviate(s.group(1), 120));
            }
        }
        HttpHeaders headers = request.getHeaders();
        entry.setRequestHeaders(headerLines(headers));
        MediaType contentType = headers.getContentType();
        entry.setRequestContentType(contentType == null ? null : abbreviate(contentType.toString(), 120));
        entry.setRequestSize(body == null ? 0L : (long) body.length);
        entry.setRequestBody(bodyText(body, contentType, logService.getMaxBodyLength()));

        SapHttpLogContext.Scope scope = SapHttpLogContext.current();
        if (scope != null) {
            entry.setBusinessProcess(abbreviate(scope.businessProcess, 120));
            entry.setSubProcess(abbreviate(scope.subProcess, 120));
            entry.setBusinessObjectId(abbreviate(scope.businessObjectId, 80));
            entry.setPointerId(scope.pointerId);
        }
        entry.setCaller(caller());
        entry.setUserName(signedInUser());
        entry.setThreadName(abbreviate(Thread.currentThread().getName(), 80));
    }

    /** Request headers as lines; the Authorization value is replaced by its scheme and "***". */
    static String headerLines(HttpHeaders headers) {
        StringBuilder lines = new StringBuilder();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            for (String value : header.getValue()) {
                String shown = value;
                if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(header.getKey())
                        || HttpHeaders.PROXY_AUTHORIZATION.equalsIgnoreCase(header.getKey())
                        || HttpHeaders.COOKIE.equalsIgnoreCase(header.getKey())) {
                    int space = value == null ? -1 : value.indexOf(' ');
                    shown = (space > 0 ? value.substring(0, space) + " " : "") + "***";
                }
                lines.append(header.getKey()).append(": ").append(shown).append('\n');
            }
        }
        return abbreviate(lines.toString().trim(), 8000);
    }

    /** The first portal frame outside this package and the gateway client, as "Class.method". */
    private static String caller() {
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            String cls = frame.getClassName();
            if (!cls.startsWith("pfs.lms.enquiry.")
                    || cls.equals(SapHttpLoggingInterceptor.class.getName())
                    || cls.equals(SapRestTemplates.class.getName())
                    || cls.endsWith("CollateralSapGatewayClient")
                    || cls.contains("$$")) {
                continue; // the log itself, the gateway client and Spring proxies are not the caller
            }
            String simple = cls.substring(cls.lastIndexOf('.') + 1);
            int inner = simple.indexOf('$');
            if (inner > 0) {
                simple = simple.substring(0, inner);
            }
            return abbreviate(simple + "." + frame.getMethodName(), 200);
        }
        return null;
    }

    private static String signedInUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(String.valueOf(authentication.getPrincipal()))) {
                return null;
            }
            return abbreviate(authentication.getName(), 120);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    // ----------------------------------------------------------------------------------------- response

    private void describeResponse(SapHttpLog entry, ClientHttpResponse response) throws IOException {
        int status = response.getRawStatusCode();
        entry.setResponseStatus(status);
        entry.setResponseStatusText(abbreviate(response.getStatusText(), 120));
        entry.setSuccess(status >= 200 && status < 300);
        MediaType contentType = response.getHeaders().getContentType();
        entry.setResponseContentType(contentType == null ? null : abbreviate(contentType.toString(), 120));
        byte[] body = StreamUtils.copyToByteArray(response.getBody()); // buffered: the caller reads it again
        entry.setResponseSize((long) body.length);
        String text = bodyText(body, contentType, logService.getMaxBodyLength());
        entry.setResponseBody(text);
        if (!entry.isSuccess()) {
            entry.setErrorMessage(abbreviate(sapError(body, contentType, status, response.getStatusText()), 2000));
            entry.setSapTransactionId(abbreviate(sapTransactionId(body, contentType), 64));
        }
    }

    /** SAP's error text (error/message/value) when the body is its JSON error document, else the HTTP status. */
    private String sapError(byte[] body, MediaType contentType, int status, String statusText) {
        JsonNode error = errorNode(body, contentType);
        String fallback = "HTTP " + status + (statusText == null || statusText.isEmpty() ? "" : " " + statusText);
        if (error == null) {
            return fallback;
        }
        JsonNode message = error.path("message");
        String text = message.isObject() ? message.path("value").asText("") : message.asText("");
        if (text.isEmpty()) {
            return fallback;
        }
        String code = error.path("code").asText("");
        return fallback + ": " + text + (code.isEmpty() ? "" : " [" + code + "]");
    }

    private String sapTransactionId(byte[] body, MediaType contentType) {
        JsonNode error = errorNode(body, contentType);
        if (error == null) {
            return null;
        }
        String id = error.path("innererror").path("transactionid").asText("");
        return id.isEmpty() ? null : id;
    }

    private JsonNode errorNode(byte[] body, MediaType contentType) {
        if (body == null || body.length == 0 || !isText(contentType)) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode error = root.path("error");
            return error.isObject() ? error : null;
        } catch (IOException | RuntimeException ex) {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------ helpers

    /** The body as text when it is text (JSON, XML, plain, form); otherwise a short description. */
    static String bodyText(byte[] body, MediaType contentType, int maxLength) {
        if (body == null || body.length == 0) {
            return null;
        }
        if (!isText(contentType)) {
            return "<binary body: " + body.length + " bytes"
                    + (contentType == null ? "" : ", " + contentType) + ">";
        }
        Charset charset = contentType != null && contentType.getCharset() != null ? contentType.getCharset()
                : StandardCharsets.UTF_8;
        return abbreviate(new String(body, charset), maxLength);
    }

    static boolean isText(MediaType type) {
        if (type == null) {
            return true; // no content type: assume text (SAP error documents, plain RestTemplate payloads)
        }
        String subtype = type.getSubtype().toLowerCase();
        return "text".equalsIgnoreCase(type.getType())
                || subtype.contains("json") || subtype.contains("xml") || subtype.contains("javascript")
                || subtype.contains("x-www-form-urlencoded") || subtype.contains("atom");
    }

    static String abbreviate(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        return text.length() <= maxLength ? text : text.substring(0, Math.max(0, maxLength - 1)) + "…";
    }
}
