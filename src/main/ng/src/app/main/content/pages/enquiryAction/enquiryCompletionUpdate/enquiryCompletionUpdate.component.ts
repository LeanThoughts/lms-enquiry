import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../enquiryAction.service';

@Component({
  selector: 'fuse-enquiry-completion-update',
  templateUrl: './enquiryCompletionUpdate.component.html',
  styleUrls: ['./enquiryCompletionUpdate.component.scss']
})
export class EnquiryCompletionUpdateComponent {

    dialogTitle = "Enquiry Completion";

    _enquiryCompletion: any;
    _enquiryCompletionForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<EnquiryCompletionUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._enquiryCompletion = Object.assign({}, _dialogData.enquiryCompletion);

        this._enquiryCompletionForm = _formBuilder.group({
            productType: [ this._enquiryCompletion.productType || '' ],
            term: [ this._enquiryCompletion.term || '' ],
            remarks: [ this._enquiryCompletion.remarks || '' ],
            date: [ this._enquiryCompletion.date || undefined ],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._enquiryCompletionForm.valid) {
            var formValues = this._enquiryCompletionForm.value;

            var dt = new Date(formValues.date);
            formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._enquiryCompletion) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._enquiryActionService.createEnquiryCompletion(formValues).subscribe(response => {
                    this._matSnackBar.open('Enquiry Completion details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'enquiryCompletion': response });
                });
            }
            else {
                console.log('updating');
                this._enquiryCompletion.productType = formValues.productType;
                this._enquiryCompletion.term = formValues.term;
                this._enquiryCompletion.remarks = formValues.remarks;
                this._enquiryCompletion.date = formValues.date;
                this._enquiryActionService.updateEnquiryCompletion(this._enquiryCompletion).subscribe(response => {
                    this._matSnackBar.open('Enquiry Completion details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'enquiryCompletion': response });
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
