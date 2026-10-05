package pfs.lms.enquiry.usermanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Read model of a portal user returned by the User Management API.
 */
public class UserDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String role;
    private String roleDescription;
    private String sapBPNumber;
    private String riskDepartment;
    private String riskDepartmentName;
    private boolean departmentHead;
    private boolean riskPortalDisplayOnlyAccess;
    private boolean status;
    /** Valid from (set to the creation date). */
    private LocalDate startDate;
    /** Valid to: 31.12.9999 while active, the deactivation date once deactivated. */
    private LocalDate endDate;
    private LocalDate createdOn;
    private LocalTime createdAt;
    private String createdByUserName;
    private LocalDate changedOn;
    private LocalTime changedAt;
    private String changedByUserName;

    public UserDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public String getRiskDepartmentName() { return riskDepartmentName; }
    public void setRiskDepartmentName(String riskDepartmentName) { this.riskDepartmentName = riskDepartmentName; }

    public boolean isDepartmentHead() { return departmentHead; }
    public void setDepartmentHead(boolean departmentHead) { this.departmentHead = departmentHead; }

    public boolean isRiskPortalDisplayOnlyAccess() { return riskPortalDisplayOnlyAccess; }
    public void setRiskPortalDisplayOnlyAccess(boolean riskPortalDisplayOnlyAccess) { this.riskPortalDisplayOnlyAccess = riskPortalDisplayOnlyAccess; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDate createdOn) { this.createdOn = createdOn; }

    public LocalTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedByUserName() { return createdByUserName; }
    public void setCreatedByUserName(String createdByUserName) { this.createdByUserName = createdByUserName; }

    public LocalDate getChangedOn() { return changedOn; }
    public void setChangedOn(LocalDate changedOn) { this.changedOn = changedOn; }

    public LocalTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalTime changedAt) { this.changedAt = changedAt; }

    public String getChangedByUserName() { return changedByUserName; }
    public void setChangedByUserName(String changedByUserName) { this.changedByUserName = changedByUserName; }
}
