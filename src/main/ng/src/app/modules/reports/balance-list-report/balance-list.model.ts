import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';

export { compareCells, displayDate };

/** Loans Balance List (Reports › Loans Management): the SAP report "Balance List" (FNR7, entity set ZFNR7Report). */

export interface BalanceListSelection {
    /** Fixed PFS */
    companyCode: string;
    /** Select-options */
    contract: SelectOptionLine[];
    productType: SelectOptionLine[];
    loanType: SelectOptionLine[];
    currency: SelectOptionLine[];
    acctRef: SelectOptionLine[];
    changedBy: SelectOptionLine[];
    evaluateCompleted: boolean;
    keyDate: string;
    displayCurrency: string;
    nonZeroOnly: boolean;
    totalsForZero: boolean;
    totalsOnly: boolean;
}

/** A row: the output properties of the entity ZFNR7Report by their lower-camel-case names. */
export type BalanceListRow = Record<string, string | null>;

export interface BalanceListReport {
    rows: BalanceListRow[];
    total: number;
    generatedOn: string;
    query: string;
}

/** A column of the result table; every column is sortable. */
export interface BalanceListColumn {
    key: string;
    label: string;
    /** 'number' sorts numerically (leading zeros ignored); 'amount' is a number shown with digit grouping */
    type: 'text' | 'number' | 'amount' | 'date' | 'time';
    mono?: boolean;
    /** Minimum width in rem */
    width?: number;
}

/** The entity's output properties, in the order of the table. */
export const BALANCE_LIST_COLUMNS: BalanceListColumn[] = [
    { key: 'ranl', label: 'Contract Number', type: 'number', mono: true, width: 8 },
    { key: 'rantrag', label: 'Application No.', type: 'text', mono: true, width: 8 },
    { key: 'bukrs', label: 'Company Code', type: 'text', mono: true, width: 5 },
    { key: 'gsart', label: 'Business Type', type: 'text', mono: true, width: 5 },
    { key: 'santwhr', label: 'Contract Currency', type: 'text', mono: true, width: 6 },
    { key: 'rrefkont', label: 'Acct Assignment Ref.', type: 'text', mono: true, width: 8 },
    { key: 'moratorium', label: 'Moratorium', type: 'text', width: 5 },
    { key: 'status', label: 'Status', type: 'text', width: 9 },
    { key: 'projectName', label: 'Project Name', type: 'text', width: 12 },
    { key: 'anzwhr', label: 'Display Currency', type: 'text', mono: true, width: 6 },
    { key: 'sanctionedAmt', label: 'Sanctioned Amount', type: 'amount', mono: true, width: 9 },
    { key: 'bzusage', label: 'Commitment Capital', type: 'amount', mono: true, width: 9 },
    { key: 'nomkap', label: 'Nominal Capital', type: 'amount', mono: true, width: 9 },
    { key: 'effkap', label: 'Effective Capital', type: 'amount', mono: true, width: 9 },
    { key: 'tilgng', label: 'Repayment', type: 'amount', mono: true, width: 9 },
    { key: 'zinsen', label: 'Interest', type: 'amount', mono: true, width: 9 },
    { key: 'versch', label: 'Versch.', type: 'amount', mono: true, width: 9 },
    { key: 'ueberz', label: 'Ueberz.', type: 'amount', mono: true, width: 9 },
    { key: 'briwr', label: 'Briwr', type: 'amount', mono: true, width: 9 },
    { key: 'bvzwr', label: 'Bvzwr', type: 'amount', mono: true, width: 9 },
    { key: 'bvkwr', label: 'Bvkwr', type: 'amount', mono: true, width: 9 },
    { key: 'rbear', label: 'Last Changed by', type: 'text', mono: true, width: 8 }
];

/** An amount from SAP ("1250000.500") with digit grouping and at least two decimals. */
export function displayAmount(value: string | null): string {
    if (value === null || value === undefined || value === '') {
        return '';
    }
    const n = Number(value);
    return Number.isNaN(n) ? value : n.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 3 });
}

/** Today as yyyy-MM-dd in the browser's time zone (the SAP default of the key date). */
export function today(): string {
    const d = new Date();
    const two = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${two(d.getMonth() + 1)}-${two(d.getDate())}`;
}

/** The defaults of the SAP selection screen: company PFS, completed contracts evaluated, key date today. */
export function defaultBalanceListSelection(): BalanceListSelection {
    return {
        companyCode: 'PFS', contract: [], productType: [], loanType: [], currency: [], acctRef: [], changedBy: [],
        evaluateCompleted: true, keyDate: today(), displayCurrency: '', nonZeroOnly: false, totalsForZero: false, totalsOnly: false
    };
}
