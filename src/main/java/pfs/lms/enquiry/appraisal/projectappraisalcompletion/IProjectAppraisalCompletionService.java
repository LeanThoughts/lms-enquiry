package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

public interface IProjectAppraisalCompletionService {

    ProjectAppraisalCompletion createProjectAppraisalCompletion(
            ProjectAppraisalCompletionResource projectAppraisalCompletionResource, String username);

    ProjectAppraisalCompletion updateProjectAppraisalCompletion(
            ProjectAppraisalCompletionResource projectAppraisalCompletionResource, String username) throws CloneNotSupportedException;
}
