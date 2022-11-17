package pfs.lms.enquiry.action.projectproposal.financials;

import lombok.*;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.domain.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"projectProposal"}, callSuper = false)
public class PromoterBorrowerFinancial extends AggregateRoot<PromoterBorrowerFinancial> implements Cloneable {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ProjectProposal projectProposal;

    private String fiscalPeriod;
    private Double revenue;
    private Double ebitda;
    private Double depreciation;
    private Double interestExpense;
    private Double pbt;
    private Double pat;
    private Double netCashAccruals;
    private Double wcDebt;
    private Double cpltd;
    private Double ltDebt;
    private Double totalDebt;
    private Double totalOutstandingLiabilities;
    private Double shareCapital;
    private Double reservesAndSurplus;
    private Double tangibleNetWorth;
    private Double adjustedTangibleNetWorth;
    private Double cashAndBankBalance;
    private Double currentAssets;
    private Double currentLiabilities;
    private Double subAsso;
    private Double netFixedAssets;
    private Double quasiEquity;
    private Double ebitdaMarginPercentage;
    private Double ebitdaInterest;
    private Double cashDscr;
    private Double dscr;
    private Double totalDebtEbitda;
    private Double termDebtEbitda;
    private Double totalDebtTnw;
    private Double tnw;
    private Double currentRatio;

    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
