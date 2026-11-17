import { SelectOptionLine } from '../shared/select-option.model';
import { compareCells, displayDate } from '../workflow-status-report/workflow-status.model';

export { compareCells, displayDate };

/** Loans KYC Report (Reports › Loans Management): the SAP report "Loans KYC Report" (KycReportSet). */

export interface KycSelection {
    /** Fixed PFS: the entity has no company code property, the ABAP applies it */
    companyCode: string;
    /** Select-options: loan contract, business partner and loan contract status */
    contract: SelectOptionLine[];
    partner: SelectOptionLine[];
    status: SelectOptionLine[];
    includeAuthSignatories: boolean;
    runDate: string;
    effCapOnly: boolean;
    /** Select-option: KYC / Re-KYC compliance date */
    kycDate: SelectOptionLine[];
}

/** A row: the output properties of the entity KycReport by their lower-camel-case names. */
export type KycRow = Record<string, string | null>;

export interface KycReport {
    rows: KycRow[];
    total: number;
    generatedOn: string;
    query: string;
}

/** A column of the result table; every column is sortable. */
export interface KycColumn {
    key: string;
    label: string;
    /** 'text' sorts alphabetically, 'number' numerically (leading zeros ignored), 'date' by its ISO text */
    type: 'text' | 'number' | 'date' | 'time';
    mono?: boolean;
    /** Status text shown as a coloured pill */
    pill?: boolean;
    /** Minimum width in rem */
    width?: number;
}

/** The entity's output properties with the SAP labels, in the order of the table. */
export const KYC_COLUMNS: KycColumn[] = [
    { key: 'contractNo', label: 'Contract Number', type: 'number', mono: true, width: 8 },
    { key: 'status', label: 'Status', type: 'text', mono: true, width: 4 },
    { key: 'serialNo', label: 'Serial Number', type: 'number', mono: true, width: 5 },
    { key: 'partnerId', label: 'Busn. Partner', type: 'number', mono: true, width: 7 },
    { key: 'partnerName', label: 'Borrower Name', type: 'text', width: 14 },
    { key: 'role', label: 'Partner Role', type: 'text', width: 8 },
    { key: 'projectName', label: 'Project Name', type: 'text', width: 12 },
    { key: 'loanAgmtDate', label: 'Date of Loan Agrmt', type: 'date', mono: true, width: 7 },
    { key: 'kycStatus', label: 'KYC Status', type: 'text', pill: true, width: 8 },
    { key: 'kycComplianceDate', label: 'KYC Compliance Date', type: 'date', mono: true, width: 7 },
    { key: 'reKycComplianceDate', label: 'Re-KYC Compliance Dt', type: 'date', mono: true, width: 7 },
    { key: 'ckycComplDate', label: 'CKYC Compliance Date', type: 'date', mono: true, width: 7 },
    { key: 'ckycRefNo', label: 'CKYC Ref. Num', type: 'text', mono: true, width: 8 },
    { key: 'pan', label: 'PAN', type: 'text', width: 3.5 },
    { key: 'panNumber', label: 'PAN Number', type: 'text', mono: true, width: 7 },
    { key: 'coi', label: 'COI', type: 'text', width: 3.5 },
    { key: 'moa', label: 'MOA', type: 'text', width: 3.5 },
    { key: 'aoa', label: 'AOA', type: 'text', width: 3.5 },
    { key: 'cin', label: 'CIN', type: 'text', mono: true, width: 9 },
    { key: 'lei', label: 'LEI No', type: 'text', mono: true, width: 9 },
    { key: 'addrProof', label: 'Address Proof', type: 'text', width: 5 },
    { key: 'boardRes', label: 'Board Resolution', type: 'text', width: 5 },
    { key: 'authSignatory', label: 'Authorized Signatory', type: 'text', width: 6 },
    { key: 'kycRiskCategoryDesc', label: 'KYC Risk Cat. Desc', type: 'text', width: 9 },
    { key: 'reKycRiskCategoryDesc', label: 'Re-KYC Risk Cat.Desc', type: 'text', width: 9 }
];

/** Today as yyyy-MM-dd in the browser's time zone (the SAP default of the Run Date). */
export function today(): string {
    const d = new Date();
    const two = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${two(d.getMonth() + 1)}-${two(d.getDate())}`;
}

/** The defaults of the SAP selection screen: company PFS, status 60, today, loans having effective capital. */
export function defaultKycSelection(): KycSelection {
    return {
        companyCode: 'PFS', contract: [], partner: [], status: [{ sign: 'I', option: 'EQ', low: '60', high: '' }],
        includeAuthSignatories: false, runDate: today(), effCapOnly: true, kycDate: []
    };
}
