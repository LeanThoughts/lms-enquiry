import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';
import { displayAmount, today } from '../balance-list-report/balance-list.model';
import { ReportTiming } from '../shared/report-timing';

export { compareCells, displayAmount, displayDate, today };

/**
 * Portfolio Exposure Report (Reports › Loans Management): the SAP Loans Portfolio Exposure Report read through the gateway
 * entity set PortfolioExposureReportSet of ZPFS_LMS_REPORTS_SRV, one row per loan.
 */

export interface PortfolioExposureSelection {
    /** Shown like on the SAP screen; not a property of the entity, so not sent */
    companyCode: string;
    /** Loan Contract (Ranl), select-option */
    contract: SelectOptionLine[];
    /** Run Date (Rundt), yyyy-MM-dd */
    runDate: string;
    /** Skip facility drawdowns (SkDr); checked by default as on the SAP screen */
    skipDrawdowns: boolean;
}

export type PortfolioExposureColumnType = 'text' | 'amount' | 'number' | 'date';

/** A column of the result: the entity property, its heading and its kind. */
export interface PortfolioExposureColumn {
    key: string;
    label: string;
    type: PortfolioExposureColumnType;
}

/** One loan: the values by property (dates yyyy-MM-dd, amounts as plain numbers). */
export type PortfolioExposureRow = Record<string, string | null>;

export interface PortfolioExposureReport {
    columns: PortfolioExposureColumn[];
    rows: PortfolioExposureRow[];
    total: number;
    /** Run time (browser stopwatch and server headers), set by the service */
    timing?: ReportTiming;
    runDate: string;
    generatedOn: string;
    query: string;
}

/** The defaults of the SAP selection screen: company PFS, all loan contracts, run date today, skip facility drawdowns. */
export function defaultPortfolioExposureSelection(): PortfolioExposureSelection {
    return { companyCode: 'PFS', contract: [], runDate: today(), skipDrawdowns: true };
}
