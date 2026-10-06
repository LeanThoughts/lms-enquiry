package pfs.lms.enquiry.collateral.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.collateral.dto.ChecklistIdConfigurationDto;
import pfs.lms.enquiry.collateral.service.impl.CollateralNumberRangeService;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;

/**
 * Configuration app "Checklist ID Number Range" (menu Configuration). Every signed-in user may display it; the roles in
 * collateral.configuration-roles may change the highest ZID_NO used in SAP.
 */
@ApiController
public class CollateralConfigurationController {

    private final CollateralNumberRangeService numberRangeService;

    public CollateralConfigurationController(CollateralNumberRangeService numberRangeService) {
        this.numberRangeService = numberRangeService;
    }

    /** GET /api/collaterals/configuration/checklist-id */
    @GetMapping("/collaterals/configuration/checklist-id")
    public ResponseEntity<ChecklistIdConfigurationDto> getChecklistIdConfiguration(HttpServletRequest request) {
        return ResponseEntity.ok(numberRangeService.getConfiguration(signedInUser(request)));
    }

    /** PUT /api/collaterals/configuration/checklist-id with body {"sapHighestNumber": 912000} */
    @PutMapping("/collaterals/configuration/checklist-id")
    public ResponseEntity<ChecklistIdConfigurationDto> setSapHighestNumber(@RequestBody JsonNode body,
                                                                           HttpServletRequest request) {
        JsonNode value = body == null ? null : body.get("sapHighestNumber");
        if (value == null || !value.canConvertToLong() || !value.isIntegralNumber()) {
            throw new LmsException("Highest ZID_NO in SAP must be a whole number.", HttpStatus.PRECONDITION_FAILED);
        }
        return ResponseEntity.ok(numberRangeService.setSapHighestNumber(value.asLong(), signedInUser(request)));
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
