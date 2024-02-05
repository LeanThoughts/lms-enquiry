import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { DocumentationService } from '../documentation.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-llc-fee-update-dialog',
    templateUrl: './llcFeeUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LLCFeeUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add LLC Fee';

    selectedLLCFee: any;

    llcFeeForm: FormGroup;

    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _documentationService: DocumentationService,
        public _dialogRef: MatDialogRef<LLCFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected llc fee details from the dialog's data attribute.
        if (_dialogData.operation === 'addLLCFee') {
            this.dialogTitle = 'Add LLC Fee';
            this.selectedLLCFee = {};
        }
        else {
            this.dialogTitle = 'Modify LLC Fee';
            this.selectedLLCFee = Object.assign({}, _dialogData.selectedLLCFee);
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
        this.llcFeeForm = this._formBuilder.group({
            invoiceDate: [this.selectedLLCFee.invoiceDate || ''],
            invoiceNumber: [this.selectedLLCFee.invoiceNumber || ''],
            feeName: [this.selectedLLCFee.feeName || ''],
            feeAmount: [this.selectedLLCFee.feeAmount || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            statusOfFeeReceipt: [this.selectedLLCFee.statusOfFeeReceipt || ''],
            remarks: [this.selectedLLCFee.remarks || ''],
            documentName: [this.selectedLLCFee.documentName || ''],
            documentType: [this.selectedLLCFee.documentType || ''],
            file: ['']
        }); 
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.llcFeeForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.llcFeeForm.valid) {
            if (this.llcFeeForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.llcFeeForm.get('file').value);      
                this._documentationService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLegalCounsel(response.fileReference);
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
                    this.saveLegalCounsel('');
                }
            }
        }
    }

    /**
     * saveLegalCounsel()
     */
    saveLegalCounsel(fileReference: string): void {
        if (this.llcFeeForm.valid) {
            // To solve the utc time zone issue
            var llcFee = this.llcFeeForm.value;
            var dt = new Date(llcFee.invoiceDate);
            llcFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLLCFee') {
                llcFee.fileReference = fileReference;
                llcFee.loanApplicationId = this._dialogData.loanApplicationId;
                this._documentationService.createLLCFee(llcFee).subscribe(() => {
                    this._matSnackBar.open('LLC Fee added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedLLCFee.fileReference = fileReference;
                }    
                this.selectedLLCFee.invoiceDate  = llcFee.invoiceDate;
                this.selectedLLCFee.invoiceNumber = llcFee.invoiceNumber;
                this.selectedLLCFee.feeName = llcFee.feeName;
                this.selectedLLCFee.feeAmount = llcFee.feeAmount;
                this.selectedLLCFee.statusOfFeeReceipt = llcFee.statusOfFeeReceipt;
                this.selectedLLCFee.remarks = llcFee.remarks;
                this.selectedLLCFee.documentName = llcFee.documentName;
                this.selectedLLCFee.documentType = llcFee.documentType;

                this._documentationService.updateLLCFee(this.selectedLLCFee).subscribe(() => {
                    this._matSnackBar.open('LLC Fee updated successfully.', 'OK', { duration: 7000 });
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
