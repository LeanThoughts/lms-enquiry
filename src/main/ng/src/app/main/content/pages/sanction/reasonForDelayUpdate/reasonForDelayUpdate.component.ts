import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SanctionService } from '../sanction.service';

@Component({
    selector: 'fuse-sanction-reason-for-delay-update-dialog',
    templateUrl: './reasonForDelayUpdate.component.html',
    styleUrls: ['./reasonForDelayUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SanctionReasonForDelayUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Reason for Delay';

    loanApplicationId = '';
    selectedReason: any;

    reasonForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _sanctionService: SanctionService,
        public _dialogRef: MatDialogRef<SanctionReasonForDelayUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedReason = Object.assign({}, _dialogData.selectedReason);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedReason !== undefined) {
            if (_dialogData.operation === 'updateReason') {
                this.dialogTitle = 'Modify Reason for Delay';
            }
        }
        this.reasonForm = this._formBuilder.group({
            date: [this.selectedReason.date || ''],
            reason: [this.selectedReason.reason || ''],
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.reasonForm.valid) {
            var reasonForDelay = this.reasonForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(reasonForDelay.date);
            reasonForDelay.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addReason') {
                reasonForDelay.loanApplicationId = this.loanApplicationId;
                this._sanctionService.createSanctionReasonForDelay(reasonForDelay).subscribe(() => {
                    this._matSnackBar.open('Reason for delay added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedReason.reason = reasonForDelay.reason;
                this.selectedReason.date = reasonForDelay.date;
                this._sanctionService.updateSanctionReasonForDelay(this.selectedReason).subscribe(() => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
