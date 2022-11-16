import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-collateral-detail-update',
  templateUrl: './collateralDetailUpdate.component.html',
  styleUrls: ['./collateralDetailUpdate.component.scss']
})
export class CollateralDetailUpdateComponent {

    dialogTitle = "Collateral Details";

    _collateralDetail: any;
    _collateralDetailUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<CollateralDetailUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected collateral details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._collateralDetail = Object.assign({}, _dialogData.collateralDetail);

        this._collateralDetailUpdateForm = _formBuilder.group({
            collateralType: [ this._collateralDetail.collateralType || '' ],
            details: [ this._collateralDetail.details || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._collateralDetailUpdateForm.valid) {
            var formValues = this._collateralDetailUpdateForm.value;

            if (JSON.stringify(this._collateralDetail) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._dialogData.projectProposalId;
                this._enquiryActionService.createCollateralDetail(formValues).subscribe(response => {
                    this._matSnackBar.open('Collateral details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('updating');
                this._collateralDetail.collateralType = formValues.collateralType;
                this._collateralDetail.details = formValues.details;
                this._enquiryActionService.updateCollateralDetail(this._collateralDetail).subscribe(response => {
                    this._matSnackBar.open('Collateral details updated successfully.', 'OK', { duration: 7000 });
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
