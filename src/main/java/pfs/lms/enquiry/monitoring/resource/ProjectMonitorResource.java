package pfs.lms.enquiry.monitoring.resource;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ProjectMonitorResource {
    public String loanNumber;
    public String projectName;
    public String downloadDate;
    public List<ProjectMonitorItemResource> projectMonitorItemResourceList;

    public ProjectMonitorResource() {
        List<ProjectMonitorItemResource> projectMonitorItemResourceList = new ArrayList<>();
        this.projectMonitorItemResourceList = projectMonitorItemResourceList;
    }

    public ProjectMonitorItemResource addItem(ProjectMonitorItemResource projectMonitorItemResource){
        this.projectMonitorItemResourceList.add(projectMonitorItemResource);
        return projectMonitorItemResource;
    }

}
