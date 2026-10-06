package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Number range of Collateral Management, e.g. CHECKLIST_ID: the next Checklist ID No. (ZID_NO) given to a collateral
 * created in the portal. One row per range; the row is locked while a number is drawn, so numbers are never given twice.
 * The next number is never below the highest SAP number + 1 ({@link #sapHighestNumber}, maintained in the
 * Configuration app) nor below the property collateral.checklist-id.start-number.
 */
@Entity
@Table(name = "collateral_number_range")
public class CollateralNumberRange {

    @Id
    @Column(name = "range_name", length = 40)
    private String name;

    @Column(name = "next_number", nullable = false)
    private Long nextNumber;

    /** Highest number used in SAP (e.g. highest ZID_NO); portal numbers start above it. */
    @Column(name = "sap_highest_number")
    private Long sapHighestNumber;

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public CollateralNumberRange() {
    }

    public CollateralNumberRange(String name, Long nextNumber) {
        this.name = name;
        this.nextNumber = nextNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getNextNumber() { return nextNumber; }
    public void setNextNumber(Long nextNumber) { this.nextNumber = nextNumber; }

    public Long getSapHighestNumber() { return sapHighestNumber; }
    public void setSapHighestNumber(Long sapHighestNumber) { this.sapHighestNumber = sapHighestNumber; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
