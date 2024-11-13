import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-credit-rating-update',
  templateUrl: './creditRatingUpdate.component.html',
  styleUrls: ['./creditRatingUpdate.component.scss']
})
export class CreditRatingUpdateComponent {

    dialogTitle = "Credit Rating";

    _selectedCreditRating: any;

    _creditRatingCodes: any;
    _creditRatingAgencies: any;
    _creditRatingUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder,
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<CreditRatingUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        this._enquiryActionService.getCreditRatingCodes().subscribe(response => {
            this._creditRatingCodes = response.creditRatingCodes;
        });
        this._enquiryActionService.getCreditRatingAgencies().subscribe(response => {
            this._creditRatingAgencies = response.creditRatingAgencies;
        });

        // Fetch selected collateral details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._selectedCreditRating = Object.assign({}, _dialogData._creditRating);

        this._creditRatingUpdateForm = _formBuilder.group({
            creditRating: [ this._selectedCreditRating.creditRating || '' ],
            creditRatingAgency: [ this._selectedCreditRating.creditRatingAgency || '' ],
            creditStandingInstruction: [ this._selectedCreditRating.creditStandingInstruction || '' ],
            creditStandingText: [ this._selectedCreditRating.creditStandingText || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._creditRatingUpdateForm.valid) {
            var formValues = this._creditRatingUpdateForm.value;

            if (JSON.stringify(this._selectedCreditRating) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._dialogData.projectProposalId;
                this._enquiryActionService.createCreditRating(formValues).subscribe(response => {
                    this._matSnackBar.open('Credit rating created successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('updating');
                this._selectedCreditRating.creditRating = formValues.creditRating;
                this._selectedCreditRating.creditRatingAgency = formValues.creditRatingAgency;
                this._selectedCreditRating.creditStandingInstruction= formValues.creditStandingInstruction;
                this._selectedCreditRating.creditStandingText= formValues.creditStandingText;
                this._enquiryActionService.updateCreditRating(this._selectedCreditRating).subscribe(response => {
                    this._matSnackBar.open('Credit rating updated successfully.', 'OK', { duration: 7000 });
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
