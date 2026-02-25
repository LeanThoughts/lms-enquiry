package pfs.lms.enquiry.businesspartner.domain;

import lombok.*;
import pfs.lms.enquiry.domain.AggregateRoot;
import pfs.lms.enquiry.domain.Partner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BusinessPartnerFinancial extends AggregateRoot<BusinessPartnerFinancial> implements Cloneable {

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

    private String fiscalYear;

    private BigDecimal revenue;
    private BigDecimal netCashAccruals;
    private BigDecimal depreciation;
    private BigDecimal ebitda;
    private BigDecimal pbt;
    private BigDecimal pat;
    private BigDecimal interestExpenses;

    private BigDecimal wcstDebt;
    private BigDecimal ltDebt;
    private BigDecimal totalOutstandingLiabilities;
    private BigDecimal reservesAndSurplus;

    private BigDecimal adjTangibleNetWorth;
    private BigDecimal currentAssets;
    private BigDecimal invInSubAsso;
    private BigDecimal fccbQuasiEquity;
    private BigDecimal cpltd;
    private BigDecimal totalDebt;
    private BigDecimal shareCapital;
    private BigDecimal tangibleNetWorth;
    private BigDecimal cashAndBankBalance;
    private BigDecimal currentLiabilities;
    private BigDecimal netFixedAssets;

    private BigDecimal ebitdaMarginPercentage;
    private BigDecimal ebitdaInterest;
    private BigDecimal cashDSCR;
    private BigDecimal totalDebtEbitda;
    private BigDecimal termDebtEbitda;
    private BigDecimal dscr;
    private BigDecimal totalDebtTnw;
    private BigDecimal tolTnw;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}