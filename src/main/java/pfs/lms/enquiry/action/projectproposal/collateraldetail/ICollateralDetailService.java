package pfs.lms.enquiry.action.projectproposal.collateraldetail;

public interface ICollateralDetailService {

    CollateralDetail create(CollateralDetailResource resource, String username);

    CollateralDetail update(CollateralDetailResource resource, String username) throws CloneNotSupportedException;
}
