package pfs.lms.enquiry.action.projectproposal.projectcost;

public interface IProjectCostService {

    ProjectCost create(ProjectCostResource resource, String username);

    ProjectCost update(ProjectCostResource resource, String username) throws CloneNotSupportedException;
}
