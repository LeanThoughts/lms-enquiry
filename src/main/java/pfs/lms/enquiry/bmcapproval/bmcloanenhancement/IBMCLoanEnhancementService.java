package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import java.util.UUID;

public interface IBMCLoanEnhancementService {

    BMCLoanEnhancement create(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username);

    BMCLoanEnhancement update(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username) throws CloneNotSupportedException;

    BMCLoanEnhancement delete(UUID bmcLoanEnhancementId, String username);
}
