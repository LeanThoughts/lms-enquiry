import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../loanAppraisal.service';
import { appraisalReasonsForDelay } from '../loanAppraisal.constants';

@Component({
  selector: 'fuse-reason-for-delay-update',
  templateUrl: './reasonForDelayUpdate.component.html',
  styleUrls: ['./reasonForDelayUpdate.component.scss']
})
export class ReasonForDelayUpdateComponent {

    dialogTitle = "Update Reason For Delay";

    _reasonForDelayForm: FormGroup;
    _reasonsForDelay = appraisalReasonsForDelay;

    _selectedReasonForDelay: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<ReasonForDelayUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        if (_dialogData.operation === 'update') {
            this._selectedReasonForDelay = Object.assign({}, _dialogData.selectedReasonForDelay);
        }
        else {
            this.dialogTitle = "Add Reason For Delay";
            this._selectedReasonForDelay = {};
        }

        this._reasonForDelayForm = _formBuilder.group({
            statusOfProposal: [ this._selectedReasonForDelay.statusOfProposal || null ],
            date: [ this._selectedReasonForDelay.date || null ],
            heldBy: [ this._selectedReasonForDelay.heldBy || null ],
            reasonForDelay: [ this._selectedReasonForDelay.reasonForDelay || null ],
            remarks: [ this._selectedReasonForDelay.remarks || null ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._reasonForDelayForm.valid) {
            var formValues = this._reasonForDelayForm.value;

            if (formValues.date) {
                var dt = new Date(formValues.date);
                formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (this._dialogData.operation === 'add') { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createReasonForDelay(formValues).subscribe(response => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('updating');
                this._selectedReasonForDelay.statusOfProposal = formValues.statusOfProposal;
                this._selectedReasonForDelay.date = formValues.date;
                this._selectedReasonForDelay.heldBy = formValues.heldBy;
                this._selectedReasonForDelay.reasonForDelay = formValues.reasonForDelay;
                this._selectedReasonForDelay.remarks = formValues.remarks;
                this._loanAppraisalService.updateReasonForDelay(this._selectedReasonForDelay).subscribe(response => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }
}
