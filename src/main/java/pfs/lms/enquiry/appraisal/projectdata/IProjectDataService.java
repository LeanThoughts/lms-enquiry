package pfs.lms.enquiry.appraisal.projectdata;

import java.util.UUID;

public interface IProjectDataService {

    ProjectData getProjectData(UUID loanAppraisalId);

    ProjectData createProjectData(ProjectDataResource projectDataResource);

    ProjectData updateProjectData(ProjectDataResource projectDataResource);
}
