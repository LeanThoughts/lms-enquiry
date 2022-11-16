import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-share-holder-update',
  templateUrl: './shareHolderUpdate.component.html',
  styleUrls: ['./shareHolderUpdate.component.scss']
})
export class ShareHolderUpdateComponent {

    dialogTitle = "Share Holder Details";

    _shareHolder: any;
    _shareHolderUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder,
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<ShareHolderUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected collateral details from the dialog's data attribute
        console.log('_dialogData', _dialogData);
        this._shareHolder = Object.assign({}, _dialogData.shareHolder);

        this._shareHolderUpdateForm = _formBuilder.group({
            companyName: [ this._shareHolder.companyName || '' ],
            equityCapital: [ this._shareHolder.equityCapital || '' ],
            percentageHolding: [ this._shareHolder.percentageHolding || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this._shareHolderUpdateForm.valid) {
            var formValues = this._shareHolderUpdateForm.value;

            if (JSON.stringify(this._shareHolder) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._dialogData.projectProposalId;
                this._enquiryActionService.createShareHolder(formValues).subscribe(response => {
                    this._matSnackBar.open('Share holder details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                console.log('updating');
                this._shareHolder.companyName = formValues.companyName;
                this._shareHolder.equityCapital = formValues.equityCapital;
                this._shareHolder.percentageHolding= formValues.percentageHolding;
                this._enquiryActionService.updateShareHolder(this._shareHolder).subscribe(response => {
                    this._matSnackBar.open('Share holder details updated successfully.', 'OK', { duration: 7000 });
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
