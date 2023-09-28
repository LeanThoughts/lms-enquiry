import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SanctionService } from '../sanction.service';
import { BoardApprovalService } from '../../boardApproval/boardApproval.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-sanction-letter-update-dialog',
    templateUrl: './sanctionLetterUpdate.component.html',
    styleUrls: ['./sanctionLetterUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SanctionLetterUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Sanction Letter';

    loanApplicationId = '';
    selectedSanctionLetter: any;

    sanctionLetterForm: FormGroup;

    documentTypes = LoanMonitoringConstants.documentTypes;
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _sanctionService: SanctionService, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<SanctionLetterUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedSanctionLetter = Object.assign({}, _dialogData.selectedSanctionLetter);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedSanctionLetter !== undefined) {
            if (_dialogData.operation === 'updateSanctionLetter') {
                this.dialogTitle = 'Modify Sanction Letter';
            }
        }
        this.sanctionLetterForm = this._formBuilder.group({
            serialNumber: [this.selectedSanctionLetter.serialNumber || ''],
            sanctionLetterIssueDate: [this.selectedSanctionLetter.sanctionLetterIssueDate || ''],
            borrowerRequestLetterDate: [this.selectedSanctionLetter.borrowerRequestLetterDate || ''],
            sanctionLetterAcceptanceDate: [this.selectedSanctionLetter.sanctionLetterAcceptanceDate || ''],
            documentType: [this.selectedSanctionLetter.documentType || ''],
            documentTitle: [this.selectedSanctionLetter.documentTitle || ''],
            fileReference: [this.selectedSanctionLetter.fileReference || ''],
            remarks: [this.selectedSanctionLetter.remarks || ''],
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
            this.sanctionLetterForm.get('file').setValue(file);
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }  

    /**
     * submit()
     */
    submit(): void {
        if (this.sanctionLetterForm.valid) {
            if (this.sanctionLetterForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.sanctionLetterForm.get('file').value);      
                this._sanctionService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveSanctionLetter(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addCLAReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveSanctionLetter('');
                }
            }
        }
    }

    saveSanctionLetter(fileReference: string): void {
        if (this.sanctionLetterForm.valid) {
            var sanctionLetter = this.sanctionLetterForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(sanctionLetter.sanctionLetterIssueDate);
            sanctionLetter.sanctionLetterIssueDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.borrowerRequestLetterDate);
            sanctionLetter.borrowerRequestLetterDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.sanctionLetterAcceptanceDate);
            sanctionLetter.sanctionLetterAcceptanceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addSanctionLetter') {
                sanctionLetter.loanApplicationId = this.loanApplicationId;
                sanctionLetter.fileReference = fileReference;
                this._sanctionService.createSanctionLetter(sanctionLetter).subscribe(() => {
                    this._matSnackBar.open('Sanction letter added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedSanctionLetter.fileReference = fileReference;
                }
                this.selectedSanctionLetter.sanctionLetterIssueDate = sanctionLetter.sanctionLetterIssueDate;
                this.selectedSanctionLetter.borrowerRequestLetterDate = sanctionLetter.borrowerRequestLetterDate;
                this.selectedSanctionLetter.sanctionLetterAcceptanceDate = sanctionLetter.sanctionLetterAcceptanceDate;
                this.selectedSanctionLetter.documentType = sanctionLetter.documentType;
                this.selectedSanctionLetter.documentTitle = sanctionLetter.documentTitle;
                this.selectedSanctionLetter.fileReference = sanctionLetter.fileReference;
                this.selectedSanctionLetter.remarks = sanctionLetter.remarks;
                this._sanctionService.updateSanctionLetter(this.selectedSanctionLetter).subscribe(() => {
                    this._matSnackBar.open('Sanction letter updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
