package pfs.lms.enquiry.reports.variant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.exception.LmsException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Report variants: saving the selection screen of a report under a name, listing the variants a user can use
 * (own and shared) and deleting own variants. Saving under an existing name of the same user overwrites it.
 * <p>
 * The same store keeps the column layouts of the reports (the hidden columns, saved like a variant): a layout is a
 * variant of the report key {@code <report>_layout}, so every report registered in {@link #REPORTS} has layouts too.
 */
@Service
public class ReportVariantService {

    static final Set<String> REPORTS = new HashSet<>(Arrays.asList("workflow-status", "icc-approval", "kyc", "balance-list", "disbursement"));
    static final int MAX_PARAMETERS = 50_000;

    private final ReportVariantRepository repository;
    private final ObjectMapper mapper;

    public ReportVariantService(ReportVariantRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ReportVariant> list(String report, String user) {
        return repository.findVisible(checkReport(report), user);
    }

    /** Creates the variant of the user or overwrites the one of the same name. */
    @Transactional
    public ReportVariant save(String report, String user, String name, String description, boolean shared,
                              boolean defaultVariant, JsonNode parameters) {
        checkReport(report);
        String cleanName = name == null ? "" : name.trim();
        if (cleanName.isEmpty() || cleanName.length() > 40 || cleanName.chars().anyMatch(Character::isISOControl)) {
            throw new LmsException("The variant name is required (up to 40 characters).", HttpStatus.PRECONDITION_FAILED);
        }
        if (description != null && description.trim().length() > 120) {
            throw new LmsException("The description can have up to 120 characters.", HttpStatus.PRECONDITION_FAILED);
        }
        if (parameters == null || !parameters.isObject()) {
            throw new LmsException("The variant has no selection parameters.", HttpStatus.PRECONDITION_FAILED);
        }
        String json = parameters.toString();
        if (json.length() > MAX_PARAMETERS) {
            throw new LmsException("The selection is too large to be saved as a variant.", HttpStatus.PRECONDITION_FAILED);
        }
        LocalDateTime now = LocalDateTime.now();
        ReportVariant variant = repository.findByReportAndOwnerNameAndName(report, user, cleanName).orElseGet(() -> {
            ReportVariant v = new ReportVariant();
            v.setReport(report);
            v.setOwnerName(user);
            v.setName(cleanName);
            v.setCreatedOn(now);
            return v;
        });
        variant.setDescription(description == null || description.trim().isEmpty() ? null : description.trim());
        variant.setShared(shared);
        variant.setDefaultVariant(defaultVariant);
        variant.setParameters(json);
        variant.setChangedOn(now);
        if (defaultVariant) {
            for (ReportVariant other : repository.findByReportAndOwnerNameAndDefaultVariantTrue(report, user)) {
                if (!other.getId().equals(variant.getId())) {
                    other.setDefaultVariant(false);
                    repository.save(other);
                }
            }
        }
        return repository.save(variant);
    }

    /** Only the owner can delete a variant. */
    @Transactional
    public void delete(String report, String user, UUID id) {
        checkReport(report);
        ReportVariant variant = repository.findById(id)
                .filter(v -> v.getReport().equals(report))
                .orElseThrow(() -> new LmsException("The variant does not exist.", HttpStatus.NOT_FOUND));
        if (!variant.getOwnerName().equals(user)) {
            throw new LmsException("Only the owner can delete a variant.", HttpStatus.FORBIDDEN);
        }
        repository.delete(variant);
    }

    public JsonNode parameters(ReportVariant variant) {
        try {
            return mapper.readTree(variant.getParameters());
        } catch (Exception ex) {
            return mapper.createObjectNode();
        }
    }

    /** Suffix of the key under which the column layout of a report is stored, e.g. kyc_layout. */
    static final String LAYOUT_SUFFIX = "_layout";

    /** A report key, or the layout key of a report ({@code <report>_layout}); layouts need no registration of their own. */
    private static String checkReport(String report) {
        String base = report != null && report.endsWith(LAYOUT_SUFFIX)
                ? report.substring(0, report.length() - LAYOUT_SUFFIX.length()) : report;
        if (!REPORTS.contains(base)) {
            throw new LmsException("Unknown report " + report + ".", HttpStatus.NOT_FOUND);
        }
        return report;
    }
}
