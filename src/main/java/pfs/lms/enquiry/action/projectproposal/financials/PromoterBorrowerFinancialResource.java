package pfs.lms.enquiry.action.projectproposal.financials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PromoterBorrowerFinancialResource {

    private UUID id;
    private UUID projectProposalId;

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
}
