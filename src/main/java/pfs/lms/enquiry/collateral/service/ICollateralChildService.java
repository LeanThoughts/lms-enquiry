package pfs.lms.enquiry.collateral.service;

import com.fasterxml.jackson.databind.JsonNode;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralItem;

import java.util.List;
import java.util.UUID;

/**
 * Rows of the child tables of a collateral: Coverage, RoC, CERSAI and NeSL. Write methods expect the role check
 * to be done.
 */
public interface ICollateralChildService {

    List<? extends CollateralChildRecord<?>> list(CollateralChildType type, UUID itemId);

    CollateralChildRecord<?> create(CollateralChildType type, UUID itemId, JsonNode request, String userName);

    CollateralChildRecord<?> update(CollateralChildType type, UUID id, JsonNode request, String userName);

    void delete(CollateralChildType type, UUID id, String userName);

    /** Deletes all child rows of a collateral (before the collateral itself is deleted). */
    void deleteAllOfItem(CollateralItem item, String userName);

    /** Copies the Checklist ID No. of the collateral to its child rows (after it was given a number). */
    void updateChecklistIdNo(CollateralItem item);
}
