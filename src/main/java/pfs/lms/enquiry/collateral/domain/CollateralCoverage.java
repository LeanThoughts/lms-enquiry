package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Collateral Coverage of a collateral: a row of SAP table ZCOL_COV (1:N with the collateral).
 * All SAP columns are kept except the client (MANDT) and the SAP audit include. Generated from the SAP field list.
 */
@Entity
@Table(name = "collateral_coverage")
public class CollateralCoverage extends CollateralChildRecord<CollateralCoverage> {

    /** Serial Number */
    @SapField("SERIAL_NO")
    private Integer serialNumber;

    /** Effective from date */
    @SapField("EFFECTIVE_FROM_DATE")
    private LocalDate effectiveFromDate;

    /** Expected Coverage Value (Amt) */
    @SapField("EXP_COV_VALUE_AMT")
    @Column(precision = 13, scale = 2)
    private BigDecimal expectedCoverageAmount;

    /** Expected Coverage Value (%) */
    @SapField("EXP_COV_VALUE_PCT")
    @Column(precision = 5, scale = 2)
    private BigDecimal expectedCoveragePercentage;

    /** Coverage Basis */
    @SapField("COV_BASIS")
    @Column(length = 1)
    private String coverageBasis;

    /** Coverage Basis description */
    @SapField("COV_BASIS_DESC")
    @Column(length = 60)
    private String coverageBasisDescription;

    /** Basis Amount */
    @SapField("BASIS_AMOUNT")
    @Column(precision = 13, scale = 2)
    private BigDecimal basisAmount;

    /** Coverage Amount */
    @SapField("COVERAGE_AMOUNT")
    @Column(precision = 13, scale = 2)
    private BigDecimal coverageAmount;

    /** Remarks */
    @SapField("REMARKS")
    @Column(length = 60)
    private String remarks;

    public CollateralCoverage() {
    }

    public Integer getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }
    public LocalDate getEffectiveFromDate() { return effectiveFromDate; }
    public void setEffectiveFromDate(LocalDate effectiveFromDate) { this.effectiveFromDate = effectiveFromDate; }
    public BigDecimal getExpectedCoverageAmount() { return expectedCoverageAmount; }
    public void setExpectedCoverageAmount(BigDecimal expectedCoverageAmount) { this.expectedCoverageAmount = expectedCoverageAmount; }
    public BigDecimal getExpectedCoveragePercentage() { return expectedCoveragePercentage; }
    public void setExpectedCoveragePercentage(BigDecimal expectedCoveragePercentage) { this.expectedCoveragePercentage = expectedCoveragePercentage; }
    public String getCoverageBasis() { return coverageBasis; }
    public void setCoverageBasis(String coverageBasis) { this.coverageBasis = coverageBasis; }
    public String getCoverageBasisDescription() { return coverageBasisDescription; }
    public void setCoverageBasisDescription(String coverageBasisDescription) { this.coverageBasisDescription = coverageBasisDescription; }
    public BigDecimal getBasisAmount() { return basisAmount; }
    public void setBasisAmount(BigDecimal basisAmount) { this.basisAmount = basisAmount; }
    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
