package pfs.lms.enquiry.sapintegrationservice;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Detail;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Filter;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.PurgeResult;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.ReportPage;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * REST API of the Administration report "SAP Integration Log", under /api from {@link ApiController}:
 * <ul>
 * <li>{@code GET /administration/sap-http-log} — paged, filtered list with totals;</li>
 * <li>{@code GET /administration/sap-http-log/{id}} — one call with request and response;</li>
 * <li>{@code GET /administration/sap-http-log/export} — CSV of the filtered calls;</li>
 * <li>{@code DELETE /administration/sap-http-log?olderThanDays=n} — purge (role-gated).</li>
 * </ul>
 */
@ApiController
public class SapHttpLogController {

    private static final String BASE = "/administration/sap-http-log";

    private final SapHttpLogReportService service;

    public SapHttpLogController(SapHttpLogReportService service) {
        this.service = service;
    }

    @GetMapping(BASE)
    public ResponseEntity<ReportPage> getReport(@RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String from,
                                                @RequestParam(required = false) String to,
                                                @RequestParam(required = false) String method,
                                                @RequestParam(required = false) String outcome,
                                                @RequestParam(required = false) String businessProcess,
                                                @RequestParam(required = false) String serviceName,
                                                @RequestParam(required = false) String search,
                                                HttpServletRequest request) {
        Filter filter = filter(from, to, method, outcome, businessProcess, serviceName, search);
        return ResponseEntity.ok(service.getReport(filter, page, size, signedInUser(request)));
    }

    @GetMapping(BASE + "/{id}")
    public ResponseEntity<Detail> getDetail(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(service.getDetail(id, signedInUser(request)));
    }

    @GetMapping(BASE + "/export")
    public ResponseEntity<Resource> export(@RequestParam(required = false) String from,
                                           @RequestParam(required = false) String to,
                                           @RequestParam(required = false) String method,
                                           @RequestParam(required = false) String outcome,
                                           @RequestParam(required = false) String businessProcess,
                                           @RequestParam(required = false) String serviceName,
                                           @RequestParam(required = false) String search,
                                           HttpServletRequest request) {
        Filter filter = filter(from, to, method, outcome, businessProcess, serviceName, search);
        byte[] bytes = service.toCsv(filter, signedInUser(request)).getBytes(StandardCharsets.UTF_8);
        String fileName = "sap-integration-log-" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(new ByteArrayResource(bytes));
    }

    @DeleteMapping(BASE)
    public ResponseEntity<PurgeResult> purge(@RequestParam(required = false) Integer olderThanDays,
                                             HttpServletRequest request) {
        return ResponseEntity.ok(service.purge(olderThanDays, signedInUser(request)));
    }

    private static Filter filter(String from, String to, String method, String outcome, String businessProcess,
                                 String serviceName, String search) {
        Filter filter = new Filter();
        filter.from = from;
        filter.to = to;
        filter.method = method;
        filter.outcome = outcome;
        filter.businessProcess = businessProcess;
        filter.serviceName = serviceName;
        filter.search = search;
        return filter;
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
