package pfs.lms.enquiry.sapintegrationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.domain.User;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Count;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Detail;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Filter;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.PurgeResult;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.ReportPage;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Row;
import pfs.lms.enquiry.sapintegrationservice.SapHttpLogDtos.Summary;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * The Administration report "SAP Integration Log": the calls in {@link SapHttpLog}, filtered, summarised, paged,
 * with the full request/response of one call on demand and a CSV export. Access is limited to the roles in
 * {@code administration.sap-http-log.roles} (comma separated; empty = every signed-in user); purging the log to
 * {@code administration.sap-http-log.purge-roles} (empty = the same roles as viewing).
 */
@Service
public class SapHttpLogReportService {

    static final int DEFAULT_PAGE_SIZE = 25;
    static final int MAX_PAGE_SIZE = 200;
    private static final int MAX_EXPORT_ROWS = 10_000;
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final SapHttpLogRepository repository;
    private final SapHttpLogService logService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final List<String> viewRoles;
    private final List<String> purgeRoles;

    public SapHttpLogReportService(SapHttpLogRepository repository, SapHttpLogService logService,
                                   UserRepository userRepository, EntityManager entityManager,
                                   @Value("${administration.sap-http-log.roles:}") String viewRoles,
                                   @Value("${administration.sap-http-log.purge-roles:}") String purgeRoles) {
        this.repository = repository;
        this.logService = logService;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.viewRoles = roles(viewRoles);
        this.purgeRoles = roles(purgeRoles);
    }

    private static List<String> roles(String list) {
        return Arrays.stream((list == null ? "" : list).split(","))
                .map(String::trim).filter(role -> !role.isEmpty()).collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ report

    @Transactional(readOnly = true)
    public ReportPage getReport(Filter filter, Integer page, Integer size, String principalName) {
        ReportPage result = new ReportPage();
        result.setGeneratedOn(Instant.now().toString());
        result.setLoggingEnabled(logService.isEnabled());
        result.setRetentionDays(logService.getRetentionDays());
        User user = user(principalName);
        result.setUserRole(user != null ? user.getRole() : null);
        result.setCanView(canView(user));
        result.setCanPurge(canPurge(user));
        if (!result.isCanView()) {
            return result;
        }
        result.setBusinessProcesses(repository.findDistinctBusinessProcesses());
        result.setServiceNames(repository.findDistinctServiceNames());

        Specification<SapHttpLog> where = specification(filter);
        int pageSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        int pageIndex = page == null || page < 0 ? 0 : page;
        Page<SapHttpLog> found = repository.findAll(where, PageRequest.of(pageIndex, pageSize, sort()));
        if (pageIndex > 0 && found.isEmpty() && found.getTotalElements() > 0) {
            // the page is beyond the end (rows were purged meanwhile): give the last page
            pageIndex = (int) ((found.getTotalElements() - 1) / pageSize);
            found = repository.findAll(where, PageRequest.of(pageIndex, pageSize, sort()));
        }
        result.setPage(pageIndex);
        result.setSize(pageSize);
        result.setTotal(found.getTotalElements());
        result.setRows(found.getContent().stream().map(SapHttpLogReportService::row).collect(Collectors.toList()));
        result.setSummary(summarize(slim(where)));
        return result;
    }

    /** One call with headers and bodies; 404 when unknown. */
    @Transactional(readOnly = true)
    public Detail getDetail(Long id, String principalName) {
        requireView(principalName);
        SapHttpLog entry = repository.findById(id)
                .orElseThrow(() -> new LmsException("SAP call " + id + " is not in the log.", HttpStatus.NOT_FOUND));
        Detail detail = new Detail();
        fill(detail, entry);
        detail.setHost(entry.getHost());
        detail.setRequestHeaders(entry.getRequestHeaders());
        detail.setRequestContentType(entry.getRequestContentType());
        detail.setRequestBody(entry.getRequestBody());
        detail.setResponseContentType(entry.getResponseContentType());
        detail.setResponseBody(entry.getResponseBody());
        detail.setThreadName(entry.getThreadName());
        return detail;
    }

    /** The filtered calls as CSV (newest first, at most {@value #MAX_EXPORT_ROWS}). */
    @Transactional(readOnly = true)
    public String toCsv(Filter filter, String principalName) {
        requireView(principalName);
        List<SapHttpLog> rows = repository.findAll(specification(filter), PageRequest.of(0, MAX_EXPORT_ROWS, sort()))
                .getContent();
        String[] headers = {"Id", "Requested At", "Method", "URL", "Service", "Entity Set", "HTTP Status", "Outcome",
                "Duration (ms)", "Error", "SAP Transaction Id", "Business Process", "Sub Process",
                "Business Object Id", "Pointer Id", "Caller", "User", "Request Size", "Response Size",
                "Request Body", "Response Body"};
        StringBuilder csv = new StringBuilder(String.join(",", headers)).append("\r\n");
        for (SapHttpLog r : rows) {
            csv.append(String.join(",",
                    csv(r.getId()), csv(timestamp(r.getRequestedAt())), csv(r.getHttpMethod()), csv(r.getUrl()),
                    csv(r.getServiceName()), csv(r.getEntitySet()), csv(r.getResponseStatus()),
                    csv(r.isSuccess() ? "Success" : "Failed"), csv(r.getDurationMs()), csv(r.getErrorMessage()),
                    csv(r.getSapTransactionId()), csv(r.getBusinessProcess()), csv(r.getSubProcess()),
                    csv(r.getBusinessObjectId()), csv(r.getPointerId()), csv(r.getCaller()), csv(r.getUserName()),
                    csv(r.getRequestSize()), csv(r.getResponseSize()), csv(r.getRequestBody()),
                    csv(r.getResponseBody()))).append("\r\n");
        }
        return csv.toString();
    }

    /** Deletes calls older than the given days; 403 unless the user may purge. */
    public PurgeResult purge(Integer olderThanDays, String principalName) {
        User user = user(principalName);
        if (!canPurge(user)) {
            throw new LmsException("No access to purge the SAP integration log.", HttpStatus.FORBIDDEN);
        }
        int days = olderThanDays == null || olderThanDays < 0 ? 0 : olderThanDays;
        return new PurgeResult(logService.purgeOlderThan(days), days);
    }

    // ------------------------------------------------------------------------------------------------ query

    static Specification<SapHttpLog> specification(Filter f) {
        return (root, query, cb) -> {
            List<Predicate> and = new ArrayList<>();
            if (f == null) {
                return cb.and();
            }
            LocalDate from = date(f.from);
            if (from != null) {
                and.add(cb.greaterThanOrEqualTo(root.get("requestedAt"), from.atStartOfDay()));
            }
            LocalDate to = date(f.to);
            if (to != null) {
                and.add(cb.lessThan(root.get("requestedAt"), to.plusDays(1).atStartOfDay()));
            }
            if (notBlank(f.method)) {
                and.add(cb.equal(cb.upper(root.get("httpMethod")), f.method.trim().toUpperCase(Locale.ROOT)));
            }
            if ("success".equalsIgnoreCase(f.outcome)) {
                and.add(cb.isTrue(root.get("success")));
            } else if ("failed".equalsIgnoreCase(f.outcome)) {
                and.add(cb.isFalse(root.get("success")));
            }
            if (notBlank(f.businessProcess)) {
                and.add(cb.equal(root.get("businessProcess"), f.businessProcess.trim()));
            }
            if (notBlank(f.serviceName)) {
                and.add(cb.equal(root.get("serviceName"), f.serviceName.trim()));
            }
            if (notBlank(f.search)) {
                String like = "%" + f.search.trim().toLowerCase(Locale.ROOT) + "%";
                and.add(cb.or(
                        cb.like(cb.lower(root.get("url")), like),
                        cb.like(cb.lower(root.get("entitySet")), like),
                        cb.like(cb.lower(root.get("errorMessage")), like),
                        cb.like(cb.lower(root.get("businessObjectId")), like),
                        cb.like(cb.lower(root.get("subProcess")), like),
                        cb.like(cb.lower(root.get("caller")), like),
                        cb.like(cb.lower(root.get("userName")), like),
                        cb.like(cb.lower(root.get("sapTransactionId")), like),
                        cb.like(cb.lower(root.get("requestBody")), like),
                        cb.like(cb.lower(root.get("responseBody")), like)));
            }
            return cb.and(and.toArray(new Predicate[0]));
        };
    }

    private static Sort sort() {
        return Sort.by(Sort.Order.desc("requestedAt"), Sort.Order.desc("id"));
    }

    /**
     * The filtered calls with only the columns the summary needs (no bodies / headers), so that the totals do not
     * load the LONGTEXT columns of every row.
     */
    private List<SapHttpLog> slim(Specification<SapHttpLog> where) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<SapHttpLog> root = query.from(SapHttpLog.class);
        query.multiselect(root.get("httpMethod"), root.get("responseStatus"), root.get("entitySet"),
                root.get("success"), root.get("durationMs"), root.get("requestedAt"));
        Predicate predicate = where.toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }
        List<SapHttpLog> rows = new ArrayList<>();
        for (Tuple t : entityManager.createQuery(query).getResultList()) {
            SapHttpLog row = new SapHttpLog();
            row.setHttpMethod((String) t.get(0));
            row.setResponseStatus((Integer) t.get(1));
            row.setEntitySet((String) t.get(2));
            row.setSuccess(Boolean.TRUE.equals(t.get(3)));
            row.setDurationMs((Long) t.get(4));
            row.setRequestedAt((LocalDateTime) t.get(5));
            rows.add(row);
        }
        return rows;
    }

    static Summary summarize(List<SapHttpLog> rows) {
        Summary summary = new Summary();
        summary.setTotal(rows.size());
        summary.setFailed(rows.stream().filter(r -> !r.isSuccess()).count());
        summary.setSucceeded(summary.getTotal() - summary.getFailed());
        summary.setAverageDurationMs(Math.round(rows.stream().filter(r -> r.getDurationMs() != null)
                .mapToLong(SapHttpLog::getDurationMs).average().orElse(0)));
        summary.setMaxDurationMs(rows.stream().filter(r -> r.getDurationMs() != null)
                .mapToLong(SapHttpLog::getDurationMs).max().orElse(0));
        rows.stream().map(SapHttpLog::getRequestedAt).filter(t -> t != null).max(Comparator.naturalOrder())
                .ifPresent(t -> summary.setLastCallAt(timestamp(t)));
        rows.stream().filter(r -> !r.isSuccess()).map(SapHttpLog::getRequestedAt).filter(t -> t != null)
                .max(Comparator.naturalOrder()).ifPresent(t -> summary.setLastFailureAt(timestamp(t)));
        summary.setByMethod(counts(rows.stream().map(r -> r.getHttpMethod() == null ? "?" : r.getHttpMethod())));
        summary.setByStatus(counts(rows.stream().map(r -> r.getResponseStatus() == null || r.getResponseStatus() == 0
                ? "No response" : String.valueOf(r.getResponseStatus()))));
        summary.setByEntitySet(counts(rows.stream().map(r -> r.getEntitySet() == null ? "(other)" : r.getEntitySet()))
                .stream().sorted(Comparator.comparingLong(Count::getCount).reversed().thenComparing(Count::getKey))
                .limit(12).collect(Collectors.toList()));
        return summary;
    }

    private static List<Count> counts(java.util.stream.Stream<String> keys) {
        Map<String, Long> counts = keys.collect(Collectors.groupingBy(k -> k, TreeMap::new, Collectors.counting()));
        return counts.entrySet().stream().map(e -> new Count(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------ mapping

    static Row row(SapHttpLog entry) {
        Row row = new Row();
        fill(row, entry);
        return row;
    }

    private static void fill(Row row, SapHttpLog e) {
        row.setId(e.getId());
        row.setRequestedAt(timestamp(e.getRequestedAt()));
        row.setDurationMs(e.getDurationMs());
        row.setHttpMethod(e.getHttpMethod());
        row.setUrl(e.getUrl());
        row.setServiceName(e.getServiceName());
        row.setEntitySet(e.getEntitySet());
        row.setResponseStatus(e.getResponseStatus());
        row.setResponseStatusText(e.getResponseStatusText());
        row.setSuccess(e.isSuccess());
        row.setErrorMessage(e.getErrorMessage());
        row.setSapTransactionId(e.getSapTransactionId());
        row.setBusinessProcess(e.getBusinessProcess());
        row.setSubProcess(e.getSubProcess());
        row.setBusinessObjectId(e.getBusinessObjectId());
        row.setPointerId(e.getPointerId());
        row.setCaller(e.getCaller());
        row.setUserName(e.getUserName());
        row.setRequestSize(e.getRequestSize());
        row.setResponseSize(e.getResponseSize());
    }

    // ------------------------------------------------------------------------------------------------ access

    private User user(String principalName) {
        return principalName == null ? null : userRepository.findByEmail(principalName);
    }

    private void requireView(String principalName) {
        if (!canView(user(principalName))) {
            throw new LmsException("No access to the SAP integration log.", HttpStatus.FORBIDDEN);
        }
    }

    private boolean canView(User user) {
        return allowed(user, viewRoles);
    }

    private boolean canPurge(User user) {
        return allowed(user, purgeRoles.isEmpty() ? viewRoles : purgeRoles);
    }

    private static boolean allowed(User user, List<String> roles) {
        if (roles.isEmpty()) {
            return true;
        }
        return user != null && user.isStatus() && user.getRole() != null && roles.contains(user.getRole().trim());
    }

    // ------------------------------------------------------------------------------------------------ helpers

    static String timestamp(LocalDateTime time) {
        return time == null ? null : TIMESTAMP.format(time);
    }

    private static LocalDate date(String iso) {
        if (!notBlank(iso)) {
            return null;
        }
        try {
            return LocalDate.parse(iso.trim());
        } catch (DateTimeParseException ex) {
            throw new LmsException("Invalid date " + iso + " (expected yyyy-MM-dd).", HttpStatus.BAD_REQUEST);
        }
    }

    private static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String csv(Object value) {
        if (value == null) {
            return "";
        }
        String text = value.toString();
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }
}
