package pfs.lms.enquiry.appraisal.customerrejection;

public interface ICustomerRejectionService {

    CustomerRejection createCustomerRejection(CustomerRejectionResource customerRejectionResource);

    CustomerRejection updateCustomerRejection(CustomerRejectionResource customerRejectionResource);
}
