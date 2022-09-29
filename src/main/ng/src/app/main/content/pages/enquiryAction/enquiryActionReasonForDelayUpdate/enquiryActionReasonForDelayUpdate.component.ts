import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../enquiryAction.service';

@Component({
  selector: 'fuse-enquiry-action-reason-for-delay-update',
  templateUrl: './enquiryActionReasonForDelayUpdate.component.html',
  styleUrls: ['./enquiryActionReasonForDelayUpdate.component.scss']
})
export class EnquiryActionReasonForDelayUpdateComponent {

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
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<EnquiryActionReasonForDelayUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        console.log('_dialogData', _dialogData);
        this._reasonForDelay = Object.assign({}, _dialogData.reasonForDelay);

        this._reasonForDelayForm = _formBuilder.group({
            reason: [ this._reasonForDelay.reason || '' ],
            date: [ this._reasonForDelay.date || undefined ],
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
                this._enquiryActionService.createReasonForDelay(formValues).subscribe(response => {
                    this._matSnackBar.open('Reason for delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'reasonForDelay': response });
                });
            }
            else {
                console.log('updating');
                this._reasonForDelay.reason = formValues.reason;
                this._reasonForDelay.date = formValues.date;
                this._enquiryActionService.updateReasonForDelay(this._reasonForDelay).subscribe(response => {
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
