package pfs.lms.enquiry.collateral.dto;

/** Body of PUT …/project-evaluations/major-collateral-type: the new code, or null / blank to clear it. */
public class MajorCollateralTypeRequest {

    private String majorCollateralType;

    public String getMajorCollateralType() { return majorCollateralType; }
    public void setMajorCollateralType(String majorCollateralType) { this.majorCollateralType = majorCollateralType; }
}
