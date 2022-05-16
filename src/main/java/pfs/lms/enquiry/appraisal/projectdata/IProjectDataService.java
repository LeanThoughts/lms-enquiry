package pfs.lms.enquiry.appraisal.projectdata;

import java.util.UUID;

public interface IProjectDataService {

    ProjectData getProjectData(UUID loanAppraisalId);

    ProjectData createProjectData(ProjectDataResource projectDataResource, String username);

    ProjectData updateProjectData(ProjectDataResource projectDataResource, String username) throws CloneNotSupportedException;
}
