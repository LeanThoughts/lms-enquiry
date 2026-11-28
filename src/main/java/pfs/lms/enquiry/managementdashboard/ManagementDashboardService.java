package pfs.lms.enquiry.managementdashboard;

import org.springframework.stereotype.Service;
import pfs.lms.enquiry.managementdashboard.ManagementDashboardDtos.Dashboard;

import java.time.LocalDate;

/**
 * The Management Dashboard: reads the SAP snapshot for a run date through {@link ManagementDashboardSapClient} and
 * returns it as the portal's {@link Dashboard}. Nothing is stored in the portal; every call goes to SAP.
 */
@Service
public class ManagementDashboardService {

    private final ManagementDashboardSapClient sapClient;

    public ManagementDashboardService(ManagementDashboardSapClient sapClient) {
        this.sapClient = sapClient;
    }

    /**
     * The dashboard of the run date (today when null) with amounts in Rs crore (default) or rupees.
     */
    public Dashboard get(LocalDate runDate, Boolean amountInCrore) {
        LocalDate date = runDate == null ? LocalDate.now() : runDate;
        boolean inCrore = amountInCrore == null || amountInCrore;
        Dashboard dashboard = ManagementDashboardMapper.map(sapClient.read(date, inCrore));
        // The keys as requested, should SAP echo them differently (e.g. the date without time zone)
        if (dashboard.runDate == null) {
            dashboard.runDate = date;
        }
        dashboard.amountInCrore = inCrore;
        dashboard.source = sapClient.url(date, inCrore);
        return dashboard;
    }
}
