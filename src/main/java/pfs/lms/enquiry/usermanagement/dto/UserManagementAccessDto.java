package pfs.lms.enquiry.usermanagement.dto;

/**
 * Tells the UI whether the signed-in user may maintain users.
 */
public class UserManagementAccessDto {

    private boolean allowed;
    private String email;
    private String role;

    public UserManagementAccessDto() {
    }

    public UserManagementAccessDto(boolean allowed, String email, String role) {
        this.allowed = allowed;
        this.email = email;
        this.role = role;
    }

    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
