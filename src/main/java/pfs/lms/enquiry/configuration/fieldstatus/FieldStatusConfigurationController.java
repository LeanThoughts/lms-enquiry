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
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.Changes;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.CopyRequest;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntityField;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.EntitySetField;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.Overview;
import pfs.lms.enquiry.configuration.fieldstatus.FieldStatusDtos.RoleFieldStatus;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;

/**
 * REST API of the Configuration app "Business Partner Field Status" (under /api from {@link ApiController}).
 * <pre>
 * GET    /configuration/bupa-field-status                          roles, counts, access
 * GET    /configuration/bupa-field-status/{role}                   rows of a role
 * PUT    /configuration/bupa-field-status/{role}                   save changed rows
 * POST   /configuration/bupa-field-status/{role}/entity-fields     add an entity field
 * POST   /configuration/bupa-field-status/{role}/entity-set-fields add an entity set field
 * POST   /configuration/bupa-field-status/{role}/copy              copy missing rows from another role
 * DELETE /configuration/bupa-field-status/entity-fields/{id}
 * DELETE /configuration/bupa-field-status/entity-set-fields/{id}
 * </pre>
 */
@ApiController
public class FieldStatusConfigurationController {

    private static final String BASE = "/configuration/bupa-field-status";

    private final FieldStatusConfigurationService service;

    public FieldStatusConfigurationController(FieldStatusConfigurationService service) {
        this.service = service;
    }

    @GetMapping(BASE)
    public ResponseEntity<Overview> getOverview(HttpServletRequest request) {
        return ResponseEntity.ok(service.getOverview(signedInUser(request)));
    }

    @GetMapping(BASE + "/{roleCode}")
    public ResponseEntity<RoleFieldStatus> getRole(@PathVariable String roleCode, HttpServletRequest request) {
        signedInUser(request);
        return ResponseEntity.ok(service.getRole(roleCode));
    }

    @PutMapping(BASE + "/{roleCode}")
    public ResponseEntity<RoleFieldStatus> saveChanges(@PathVariable String roleCode, @RequestBody Changes changes,
                                                       HttpServletRequest request) {
        return ResponseEntity.ok(service.saveChanges(roleCode, changes, signedInUser(request)));
    }

    @PostMapping(BASE + "/{roleCode}/entity-fields")
    public ResponseEntity<RoleFieldStatus> addEntityField(@PathVariable String roleCode, @RequestBody EntityField field,
                                                          HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEntityField(roleCode, field, signedInUser(request)));
    }

    @PostMapping(BASE + "/{roleCode}/entity-set-fields")
    public ResponseEntity<RoleFieldStatus> addEntitySetField(@PathVariable String roleCode,
                                                             @RequestBody EntitySetField field,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addEntitySetField(roleCode, field, signedInUser(request)));
    }

    @PostMapping(BASE + "/{roleCode}/copy")
    public ResponseEntity<RoleFieldStatus> copyFromRole(@PathVariable String roleCode, @RequestBody CopyRequest copy,
                                                        HttpServletRequest request) {
        return ResponseEntity.ok(service.copyFromRole(roleCode, copy != null ? copy.getSourceRoleCode() : null,
                copy != null && Boolean.TRUE.equals(copy.getEntityFieldsOnly()), signedInUser(request)));
    }

    @DeleteMapping(BASE + "/entity-fields/{id}")
    public ResponseEntity<RoleFieldStatus> deleteEntityField(@PathVariable Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(service.deleteEntityField(id, signedInUser(request)));
    }

    @DeleteMapping(BASE + "/entity-set-fields/{id}")
    public ResponseEntity<RoleFieldStatus> deleteEntitySetField(@PathVariable Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(service.deleteEntitySetField(id, signedInUser(request)));
    }

    private static String signedInUser(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        if (user == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
