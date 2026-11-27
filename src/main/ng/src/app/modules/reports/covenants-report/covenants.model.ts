import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';
import { displayAmount } from '../balance-list-report/balance-list.model';
import { ReportTiming } from '../shared/report-timing';

export { compareCells, displayAmount, displayDate };

/**
 * Covenants Report (Reports › Loans Management): the covenants of the loans read through the gateway entity set
 * CovenantsReportSet of ZPFS_LMS_REPORTS_SRV, one row per covenant.
 */

export interface CovenantsSelection {
    /** Company Code (Bukrs) */
    companyCode: string;
    /** Loan Contract (Ranl), select-option */
    contract: SelectOptionLine[];
}

export type CovenantsColumnType = 'text' | 'amount' | 'number' | 'date';

/** A column of the result: the entity property, its heading and its kind. */
export interface CovenantsColumn {
    key: string;
    label: string;
    type: CovenantsColumnType;
}

/** One covenant: the values by property (dates yyyy-MM-dd, amounts as plain numbers). */
export type CovenantsRow = Record<string, string | null>;

export interface CovenantsReport {
    columns: CovenantsColumn[];
    rows: CovenantsRow[];
    total: number;
    /** Run time (browser stopwatch and server headers), set by the service */
    timing?: ReportTiming;
    generatedOn: string;
    query: string;
}

/** The defaults of the selection screen: company PFS, all loan contracts. */
export function defaultCovenantsSelection(): CovenantsSelection {
    return { companyCode: 'PFS', contract: [] };
}
