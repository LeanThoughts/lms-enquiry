package pfs.lms.enquiry.reports.timing;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Adds the run time of a report request to its response, just before the body is written:
 * {@code X-Report-Server-Ms} (time on the server), {@code X-Report-Sap-Ms} / {@code X-Report-Sap-Calls} (time in and
 * number of SAP gateway calls) and the standard {@code Server-Timing} header (shown in the browser's developer tools).
 * Applies to all controllers under {@code pfs.lms.enquiry.reports}, so new reports get it automatically.
 */
@ControllerAdvice(basePackages = "pfs.lms.enquiry.reports")
public class ReportTimingAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (request instanceof ServletServerHttpRequest) {
            addHeaders(((ServletServerHttpRequest) request).getServletRequest(), response.getHeaders(), System.nanoTime());
        }
        return body;
    }

    static void addHeaders(HttpServletRequest request, HttpHeaders headers, long now) {
        Object start = request.getAttribute(ReportTiming.START);
        if (!(start instanceof Long)) {
            return;
        }
        long serverMs = ReportTiming.millis(now - (Long) start);
        Object sapNanos = request.getAttribute(ReportTiming.SAP_NANOS);
        Object sapCalls = request.getAttribute(ReportTiming.SAP_CALLS);
        headers.set(ReportTiming.SERVER_HEADER, String.valueOf(serverMs));
        String serverTiming = "app;desc=\"Portal\";dur=" + serverMs;
        if (sapNanos instanceof Long) {
            long sapMs = ReportTiming.millis((Long) sapNanos);
            headers.set(ReportTiming.SAP_HEADER, String.valueOf(sapMs));
            headers.set(ReportTiming.SAP_CALLS_HEADER, String.valueOf(sapCalls instanceof Integer ? sapCalls : 1));
            serverTiming = "sap;desc=\"SAP gateway\";dur=" + sapMs + ", " + serverTiming;
        }
        headers.set("Server-Timing", serverTiming);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, String.join(", ", ReportTiming.SERVER_HEADER,
                ReportTiming.SAP_HEADER, ReportTiming.SAP_CALLS_HEADER, "Server-Timing"));
    }
}
