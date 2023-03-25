package pfs.lms.enquiry.monitoring.insurance;

import java.util.List;
import java.util.UUID;

public interface IInsuranceService {

    Insurance saveInsurance(InsuranceResource resource, String userName);

    Insurance updateInsurance(InsuranceResource resource, String userName) throws CloneNotSupportedException;

    List<Insurance> getInsurances(UUID loanApplicationId);
}
