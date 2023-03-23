package pfs.lms.enquiry.monitoring.insurance;

public interface IInsuranceService {

    Insurance saveInsurance(InsuranceResource resource, String userName);

    Insurance updateInsurance(InsuranceResource resource, String userName) throws CloneNotSupportedException;
}
