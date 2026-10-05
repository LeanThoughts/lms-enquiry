package pfs.lms.enquiry.usermanagement.dto;

/**
 * Risk department option (code + description) for the User Management UI.
 */
public class DepartmentDto {

    private String code;
    private String value;

    public DepartmentDto() {
    }

    public DepartmentDto(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
