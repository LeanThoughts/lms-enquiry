import { Type } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Observable } from "rxjs";

// Service prototype method used for fetching, creating and updating entities
export type ServiceMethod = (...args: any[]) => Observable<any>;

// How the search strings are passed to the create function
export type PassSearchStringVia = 'Object' | 'Query';

// Field types rendered by the generic update dialog and generic update components
export type FieldType = 'text' | 'number' | 'date' | 'select' | 'combobox' | 'file' | 'header';

// Column types formatted by the generic list component
export type ColumnType = 'text' | 'number' | 'date' | 'select' | 'file';

/**
 * Column displayed by the generic list component
 */
export interface ColumnConfig {
    name: string;
    header: string;
    type: ColumnType;
}

/**
 * Field rendered by the generic update dialog and generic update components
 */
export interface FieldConfig {
    row: number;
    span: number;
    name: string;
    label: string;
    type: FieldType;
    nullOption?: boolean;
    required?: boolean | { dependsOn: string }; // Required always, or only when the named field has a value
    readOnly?: boolean;
    readOnlyOnUpdate?: boolean; // Editable on create, disabled on update (disabled values are not submitted)
    maxLength?: number;
    pattern?: RegExp;
    options?: any[];
    displayFunction?: (...args: any[]) => any; // Function to display the value of the combobox field or to get options for the dependent select field
    itemTemplate?: string;
    displayKey?: string;
    valueKey?: string;
    defaultValue?: any;
    dependsOn?: string; // Name of the field that the dependent select field depends on
    viewOperationKey?: string; // Key to be used to display the value in the view mode
    maxValue?: 'currentDate' | number; // Maximum value for numeric or date fields
    minValue?: string | number; // Minimum value for numeric or date fields: another field's name, or a dotted path into routeResolvedData
    visibleWhen?: string | { field: string; value: any }; // Field that must have a value (or the given value, or one of the given values) for this field to be shown
    onValueChange?: boolean; // Call genericOnValueChangeFunction when the field value changes
}

/**
 * Configuration of the generic list component for an entity
 */
export interface ListConfig {
    header?: string;
    displayedColumns: ColumnConfig[];
    fetchFunction: ServiceMethod;
    createButton?: boolean;
    updateButton?: boolean;
    deleteButton?: boolean;
    viewButton?: boolean;
    deleteFunction?: ServiceMethod; // Called with the selected object's id
    deleteConfirmationMessage?: string;
    deleteSuccessMessage?: string;
    disableUpdate?: (selectedObject: any) => boolean; // Disable the update button for the selected object
    disableDelete?: (selectedObject: any) => boolean; // Disable the delete button for the selected object
    disableCreateButtonAfterCreate?: boolean;
    onlyEmitCreateEvent?: boolean;
    onlyEmitUpdateEvent?: boolean;
    onlyEmitViewEvent?: boolean;
    emitOnCreateSuccess?: boolean;
    emitOnUpdateSuccess?: boolean;
    updateDialogWidth?: string;
    viewDialogWidth?: string;
    routeResolvedData?: string[]; // Keys of the route resolved data passed to the update dialog
}

/**
 * Create and update settings shared by the generic update dialog and generic update components
 */
interface CreateUpdateConfig {
    searchString1ForCreate?: string;
    passSearchString1Via?: PassSearchStringVia;
    searchString2ForCreate?: string;
    passSearchString2Via?: PassSearchStringVia;
    createFunction?: ServiceMethod;
    updateFunction?: ServiceMethod;
    createSuccessMessage?: string;
    updateSuccessMessage?: string;
    trackObjectAfterCreateAndUpdate?: string; // Property of the create/update response that becomes the service's selectedEntity$
    fieldsConfig: FieldConfig[];
}

/**
 * Configuration of the generic update dialog component for an entity
 */
export interface UpdateDialogConfig extends CreateUpdateConfig {
    createDialogTitle?: string;
    updateDialogTitle?: string;
    viewDialogTitle?: string;
    submitConfirmationMessage?: string; // Ask the user to confirm before the create/update is submitted
}

/**
 * Configuration of the generic (inline) update component for an entity
 */
export interface UpdateConfig extends CreateUpdateConfig {
    genericOnValueChangeFunction?: (formGroup?: FormGroup, fieldName?: string) => void;
}

/**
 * All generic component configurations of a functional stage, keyed by entity name. Every entity of the stage is served by
 * the stage's service, and entity names must be unique across stages.
 */
export interface StageConfig {
    service: Type<unknown>;
    list?: Record<string, ListConfig>;
    updateDialog?: Record<string, UpdateDialogConfig>;
    update?: Record<string, UpdateConfig>;
}
