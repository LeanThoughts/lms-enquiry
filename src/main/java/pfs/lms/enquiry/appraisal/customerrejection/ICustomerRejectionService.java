package pfs.lms.enquiry.appraisal.customerrejection;

public interface ICustomerRejectionService {

    CustomerRejection createCustomerRejection(CustomerRejectionResource customerRejectionResource, String username);

    CustomerRejection updateCustomerRejection(CustomerRejectionResource customerRejectionResource,String username) throws CloneNotSupportedException;
}
