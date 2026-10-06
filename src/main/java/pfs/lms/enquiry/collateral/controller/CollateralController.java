package pfs.lms.enquiry.collateral.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.JsonNode;
import pfs.lms.enquiry.collateral.domain.CollateralChildRecord;
import pfs.lms.enquiry.collateral.domain.CollateralChildType;
import pfs.lms.enquiry.collateral.domain.CollateralItem;
import pfs.lms.enquiry.collateral.dto.CollateralAccessDto;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.dto.CollateralItemDetailDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;
import pfs.lms.enquiry.collateral.service.ICollateralAuthorizationService;
import pfs.lms.enquiry.collateral.dto.PartnerSearchResultDto;
import pfs.lms.enquiry.collateral.service.ICollateralChildService;
import pfs.lms.enquiry.collateral.service.ICollateralPartnerService;
import pfs.lms.enquiry.collateral.service.ICollateralService;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST API of Collateral Management. All paths sit under /api (from {@link ApiController}).
 * Every signed-in user may read; creating, changing and deleting need a write role (ZLM023, ZLM018, ZLM035).
 */
@ApiController
public class CollateralController {

    private final ICollateralService collateralService;
    private final ICollateralChildService childService;
    private final ICollateralPartnerService partnerService;
    private final ICollateralAuthorizationService authorizationService;

    public CollateralController(ICollateralService collateralService,
                                ICollateralChildService childService,
                                ICollateralPartnerService partnerService,
                                ICollateralAuthorizationService authorizationService) {
        this.collateralService = collateralService;
        this.childService = childService;
        this.partnerService = partnerService;
        this.authorizationService = authorizationService;
    }

    /** Whether the signed-in user may change collaterals (drives the Create, Change and Delete buttons). */
    @GetMapping("/collaterals/access")
    public ResponseEntity<CollateralAccessDto> getAccess(HttpServletRequest request) {
        return ResponseEntity.ok(authorizationService.getAccess(currentUser(request)));
    }

    /** All dropdown values, keyed by list name. */
    @GetMapping("/collaterals/value-lists")
    public ResponseEntity<Map<String, List<ValueEntryDto>>> getValueLists(HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(collateralService.getValueLists());
    }

    /** Agreement types for a collateral object type, e.g. GET /api/collaterals/agreement-types?collateralObjectType=Z00007 */
    @GetMapping("/collaterals/agreement-types")
    public ResponseEntity<List<ValueEntryDto>> getAgreementTypes(@RequestParam(required = false) String collateralObjectType,
                                                                 HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(collateralService.getAgreementTypes(collateralObjectType));
    }

    /** Collateral list (checklist) of a loan application. */
    @GetMapping("/collaterals/loans/{loanApplicationId}")
    public ResponseEntity<CollateralChecklistDto> getChecklist(@PathVariable UUID loanApplicationId,
                                                               HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(collateralService.getChecklist(loanApplicationId));
    }

    /** Collateral list by checklist id, e.g. for a workflow task. */
    @GetMapping("/collaterals/checklists/{checklistId}")
    public ResponseEntity<CollateralChecklistDto> getChecklistById(@PathVariable UUID checklistId,
                                                                   HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(collateralService.getChecklistById(checklistId));
    }

    @PostMapping("/collaterals/loans/{loanApplicationId}/items")
    public ResponseEntity<CollateralItem> createItem(@PathVariable UUID loanApplicationId,
                                                     @RequestBody CollateralItem item,
                                                     HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(collateralService.createItem(loanApplicationId, item, user));
    }

    @GetMapping("/collaterals/items/{itemId}")
    public ResponseEntity<CollateralItemDetailDto> getItem(@PathVariable UUID itemId, HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(collateralService.getItem(itemId));
    }

    @PutMapping("/collaterals/items/{itemId}")
    public ResponseEntity<CollateralItem> updateItem(@PathVariable UUID itemId,
                                                     @RequestBody CollateralItem item,
                                                     HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.ok(collateralService.updateItem(itemId, item, user));
    }

    @DeleteMapping("/collaterals/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId, HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        collateralService.deleteItem(itemId, user);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------------------------------------------- partners

    /**
     * Partner search for Security Trustee, Security Agent and Custodian, e.g.
     * GET /api/collaterals/partners?name1=sbi&amp;defaultPartnerRole=ZLM010 (at most 200 partners).
     */
    @GetMapping("/collaterals/partners")
    public ResponseEntity<List<PartnerSearchResultDto>> searchPartners(@RequestParam(required = false) String name1,
                                                                       @RequestParam(required = false) String name2,
                                                                       @RequestParam(required = false) String defaultPartnerRole,
                                                                       @RequestParam(required = false) String searchTerm1,
                                                                       @RequestParam(required = false) String searchTerm2,
                                                                       HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(partnerService.search(name1, name2, defaultPartnerRole, searchTerm1, searchTerm2));
    }

    /** Business partner roles for the partner search. */
    @GetMapping("/collaterals/partner-roles")
    public ResponseEntity<List<ValueEntryDto>> getPartnerRoles(HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(partnerService.getPartnerRoles());
    }

    // ---------------------------------------------------------------------------------------------- child rows
    // {type} is one of: coverages, roc, cersai, nesl, documents, securities

    /** Child rows of a collateral, e.g. GET /api/collaterals/items/{itemId}/coverages */
    @GetMapping("/collaterals/items/{itemId}/{type}")
    public ResponseEntity<List<? extends CollateralChildRecord<?>>> listChildren(@PathVariable UUID itemId,
                                                                               @PathVariable String type,
                                                                               HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(childService.list(childType(type), itemId));
    }

    @PostMapping("/collaterals/items/{itemId}/{type}")
    public ResponseEntity<CollateralChildRecord<?>> createChild(@PathVariable UUID itemId, @PathVariable String type,
                                                                @RequestBody JsonNode row, HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(childService.create(childType(type), itemId, row, user));
    }

    @PutMapping("/collaterals/{type}/{id}")
    public ResponseEntity<CollateralChildRecord<?>> updateChild(@PathVariable String type, @PathVariable UUID id,
                                                                @RequestBody JsonNode row, HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.ok(childService.update(childType(type), id, row, user));
    }

    @DeleteMapping("/collaterals/{type}/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable String type, @PathVariable UUID id, HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        childService.delete(childType(type), id, user);
        return ResponseEntity.noContent().build();
    }

    private static CollateralChildType childType(String path) {
        CollateralChildType type = CollateralChildType.fromPath(path);
        if (type == null) {
            throw new LmsException("Unknown collateral table: " + path, HttpStatus.NOT_FOUND);
        }
        return type;
    }

    private static void signedIn(HttpServletRequest request) {
        if (currentUser(request) == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
    }

    private static String currentUser(HttpServletRequest request) {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
    }
}
