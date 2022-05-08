package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

public interface IProjectAppraisalCompletionService {

    ProjectAppraisalCompletion createProjectAppraisalCompletion(
            ProjectAppraisalCompletionResource projectAppraisalCompletionResource);

    ProjectAppraisalCompletion updateProjectAppraisalCompletion(
            ProjectAppraisalCompletionResource projectAppraisalCompletionResource);
}
