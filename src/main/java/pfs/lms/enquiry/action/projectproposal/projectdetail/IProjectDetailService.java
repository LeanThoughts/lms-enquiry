package pfs.lms.enquiry.action.projectproposal.projectdetail;

public interface IProjectDetailService {

    ProjectDetail create(ProjectDetailResource resource, String username);

    ProjectDetail update(ProjectDetailResource resource, String username) throws CloneNotSupportedException;
}
