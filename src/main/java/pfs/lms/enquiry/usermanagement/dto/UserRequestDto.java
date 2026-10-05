package pfs.lms.enquiry.usermanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Request body for creating or updating a portal user from the User Management UI.
 */
public class UserRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @NotBlank(message = "E-mail is required")
    @Email(message = "E-mail is not valid")
    private String email;

    @Size(max = 100, message = "User name must be at most 100 characters")
    private String userName;

    /** Role code from the UserRole master. */
    @NotBlank(message = "Role is required")
    private String role;

    /**
     * Role description as chosen in the UI. Optional; used to pick the right
     * entry when several UserRole rows share the same code.
     */
    private String roleDescription;

    private String sapBPNumber;

    /** Department code from the Department master. Optional. */
    private String riskDepartment;

    private Boolean departmentHead;

    private Boolean riskPortalDisplayOnlyAccess;

    /**
     * true = active. Ignored on create (new users are always active, valid from today to 31.12.9999).
     * On update: false deactivates (end date = today), true re-activates (end date = 31.12.9999); null = unchanged.
     */
    private Boolean status;

    public UserRequestDto() {
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRoleDescription() { return roleDescription; }
    public void setRoleDescription(String roleDescription) { this.roleDescription = roleDescription; }

    public String getSapBPNumber() { return sapBPNumber; }
    public void setSapBPNumber(String sapBPNumber) { this.sapBPNumber = sapBPNumber; }

    public String getRiskDepartment() { return riskDepartment; }
    public void setRiskDepartment(String riskDepartment) { this.riskDepartment = riskDepartment; }

    public Boolean getDepartmentHead() { return departmentHead; }
    public void setDepartmentHead(Boolean departmentHead) { this.departmentHead = departmentHead; }

    public Boolean getRiskPortalDisplayOnlyAccess() { return riskPortalDisplayOnlyAccess; }
    public void setRiskPortalDisplayOnlyAccess(Boolean riskPortalDisplayOnlyAccess) { this.riskPortalDisplayOnlyAccess = riskPortalDisplayOnlyAccess; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
