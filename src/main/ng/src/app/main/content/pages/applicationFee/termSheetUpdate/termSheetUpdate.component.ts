import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';

@Component({
    selector: 'fuse-term-sheet-update-dialog',
    templateUrl: './termSheetUpdate.component.html',
    styleUrls: ['./termSheetUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class TermSheetUpdateDialogComponent implements OnInit {

    dialogTitle = '';

    loanApplicationId = '';
    selectedTermSheet: any;

    issuanceForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        public _dialogRef: MatDialogRef<TermSheetUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedTermSheet = Object.assign({}, _dialogData.selectedTermSheet);
        this.loanApplicationId = _dialogData.loanApplicationId;

        this.issuanceForm = this._formBuilder.group({
            issuanceDate: [this.selectedTermSheet.issuanceDate || ''],
            acceptanceDate: [this.selectedTermSheet.acceptanceDate || ''],
            status: [this.selectedTermSheet.status || ''],
            file: ['']
        });

        if (_dialogData.operation === 'modifyDraft')
            this.dialogTitle = 'Modify Draft Term Sheet';
        else if (_dialogData.operation === 'modifyAcceptance')
            this.dialogTitle = "Modify Acceptance of Term Sheet";
        else if (_dialogData.operation === 'newDraft') {
            this.dialogTitle = 'New Draft Term Sheet';
            this.issuanceForm.controls.status.setValue('Draft');
        }
        else {
            this.dialogTitle = "New Acceptance of Term Sheet";
            this.issuanceForm.controls.status.setValue('Final');
        }
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
            this.issuanceForm.get('file').setValue(file);
        }
    }
    
    /**
     * submit()
     */
    submit(): void {
        if (this.issuanceForm.valid) {
            if (this.issuanceForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.issuanceForm.get('file').value);
                this._applicationFeeService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveTermSheet(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation.includes('new')) {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveTermSheet('');
                }
            }
        }
    }

    /**
     * saveTermSheet()
     */
    saveTermSheet(fileReference: string) {
        if (this.issuanceForm.valid) {
            var termSheet = this.issuanceForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(termSheet.issuanceDate);
            termSheet.issuanceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(termSheet.acceptanceDate);
            termSheet.acceptanceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            
            termSheet.fileReference = fileReference;

            if (this._dialogData.operation.includes('new')) {
                termSheet.loanApplicationId = this.loanApplicationId;
                this._applicationFeeService.createTermSheet(termSheet).subscribe(() => {
                    this._matSnackBar.open('Term Sheet details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedTermSheet.issuanceDate = termSheet.issuanceDate;
                this.selectedTermSheet.acceptanceDate = termSheet.acceptanceDate;
                this.selectedTermSheet.status = termSheet.status;
                if (termSheet.fileReference !== '') {
                    this.selectedTermSheet.fileReference = termSheet.fileReference;
                }
                this._applicationFeeService.updateTermSheet(this.selectedTermSheet).subscribe(() => {
                    this._matSnackBar.open('Term Sheet details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
