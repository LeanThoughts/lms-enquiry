import { Component, OnInit } from '@angular/core';
import { DatePickerModule, DialogCloseButtonComponent, DialogModule, DialogRef, DialogService, FormModule, LayoutGridModule } from '@fundamental-ngx/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReferenceInterestRateService } from '../reference-interest-rate.service';
import { MessageService } from '../../../message.service';
import { TAX_PERCENTAGE_REGEX } from '../../../common/common.regex';
import { CustomDialogComponent } from '../../../custom-dialog.component';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'app-reference-interest-rate-update',
    imports: [
        DatePipe,
        DialogModule,
        DialogCloseButtonComponent,
        FormModule,
        ReactiveFormsModule,
        LayoutGridModule,
        DatePickerModule
    ],
    templateUrl: './reference-interest-rate-update.component.html',
    styleUrl: './reference-interest-rate-update.component.scss'
})
export class ReferenceInterestRateUpdateComponent implements OnInit {

    title: string = '';
    referenceInterestRateForm!: FormGroup;
    selectedReferenceRateType: any;
    selectedReferenceInterestValue: any;

    /**
     * Constructor
     */
    constructor(
        public dialogRef: DialogRef,
        private messageService: MessageService,
        private dialogService: DialogService,
        private referenceInterestRateService: ReferenceInterestRateService
    ) {}

    /**
     * On init
     */
    ngOnInit(): void {
        // Get dialog data
        this.selectedReferenceRateType = this.dialogRef.data.referenceRateType;
        if (this.dialogRef.data.operation === 'Create') {
            this.title = 'Add Reference Interest Value';
        } 
        else if (this.dialogRef.data.operation === 'Update') {
            this.title = 'Update Reference Interest Value';
            this.selectedReferenceInterestValue = this.dialogRef.data.referenceInterestValue;
        } 
        else if (this.dialogRef.data.operation === 'View') {
            this.title = 'View Reference Interest Value';
            this.selectedReferenceInterestValue = this.dialogRef.data.referenceInterestValue;
        }
        // Initialize form
        this.referenceInterestRateForm = new FormGroup({
            referenceRateType: new FormControl(this.selectedReferenceRateType.code),
            validFromDate: new FormControl(this.selectedReferenceInterestValue?.validFromDate || '', [Validators.required]),
            interestRate: new FormControl(this.selectedReferenceInterestValue?.interestRate || '', 
                [Validators.required, Validators.pattern(TAX_PERCENTAGE_REGEX)])
        });
    }

    /**
     * Confirm reference interest rate create/update
     */
    confirmReferenceInterestRateUpdate(): void {
        if (this.referenceInterestRateForm.valid) {
            const dialogRef = this.dialogService.open(CustomDialogComponent, {
                data: {
                    title: 'Confirm Reference Interest Rate Create/Update',
                    description: 'Are you sure you want to create/update the reference interest rate? This will impact cash flows of existing '
                        + 'loans in the system.',
                },
                width: '500px'
            });
            // Subscribe to the dialog close event to intercept the action taken.
            dialogRef.afterClosed.subscribe({
                next: (result: any) => {
                    if (result.continue) {
                        this.saveReferenceInterestRate();
                    }
                }
            });
        }
        else {
            this.referenceInterestRateForm.markAllAsTouched();
        }
    }

    /**
     * Save reference interest rate
     */
    saveReferenceInterestRate(): void {
        var formValues = this.referenceInterestRateForm.value;
        formValues.referenceInterestRate = this.selectedReferenceRateType.id;
        const validFromDate = new Date(Date.UTC(formValues.validFromDate.year, formValues.validFromDate.month - 1, 
            formValues.validFromDate.day));
        formValues.validFromDate = validFromDate;

        const handleError = (operation: string) => (error: any) => {
            this.messageService.showError(
                `${error.message}!! Error ${operation} reference interest rate. Please try again. If the problem persists, please contact `
                + `the administrator.`
            );
        };

        if (this.dialogRef.data.operation === 'Create') {
            this.referenceInterestRateService.saveReferenceInterestRate(formValues).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Reference interest rate value created successfully');
                    this.dialogRef.close('Updated');
                },
                error: handleError('saving')
            });
        } else {
            formValues.id = this.selectedReferenceInterestValue.id;
            console.log(formValues);
            this.referenceInterestRateService.updateReferenceInterestRate(formValues).subscribe({
                next: (result) => {
                    this.messageService.showSuccess('Reference interest rate value updated successfully');
                    this.dialogRef.close('Updated');
                },
                error: handleError('updating')
            });
        }
    }
}
