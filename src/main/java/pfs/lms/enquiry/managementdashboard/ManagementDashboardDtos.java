package pfs.lms.enquiry.managementdashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON shape of the Management Dashboard as the portal returns it: the SAP entity {@code Dashboard} of
 * {@code ZPFS_LMS_DASHBOARD_SRV} with its ten navigations, one list per card of the screen. Property names follow
 * the gateway properties in camel case, so the Angular model and the SAP model read the same.
 */
public final class ManagementDashboardDtos {

    private ManagementDashboardDtos() {
    }

    /** The whole dashboard: header of entity Dashboard plus its expanded navigations. */
    public static class Dashboard {
        public LocalDate runDate;               // key RunDate
        public boolean amountInCrore;           // key AmtInCr: amounts in Rs crore (true) or rupees (false)
        public String dashboardId;              // DashboardId, e.g. the snapshot id
        public String fiscalYear;               // Fyear, e.g. 2026
        public String reportingMonth;           // RepMonth, e.g. 11
        public String fiscalYearFull;           // FyearFull, e.g. 2026-27
        public LocalDateTime updatedOn;         // UpdateDate + UpdateTime: when SAP last refreshed the snapshot
        public String source;                   // The SAP URL the figures were read from

        public List<BudgetActual> budgetActual = new ArrayList<>();
        public List<InterestRate> interestRates = new ArrayList<>();
        public List<Rating> ratings = new ArrayList<>();
        public List<SmaOverdue> smaOverdue = new ArrayList<>();
        public List<EclOverdue> eclOverdue = new ArrayList<>();
        public List<LoanBook> loanBook = new ArrayList<>();
        public List<CashFlow> cashFlow = new ArrayList<>();
        public List<CollectionEfficiency> collectionEfficiency = new ArrayList<>();
        public List<Liquidity> liquidity = new ArrayList<>();
        public List<Proposal> proposals = new ArrayList<>();
    }

    /** Entity BudgetActual (ZLMSDB_STR_BUDG_ACT_OUT). */
    public static class BudgetActual {
        public String particulars;
        public BigDecimal budget;
        public BigDecimal actual;
    }

    /** Entity InterestRate (ZLMSDB_STR_INT_RATE_OUT). */
    public static class InterestRate {
        public String particulars;
        public BigDecimal percentage;
        public LocalDate effectiveFrom;
    }

    /** Entity Rating (ZLMDB_STR_RATING). */
    public static class Rating {
        public String ratingTypeDesc;
        public String crisilRating;
        public String icraRating;
    }

    /** Entity SmaOverdue (ZLMSDB_GW_S_SMA). */
    public static class SmaOverdue {
        public String smaCategory;
        public String particulars;
        public BigDecimal principalOverdue;
        public BigDecimal interestOverdue;
        public BigDecimal overdue;
        public BigDecimal outstanding;
    }

    /** Entity EclOverdue (ZLMSDB_GW_S_ECL). */
    public static class EclOverdue {
        public String eclClassification;
        public String particulars;
        public BigDecimal principalOverdue;
        public BigDecimal interestOverdue;
        public BigDecimal overdue;
        public BigDecimal effectiveCapital;
    }

    /** Entity LoanBook (ZLMSDB_GW_S_LOAN_BOOK), one row per quarter of the fiscal year. */
    public static class LoanBook {
        public String fiscalYear;
        public String quarter;
        public BigDecimal loanBookGross;
        public BigDecimal loanBookNetOfProvisions;
        public BigDecimal grossNpa;
        public String grossNpaPercent;          // SAP sends the percentages formatted as text
        public BigDecimal netNpa;
        public String netNpaPercent;
        public BigDecimal provisionAllLoans;
        public BigDecimal provisionNpaLoans;
        public BigDecimal provisionCoverageRatio;
        public BigDecimal provisionCoverageRatioNpa;
        public BigDecimal costOfFunds;
        public String yieldPercent;
    }

    /** Entity CashFlow (ZLMSDB_GW_S_CASH_FLOW), one row per evaluation period. */
    public static class CashFlow {
        public String evaluationPeriod;
        public String periodDesc;
        public BigDecimal inflowPrincipal;
        public BigDecimal inflowInterest;
        public BigDecimal inflowPrepayment;
        public BigDecimal outflowPrincipal;
        public BigDecimal outflowInterest;
        public BigDecimal outflowDisbursement;
        public BigDecimal totalInflow;
        public BigDecimal totalOutflow;
        public BigDecimal netFlow;
    }

    /** Entity CollEfficiency (ZLMSDB_STR_COLL_EFF_OUT), one row per month. */
    public static class CollectionEfficiency {
        public String monthNumber;
        public String monthName;
        public String collectionEfficiency;     // SAP sends the percentage formatted as text
    }

    /** Entity Liquidity (ZLMSDB_STR_LIQ_OUT). */
    public static class Liquidity {
        public String particulars;
        public BigDecimal amount;
    }

    /** Entity Proposal (ZLMSDB_STR_LOAN_PROPOSALS_OUT). */
    public static class Proposal {
        public String particulars;
        public String enquiryCount;
        public BigDecimal amount;
    }
}
