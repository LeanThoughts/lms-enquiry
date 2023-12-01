package pfs.lms.enquiry.domain;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User extends AggregateRoot<User> {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String role;
    private String roleDescription;
    private boolean status;
    private String userName;
    private String sapBPNumber;
    private String riskDepartment;
    private boolean riskPortalDisplayOnlyAccess;

    @Nullable
    private boolean departmentHead;
    private boolean passwordReset;

    public User(String firstName, String lastName, String email, String role, boolean status, String userName, String sapBPNumber, String riskDepartment, boolean departmentHead, boolean riskPortalDisplayOnlyAccess) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.userName = userName;
        this.sapBPNumber = sapBPNumber;
        this.riskDepartment = riskDepartment;
        this.departmentHead = departmentHead;
        this.riskPortalDisplayOnlyAccess = riskPortalDisplayOnlyAccess;
        registerEvent(UserCreated.of(this));
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRole() {
        return this.role;
    }

    public boolean isStatus() {
        return this.status;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getSapBPNumber() {
        return this.sapBPNumber;
    }

    public String getRiskDepartment() {
        return this.riskDepartment;
    }

    public boolean isDepartmentHead() {
        return this.departmentHead;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSapBPNumber(String sapBPNumber) {
        this.sapBPNumber = sapBPNumber;
    }

    public void setRiskDepartment(String riskDepartment) {
        this.riskDepartment = riskDepartment;
    }

    public void setDepartmentHead(@Nullable boolean departmentHead) {
        this.departmentHead = departmentHead;
    }

    public void setRiskPortalDisplayOnlyAccess(boolean riskPortalDisplayOnlyAccess) {
        this.riskPortalDisplayOnlyAccess = riskPortalDisplayOnlyAccess;
    }

    public boolean getRiskPortalDisplayOnlyAccess() {
        return this.riskPortalDisplayOnlyAccess;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public User setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
        return this;
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class UserCreated {
        final User user;
    }
}