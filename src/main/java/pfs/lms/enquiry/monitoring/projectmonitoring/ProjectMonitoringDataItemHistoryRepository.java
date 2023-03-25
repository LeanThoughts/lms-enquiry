package pfs.lms.enquiry.monitoring.projectmonitoring;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectMonitoringDataItemHistoryRepository extends JpaRepository<ProjectMonitoringDataItemHistory, String> {
    List<ProjectMonitoringDataItemHistory>
        findByProjectMonitoringDataIdAndParticularsOrderByDateOfEntryDesc(String projectMonitoringDataId, String particulars);
    List<ProjectMonitoringDataItemHistory>
    findByProjectMonitoringDataIdOrderByDateOfEntryDesc(String projectMonitoringDataId );

}
