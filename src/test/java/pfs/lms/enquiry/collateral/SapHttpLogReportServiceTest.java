package pfs.lms.enquiry.collateral;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLog;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogContext;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Detail;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Filter;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.ReportPage;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Row;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogReportService;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogRepository;
import pfs.lms.enquiry.sapintegrationservice.SapRestTemplates;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * SAP Integration Log (Administration): calls made through a logged RestTemplate are stored with request, response
 * and context; the report filters, summarises, pages and exports them; the detail carries the bodies; purge deletes
 * by age and is role-gated.
 */
@DisplayName("Administration: SAP integration log")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SapHttpLogReportServiceTest extends CollateralTestSupport {

    private SapHttpLogReportService report;
    private SapHttpLogRepository repository;
    private HttpServer sap;
    private String base;

    @Override
    protected Map<String, Object> properties() {
        return Collections.singletonMap("administration.sap-http-log.purge-roles", "ZLM023");
    }

    @BeforeAll
    void setUp() throws Exception {
        report = bean(SapHttpLogReportService.class);
        repository = bean(SapHttpLogRepository.class);
        sap = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        sap.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            byte[] body;
            int status;
            if (path.contains("CoverageSet")) {
                body = ("{\"error\":{\"code\":\"X\",\"message\":{\"lang\":\"en\",\"value\":\"Invalid key predicate\"},"
                        + "\"innererror\":{\"transactionid\":\"TX1\"}}}").getBytes(StandardCharsets.UTF_8);
                status = 400;
            } else {
                body = "{\"d\":{\"ZidNo\":\"0000000912\"}}".getBytes(StandardCharsets.UTF_8);
                status = 201;
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        sap.start();
        base = "http://127.0.0.1:" + sap.getAddress().getPort() + "/sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/";
    }

    @Test
    @Order(1)
    @DisplayName("every call through a logged RestTemplate is stored: method, URL, masked headers, bodies, outcome")
    void callsAreLogged() {
        RestTemplate restTemplate = SapRestTemplates.logged();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpzZWNyZXQ=");
        headers.set("Content-Type", "application/json");

        try (SapHttpLogContext.Scope ignored = SapHttpLogContext.open("Collateral Management", "Collateral Item",
                "item-1", 7L)) {
            restTemplate.exchange(base + "ChecklistHeaderSet?sap-client=300", HttpMethod.POST,
                    new HttpEntity<>("{\"ZidNo\":\"0000000912\",\"DocTitle\":\"Deed\"}", headers), String.class);
        }
        assertThatThrownBy(() -> restTemplate.exchange(base + "CoverageSet(ZidNo='1')?sap-client=300", HttpMethod.PUT,
                new HttpEntity<>("{\"a\":1}", headers), String.class)).isInstanceOf(HttpClientErrorException.class);

        assertThat(repository.count()).isEqualTo(2);
        SapHttpLog post = repository.findAll().stream().filter(l -> "POST".equals(l.getHttpMethod())).findFirst().get();
        assertThat(post.isSuccess()).isTrue();
        assertThat(post.getResponseStatus()).isEqualTo(201);
        assertThat(post.getServiceName()).isEqualTo("ZPFS_LMS_COLLATERAL_SRV");
        assertThat(post.getEntitySet()).isEqualTo("ChecklistHeaderSet");
        assertThat(post.getRequestBody()).contains("\"DocTitle\":\"Deed\"");
        assertThat(post.getResponseBody()).contains("0000000912");
        assertThat(post.getRequestHeaders()).contains("Authorization: Basic ***").doesNotContain("dXNlcjpzZWNyZXQ=");
        assertThat(post.getBusinessProcess()).isEqualTo("Collateral Management");
        assertThat(post.getBusinessObjectId()).isEqualTo("item-1");
        assertThat(post.getPointerId()).isEqualTo(7L);
        assertThat(post.getDurationMs()).isNotNull();

        SapHttpLog put = repository.findAll().stream().filter(l -> "PUT".equals(l.getHttpMethod())).findFirst().get();
        assertThat(put.isSuccess()).isFalse();
        assertThat(put.getResponseStatus()).isEqualTo(400);
        assertThat(put.getErrorMessage()).contains("Invalid key predicate");
        assertThat(put.getSapTransactionId()).isEqualTo("TX1");
        assertThat(put.getBusinessProcess()).isNull();
    }

    @Test
    @Order(2)
    @DisplayName("a call made inside another transaction (a web request) is logged even when that transaction rolls back")
    void loggedInsideOuterTransaction() {
        long before = repository.count();
        RestTemplate restTemplate = SapRestTemplates.logged();
        tx.execute(status -> {
            restTemplate.getForObject(base + "WorkflowStatusSet?$format=json", String.class);
            status.setRollbackOnly();
            return null;
        });
        assertThat(repository.count()).isEqualTo(before + 1);
        SapHttpLog get = repository.findAll().stream().filter(l -> "GET".equals(l.getHttpMethod())).findFirst().get();
        assertThat(get.getEntitySet()).isEqualTo("WorkflowStatusSet");
        assertThat(get.isSuccess()).isTrue();
        repository.delete(get);
    }

    @Test
    @Order(3)
    @DisplayName("the report lists newest first with totals; filters by outcome, method, process, date and text")
    void reportAndFilters() {
        // an old successful GET, for the date filter and the purge
        SapHttpLog old = new SapHttpLog();
        old.setRequestedAt(LocalDateTime.now().minusDays(400));
        old.setHttpMethod("GET");
        old.setUrl("http://sap/sap/opu/odata/sap/ZPFS_LOAN_ENQ_PORTAL_SRV/LoanApplicationSet");
        old.setServiceName("ZPFS_LOAN_ENQ_PORTAL_SRV");
        old.setEntitySet("LoanApplicationSet");
        old.setResponseStatus(200);
        old.setSuccess(true);
        old.setDurationMs(50L);
        repository.save(old);

        ReportPage page = report.getReport(new Filter(), 0, 25, WRITER);
        assertThat(page.isCanView()).isTrue();
        assertThat(page.isCanPurge()).isTrue();          // ZLM023 may purge
        assertThat(page.isLoggingEnabled()).isTrue();
        assertThat(page.getTotal()).isEqualTo(3);
        assertThat(page.getRows()).extracting(Row::getHttpMethod).containsExactly("PUT", "POST", "GET");
        assertThat(page.getSummary().getFailed()).isEqualTo(1);
        assertThat(page.getSummary().getSucceeded()).isEqualTo(2);
        assertThat(page.getSummary().getByStatus()).extracting("key").contains("201", "400", "200");
        assertThat(page.getBusinessProcesses()).containsExactly("Collateral Management");
        assertThat(page.getServiceNames()).containsExactly("ZPFS_LMS_COLLATERAL_SRV", "ZPFS_LOAN_ENQ_PORTAL_SRV");

        Filter failed = new Filter();
        failed.outcome = "failed";
        assertThat(report.getReport(failed, 0, 25, WRITER).getRows()).extracting(Row::getHttpMethod).containsExactly("PUT");

        Filter process = new Filter();
        process.businessProcess = "Collateral Management";
        assertThat(report.getReport(process, 0, 25, WRITER).getTotal()).isEqualTo(1);

        Filter recent = new Filter();
        recent.from = LocalDate.now().minusDays(1).toString();
        recent.to = LocalDate.now().toString();
        assertThat(report.getReport(recent, 0, 25, WRITER).getTotal()).isEqualTo(2);

        Filter text = new Filter();
        text.search = "predicate";
        assertThat(report.getReport(text, 0, 25, WRITER).getRows()).extracting(Row::getHttpMethod).containsExactly("PUT");

        Filter method = new Filter();
        method.method = "get";
        assertThat(report.getReport(method, 0, 25, WRITER).getTotal()).isEqualTo(1);

        // paging: page size 1, second page is the POST; a page beyond the end falls back to the last page
        assertThat(report.getReport(new Filter(), 1, 1, WRITER).getRows()).extracting(Row::getHttpMethod).containsExactly("POST");
        assertThat(report.getReport(new Filter(), 9, 1, WRITER).getPage()).isEqualTo(2);

        // the reader role may view but not purge (purge roles = ZLM023)
        ReportPage reader = report.getReport(new Filter(), 0, 25, READER);
        assertThat(reader.isCanView()).isTrue();
        assertThat(reader.isCanPurge()).isFalse();
    }

    @Test
    @Order(4)
    @DisplayName("the detail carries request and response; the CSV export has one line per call")
    void detailAndCsv() {
        Row failedRow = report.getReport(new Filter(), 0, 25, WRITER).getRows().get(0);
        Detail detail = report.getDetail(failedRow.getId(), WRITER);
        assertThat(detail.getHttpMethod()).isEqualTo("PUT");
        assertThat(detail.getRequestBody()).isEqualTo("{\"a\":1}");
        assertThat(detail.getResponseBody()).contains("Invalid key predicate");
        assertThat(detail.getRequestHeaders()).contains("Authorization: Basic ***");
        assertThatThrownBy(() -> report.getDetail(999_999L, WRITER)).isInstanceOf(LmsException.class);

        String csv = report.toCsv(new Filter(), WRITER);
        assertThat(csv.split("\r\n")).hasSize(4);
        assertThat(csv).startsWith("Id,Requested At,Method,URL,").contains("Invalid key predicate");
    }

    @Test
    @Order(5)
    @DisplayName("purge deletes calls older than the given days and is refused for other roles")
    void purge() {
        assertThatThrownBy(() -> report.purge(30, READER)).isInstanceOf(LmsException.class);
        assertThat(report.purge(365, WRITER).getDeleted()).isEqualTo(1);
        assertThat(repository.count()).isEqualTo(2);
        sap.stop(0);
    }
}
