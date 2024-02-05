import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { DocumentationService } from '../documentation.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-legal-counsel-fee-update-dialog',
    templateUrl: './llcLegalCounselFeeUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LLCLegalCounselFeeUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add LLC Fee charged by Legal Counsel';

    selectedLegalCounselFee: any;

    legalCounselFeeForm: FormGroup;

    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _documentationService: DocumentationService,
        public _dialogRef: MatDialogRef<LLCLegalCounselFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected llc fee details from the dialog's data attribute.
        if (_dialogData.operation === 'addLegalCounselFee') {
            this.dialogTitle = 'Add LLC Fee charged by Legal Counsel';
            this.selectedLegalCounselFee = {};
        }
        else {
            this.dialogTitle = 'Modify LLC Fee charged by Legal Counsel';
            this.selectedLegalCounselFee = Object.assign({}, _dialogData.selectedLegalCounselFee);
        }

        // Sort document types array
        this.documentTypes = this.documentTypes.sort((obj1, obj2) => {
            return obj1.value.localeCompare(obj2.value);
        });    
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.legalCounselFeeForm = this._formBuilder.group({
            invoiceDate: [this.selectedLegalCounselFee.invoiceDate || ''],
            invoiceNumber: [this.selectedLegalCounselFee.invoiceNumber || ''],
            feeName: [this.selectedLegalCounselFee.feeName || ''],
            feeAmount: [this.selectedLegalCounselFee.feeAmount || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            statusOfFeeReceipt: [this.selectedLegalCounselFee.statusOfFeeReceipt || ''],
            remarks: [this.selectedLegalCounselFee.remarks || ''],
            documentName: [this.selectedLegalCounselFee.documentName || ''],
            documentType: [this.selectedLegalCounselFee.documentType || ''],
            file: ['']
        }); 
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.legalCounselFeeForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.legalCounselFeeForm.valid) {
            if (this.legalCounselFeeForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.legalCounselFeeForm.get('file').value);      
                this._documentationService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLegalCounselFee(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addLLCFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveLegalCounselFee('');
                }
            }
        }
    }

    /**
     * saveLegalCounselFee()
     */
    saveLegalCounselFee(fileReference: string): void {
        if (this.legalCounselFeeForm.valid) {
            // To solve the utc time zone issue
            var legalCounselFee = this.legalCounselFeeForm.value;
            var dt = new Date(legalCounselFee.invoiceDate);
            legalCounselFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLegalCounselFee') {
                legalCounselFee.fileReference = fileReference;
                legalCounselFee.loanApplicationId = this._dialogData.loanApplicationId;
                this._documentationService.createLegalCounselFee(legalCounselFee).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel Fee added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedLegalCounselFee.fileReference = fileReference;
                }    
                this.selectedLegalCounselFee.invoiceDate  = legalCounselFee.invoiceDate;
                this.selectedLegalCounselFee.invoiceNumber = legalCounselFee.invoiceNumber;
                this.selectedLegalCounselFee.feeName = legalCounselFee.feeName;
                this.selectedLegalCounselFee.feeAmount = legalCounselFee.feeAmount;
                this.selectedLegalCounselFee.statusOfFeeReceipt = legalCounselFee.statusOfFeeReceipt;
                this.selectedLegalCounselFee.remarks = legalCounselFee.remarks;
                this.selectedLegalCounselFee.documentName = legalCounselFee.documentName;
                this.selectedLegalCounselFee.documentType = legalCounselFee.documentType;

                this._documentationService.updateLegalCounselFee(this.selectedLegalCounselFee).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel Fee updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
