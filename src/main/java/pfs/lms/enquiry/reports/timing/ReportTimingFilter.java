package pfs.lms.enquiry.reports.timing;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Stamps the start time of every report request ({@code …/reports/…}) for {@link ReportTimingAdvice}. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReportTimingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri == null || !uri.contains("/reports/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        request.setAttribute(ReportTiming.START, System.nanoTime());
        chain.doFilter(request, response);
    }
}
