import { Component, Input, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import {
    ButtonComponent,
    CheckboxComponent,
    DatePickerComponent,
    DialogService,
    FdDate,
    FormModule,
    IconComponent,
    SelectModule
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { FieldDef, PartnerSearchResult, ValueEntry, formatDate } from '../collateral.model';
import { maxLengthOf } from '../collateral-form';
import {
    PartnerSearchDialogComponent,
    PartnerSearchDialogData
} from '../partner-search-dialog/partner-search-dialog.component';

let nextId = 0;

/**
 * One field of the collateral detail page: plain text in display mode, the matching input in change mode.
 */
@Component({
    selector: 'app-collateral-field',
    templateUrl: './collateral-field.component.html',
    styleUrl: './collateral-field.component.scss',
    imports: [ReactiveFormsModule, FormModule, SelectModule, DatePickerComponent, CheckboxComponent, IconComponent,
        ButtonComponent]
})
export class CollateralFieldComponent implements OnDestroy {

    @Input({ required: true }) def!: FieldDef;
    @Input({ required: true }) form!: FormGroup;
    @Input() edit = false;
    /** Dropdown values for select fields. */
    @Input() options: ValueEntry[] = [];

    /**
     * Names of business partners keyed by party number (type 'partner'). Shared with the page: a partner chosen in
     * the search is added to it.
     */
    @Input() partnerNames: Record<string, string> = {};

    readonly id = `cm-field-${nextId++}`;

    private readonly destroy$ = new Subject<void>();

    constructor(private dialogService: DialogService) {}

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    /** Name of the partner in a 'partner' field, if known. */
    get partnerName(): string {
        const value = this.value;
        return value ? this.partnerNames[String(value)] ?? '' : '';
    }

    /** Opens the partner search; the chosen partner's party number goes into the field. */
    searchPartner(): void {
        const data: PartnerSearchDialogData = { fieldLabel: this.def.label };
        this.dialogService.open(PartnerSearchDialogComponent, { data, responsivePadding: true }).afterClosed
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (partner: unknown) => {
                    if (partner && typeof partner === 'object') {
                        const chosen = partner as PartnerSearchResult;
                        const number = String(chosen.partyNumber);
                        this.partnerNames[number] = [chosen.partyName1, chosen.partyName2].filter(Boolean).join(' ') || number;
                        this.control.setValue(number);
                        this.control.markAsDirty();
                        this.control.markAsTouched();
                    }
                },
                error: () => { /* dismissed */ }
            });
    }

    get control(): FormControl {
        return this.form.get(this.def.key) as FormControl;
    }

    get editable(): boolean {
        return this.edit && this.def.type !== 'readonly' && this.def.type !== 'separator';
    }

    get maxLength(): number | null {
        return maxLengthOf(this.def) ?? null;
    }

    get value(): unknown {
        return this.control?.value;
    }

    /** Text shown in display mode. */
    get display(): string {
        const value = this.value;
        if (value === null || value === undefined || value === '') {
            return '';
        }
        switch (this.def.type) {
            case 'select': {
                const entry = this.options.find(option => option.code === value);
                return entry ? `${entry.code} ${entry.description}` : String(value);
            }
            case 'date':
                return value instanceof FdDate ? fdDateText(value) : formatDate(value);
            case 'partner':
                return this.partnerName ? `${value} · ${this.partnerName}` : String(value);
            case 'number':
                return Number(value).toLocaleString('en-IN', {
                    minimumFractionDigits: this.def.decimals ?? 0,
                    maximumFractionDigits: this.def.decimals ?? 3
                });
            default:
                return String(value);
        }
    }

    get invalid(): boolean {
        return !!this.control && this.control.invalid && (this.control.touched || this.control.dirty);
    }

    get errorText(): string {
        const errors = this.control?.errors;
        if (!errors || !this.invalid) {
            return '';
        }
        if (errors['required']) {
            return `${this.def.label} is required.`;
        }
        if (errors['maxlength']) {
            return `At most ${errors['maxlength'].requiredLength} characters.`;
        }
        if (errors['max'] || errors['min']) {
            return `Enter a value between 0 and ${this.def.max ?? 'the maximum'}.`;
        }
        if (errors['decimals']) {
            return `At most ${this.def.decimals} decimal places.`;
        }
        if (errors['integer']) {
            return 'Enter a whole number.';
        }
        if (errors['pattern'] && this.def.type === 'partner') {
            return 'Enter a party number (digits only) or use the search.';
        }
        if (errors['dateOrder']) {
            return errors['dateOrder'];
        }
        if (this.def.type === 'date') {
            return 'Enter a valid date.';
        }
        return 'Check this value.';
    }
}

function fdDateText(date: FdDate): string {
    if (!date || !date.year) {
        return '';
    }
    return `${String(date.day).padStart(2, '0')}.${String(date.month).padStart(2, '0')}.${date.year}`;
}
