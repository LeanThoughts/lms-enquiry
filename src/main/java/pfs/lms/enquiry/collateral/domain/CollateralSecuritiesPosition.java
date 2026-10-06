package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Securities position of a collateral: a row of SAP table ZCOL_SEC_POS (1:N with the collateral).
 * All SAP columns are kept except the client (MANDT) and the SAP audit include. Generated from the SAP field list.
 */
@Entity
@Table(name = "collateral_securities_position")
public class CollateralSecuritiesPosition extends CollateralChildRecord<CollateralSecuritiesPosition> {

    /** Serial Number */
    @SapField("SERIAL_NO")
    private Integer serialNumber;

    /** Sec. Change Date */
    @SapField("SEC_CHANGE_DATE")
    private LocalDate securitiesChangeDate;

    /** Short name of securities */
    @SapField("SEC_POS_DESC")
    @Column(length = 40)
    private String securitiesShortName;

    /** Number of Units in a Securities Position */
    @SapField("ZSEC_NO_OF_UNITS")
    @Column(precision = 15, scale = 5)
    private BigDecimal numberOfUnits;

    /** Nominal Value of a Position */
    @SapField("ZSEC_VALUE")
    @Column(precision = 17, scale = 2)
    private BigDecimal nominalValue;

    /** Currency of nominal value of a position */
    @SapField("ZSEC_VALUE_CURR")
    @Column(length = 5)
    private String nominalValueCurrency;

    /** Percentage Holding */
    @SapField("ZSEC_PCT_HOLDING")
    @Column(precision = 6, scale = 3)
    private BigDecimal holdingPercentage;

    /** Securities Type */
    @SapField("SECURITIES_TYPE")
    @Column(length = 20)
    private String securitiesType;

    /** Remarks */
    @SapField("REMARKS")
    @Column(length = 60)
    private String remarks;

    public CollateralSecuritiesPosition() {
    }

    public Integer getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }
    public LocalDate getSecuritiesChangeDate() { return securitiesChangeDate; }
    public void setSecuritiesChangeDate(LocalDate securitiesChangeDate) { this.securitiesChangeDate = securitiesChangeDate; }
    public String getSecuritiesShortName() { return securitiesShortName; }
    public void setSecuritiesShortName(String securitiesShortName) { this.securitiesShortName = securitiesShortName; }
    public BigDecimal getNumberOfUnits() { return numberOfUnits; }
    public void setNumberOfUnits(BigDecimal numberOfUnits) { this.numberOfUnits = numberOfUnits; }
    public BigDecimal getNominalValue() { return nominalValue; }
    public void setNominalValue(BigDecimal nominalValue) { this.nominalValue = nominalValue; }
    public String getNominalValueCurrency() { return nominalValueCurrency; }
    public void setNominalValueCurrency(String nominalValueCurrency) { this.nominalValueCurrency = nominalValueCurrency; }
    public BigDecimal getHoldingPercentage() { return holdingPercentage; }
    public void setHoldingPercentage(BigDecimal holdingPercentage) { this.holdingPercentage = holdingPercentage; }
    public String getSecuritiesType() { return securitiesType; }
    public void setSecuritiesType(String securitiesType) { this.securitiesType = securitiesType; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
