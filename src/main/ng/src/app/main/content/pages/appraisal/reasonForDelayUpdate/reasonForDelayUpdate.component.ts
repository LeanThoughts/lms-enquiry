import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-reason-for-delay-update',
  templateUrl: './reasonForDelayUpdate.component.html',
  styleUrls: ['./reasonForDelayUpdate.component.scss']
})
export class ReasonForDelayUpdateComponent {

    dialogTitle = "Update Reason For Delay";

    _reasonForDelay: any;
    _reasonForDelayForm: FormGroup;

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
        this._reasonForDelay = Object.assign({}, _dialogData.reasonForDelay);

        this._reasonForDelayForm = _formBuilder.group({
            statusOfProposal: [ this._reasonForDelay.statusOfProposal || '' ],
            date: [ this._reasonForDelay.date || undefined ],
            heldBy: [ this._reasonForDelay.heldBy || '' ],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._reasonForDelayForm.valid) {
            var formValues = this._reasonForDelayForm.value;

            var dt = new Date(formValues.date);
            formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._reasonForDelay) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createReasonForDelay(formValues).subscribe(response => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'reasonForDelay': response });
                });
            }
            else {
                console.log('updating');
                this._reasonForDelay.statusOfProposal = formValues.statusOfProposal;
                this._reasonForDelay.date = formValues.date;
                this._reasonForDelay.heldBy = formValues.heldBy;
                this._loanAppraisalService.updateReasonForDelay(this._reasonForDelay).subscribe(response => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'reasonForDelay': response });
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
