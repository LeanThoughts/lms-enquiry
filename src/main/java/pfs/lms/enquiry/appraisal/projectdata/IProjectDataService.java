package pfs.lms.enquiry.appraisal.projectdata;

import java.util.UUID;

public interface IProjectDataService {

    ProjectDataResource getProjectData(UUID loanAppraisalId);

    ProjectDataResource createProjectData(ProjectDataResource projectDataResource);

    ProjectDataResource updateProjectData(ProjectDataResource projectDataResource);
}
