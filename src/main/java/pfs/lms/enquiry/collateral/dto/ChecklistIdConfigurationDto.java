package pfs.lms.enquiry.collateral.dto;

import java.time.LocalDateTime;

/** Configuration app "Checklist ID Number Range": the numbers and who may change them. */
public class ChecklistIdConfigurationDto {

    /** Highest ZID_NO used in SAP; portal numbers start above it. Null when not maintained yet. */
    private Long sapHighestNumber;
    /** The Checklist ID No. the next collateral created in the portal gets. */
    private Long nextNumber;
    /** Highest Checklist ID No. of all collaterals in the portal. */
    private Long highestNumberInPortal;
    /** Highest Checklist ID No. loaded from SAP by the migration. */
    private Long highestMigratedNumber;
    /** Lower limit from the property collateral.checklist-id.start-number. */
    private Long startNumber;
    /** Upper limit: ZID_NO is CHAR 10 in SAP. */
    private Long maximumNumber;
    private String changedBy;
    private LocalDateTime changedAt;
    private boolean canChange;
    private String userRole;

    public Long getSapHighestNumber() { return sapHighestNumber; }
    public void setSapHighestNumber(Long sapHighestNumber) { this.sapHighestNumber = sapHighestNumber; }

    public Long getNextNumber() { return nextNumber; }
    public void setNextNumber(Long nextNumber) { this.nextNumber = nextNumber; }

    public Long getHighestNumberInPortal() { return highestNumberInPortal; }
    public void setHighestNumberInPortal(Long highestNumberInPortal) { this.highestNumberInPortal = highestNumberInPortal; }

    public Long getHighestMigratedNumber() { return highestMigratedNumber; }
    public void setHighestMigratedNumber(Long highestMigratedNumber) { this.highestMigratedNumber = highestMigratedNumber; }

    public Long getStartNumber() { return startNumber; }
    public void setStartNumber(Long startNumber) { this.startNumber = startNumber; }

    public Long getMaximumNumber() { return maximumNumber; }
    public void setMaximumNumber(Long maximumNumber) { this.maximumNumber = maximumNumber; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public boolean isCanChange() { return canChange; }
    public void setCanChange(boolean canChange) { this.canChange = canChange; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
}
