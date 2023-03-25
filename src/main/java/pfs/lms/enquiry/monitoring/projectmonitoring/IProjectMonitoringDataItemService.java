package pfs.lms.enquiry.monitoring.projectmonitoring;

import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.resource.ProjectMonitorResource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface IProjectMonitoringDataItemService {
    ProjectMonitoringDataItem saveProjectMonitoringDataItem(ProjectMonitoringDataItemResource projectMonitoringDataItemResource, HttpServletRequest servletRequest);
    ProjectMonitoringDataItem updateProjectMonitoringDataItem(UUID projectMonitoringDataItemId,
                                                              ProjectMonitoringDataItemResource projectMonitoringDataItemResource, HttpServletRequest servletRequest) throws CloneNotSupportedException;

     ProjectMonitorResource  getProjectMonitoringDataItemHistory(LoanMonitor loanMonitor);

}
