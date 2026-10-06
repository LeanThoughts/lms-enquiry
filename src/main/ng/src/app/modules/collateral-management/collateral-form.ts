import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { FdDate } from '@fundamental-ngx/core';
import { FieldDef, ITEM_MAX_LENGTH, toIsoDate } from './collateral.model';

/**
 * Form helpers shared by the collateral detail page and the child dialogs: controls with validators built from
 * field definitions, and conversion between backend records and form values.
 */

/** Maximum length of a text field: from its definition or the SAP length of the collateral field. */
export function maxLengthOf(def: FieldDef): number | undefined {
    return def.maxLength ?? ITEM_MAX_LENGTH[def.key];
}

/** A form control per field, with required, length, range and decimal validators. */
export function buildControls(fields: FieldDef[]): Record<string, FormControl> {
    const controls: Record<string, FormControl> = {};
    for (const def of fields) {
        if (def.type === 'separator') {
            continue;
        }
        const validators: ValidatorFn[] = [];
        if (def.required) {
            validators.push(Validators.required);
        }
        const maxLength = maxLengthOf(def);
        if (maxLength && (def.type === 'text' || def.type === 'select' || def.type === 'partner')) {
            validators.push(Validators.maxLength(maxLength));
        }
        if (def.type === 'partner') {
            validators.push(Validators.pattern(/^\d{1,10}$/));
        }
        if (def.type === 'number' || def.type === 'integer') {
            validators.push(Validators.min(0));
            if (def.max !== undefined) {
                validators.push(Validators.max(def.max));
            }
            validators.push(def.type === 'integer' ? integerValidator : decimalsValidator(def.decimals ?? 2));
        }
        controls[def.key] = new FormControl(null, validators);
    }
    return controls;
}

/** Form values of a record: dates as FdDate, check boxes as booleans. */
export function toFormValue(fields: FieldDef[], record: Record<string, unknown>): Record<string, unknown> {
    const value: Record<string, unknown> = {};
    for (const def of fields) {
        if (def.type === 'separator') {
            continue;
        }
        const raw = record[def.key];
        value[def.key] = def.type === 'date' ? toFdDate(raw) : def.type === 'checkbox' ? !!raw : raw ?? null;
    }
    return value;
}

/** The record with the form values applied; fields without an input keep their value. */
export function fromFormValue(fields: FieldDef[], form: FormGroup, record: Record<string, unknown>): Record<string, unknown> {
    const result: Record<string, unknown> = { ...record };
    for (const def of fields) {
        if (def.type === 'readonly' || def.type === 'separator') {
            continue;
        }
        const raw = form.controls[def.key].value;
        if (def.type === 'date') {
            result[def.key] = fromFdDate(raw);
        } else if (def.type === 'number' || def.type === 'integer') {
            result[def.key] = raw === null || raw === undefined || raw === '' ? null : Number(raw);
        } else if (def.type === 'checkbox') {
            result[def.key] = !!raw;
        } else {
            result[def.key] = typeof raw === 'string' && raw.trim() === '' ? null : raw;
        }
    }
    return result;
}

export function integerValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    return value === null || value === '' || value === undefined || Number.isInteger(Number(value)) ? null : { integer: true };
}

export function decimalsValidator(decimals: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        if (value === null || value === '' || value === undefined) {
            return null;
        }
        const fraction = String(value).split('.')[1] ?? '';
        return fraction.length > decimals ? { decimals: true } : null;
    };
}

/** Puts a "dateOrder" error on the "to" control when it is before the "from" control. */
export function dateOrderValidator(fromKey: string, toKey: string, message: string): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
        const toControl = group.get(toKey);
        const from = fromFdDate(group.get(fromKey)?.value);
        const to = fromFdDate(toControl?.value);
        const wrongOrder = !!from && !!to && to < from;
        if (toControl) {
            const { dateOrder, ...others } = toControl.errors ?? {};
            if (wrongOrder) {
                toControl.setErrors({ ...others, dateOrder: message });
            } else if (dateOrder) {
                toControl.setErrors(Object.keys(others).length ? others : null);
            }
        }
        return null;
    };
}

export function toFdDate(value: unknown): FdDate | null {
    const iso = toIsoDate(value);
    if (!iso) {
        return null;
    }
    const [year, month, day] = iso.split('-').map(Number);
    return new FdDate(year, month, day);
}

export function fromFdDate(value: unknown): string | null {
    if (value instanceof FdDate && value.year) {
        return `${value.year}-${String(value.month).padStart(2, '0')}-${String(value.day).padStart(2, '0')}`;
    }
    return toIsoDate(value);
}
