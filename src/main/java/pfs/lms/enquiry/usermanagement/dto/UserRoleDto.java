package pfs.lms.enquiry.usermanagement.dto;

import java.util.UUID;

/**
 * A role master entry with the number of users holding it and a data-quality check result.
 */
public class UserRoleDto {

    private UUID id;
    private String code;
    private String value;
    private long userCount;
    /** null when the entry is fine, otherwise a short description of the problem. */
    private String issue;

    public UserRoleDto() {
    }

    public UserRoleDto(UUID id, String code, String value, long userCount, String issue) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.userCount = userCount;
        this.issue = issue;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public long getUserCount() { return userCount; }
    public void setUserCount(long userCount) { this.userCount = userCount; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }
}
