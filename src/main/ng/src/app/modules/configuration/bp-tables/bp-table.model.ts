/** Business partner configuration tables (REST /configuration/bp-tables). */

export interface BpOption {
    value: string;
    label: string;
}

export interface BpTableField {
    name: string;
    label: string;
    type: 'TEXT' | 'NUMBER' | 'BOOLEAN';
    idKind: 'NONE' | 'ID' | 'GENERATED_ID' | 'NEXT_NUMBER_ID';
    businessKey: boolean;
    required: boolean;
    maxLength: number;
    /** Values come from another table (its key) */
    referenceTable: string | null;
    referenceEntity: boolean;
    /** Fixed values */
    options: BpOption[];
    idField: boolean;
    /** Shown in the table and the dialog */
    visible: boolean;
    /** Can be changed on an existing row (keys cannot) */
    changeable: boolean;
}

export interface BpTableDefinition {
    key: string;
    title: string;
    description: string;
    /** The CommandLineRunner that delivers the initial rows */
    configClass: string;
    fields: BpTableField[];
    valueField: string;
    labelField: string;
}

export interface BpRow {
    id: string;
    values: Record<string, unknown>;
    /** Descriptions of reference values */
    labels: Record<string, string>;
}

export interface BpTablePage {
    table: BpTableDefinition;
    rows: BpRow[];
    page: number;
    size: number;
    total: number;
    search: string;
    canChange: boolean;
    userRole: string | null;
    references: Record<string, BpOption[]>;
}

export const BP_PAGE_SIZES = [10, 25, 50, 100];

/** Dropdown values of a column (referenced table or fixed values), or null for free text */
export function fieldOptions(field: BpTableField, references: Record<string, BpOption[]>): BpOption[] | null {
    if (field.referenceTable) {
        return references[field.referenceTable] ?? [];
    }
    return field.options?.length ? field.options : null;
}
