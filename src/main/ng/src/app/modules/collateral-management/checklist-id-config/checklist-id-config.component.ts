import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import {
    ButtonComponent,
    DynamicPageModule,
    FormModule,
    IconComponent,
    MessageStripComponent,
    ToolbarComponent
} from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from '../../../message.service';
import { CollateralService } from '../collateral.service';
import { ChecklistIdConfiguration } from '../collateral.model';

/**
 * Configuration app "Checklist ID Number Range" (menu Configuration). Shows how Checklist ID Nos. (SAP ZID_NO) of new
 * collaterals are numbered and lets the configuration roles maintain the highest ZID_NO used in SAP: portal numbers
 * continue above it.
 */
@Component({
    selector: 'app-checklist-id-config',
    templateUrl: './checklist-id-config.component.html',
    styleUrl: './checklist-id-config.component.scss',
    imports: [ReactiveFormsModule, DynamicPageModule, ToolbarComponent, ButtonComponent, IconComponent,
        MessageStripComponent, FormModule]
})
export class ChecklistIdConfigComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    config: ChecklistIdConfiguration | null = null;
    edit = false;
    waitMessage = '';
    loadFailed = false;
    errorMessage = '';
    noEditAccessDismissed = false;

    readonly sapHighest = new FormControl<number | null>(null, [Validators.required, Validators.min(0)]);

    constructor(private collateralService: CollateralService, private messageService: MessageService) {}

    ngOnInit(): void {
        this.load();
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    get noEditAccessText(): string {
        const role = this.config?.userRole?.trim() || '(none)';
        return `No edit access for the user with the role ${role}. Opening the Checklist ID number range in display mode`;
    }

    /** Lowest value allowed for the highest SAP ZID_NO: the highest number already loaded from SAP. */
    get minimum(): number {
        return this.config?.highestMigratedNumber ?? 0;
    }

    /** The number the next collateral would get with the value being entered (numbers never go down). */
    get preview(): number | null {
        const value = this.sapHighest.value;
        if (!this.config || value === null || value === undefined || !Number.isInteger(Number(value))) {
            return null;
        }
        return Math.max(this.config.nextNumber, Number(value) + 1);
    }

    get previewIsJump(): boolean {
        return this.preview !== null && !!this.config && this.preview > this.config.nextNumber;
    }

    format(value: number | null | undefined): string {
        return value === null || value === undefined ? '–' : String(value);
    }

    formatChanged(): string {
        if (!this.config?.changedAt) {
            return '';
        }
        const at = new Date(this.config.changedAt);
        const when = isNaN(at.getTime()) ? this.config.changedAt
            : at.toLocaleString('en-IN', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        return `Last changed by ${this.config.changedBy || '–'} on ${when}`;
    }

    load(): void {
        this.waitMessage = 'Loading the Checklist ID number range. Please wait';
        this.loadFailed = false;
        this.collateralService.getChecklistIdConfiguration().pipe(takeUntil(this.destroy$)).subscribe({
            next: config => {
                this.config = config;
                this.waitMessage = '';
                this.edit = false;
                this.sapHighest.reset(config.sapHighestNumber);
            },
            error: error => {
                this.waitMessage = '';
                this.loadFailed = true;
                this.messageService.showError(this.collateralService.errorMessage(error,
                    'The Checklist ID number range could not be loaded.'));
            }
        });
    }

    change(): void {
        this.errorMessage = '';
        this.sapHighest.reset(this.config?.sapHighestNumber ?? null);
        this.edit = true;
    }

    cancel(): void {
        this.errorMessage = '';
        this.edit = false;
        this.sapHighest.reset(this.config?.sapHighestNumber ?? null);
    }

    save(): void {
        this.sapHighest.markAsTouched();
        const value = this.sapHighest.value;
        const max = (this.config?.maximumNumber ?? 9999999999) - 1;
        if (value === null || value === undefined || !Number.isInteger(Number(value)) || Number(value) < 0
            || Number(value) > max) {
            this.errorMessage = `Enter a whole number between ${this.minimum} and ${max}.`;
            return;
        }
        if (Number(value) < this.minimum) {
            this.errorMessage = `Highest ZID_NO in SAP cannot be below ${this.minimum}, the highest Checklist ID No. already loaded from SAP.`;
            return;
        }
        this.errorMessage = '';
        this.waitMessage = 'Saving the Checklist ID number range. Please wait';
        this.collateralService.setSapHighestNumber(Number(value)).pipe(takeUntil(this.destroy$)).subscribe({
            next: config => {
                this.config = config;
                this.waitMessage = '';
                this.edit = false;
                this.sapHighest.reset(config.sapHighestNumber);
                this.messageService.showSuccess(`Saved. The next collateral gets Checklist ID No. ${config.nextNumber}.`);
            },
            error: error => {
                this.waitMessage = '';
                this.errorMessage = this.collateralService.errorMessage(error, 'The number range could not be saved.');
            }
        });
    }
}
