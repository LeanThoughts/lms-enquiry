package pfs.lms.enquiry.collateral.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Collateral Valuation of a collateral: a row of SAP table ZCOL_VALUATION (1:N with the collateral), the "Valuation"
 * tab of the checklist condition. All SAP columns are kept except the client (MANDT) and the SAP audit include; the
 * keys and portal ids (ZID_NO, ITEM_ID, BUKRS, RANL, PTL_ID, PTL_DOCU_ID, SOURCEOFENTRY) come from
 * {@link CollateralChildRecord}.
 */
@Entity
@Table(name = "collateral_valuation")
public class CollateralValuation extends CollateralChildRecord<CollateralValuation> {

    /** Serial Number (numbered per collateral) */
    @SapField("SERIAL_NO")
    private Integer serialNumber;

    /** Nominal Value of the Asset (CURR 17,2) */
    @SapField("NOM_VALUE")
    @Column(precision = 17, scale = 2)
    private BigDecimal nominalValue;

    /** Final Asset Value (CURR 17,2) */
    @SapField("AST_VALUE")
    @Column(precision = 17, scale = 2)
    private BigDecimal assetValue;

    /** Entered Value (CURR 17,2) */
    @SapField("ENTERED_AMOUNT")
    @Column(precision = 17, scale = 2)
    private BigDecimal enteredAmount;

    /** Date on which the valuation was performed */
    @SapField("VAL_DATE")
    private LocalDate valuationDate;

    /** Currency of the values (NOM_VALUE_CURR) */
    @SapField("NOM_VALUE_CURR")
    @Column(length = 5)
    private String nominalValueCurrency;

    /** Remarks */
    @SapField("REMARKS")
    @Column(length = 60)
    private String remarks;

    public CollateralValuation() {
    }

    public Integer getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }
    public BigDecimal getNominalValue() { return nominalValue; }
    public void setNominalValue(BigDecimal nominalValue) { this.nominalValue = nominalValue; }
    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }
    public BigDecimal getEnteredAmount() { return enteredAmount; }
    public void setEnteredAmount(BigDecimal enteredAmount) { this.enteredAmount = enteredAmount; }
    public LocalDate getValuationDate() { return valuationDate; }
    public void setValuationDate(LocalDate valuationDate) { this.valuationDate = valuationDate; }
    public String getNominalValueCurrency() { return nominalValueCurrency; }
    public void setNominalValueCurrency(String nominalValueCurrency) { this.nominalValueCurrency = nominalValueCurrency; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
