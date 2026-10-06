package pfs.lms.enquiry.collateral.service;

import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Collateral checklist of a loan and its collaterals. Write methods expect the role check to be done. */
public interface ICollateralService {

    /** All dropdown value lists, keyed by list name, plus DOCUMENT_TYPE and UNIT_OF_MEASURE from the portal masters. */
    Map<String, List<ValueEntryDto>> getValueLists();

    /** Agreement types offered for a collateral object type (all types until the mapping is maintained). */
    List<ValueEntryDto> getAgreementTypes(String collateralObjectType);

    CollateralChecklistDto getChecklist(UUID loanApplicationId);

    /** Collateral list of the loan the checklist belongs to (e.g. opened from a workflow task). */
    CollateralChecklistDto getChecklistById(UUID checklistId);

    CollateralItemDetailDto getItem(UUID itemId);

    /** Creates a collateral; creates the checklist header of the loan with the first collateral. */
    CollateralItem createItem(UUID loanApplicationId, CollateralItem request, String userName);

    CollateralItem updateItem(UUID itemId, CollateralItem request, String userName);

    void deleteItem(UUID itemId, String userName);
}
