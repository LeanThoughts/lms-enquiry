package pfs.lms.enquiry.monitoring.projectmonitoring;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectMonitoringDataItemRepository extends JpaRepository<ProjectMonitoringDataItem, String> {
}
