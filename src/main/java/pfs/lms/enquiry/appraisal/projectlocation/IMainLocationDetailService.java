package pfs.lms.enquiry.appraisal.projectlocation;

public interface IMainLocationDetailService {

    MainLocationDetail updateMainLocationDetails(MainLocationDetailResource mainLocationDetailResource, String username)
            throws CloneNotSupportedException;
}
