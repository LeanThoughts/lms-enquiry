package pfs.lms.enquiry.monitoring.projectmonitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ProjectMonitoringDataItemController {

    private final ProjectMonitoringDataItemService projectMonitoringDataItemService;

    @PutMapping("/projectMonitoringDataItems/{projectMonitoringDataItemId}")
    public ResponseEntity<ProjectMonitoringDataItem> updateProjectMonitoringDataItem(
            @PathVariable UUID projectMonitoringDataItemId,
            @RequestBody ProjectMonitoringDataItemResource projectMonitoringDataItemResource, HttpServletRequest request) throws CloneNotSupportedException {

        ProjectMonitoringDataItem projectMonitoringDataItem = projectMonitoringDataItemService
                .updateProjectMonitoringDataItem(projectMonitoringDataItemId, projectMonitoringDataItemResource,request);
        return ResponseEntity.ok(projectMonitoringDataItem);
    }
}
