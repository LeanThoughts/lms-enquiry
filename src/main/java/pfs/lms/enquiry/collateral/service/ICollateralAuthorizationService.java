package pfs.lms.enquiry.collateral.service;

import pfs.lms.enquiry.collateral.dto.CollateralAccessDto;

/**
 * Role check of Collateral Management (NFR 11): roles ZLM023, ZLM018 and ZLM035 may create, change and delete;
 * every other signed-in user may only display.
 */
public interface ICollateralAuthorizationService {

    CollateralAccessDto getAccess(String principalName);

    /** Throws 403 unless the user may create, change or delete collaterals. */
    void checkWriteAccess(String principalName);
}
