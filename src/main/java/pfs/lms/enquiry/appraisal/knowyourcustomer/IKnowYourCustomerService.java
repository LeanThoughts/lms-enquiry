package pfs.lms.enquiry.appraisal.knowyourcustomer;

public interface IKnowYourCustomerService {

    KnowYourCustomer updateKYC(KnowYourCustomerResource knowYourCustomerResource, String username) throws CloneNotSupportedException;
}
