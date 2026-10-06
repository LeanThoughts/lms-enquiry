import { FormGroup } from '@angular/forms';
import { FdDate } from '@fundamental-ngx/core';
import { FieldDef } from './collateral.model';
import {
    buildControls,
    dateOrderValidator,
    decimalsValidator,
    fromFdDate,
    fromFormValue,
    integerValidator,
    toFdDate,
    toFormValue
} from './collateral-form';

describe('Collateral form mapping', () => {
    const fields: FieldDef[] = [
        { key: 'checklistIdNo', label: 'Checklist ID No.', type: 'readonly' },
        { key: 'conditionGroup', label: 'Condition Group', type: 'select', list: 'CONDITION_GROUP', required: true },
        { key: 'remarks', label: 'Remarks', type: 'text', maxLength: 10 },
        { key: 'validFromDate', label: 'Valid from', type: 'date' },
        { key: 'validToDate', label: 'Valid to', type: 'date' },
        { key: 'penalChargesPercentage', label: 'Penal %', type: 'number', decimals: 2, max: 100 },
        { key: 'actionPeriod', label: 'Number', type: 'integer', max: 999 },
        { key: 'recurring', label: 'Recurring', type: 'checkbox' }
    ];

    function form(record: Record<string, unknown>): FormGroup {
        const group = new FormGroup(buildControls(fields));
        group.reset(toFormValue(fields, record));
        return group;
    }

    it('turns dates into FdDate and back into ISO dates', () => {
        const date = toFdDate('2021-06-01')!;
        expect(date instanceof FdDate).toBeTrue();
        expect([date.year, date.month, date.day]).toEqual([2021, 6, 1]);
        expect(fromFdDate(date)).toBe('2021-06-01');
        expect(fromFdDate(null)).toBeNull();
    });

    it('keeps read-only fields, trims empty text to null and converts numbers', () => {
        const group = form({ checklistIdNo: 912, conditionGroup: '04', remarks: 'x', recurring: true, validFromDate: '2021-01-01' });
        group.patchValue({ remarks: '   ', penalChargesPercentage: '2.5', actionPeriod: '30' });

        const result = fromFormValue(fields, group, { id: 'I1', checklistIdNo: 912 });

        expect(result['id']).toBe('I1');
        expect(result['checklistIdNo']).toBe(912);
        expect(result['remarks']).toBeNull();
        expect(result['penalChargesPercentage']).toBe(2.5);
        expect(result['actionPeriod']).toBe(30);
        expect(result['recurring']).toBeTrue();
        expect(result['validFromDate']).toBe('2021-01-01');
    });

    it('validates required, length, maximum, decimals and whole numbers', () => {
        const group = form({});
        expect(group.controls['conditionGroup'].hasError('required')).toBeTrue();

        group.patchValue({ conditionGroup: '04', remarks: 'x'.repeat(11), penalChargesPercentage: 101 });
        expect(group.controls['remarks'].hasError('maxlength')).toBeTrue();
        expect(group.controls['penalChargesPercentage'].hasError('max')).toBeTrue();

        expect(decimalsValidator(2)({ value: '1.234' } as never)).toEqual({ decimals: true });
        expect(decimalsValidator(2)({ value: '1.23' } as never)).toBeNull();
        expect(integerValidator({ value: '1.5' } as never)).toEqual({ integer: true });
        expect(integerValidator({ value: '15' } as never)).toBeNull();
    });

    it('puts a dateOrder error on "valid to" when it is before "valid from"', () => {
        const group = form({ validFromDate: '2022-01-01', validToDate: '2021-01-01' });
        dateOrderValidator('validFromDate', 'validToDate', 'Valid to must not be before valid from')(group);
        expect(group.controls['validToDate'].hasError('dateOrder')).toBeTrue();

        group.patchValue({ validToDate: toFdDate('2023-01-01') });
        dateOrderValidator('validFromDate', 'validToDate', 'Valid to must not be before valid from')(group);
        expect(group.controls['validToDate'].hasError('dateOrder')).toBeFalse();
    });
});
