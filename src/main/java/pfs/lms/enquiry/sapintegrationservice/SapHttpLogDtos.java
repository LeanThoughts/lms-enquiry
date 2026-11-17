package pfs.lms.enquiry.sapintegrationservice;

import java.util.ArrayList;
import java.util.List;

/** JSON shapes of the SAP Integration Log report (Administration). */
public final class SapHttpLogDtos {

    private SapHttpLogDtos() {
    }

    /** Filter of the report; every field optional. */
    public static class Filter {
        public String from;            // ISO date (inclusive)
        public String to;              // ISO date (inclusive)
        public String method;          // GET / POST / PUT / DELETE
        public String outcome;         // success | failed | (all)
        public String businessProcess;
        public String serviceName;
        public String search;          // URL, entity set, body, error, business object id, caller
    }

    /** One call in the list: everything but the bodies and headers (those are in {@link Detail}). */
    public static class Row {
        private Long id;
        private String requestedAt;
        private Long durationMs;
        private String httpMethod;
        private String url;
        private String serviceName;
        private String entitySet;
        private Integer responseStatus;
        private String responseStatusText;
        private boolean success;
        private String errorMessage;
        private String sapTransactionId;
        private String businessProcess;
        private String subProcess;
        private String businessObjectId;
        private Long pointerId;
        private String caller;
        private String userName;
        private Long requestSize;
        private Long responseSize;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getRequestedAt() { return requestedAt; }
        public void setRequestedAt(String requestedAt) { this.requestedAt = requestedAt; }
        public Long getDurationMs() { return durationMs; }
        public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public String getEntitySet() { return entitySet; }
        public void setEntitySet(String entitySet) { this.entitySet = entitySet; }
        public Integer getResponseStatus() { return responseStatus; }
        public void setResponseStatus(Integer responseStatus) { this.responseStatus = responseStatus; }
        public String getResponseStatusText() { return responseStatusText; }
        public void setResponseStatusText(String responseStatusText) { this.responseStatusText = responseStatusText; }
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
        public Long getRequestSize() { return requestSize; }
        public void setRequestSize(Long requestSize) { this.requestSize = requestSize; }
        public Long getResponseSize() { return responseSize; }
        public void setResponseSize(Long responseSize) { this.responseSize = responseSize; }
    }

    /** One call with its headers and bodies. */
    public static class Detail extends Row {
        private String host;
        private String requestHeaders;
        private String requestContentType;
        private String requestBody;
        private String responseContentType;
        private String responseBody;
        private String threadName;

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public String getRequestHeaders() { return requestHeaders; }
        public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
        public String getRequestContentType() { return requestContentType; }
        public void setRequestContentType(String requestContentType) { this.requestContentType = requestContentType; }
        public String getRequestBody() { return requestBody; }
        public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
        public String getResponseContentType() { return responseContentType; }
        public void setResponseContentType(String responseContentType) { this.responseContentType = responseContentType; }
        public String getResponseBody() { return responseBody; }
        public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
        public String getThreadName() { return threadName; }
        public void setThreadName(String threadName) { this.threadName = threadName; }
    }

    /** A key with a count (for breakdowns and filter options). */
    public static class Count {
        private String key;
        private long count;

        public Count() {
        }

        public Count(String key, long count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    /** Totals of the filtered calls. */
    public static class Summary {
        private long total;
        private long failed;
        private long succeeded;
        private long averageDurationMs;
        private long maxDurationMs;
        private String lastCallAt;
        private String lastFailureAt;
        private List<Count> byMethod = new ArrayList<>();
        private List<Count> byStatus = new ArrayList<>();
        private List<Count> byEntitySet = new ArrayList<>();

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getFailed() { return failed; }
        public void setFailed(long failed) { this.failed = failed; }
        public long getSucceeded() { return succeeded; }
        public void setSucceeded(long succeeded) { this.succeeded = succeeded; }
        public long getAverageDurationMs() { return averageDurationMs; }
        public void setAverageDurationMs(long averageDurationMs) { this.averageDurationMs = averageDurationMs; }
        public long getMaxDurationMs() { return maxDurationMs; }
        public void setMaxDurationMs(long maxDurationMs) { this.maxDurationMs = maxDurationMs; }
        public String getLastCallAt() { return lastCallAt; }
        public void setLastCallAt(String lastCallAt) { this.lastCallAt = lastCallAt; }
        public String getLastFailureAt() { return lastFailureAt; }
        public void setLastFailureAt(String lastFailureAt) { this.lastFailureAt = lastFailureAt; }
        public List<Count> getByMethod() { return byMethod; }
        public void setByMethod(List<Count> byMethod) { this.byMethod = byMethod; }
        public List<Count> getByStatus() { return byStatus; }
        public void setByStatus(List<Count> byStatus) { this.byStatus = byStatus; }
        public List<Count> getByEntitySet() { return byEntitySet; }
        public void setByEntitySet(List<Count> byEntitySet) { this.byEntitySet = byEntitySet; }
    }

    /** One page of the report. */
    public static class ReportPage {
        private List<Row> rows = new ArrayList<>();
        private Summary summary = new Summary();
        private int page;
        private int size;
        private long total;
        private List<String> businessProcesses = new ArrayList<>();
        private List<String> serviceNames = new ArrayList<>();
        private String generatedOn;
        private boolean canView;
        private boolean canPurge;
        private boolean loggingEnabled;
        private int retentionDays;
        private String userRole;

        public List<Row> getRows() { return rows; }
        public void setRows(List<Row> rows) { this.rows = rows; }
        public Summary getSummary() { return summary; }
        public void setSummary(Summary summary) { this.summary = summary; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public List<String> getBusinessProcesses() { return businessProcesses; }
        public void setBusinessProcesses(List<String> businessProcesses) { this.businessProcesses = businessProcesses; }
        public List<String> getServiceNames() { return serviceNames; }
        public void setServiceNames(List<String> serviceNames) { this.serviceNames = serviceNames; }
        public String getGeneratedOn() { return generatedOn; }
        public void setGeneratedOn(String generatedOn) { this.generatedOn = generatedOn; }
        public boolean isCanView() { return canView; }
        public void setCanView(boolean canView) { this.canView = canView; }
        public boolean isCanPurge() { return canPurge; }
        public void setCanPurge(boolean canPurge) { this.canPurge = canPurge; }
        public boolean isLoggingEnabled() { return loggingEnabled; }
        public void setLoggingEnabled(boolean loggingEnabled) { this.loggingEnabled = loggingEnabled; }
        public int getRetentionDays() { return retentionDays; }
        public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }

    /** Result of a purge. */
    public static class PurgeResult {
        private int deleted;
        private int olderThanDays;

        public PurgeResult() {
        }

        public PurgeResult(int deleted, int olderThanDays) {
            this.deleted = deleted;
            this.olderThanDays = olderThanDays;
        }

        public int getDeleted() { return deleted; }
        public void setDeleted(int deleted) { this.deleted = deleted; }
        public int getOlderThanDays() { return olderThanDays; }
        public void setOlderThanDays(int olderThanDays) { this.olderThanDays = olderThanDays; }
    }
}
