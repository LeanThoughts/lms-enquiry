package pfs.lms.enquiry.collateral.service.impl;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Which collateral agreement types (ZCMS_COL_AGMT_TYPE) may be chosen for a collateral object type
 * (ZCOLLATERAL_TYPE). See "Collateral Agreement Type F4 -LOGIC.md" and slide 30 of the requirements deck.
 */
@Component
public class CollateralAgreementTypeMapping {

    /**
     * Agreement type codes allowed for the collateral object type.
     * <p>
     * TO BE FILLED IN MANUALLY. While this returns an empty list, all agreement types are offered and accepted.
     * Example once maintained: {@code case "Z00007": return Arrays.asList("Z00007");}
     *
     * @param collateralObjectType collateral object type, e.g. "Z00007"
     * @return allowed agreement type codes, or an empty list when every agreement type is allowed
     */
    public List<String> getAllowedAgreementTypes(String collateralObjectType) {
        return Collections.emptyList();
    }
}
