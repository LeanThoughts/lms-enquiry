package pfs.lms.enquiry.reports.timing;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Timing of a report request, kept as request attributes: {@link ReportTimingFilter} stamps the start, the SAP client
 * adds the time spent waiting for SAP ({@link #recordSap}) and {@link ReportTimingAdvice} returns both as response
 * headers, so every report (current and future) shows its record count and run time without extra code.
 */
public final class ReportTiming {

    static final String START = ReportTiming.class.getName() + ".start";
    static final String SAP_NANOS = ReportTiming.class.getName() + ".sapNanos";
    static final String SAP_CALLS = ReportTiming.class.getName() + ".sapCalls";

    /** Total time on the server, in milliseconds. */
    public static final String SERVER_HEADER = "X-Report-Server-Ms";
    /** Time spent in the SAP gateway calls, in milliseconds. */
    public static final String SAP_HEADER = "X-Report-Sap-Ms";
    /** Number of SAP gateway calls. */
    public static final String SAP_CALLS_HEADER = "X-Report-Sap-Calls";

    private ReportTiming() {
    }

    /** Adds one SAP call of the given duration to the current request (no-op outside a request). */
    public static void recordSap(long nanos) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        Object total = attributes.getAttribute(SAP_NANOS, RequestAttributes.SCOPE_REQUEST);
        Object calls = attributes.getAttribute(SAP_CALLS, RequestAttributes.SCOPE_REQUEST);
        attributes.setAttribute(SAP_NANOS, (total instanceof Long ? (Long) total : 0L) + nanos, RequestAttributes.SCOPE_REQUEST);
        attributes.setAttribute(SAP_CALLS, (calls instanceof Integer ? (Integer) calls : 0) + 1, RequestAttributes.SCOPE_REQUEST);
    }

    static long millis(long nanos) {
        return Math.round(nanos / 1_000_000d);
    }
}
