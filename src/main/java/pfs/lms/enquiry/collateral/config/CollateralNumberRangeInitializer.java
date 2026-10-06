package pfs.lms.enquiry.collateral.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.collateral.service.impl.CollateralNumberRangeService;

/**
 * At startup: creates the Checklist ID number range and numbers collaterals that have no Checklist ID No. yet.
 * Runs after {@link CollateralSchemaUpgrade}. A failure is logged and does not stop the application.
 */
@Component
@Order(10)
public class CollateralNumberRangeInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CollateralNumberRangeInitializer.class);

    private final CollateralNumberRangeService numberRangeService;

    public CollateralNumberRangeInitializer(CollateralNumberRangeService numberRangeService) {
        this.numberRangeService = numberRangeService;
    }

    @Override
    public void run(String... args) {
        try {
            int numbered = numberRangeService.initialize();
            if (numbered > 0) {
                log.info("{} collaterals without Checklist ID No. were numbered.", numbered);
            }
        } catch (RuntimeException ex) {
            log.error("Collateral checklist ID number range could not be initialized", ex);
        }
    }
}
