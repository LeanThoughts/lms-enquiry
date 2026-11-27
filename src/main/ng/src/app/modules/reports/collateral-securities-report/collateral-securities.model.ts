import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';
import { displayAmount } from '../balance-list-report/balance-list.model';
import { ReportTiming } from '../shared/report-timing';

export { compareCells, displayAmount, displayDate };

/**
 * Collateral Securities Report (Reports › Collaterals): the SAP Collateral Securities Report read through the gateway
 * entity set CollateralSecurityReportSet of ZPFS_LMS_REPORTS_SRV, one row per collateral condition of a loan.
 */

export interface CollateralSecuritiesSelection {
    /** Company Code (Ccode) */
    companyCode: string;
    /** Loan Number (RRanl), select-option */
    contract: SelectOptionLine[];
    /** Loan Partner (RGpart), select-option */
    partner: SelectOptionLine[];
    /** Filter: the compliance states to include (Co, Nc, Ip, Pc, Ta) */
    complied: boolean;
    notComplied: boolean;
    inProcess: boolean;
    partiallyComplied: boolean;
    timelineAvailable: boolean;
}

/** The checkboxes of the filter block, in the order of the SAP screen. */
export const COMPLIANCE_FILTERS: { key: 'complied' | 'notComplied' | 'inProcess' | 'partiallyComplied' | 'timelineAvailable'; label: string }[] = [
    { key: 'complied', label: 'Complied' },
    { key: 'notComplied', label: 'Not Complied' },
    { key: 'inProcess', label: 'In Process' },
    { key: 'partiallyComplied', label: 'Partially Complied' },
    { key: 'timelineAvailable', label: 'Timeline Available' }
];

export type CollateralSecuritiesColumnType = 'text' | 'amount' | 'number' | 'date';

/** A column of the result: the entity property, its heading and its kind. */
export interface CollateralSecuritiesColumn {
    key: string;
    label: string;
    type: CollateralSecuritiesColumnType;
}

/** One collateral condition: the values by property (dates yyyy-MM-dd, amounts as plain numbers). */
export type CollateralSecuritiesRow = Record<string, string | null>;

export interface CollateralSecuritiesReport {
    columns: CollateralSecuritiesColumn[];
    rows: CollateralSecuritiesRow[];
    total: number;
    /** Run time (browser stopwatch and server headers), set by the service */
    timing?: ReportTiming;
    generatedOn: string;
    query: string;
}

/** The defaults of the SAP selection screen: company PFS, all loans and partners, every filter checked. */
export function defaultCollateralSecuritiesSelection(): CollateralSecuritiesSelection {
    return {
        companyCode: 'PFS', contract: [], partner: [],
        complied: true, notComplied: true, inProcess: true, partiallyComplied: true, timelineAvailable: true
    };
}
