/** Field status of the business partner screens: 0 Display only, 1 Optional, 2 Mandatory, 3 Hide. */
export type FieldStatus = 0 | 1 | 2 | 3;

export const FIELD_STATUSES: { value: FieldStatus; label: string; short: string }[] = [
    { value: 0, label: 'Display only', short: 'Display' },
    { value: 1, label: 'Optional', short: 'Optional' },
    { value: 2, label: 'Mandatory', short: 'Mandatory' },
    { value: 3, label: 'Hide', short: 'Hide' }
];

export function statusLabel(status: number | null | undefined): string {
    return FIELD_STATUSES.find(entry => entry.value === status)?.label ?? (status === null || status === undefined ? '–' : String(status));
}

export interface RoleSummary {
    code: string;
    description: string | null;
    entityFieldCount: number;
    entitySetFieldCount: number;
}

export interface FieldStatusOverview {
    roles: RoleSummary[];
    canChange: boolean;
    userRole: string | null;
    entities: string[];
    entitySets: string[];
}

/** A row of BupaRoleEntityFieldStatus. */
export interface EntityField {
    id: number;
    entity: string;
    fieldName: string;
    fieldStatus: number;
}

/** A row of BupaRoleEntitySetFieldStatus. */
export interface EntitySetField {
    id: number;
    entitySet: string;
    keyFieldValue: string;
    fieldName: string;
    keyField: boolean;
    minimumEntries: number | null;
    fieldStatus: number;
}

export interface RoleFieldStatus {
    roleCode: string;
    roleDescription: string | null;
    entityFields: EntityField[];
    entitySetFields: EntitySetField[];
}

export interface FieldStatusChanges {
    entityFields: Partial<EntityField>[];
    entitySetFields: Partial<EntitySetField>[];
}

/** Start of the app "BP Entity Set Fields". */
export interface EntitySetOverview {
    roles: RoleSummary[];
    entitySets: string[];
    /** Number of rows by role code and entity set. */
    counts: Record<string, Record<string, number>>;
    canChange: boolean;
    userRole: string | null;
}

/** The fields of one entity set of one role. */
export interface EntitySetFieldStatus {
    roleCode: string;
    roleDescription: string | null;
    entitySet: string;
    fields: EntitySetField[];
    keyFieldValues: string[];
    /** Description of the key field values, e.g. identification category Z00001 -> PAN Card. */
    keyFieldDescriptions: Record<string, string>;
}
