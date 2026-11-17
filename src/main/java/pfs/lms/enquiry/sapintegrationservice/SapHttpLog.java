package pfs.lms.enquiry.sapintegrationservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * One HTTP request the portal sent to SAP (any gateway service), with what was sent and what came back. Written by
 * {@link SapHttpLoggingInterceptor} for every call made through a {@link SapRestTemplates logged RestTemplate}, and
 * shown in the Administration report "SAP Integration Log".
 * <p>
 * The Authorization header is never stored. Bodies are stored as text (JSON / XML / plain) up to a configurable
 * length; a binary body (a file upload) is stored as a short description of its size and type.
 */
@Entity
@Table(name = "sap_http_log", indexes = {
        @Index(name = "ix_sap_http_log_requested_at", columnList = "requested_at"),
        @Index(name = "ix_sap_http_log_success", columnList = "success"),
        @Index(name = "ix_sap_http_log_business_object", columnList = "business_object_id")
})
public class SapHttpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** When the request was sent (server time). */
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    /** Round trip in milliseconds (until the response, or the failure, was received). */
    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "http_method", length = 10, nullable = false)
    private String httpMethod;

    /** Full request URL including the query string (e.g. sap-client). */
    @Column(name = "url", length = 2000, nullable = false)
    private String url;

    /** Host of the URL, for filtering by SAP system. */
    @Column(name = "host", length = 120)
    private String host;

    /** The OData service name in the URL (e.g. ZPFS_LMS_COLLATERAL_SRV), when recognisable. */
    @Column(name = "service_name", length = 120)
    private String serviceName;

    /** The entity set addressed by the URL (e.g. ChecklistHeaderSet), when recognisable. */
    @Column(name = "entity_set", length = 120)
    private String entitySet;

    /** Request headers as "Name: value" lines; Authorization is masked. */
    @Column(name = "request_headers", columnDefinition = "TEXT")
    private String requestHeaders;

    @Column(name = "request_content_type", length = 120)
    private String requestContentType;

    @Column(name = "request_body", columnDefinition = "LONGTEXT")
    private String requestBody;

    /** Size of the request body in bytes (also for binary bodies whose content is not stored). */
    @Column(name = "request_size")
    private Long requestSize;

    /** HTTP status of the response; 0 when no response was received (connection refused, timeout, …). */
    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "response_status_text", length = 120)
    private String responseStatusText;

    @Column(name = "response_content_type", length = 120)
    private String responseContentType;

    @Column(name = "response_body", columnDefinition = "LONGTEXT")
    private String responseBody;

    @Column(name = "response_size")
    private Long responseSize;

    /** True for a 2xx response. */
    @Column(name = "success", nullable = false)
    private boolean success;

    /**
     * The failure in one line: SAP's error message (error/message/value), or the HTTP status text, or the transport
     * exception. Null when successful.
     */
    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    /** SAP's transaction id from the error document (innererror/transactionid), for /IWFND/ERROR_LOG. */
    @Column(name = "sap_transaction_id", length = 64)
    private String sapTransactionId;

    /** The business process the call belongs to (e.g. CollateralManagement), when the caller said so. */
    @Column(name = "business_process", length = 120)
    private String businessProcess;

    /** The sub-process / component (e.g. Collateral Documents), when the caller said so. */
    @Column(name = "sub_process", length = 120)
    private String subProcess;

    /** The portal id of the business object sent, when the caller said so. */
    @Column(name = "business_object_id", length = 80)
    private String businessObjectId;

    /** The SAP integration pointer that triggered the call, when the caller said so. */
    @Column(name = "pointer_id")
    private Long pointerId;

    /** The portal class and method that made the call (e.g. CollateralSapIntegrationService.send). */
    @Column(name = "caller", length = 200)
    private String caller;

    /** The signed-in user on whose behalf the call was made; null for scheduled (batch) calls. */
    @Column(name = "user_name", length = 120)
    private String userName;

    /** The thread the call was made on (e.g. scheduling-1 for the sweep). */
    @Column(name = "thread_name", length = 80)
    private String threadName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getEntitySet() { return entitySet; }
    public void setEntitySet(String entitySet) { this.entitySet = entitySet; }
    public String getRequestHeaders() { return requestHeaders; }
    public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
    public String getRequestContentType() { return requestContentType; }
    public void setRequestContentType(String requestContentType) { this.requestContentType = requestContentType; }
    public String getRequestBody() { return requestBody; }
    public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
    public Long getRequestSize() { return requestSize; }
    public void setRequestSize(Long requestSize) { this.requestSize = requestSize; }
    public Integer getResponseStatus() { return responseStatus; }
    public void setResponseStatus(Integer responseStatus) { this.responseStatus = responseStatus; }
    public String getResponseStatusText() { return responseStatusText; }
    public void setResponseStatusText(String responseStatusText) { this.responseStatusText = responseStatusText; }
    public String getResponseContentType() { return responseContentType; }
    public void setResponseContentType(String responseContentType) { this.responseContentType = responseContentType; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public Long getResponseSize() { return responseSize; }
    public void setResponseSize(Long responseSize) { this.responseSize = responseSize; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getSapTransactionId() { return sapTransactionId; }
    public void setSapTransactionId(String sapTransactionId) { this.sapTransactionId = sapTransactionId; }
    public String getBusinessProcess() { return businessProcess; }
    public void setBusinessProcess(String businessProcess) { this.businessProcess = businessProcess; }
    public String getSubProcess() { return subProcess; }
    public void setSubProcess(String subProcess) { this.subProcess = subProcess; }
    public String getBusinessObjectId() { return businessObjectId; }
    public void setBusinessObjectId(String businessObjectId) { this.businessObjectId = businessObjectId; }
    public Long getPointerId() { return pointerId; }
    public void setPointerId(Long pointerId) { this.pointerId = pointerId; }
    public String getCaller() { return caller; }
    public void setCaller(String caller) { this.caller = caller; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getThreadName() { return threadName; }
    public void setThreadName(String threadName) { this.threadName = threadName; }
}
