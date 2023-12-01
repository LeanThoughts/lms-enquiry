package pfs.lms.enquiry.resource;

import lombok.Data;

@Data
public class UserResource
{
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String role;
    private String sapBPNumber;
    private String riskDepartment;
    private Boolean departmentHead;
    private Boolean riskPortalDisplayOnlyAccess;

    public UserResource(String firstName, String lastName, String email, String mobile, String password, String role,
                        String sapBPNumber, String riskDepartment, boolean departmentHead, boolean riskPortalDisplayOnlyAccess)
    {
        /*if(!isValidEmail(email))
            throw new LmsException(String.format("Invalid email address: %s", email), HttpStatus.PRECONDITION_FAILED);

        if(!isValidPassword(password))
            throw new LmsException(String.format("Invalid password: %s", password), HttpStatus.PRECONDITION_FAILED);*/

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.role = role;
        this.sapBPNumber = sapBPNumber;
        this.riskDepartment = riskDepartment;
        this.departmentHead = departmentHead;
        this.riskPortalDisplayOnlyAccess = riskPortalDisplayOnlyAccess;
    }

    private static final String emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")";

    private static final String passwordRegex = "^(?=.*[A-Z]).*(?=.*[0-9]).*(?=.*[a-z]).{8}$";

    private static boolean isValidEmail(String emailAddress)
    {
        return emailAddress == null ? false : emailAddress.matches(emailRegex);
    }

    private static boolean isValidPassword(String password)
    {
        return password == null ? false : password.matches(passwordRegex);
    }
}