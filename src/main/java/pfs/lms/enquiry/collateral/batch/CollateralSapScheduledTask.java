package pfs.lms.enquiry.collateral.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled sweep that posts the collateral components of approved checklists to SAP (see
 * {@link CollateralSapIntegrationService}). Runs on {@code collateral.sap.sweep-cron} (default: every 5 seconds); the
 * service is a no-op until {@code sap.collateralServiceUri} is configured. Scheduling is enabled application-wide
 * (@EnableScheduling on SapPfsApplication).
 */
@Component
public class CollateralSapScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(CollateralSapScheduledTask.class);

    private final CollateralSapIntegrationService collateralSapIntegrationService;

    public CollateralSapScheduledTask(CollateralSapIntegrationService collateralSapIntegrationService) {
        this.collateralSapIntegrationService = collateralSapIntegrationService;
    }

    @Scheduled(cron = "${collateral.sap.sweep-cron:*/5 * * * * *}")
    public void sweep() {
        try {
            collateralSapIntegrationService.syncApprovedCollateralsToSAP();
        } catch (Exception ex) {
            log.error("Collateral SAP integration sweep failed: {}", ex.getMessage(), ex);
        }
    }
}
