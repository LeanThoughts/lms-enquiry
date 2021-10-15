import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
  selector: 'fuse-customer-rejection-update',
  templateUrl: './customerRejection.component.html',
  styleUrls: ['./customerRejection.component.scss']
})
export class CustomerRejectionUpdateComponent {

    dialogTitle = "Update Reason For Delay";

    _customerRejection: any;
    _customerRejectionForm: FormGroup;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<CustomerRejectionUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._customerRejection = Object.assign({}, _dialogData.customerRejection);

        this._customerRejectionForm = _formBuilder.group({
            reasonForRejection: [ this._customerRejection.reasonForRejection || '' ],
            date: [ this._customerRejection.date || undefined ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._customerRejectionForm.valid) {
            var formValues = this._customerRejectionForm.value;

            var dt = new Date(formValues.date);
            formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._customerRejection) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._loanAppraisalService.createCustomerRejection(formValues).subscribe(response => {
                    this._matSnackBar.open('Customer rejection details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'customerRejection': response });
                });
            }
            else {
                console.log('updating');
                this._customerRejection.reasonForRejection = formValues.reasonForRejection;
                this._customerRejection.date = formValues.date;
                this._loanAppraisalService.updateCustomerRejection(this._customerRejection).subscribe(response => {
                    this._matSnackBar.open('Customer rejection details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'customerRejection': response });
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
