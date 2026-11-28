/**
 * Management Dashboard as the portal API returns it (GET /management-dashboard): the SAP entity Dashboard of
 * ZPFS_LMS_DASHBOARD_SRV with its ten navigations, one list per card. Decimals arrive as numbers, dates as ISO strings.
 */
export interface ManagementDashboard {
    runDate: string;                 // yyyy-MM-dd, key RunDate
    amountInCrore: boolean;          // key AmtInCr
    dashboardId: string | null;
    fiscalYear: string | null;
    reportingMonth: string | null;
    fiscalYearFull: string | null;
    updatedOn: string | null;        // ISO date-time of the SAP snapshot
    source: string | null;           // SAP URL the figures were read from

    budgetActual: BudgetActual[];
    interestRates: InterestRate[];
    ratings: Rating[];
    smaOverdue: SmaOverdue[];
    eclOverdue: EclOverdue[];
    loanBook: LoanBook[];
    cashFlow: CashFlow[];
    collectionEfficiency: CollectionEfficiency[];
    liquidity: Liquidity[];
    proposals: Proposal[];
}

export interface BudgetActual { particulars: string; budget: number | null; actual: number | null; }
export interface InterestRate { particulars: string; percentage: number | null; effectiveFrom: string | null; }
export interface Rating { ratingTypeDesc: string; crisilRating: string | null; icraRating: string | null; }
export interface SmaOverdue {
    smaCategory: string; particulars: string; principalOverdue: number | null; interestOverdue: number | null;
    overdue: number | null; outstanding: number | null;
}
export interface EclOverdue {
    eclClassification: string; particulars: string; principalOverdue: number | null; interestOverdue: number | null;
    overdue: number | null; effectiveCapital: number | null;
}
export interface LoanBook {
    fiscalYear: string; quarter: string; loanBookGross: number | null; loanBookNetOfProvisions: number | null;
    grossNpa: number | null; grossNpaPercent: string | null; netNpa: number | null; netNpaPercent: string | null;
    provisionAllLoans: number | null; provisionNpaLoans: number | null; provisionCoverageRatio: number | null;
    provisionCoverageRatioNpa: number | null; costOfFunds: number | null; yieldPercent: string | null;
}
export interface CashFlow {
    evaluationPeriod: string; periodDesc: string; inflowPrincipal: number | null; inflowInterest: number | null;
    inflowPrepayment: number | null; outflowPrincipal: number | null; outflowInterest: number | null;
    outflowDisbursement: number | null; totalInflow: number | null; totalOutflow: number | null; netFlow: number | null;
}
export interface CollectionEfficiency { monthNumber: string; monthName: string; collectionEfficiency: string | null; }
export interface Liquidity { particulars: string; amount: number | null; }
export interface Proposal { particulars: string; enquiryCount: string | null; amount: number | null; }

/** A card shows its figures as a graph (default) or as a table; the choice is kept per card in the browser. */
export type CardView = 'graph' | 'table';

/** Column of a card's table view. */
export interface CardColumn<T = any> {
    header: string;
    /** Value of the row; numbers are right-aligned and formatted, strings shown as they are. */
    value: (row: T) => string | number | null | undefined;
    /** Number format for numeric values (Angular DecimalPipe), default '1.2-2'. */
    format?: string;
    align?: 'start' | 'end';
}

/** Percentage text from SAP ("3.28", "97.2 %", "") as a number, or null. */
export function percentValue(text: string | null | undefined): number | null {
    if (text == null) {
        return null;
    }
    const n = parseFloat(String(text).replace(/[%\s,]/g, ''));
    return isNaN(n) ? null : n;
}
