import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';

export { compareCells, displayDate };

/**
 * Loans Disbursement - Periodwise and Comparison Report (Reports › Loans Management): the SAP report read through
 * DisbursementCompRptSet. SAP returns cells (RowNo, Fieldlabel, Value); the portal turns them into a table with one
 * row per RowNo and one column per Fieldlabel.
 */

export interface DisbursementSelection {
    companyCode: string;
    /** R = Report, C = Comparative */
    mode: 'R' | 'C';
    fiscalYearFrom: string;
    fiscalYearTo: string;
    /** Abstract Report: M Monthly … */
    periodType: string;
    periodFilter: string;
    /** Select-options */
    contract: SelectOptionLine[];
    partner: SelectOptionLine[];
    projectType: string;
    allLoans: boolean;
    suppressZero: boolean;
}

/** A column of the result: the key of its cells in the rows and the SAP field label. */
export interface DisbursementColumn {
    key: string;
    label: string;
}

/** A row: the cells by column key (c0, c1, …). */
export type DisbursementRow = Record<string, string | null>;

export interface DisbursementReport {
    columns: DisbursementColumn[];
    rows: DisbursementRow[];
    total: number;
    generatedOn: string;
    query: string;
}

/** The "Abstract Report" values of the SAP screen. */
export const PERIOD_TYPES: { code: string; text: string }[] = [
    { code: 'M', text: 'M Monthly' },
    { code: 'Q', text: 'Q Quarterly' },
    { code: 'H', text: 'H Half-yearly' },
    { code: 'Y', text: 'Y Yearly' }
];

/** An amount as SAP shows it ("1,25,000.50", "125.00-"); an id with leading zeros is not one. */
const AMOUNT = /^-?(0|[1-9][\d,]*)(\.\d+)?-?$/;

export function isAmount(value: string): boolean {
    return AMOUNT.test(value.trim());
}

export function amountValue(value: string): number {
    const text = value.trim().replace(/,/g, '');
    return text.endsWith('-') ? -Number(text.slice(0, -1)) : Number(text);
}

/** The fiscal year (April to March) of today, as the year it starts in. */
export function currentFiscalYear(today: Date = new Date()): string {
    return String(today.getMonth() >= 3 ? today.getFullYear() : today.getFullYear() - 1);
}

/** The defaults of the SAP selection screen: company PFS, Report, this fiscal year, Monthly, all loans, no nil rows. */
export function defaultDisbursementSelection(): DisbursementSelection {
    return {
        companyCode: 'PFS', mode: 'R', fiscalYearFrom: currentFiscalYear(), fiscalYearTo: '', periodType: 'M', periodFilter: '',
        contract: [], partner: [], projectType: '', allLoans: true, suppressZero: true
    };
}
