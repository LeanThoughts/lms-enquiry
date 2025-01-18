import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar, MatDialog } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { ReferenceInterestValueService } from '../referenceInterestValue.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';

@Component({
    selector: 'fuse-reference-interest-value-update-dialog',
    templateUrl: './referenceInterestValueUpdate.component.html',
    styleUrls: ['./referenceInterestValueUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ReferenceInterestValueUpdateComponent {

    dialogTitle = 'Add Reference Interest Value';
    selectedReferenceInterestType: any;
    selectedReferenceInterestValue: any;
    referenceInterestValueUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder,
        public _dialogRef: MatDialogRef<any>, 
        @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar,
        private _referenceInterestValueService: ReferenceInterestValueService,
        private _dialog: MatDialog) 
    {
        // Fetch selected details from the dialog's data attribute.
        console.log('_dialogData', _dialogData);
        if (_dialogData.operation === 'add') {
            this.dialogTitle = 'Add Reference Interest Value';
            this.selectedReferenceInterestType = _dialogData.selectedReferenceInterestType;
            this.selectedReferenceInterestValue = {};
        }
        else {
            this.selectedReferenceInterestValue = _dialogData.selectedReferenceInterestValue;
        }

        // Initialize the form with the selected reference interest value.
        this.referenceInterestValueUpdateForm = _formBuilder.group({
            referenceRateType: [this._dialogData.selectedReferenceInterestType.code],
            validFromDate: [this.selectedReferenceInterestValue.validFromDate || ''], 
            interestRate: [this.selectedReferenceInterestValue.interestRate || '', Validators.pattern(MonitoringRegEx.holdingPercentage)]
        });        
    }

    ngOnInit() {
        if (this._dialogData.operation === 'update') {
            this.referenceInterestValueUpdateForm.controls['validFromDate'].disable();
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.referenceInterestValueUpdateForm.valid) {
            var referenceInterestFormValue = this.referenceInterestValueUpdateForm.value;

            var dt = new Date(referenceInterestFormValue.validFromDate);
            referenceInterestFormValue.validFromDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
    
            const dialogRef = this._dialog.open(ConfirmationDialogComponent, {
                data: {
                    message: 'Are you sure? This will impact cash flows of existing loans in the system.'
                }
            });
            dialogRef.afterClosed().subscribe((result) => {
                if (result && result.response) {
                    if (this._dialogData.operation === 'add') {
                        referenceInterestFormValue.referenceInterestRate = this.selectedReferenceInterestType.id;
                        this._referenceInterestValueService.saveReferenceInterestValue(referenceInterestFormValue).subscribe(
                            (response: any) => {
                                this._matSnackBar.open('Reference Interest Value Added Successfully', 'Close', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            },
                            (error: any) => {
                                this._matSnackBar.open(error.error.message, 'Close', { duration: 7000 });
                            });
                    }
                    else {
                        this.selectedReferenceInterestValue.interestRate = referenceInterestFormValue.interestRate;
                        var referenceInterestValueToUpdate = {
                            id: this.selectedReferenceInterestValue.id,
                            interestRate: referenceInterestFormValue.interestRate,
                            validFromDate: referenceInterestFormValue.validFromDate
                        }
                        this._referenceInterestValueService.updateReferenceInterestValue(referenceInterestValueToUpdate).subscribe((response: any) => {
                            this._matSnackBar.open('Reference Interest Value Updated Successfully', 'Close', { duration: 7000 });
                            this._dialogRef.close({ 'refresh': true });
                        });
                    }        
                }
            });
        }
    }
}
