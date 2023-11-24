import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';
import { log } from 'console';

@Component({
    selector: 'fuse-icc-reason-for-delay-update-dialog',
    templateUrl: './iccReasonForDelayUpdate.component.html',
    styleUrls: ['./iccReasonForDelayUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCReasonForDelayUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Reason For Delay';

    loanApplicationId = '';
    selectedReasonForDelay: any;

    reasonForDelayForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCReasonForDelayUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedReasonForDelay = Object.assign({}, _dialogData.selectedReasonForDelay);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (this.selectedReasonForDelay.id !== undefined) {
            this.dialogTitle = 'Modify Reason For Delay';
        }
        this.reasonForDelayForm = this._formBuilder.group({
            reasonForDelay: [this.selectedReasonForDelay.reasonForDelay || ''],
            date: [this.selectedReasonForDelay.date || '']
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
        if (this.reasonForDelayForm.valid) {
            var reasonForDelay = this.reasonForDelayForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(reasonForDelay.date);
            reasonForDelay.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this.selectedReasonForDelay.id === undefined) {
                console.log('adding reason for delay');
                reasonForDelay.loanApplicationId = this.loanApplicationId;
                this._iccApprovalService.createReasonForDelay(reasonForDelay).subscribe(() => {
                    this._matSnackBar.open('Reason for Delay added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('updating reason for delay');
                this.selectedReasonForDelay.date = reasonForDelay.date;
                this.selectedReasonForDelay.reasonForDelay = reasonForDelay.reasonForDelay;
                this._iccApprovalService.updateReasonForDelay(this.selectedReasonForDelay).subscribe(() => {
                    this._matSnackBar.open('Reason for Delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
