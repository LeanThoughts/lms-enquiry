import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';
import { displayAmount, today } from '../balance-list-report/balance-list.model';
import { ReportTiming } from '../shared/report-timing';

export { compareCells, displayAmount, displayDate, today };

/**
 * Portfolio Debt Listing (Reports › Loans Management): the SAP Loans Debt Listing Report read through the gateway
 * entity set PortfolioDebtListingReportSet of ZPFS_LMS_REPORTS_SRV, one row per loan.
 */

export interface PortfolioDebtSelection {
    /** Shown like on the SAP screen; not a property of the entity, so not sent */
    companyCode: string;
    /** Loan Contract (Ranl), select-option */
    contract: SelectOptionLine[];
    /** Run Date, yyyy-MM-dd */
    runDate: string;
}

export type PortfolioDebtColumnType = 'text' | 'amount' | 'number' | 'date';

/** A column of the result: the entity property, its heading and its kind. */
export interface PortfolioDebtColumn {
    key: string;
    label: string;
    type: PortfolioDebtColumnType;
}

/** One loan: the values by property (dates yyyy-MM-dd, amounts as plain numbers). */
export type PortfolioDebtRow = Record<string, string | null>;

export interface PortfolioDebtReport {
    columns: PortfolioDebtColumn[];
    rows: PortfolioDebtRow[];
    total: number;
    /** Run time (browser stopwatch and server headers), set by the service */
    timing?: ReportTiming;
    runDate: string;
    generatedOn: string;
    query: string;
}

/** The defaults of the SAP selection screen: company PFS, all loan contracts, run date today. */
export function defaultPortfolioDebtSelection(): PortfolioDebtSelection {
    return { companyCode: 'PFS', contract: [], runDate: today() };
}
