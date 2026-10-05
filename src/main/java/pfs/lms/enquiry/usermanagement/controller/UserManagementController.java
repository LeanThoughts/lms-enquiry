package pfs.lms.enquiry.usermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.usermanagement.dto.DepartmentDto;
import pfs.lms.enquiry.usermanagement.dto.UserDto;
import pfs.lms.enquiry.usermanagement.dto.UserManagementAccessDto;
import pfs.lms.enquiry.usermanagement.dto.UserRequestDto;
import pfs.lms.enquiry.usermanagement.dto.UserRoleDto;
import pfs.lms.enquiry.usermanagement.service.IUserManagementAuthorizationService;
import pfs.lms.enquiry.usermanagement.service.IUserManagementService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST API for the User Management UI module. All paths sit under /api (from {@link ApiController}).
 * <p>
 * Users are never deleted; use PATCH /usermanagement/users/{id}/status?active=false to deactivate.
 */
@ApiController
public class UserManagementController {

    private final IUserManagementService userManagementService;
    private final IUserManagementAuthorizationService authorizationService;

    public UserManagementController(IUserManagementService userManagementService,
                                    IUserManagementAuthorizationService authorizationService) {
        this.userManagementService = userManagementService;
        this.authorizationService = authorizationService;
    }

    /** Whether the signed-in user may use the module (drives the UI guard). */
    @GetMapping("/usermanagement/access")
    public ResponseEntity<UserManagementAccessDto> getAccess(HttpServletRequest request) {
        return ResponseEntity.ok(authorizationService.getAccess(currentUser(request)));
    }

    /** List / search users, e.g. GET /api/usermanagement/users?query=iyer&role=ZLM023&status=true */
    @GetMapping("/usermanagement/users")
    public ResponseEntity<List<UserDto>> search(@RequestParam(required = false) String query,
                                                @RequestParam(required = false) String role,
                                                @RequestParam(required = false) String riskDepartment,
                                                @RequestParam(required = false) Boolean status,
                                                HttpServletRequest request) {
        authorizationService.checkAccess(currentUser(request));
        return ResponseEntity.ok(userManagementService.search(query, role, riskDepartment, status));
    }

    @GetMapping("/usermanagement/users/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id, HttpServletRequest request) {
        authorizationService.checkAccess(currentUser(request));
        return ResponseEntity.ok(userManagementService.getById(id));
    }

    @PostMapping("/usermanagement/users")
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserRequestDto body, HttpServletRequest request) {
        String username = currentUser(request);
        authorizationService.checkAccess(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(userManagementService.create(body, username));
    }

    @PutMapping("/usermanagement/users/{id}")
    public ResponseEntity<UserDto> update(@PathVariable UUID id,
                                          @Valid @RequestBody UserRequestDto body,
                                          HttpServletRequest request) {
        String username = currentUser(request);
        authorizationService.checkAccess(username);
        return ResponseEntity.ok(userManagementService.update(id, body, username));
    }

    /** Activate / deactivate, e.g. PATCH /api/usermanagement/users/{id}/status?active=false */
    @PatchMapping("/usermanagement/users/{id}/status")
    public ResponseEntity<UserDto> updateStatus(@PathVariable UUID id,
                                                @RequestParam boolean active,
                                                HttpServletRequest request) {
        String username = currentUser(request);
        authorizationService.checkAccess(username);
        return ResponseEntity.ok(userManagementService.updateStatus(id, active, username));
    }

    /** Role master with user counts and data-quality checks. */
    @GetMapping("/usermanagement/roles")
    public ResponseEntity<List<UserRoleDto>> getRoles(HttpServletRequest request) {
        authorizationService.checkAccess(currentUser(request));
        return ResponseEntity.ok(userManagementService.getRoles());
    }

    @GetMapping("/usermanagement/departments")
    public ResponseEntity<List<DepartmentDto>> getDepartments(HttpServletRequest request) {
        authorizationService.checkAccess(currentUser(request));
        return ResponseEntity.ok(userManagementService.getDepartments());
    }

    private static String currentUser(HttpServletRequest request) {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
    }
}
