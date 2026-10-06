package pfs.lms.enquiry.configuration.fieldstatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.CopyRequest;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetField;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetFieldStatus;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetOverview;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST API of the Configuration app "BP Entity Set Fields": BupaRoleEntitySetFieldStatus by role and entity set
 * (under /api from {@link ApiController}).
 * <pre>
 * GET    /configuration/bupa-entity-set-field-status                         roles, entity sets, counts, access
 * GET    /configuration/bupa-entity-set-field-status/{role}/{entitySet}      fields of the entity set
 * PUT    /configuration/bupa-entity-set-field-status/{role}/{entitySet}      save changed fields
 * POST   /configuration/bupa-entity-set-field-status/{role}/{entitySet}/fields  add a field
 * POST   /configuration/bupa-entity-set-field-status/{role}/{entitySet}/copy    copy missing fields from another role
 * DELETE /configuration/bupa-entity-set-field-status/fields/{id}
 * </pre>
 */
@ApiController
public class EntitySetFieldStatusConfigurationController {

    private static final String BASE = "/configuration/bupa-entity-set-field-status";

    private final FieldStatusConfigurationService service;

    public EntitySetFieldStatusConfigurationController(FieldStatusConfigurationService service) {
        this.service = service;
    }

    @GetMapping(BASE)
    public ResponseEntity<EntitySetOverview> getOverview(HttpServletRequest request) {
        return ResponseEntity.ok(service.getEntitySetOverview(signedInUser(request)));
    }

    @GetMapping(BASE + "/{roleCode}/{entitySet}")
    public ResponseEntity<EntitySetFieldStatus> getEntitySet(@PathVariable String roleCode, @PathVariable String entitySet,
                                                             HttpServletRequest request) {
        signedInUser(request);
        return ResponseEntity.ok(service.getEntitySet(roleCode, entitySet));
    }

    @PutMapping(BASE + "/{roleCode}/{entitySet}")
    public ResponseEntity<EntitySetFieldStatus> saveChanges(@PathVariable String roleCode, @PathVariable String entitySet,
                                                            @RequestBody List<EntitySetField> changes,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(service.saveEntitySetChanges(roleCode, entitySet, changes, signedInUser(request)));
    }

    @PostMapping(BASE + "/{roleCode}/{entitySet}/fields")
    public ResponseEntity<EntitySetFieldStatus> addField(@PathVariable String roleCode, @PathVariable String entitySet,
                                                         @RequestBody EntitySetField field, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addEntitySetField(roleCode, entitySet, field, signedInUser(request)));
    }

    @PostMapping(BASE + "/{roleCode}/{entitySet}/copy")
    public ResponseEntity<EntitySetFieldStatus> copy(@PathVariable String roleCode, @PathVariable String entitySet,
                                                     @RequestBody CopyRequest copy, HttpServletRequest request) {
        return ResponseEntity.ok(service.copyEntitySet(roleCode, entitySet, copy != null ? copy.getSourceRoleCode() : null,
                signedInUser(request)));
    }

    @DeleteMapping(BASE + "/fields/{id}")
    public ResponseEntity<EntitySetFieldStatus> deleteField(@PathVariable Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(service.deleteEntitySetFieldOfSet(id, signedInUser(request)));
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
