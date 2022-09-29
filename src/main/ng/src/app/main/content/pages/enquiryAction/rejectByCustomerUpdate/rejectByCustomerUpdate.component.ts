import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../enquiryAction.service';

@Component({
  selector: 'fuse-reject-by-customer-update',
  templateUrl: './rejectByCustomerUpdate.component.html',
  styleUrls: ['./rejectByCustomerUpdate.component.scss']
})
export class RejectByCustomerUpdateComponent {

    dialogTitle = "Rejected By Customer";

    _rejectBy: any;
    _rejectByForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<RejectByCustomerUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._rejectBy = Object.assign({}, _dialogData.rejectBy);

        this._rejectByForm = _formBuilder.group({
            rejectionCategory: [ this._rejectBy.rejectionCategory || '' ],
            rejectionDate: [ this._rejectBy.rejectionDate || undefined ],
            rejectionReason: [ this._rejectBy.rejectionReason || '' ],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._rejectByForm.valid) {
            var formValues = this._rejectByForm.value;

            var dt = new Date(formValues.rejectionDate);
            formValues.rejectionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._rejectBy) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._enquiryActionService.createRejectByCustomers(formValues).subscribe(response => {
                    this._matSnackBar.open('Reject by Customer details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'rejectBy': response });
                });
            }
            else {
                console.log('updating');
                this._rejectBy.rejectionCategory = formValues.rejectionCategory;
                this._rejectBy.rejectionDate = formValues.rejectionDate;
                this._rejectBy.rejectionReason = formValues.rejectionReason;
                this._enquiryActionService.updateRejectByCustomers(this._rejectBy).subscribe(response => {
                    this._matSnackBar.open('Reject by Customer details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'rejectBy': response });
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
