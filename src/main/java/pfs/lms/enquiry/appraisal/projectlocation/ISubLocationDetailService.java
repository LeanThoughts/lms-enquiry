package pfs.lms.enquiry.appraisal.projectlocation;

public interface ISubLocationDetailService {

    SubLocationDetail createSubLocation(SubLocationDetailResource subLocationDetailResource, String username)
            throws CloneNotSupportedException;

    SubLocationDetail updateSubLocation(SubLocationDetailResource subLocationDetailResource, String username)
            throws CloneNotSupportedException;
}
