import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';

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

    today = new Date();
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
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
            file: ['']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.formalRequestForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.formalRequestForm.valid) {
            if (this.formalRequestForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.formalRequestForm.get('file').value);
                this._applicationFeeService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveFormalRequest(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addSiteVisit') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveFormalRequest('');
                }
            }
        }
    }

    /**
     * submit()
     */
    saveFormalRequest(fileReference: string): void {
        if (this.formalRequestForm.valid) {
            var formalRequest = this.formalRequestForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(formalRequest.uploadDate);
            formalRequest.uploadDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(formalRequest.documentLetterDate);
            formalRequest.documentLetterDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(formalRequest.documentReceivedDate);
            formalRequest.documentReceivedDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            
            formalRequest.fileReference = fileReference;

            if (this._dialogData.operation === 'addFormalRequest') {
                formalRequest.loanApplicationId = this.loanApplicationId;
                this._applicationFeeService.createFormalRequest(formalRequest).subscribe(() => {
                    this._matSnackBar.open('Formal Request details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedFormalRequest.uploadDate = formalRequest.uploadDate;
                this.selectedFormalRequest.documentLetterDate = formalRequest.documentLetterDate;
                this.selectedFormalRequest.documentReceivedDate = formalRequest.documentReceivedDate;
                this.selectedFormalRequest.documentName = formalRequest.documentName;
                if (formalRequest.fileReference !== '') {
                    this.selectedFormalRequest.fileReference = formalRequest.fileReference;
                }
                this._applicationFeeService.updateFormalRequest(this.selectedFormalRequest).subscribe(() => {
                    this._matSnackBar.open('Formal Request details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
