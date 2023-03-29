package pfs.lms.enquiry.monitoring.operatingparameters;

import java.util.List;
import java.util.UUID;

public interface IOperatingParameterPLFService {
    OperatingParameterPLF saveOperatingParameterPLF(OperatingParameterPLFResource resource, String username);
    OperatingParameterPLF updateOperatingParameterPLF(OperatingParameterPLFResource resource, String username) throws CloneNotSupportedException;
    List<OperatingParameterPLFResource> getOperatingParameterPLF(String loanApplicationId, String name);

    OperatingParameterPLF deleteOperatingParameterPLF(UUID operatingParameterId, String username);
}
