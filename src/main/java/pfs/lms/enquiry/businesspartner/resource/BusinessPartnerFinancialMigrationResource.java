package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.domain.Partner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPartnerFinancialMigrationResource {
    private String partnerId;


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
}
