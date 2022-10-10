import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../enquiryAction.service';

@Component({
  selector: 'fuse-other-details-update',
  templateUrl: './otherDetailsUpdate.component.html',
  styleUrls: ['./otherDetailsUpdate.component.scss']
})
export class OtherDetailsUpdateComponent {

    dialogTitle = "Other Details";

    _otherDetails: any;
    _otherDetailsForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<OtherDetailsUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._otherDetails = Object.assign({}, _dialogData.otherDetails);

        this._otherDetailsForm = _formBuilder.group({
            nameOfSourcingCompany: [ this._otherDetails.nameOfSourcingCompany || '' ],
            contactPersonName: [ this._otherDetails.contactPersonName || '' ],
            contactNumber: [ this._otherDetails.contactNumber || '' ],
            email: [ this._otherDetails.email || '' ],           
            enquiryDate: [ this._otherDetails.enquiryDate || undefined ],
            rating: [ this._otherDetails.rating || '' ],
            creditStanding: [ this._otherDetails.creditStanding || '' ],
            creditStandingInstruction: [ this._otherDetails.creditStandingInstruction || '' ],
            creditStandingText: [ this._otherDetails.creditStandingText || '' ],
            ratingDate: [ this._otherDetails.ratingDate || undefined ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._otherDetailsForm.valid) {
            var formValues = this._otherDetailsForm.value;

            var dt = new Date(formValues.enquiryDate);
            formValues.enquiryDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            dt = new Date(formValues.ratingDate);
            formValues.ratingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._otherDetails) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.loanApplicationId = this._dialogData.loanApplicationId;
                this._enquiryActionService.createOtherDetails(formValues).subscribe(response => {
                    this._matSnackBar.open('Other details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'otherDetails': response });
                });
            }
            else {
                console.log('updating');
                this._otherDetails.nameOfSourcingCompany = formValues.nameOfSourcingCompany;
                this._otherDetails.contactPersonName = formValues.contactPersonName;
                this._otherDetails.contactNumber = formValues.contactNumber;
                this._otherDetails.email = formValues.email;
                this._otherDetails.enquiryDate = formValues.enquiryDate;
                this._otherDetails.rating = formValues.rating;
                this._otherDetails.creditStanding = formValues.creditStanding;
                this._otherDetails.creditStandingInstruction = formValues.creditStandingInstruction;
                this._otherDetails.creditStandingText = formValues.creditStandingText;
                this._otherDetails.ratingDate = formValues.ratingDate;
                this._enquiryActionService.updateOtherDetails(this._otherDetails).subscribe(response => {
                    this._matSnackBar.open('Other details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true, 'otherDetails': response });
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
