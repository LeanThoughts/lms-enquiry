package pfs.lms.enquiry.reports.timing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReportTimingTest {

    @AfterEach
    void reset() {
        RequestContextHolder.resetRequestAttributes();
    }

    /** A request with a URI and attributes (all that the timing code uses). */
    private static HttpServletRequest request(String uri) {
        Map<String, Object> attributes = new HashMap<>();
        return (HttpServletRequest) Proxy.newProxyInstance(ReportTimingTest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "getRequestURI": return uri;
                        case "getAttribute": return attributes.get((String) args[0]);
                        case "setAttribute": attributes.put((String) args[0], args[1]); return null;
                        case "removeAttribute": attributes.remove((String) args[0]); return null;
                        case "hashCode": return System.identityHashCode(proxy);
                        case "equals": return proxy == args[0];
                        default: return null;
                    }
                });
    }

    @Test
    void filterStampsReportRequestsOnly() throws Exception {
        ReportTimingFilter filter = new ReportTimingFilter();
        assertThat(filter.shouldNotFilter(request("/enquiry/api/reports/portfolio-debt-listing"))).isFalse();
        assertThat(filter.shouldNotFilter(request("/enquiry/api/loanContracts"))).isTrue();

        HttpServletRequest report = request("/enquiry/api/reports/kyc");
        boolean[] chained = {false};
        filter.doFilterInternal(report, null, (req, res) -> chained[0] = true);
        assertThat(report.getAttribute(ReportTiming.START)).isInstanceOf(Long.class);
        assertThat(chained[0]).isTrue();
    }

    @Test
    void headersCarryServerAndSapTime() {
        HttpServletRequest request = request("/api/reports/kyc");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        long start = 1_000_000_000L;
        request.setAttribute(ReportTiming.START, start);
        ReportTiming.recordSap(1_200_000_000L);   // 1200 ms
        ReportTiming.recordSap(300_000_000L);     //  300 ms

        HttpHeaders headers = new HttpHeaders();
        ReportTimingAdvice.addHeaders(request, headers, start + 1_750_000_000L);

        assertThat(headers.getFirst(ReportTiming.SERVER_HEADER)).isEqualTo("1750");
        assertThat(headers.getFirst(ReportTiming.SAP_HEADER)).isEqualTo("1500");
        assertThat(headers.getFirst(ReportTiming.SAP_CALLS_HEADER)).isEqualTo("2");
        assertThat(headers.getFirst("Server-Timing")).contains("sap;").contains("dur=1500").contains("dur=1750");
    }

    @Test
    void noSapCallNoSapHeader_andNoStampNoHeaders() {
        HttpServletRequest request = request("/api/reports/kyc/export");
        HttpHeaders headers = new HttpHeaders();
        ReportTimingAdvice.addHeaders(request, headers, System.nanoTime());
        assertThat(headers).isEmpty();

        request.setAttribute(ReportTiming.START, 0L);
        ReportTimingAdvice.addHeaders(request, headers, 25_000_000L);
        assertThat(headers.getFirst(ReportTiming.SERVER_HEADER)).isEqualTo("25");
        assertThat(headers.containsKey(ReportTiming.SAP_HEADER)).isFalse();
    }

    @Test
    void recordSapOutsideARequestIsIgnored() {
        ReportTiming.recordSap(5_000_000L);
        assertThat(RequestContextHolder.getRequestAttributes()).isNull();
    }
}
