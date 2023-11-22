import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../../iccApproval/iccApproval.service';

@Component({
    selector: 'fuse-formal-request-update-dialog',
    templateUrl: './formalRequestUpdate.component.html',
    styleUrls: ['./formalRequestUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class FormalRequestUpdateDialogComponent implements OnInit {

    dialogTitle = 'New Formal Request';

    loanApplicationId = '';
    selectedFormalRequest: any;

    formalRequestForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<FormalRequestUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedFormalRequest = Object.assign({}, _dialogData.selectedFormalRequest);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedFormalRequest !== undefined) {
            if (_dialogData.operation === 'updateFormalRequest') {
                this.dialogTitle = 'Modify Formal Request';
            }
        }

        this.formalRequestForm = this._formBuilder.group({
            documentName: [this.selectedFormalRequest.documentName || ''],
            uploadDate: [this.selectedFormalRequest.uploadDate || ''],
            documentLetterDate: [this.selectedFormalRequest.documentLetterDate || ''],
            documentReceivedDate: [this.selectedFormalRequest.documentReceivedDate || ''],
            document: [this.selectedFormalRequest.document || ''],
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.formalRequestForm.valid) {
            var formalRequest = this.formalRequestForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(formalRequest.uploadDate);
            formalRequest.uploadDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(formalRequest.documentLetterDate);
            formalRequest.documentLetterDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(formalRequest.documentReceivedDate);
            formalRequest.documentReceivedDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addICCApproval') {
                formalRequest.loanApplicationId = this.loanApplicationId;
                // this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc added successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });
            }
            else {
                this.selectedFormalRequest.uploadDate = formalRequest.uploadDate;
                this.selectedFormalRequest.documentLetterDate = formalRequest.documentLetterDate;
                this.selectedFormalRequest.documentReceivedDate = formalRequest.documentReceivedDate;
                this.selectedFormalRequest.documentName = formalRequest.documentName;
                this.selectedFormalRequest.document = formalRequest.document;
                // this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc updated successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });            
            }
        }
    }
}
