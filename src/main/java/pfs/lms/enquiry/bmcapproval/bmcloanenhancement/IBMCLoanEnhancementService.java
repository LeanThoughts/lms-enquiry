package pfs.lms.enquiry.bmcapproval.bmcloanenhancement;

import java.util.UUID;

public interface IBMCLoanEnhancementService {

    BmcLoanEnhancement create(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username);

    BmcLoanEnhancement update(BMCLoanEnhancementResource bmcLoanEnhancementResource, String username) throws CloneNotSupportedException;

    BmcLoanEnhancement delete(UUID bmcLoanEnhancementId, String username);
}
