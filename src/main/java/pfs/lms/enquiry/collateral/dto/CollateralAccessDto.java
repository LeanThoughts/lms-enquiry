package pfs.lms.enquiry.collateral.dto;

import java.util.List;

/** What the signed-in user may do in Collateral Management. Everyone signed in may display. */
public class CollateralAccessDto {

    private boolean canWrite;
    private String userName;
    private String role;
    private List<String> writeRoles;

    public CollateralAccessDto() {
    }

    public CollateralAccessDto(boolean canWrite, String userName, String role, List<String> writeRoles) {
        this.canWrite = canWrite;
        this.userName = userName;
        this.role = role;
        this.writeRoles = writeRoles;
    }

    public boolean isCanWrite() { return canWrite; }
    public void setCanWrite(boolean canWrite) { this.canWrite = canWrite; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getWriteRoles() { return writeRoles; }
    public void setWriteRoles(List<String> writeRoles) { this.writeRoles = writeRoles; }
}
