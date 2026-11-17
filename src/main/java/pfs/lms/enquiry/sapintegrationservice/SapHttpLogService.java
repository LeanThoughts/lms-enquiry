package pfs.lms.enquiry.sapintegrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Writes {@link SapHttpLog} rows and keeps the table within its retention. Settings:
 * <ul>
 * <li>{@code sap.http-log.enabled} (default true): switch the log off without touching the callers;</li>
 * <li>{@code sap.http-log.max-body-length} (default 65536): request / response bodies are cut at this many characters;</li>
 * <li>{@code sap.http-log.retention-days} (default 90): rows older than this are deleted nightly; 0 keeps everything.</li>
 * </ul>
 * A row is written in its own transaction so that it survives a rollback of the caller's transaction (the call to
 * SAP happened whether or not the portal transaction commits), and a failure to write it never fails the call.
 */
@Service
public class SapHttpLogService {

    private static final Logger log = LoggerFactory.getLogger(SapHttpLogService.class);

    private final SapHttpLogRepository repository;
    private final boolean enabled;
    private final int maxBodyLength;
    private final int retentionDays;

    public SapHttpLogService(SapHttpLogRepository repository,
                             @Value("${sap.http-log.enabled:true}") boolean enabled,
                             @Value("${sap.http-log.max-body-length:65536}") int maxBodyLength,
                             @Value("${sap.http-log.retention-days:90}") int retentionDays) {
        this.repository = repository;
        this.enabled = enabled;
        this.maxBodyLength = Math.max(256, maxBodyLength);
        this.retentionDays = Math.max(0, retentionDays);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getMaxBodyLength() {
        return maxBodyLength;
    }

    public int getRetentionDays() {
        return retentionDays;
    }

    /** Stores one call; never throws. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(SapHttpLog entry) {
        try {
            if (entry.getRequestedAt() == null) {
                entry.setRequestedAt(LocalDateTime.now());
            }
            if (entry.getHttpMethod() == null) {
                entry.setHttpMethod("?");
            }
            if (entry.getUrl() == null) {
                entry.setUrl("");
            }
            repository.save(entry);
            log.info("SAP HTTP log #{}: {} {} -> {}{}", entry.getId(), entry.getHttpMethod(), entry.getUrl(),
                    entry.getResponseStatus(), entry.isSuccess() ? "" : " (" + entry.getErrorMessage() + ")");
        } catch (RuntimeException ex) {
            log.error("SAP HTTP log: the call {} {} could not be recorded: {}", entry.getHttpMethod(), entry.getUrl(),
                    ex.getMessage(), ex);
        }
    }

    /** Deletes rows older than the given number of days; returns how many. */
    @Transactional
    public int purgeOlderThan(int days) {
        int removed = repository.deleteByRequestedAtBefore(LocalDateTime.now().minusDays(Math.max(0, days)));
        log.info("SAP HTTP log: {} rows older than {} days deleted", removed, days);
        return removed;
    }

    /** Nightly retention (02:30 server time), when {@code sap.http-log.retention-days} is set. */
    @Scheduled(cron = "${sap.http-log.purge-cron:0 30 2 * * *}")
    public void purgeExpired() {
        if (retentionDays > 0) {
            try {
                purgeOlderThan(retentionDays);
            } catch (RuntimeException ex) {
                log.error("SAP HTTP log: retention purge failed: {}", ex.getMessage());
            }
        }
    }
}
