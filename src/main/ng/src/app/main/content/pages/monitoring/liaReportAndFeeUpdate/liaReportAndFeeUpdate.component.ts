import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { LIEReportAndFeeModel } from 'app/main/content/model/lieReportAndFee.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-lia-report-fee-update-dialog',
    templateUrl: './liaReportAndFeeUpdate.component.html',
    styleUrls: ['./liaReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LIAReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add LIA Report Submission';

    selectedLIA: LIEModel;
    selectedLIAReportAndFee: LIEReportAndFeeModel;

    liaUpdateForm: FormGroup;

    reportTypes = LoanMonitoringConstants.reportTypes;
    feePaidStatuses = LoanMonitoringConstants.feePaidStatuses;
    feeReceiptStatuses = LoanMonitoringConstants.feeReceiptStatuses;
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<LIAReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedLIA = _dialogData.selectedLIA;
        if (_dialogData.selectedLIAReportAndFee !== undefined) {
            this.selectedLIAReportAndFee = Object.assign({}, _dialogData.selectedLIAReportAndFee);
            this.dialogTitle = 'Modify LIA Report Submission';
        }
        else {
            this.selectedLIAReportAndFee = new LIEReportAndFeeModel({});
        }

        this.liaUpdateForm = _formBuilder.group({
            reportType: [this.selectedLIAReportAndFee.reportType],
            dateOfReceipt: [this.selectedLIAReportAndFee.dateOfReceipt || ''], 
            invoiceDate: [this.selectedLIAReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedLIAReportAndFee.invoiceNo],
            feeAmount: [this.selectedLIAReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedLIAReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedLIAReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedLIAReportAndFee.documentTitle],
            nextReportDate: [this.selectedLIAReportAndFee.nextReportDate || ''],
            documentType: [this.selectedLIAReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedLIAReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedLIAReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedLIAReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedLIAReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedLIAReportAndFee.percentageCompletion || ''],
            remarks: [this.selectedLIAReportAndFee.remarks || '']
        });

        // Sort document types array
        this.documentTypes = this.documentTypes.sort((obj1, obj2) => {
                return obj1.value.localeCompare(obj2.value);
        });
    }

    /**
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.liaUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.liaUpdateForm.valid) {
            if (this.liaUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.liaUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLIAReportAndFee(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addLIAReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveLIAReportAndFee('');
                }
            }
        }
    }
    
    /**
     * saveLIAReportAndFee()
     */
    saveLIAReportAndFee(fileReference: string): void {
        // To solve the utc time zone issue
        var liaReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.liaUpdateForm.value);
        var dt = new Date(liaReportAndFee.dateOfReceipt);
        liaReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(liaReportAndFee.invoiceDate);
        liaReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(liaReportAndFee.nextReportDate);
        liaReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(liaReportAndFee.reportDate);
        liaReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        if (this._dialogData.operation === 'addLIAReportAndFee') {
            liaReportAndFee.fileReference = fileReference;
            this._loanMonitoringService.saveLIAReportAndFee(liaReportAndFee, this.selectedLIA.id, this._dialogData.module).subscribe((data) => {
                this._matSnackBar.open('LIA report added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            if (fileReference !== '' ) {
                this.selectedLIAReportAndFee.fileReference = fileReference;
            }
            this.selectedLIAReportAndFee.reportType = liaReportAndFee.reportType;
            this.selectedLIAReportAndFee.dateOfReceipt = liaReportAndFee.dateOfReceipt;
            this.selectedLIAReportAndFee.invoiceDate = liaReportAndFee.invoiceDate;
            this.selectedLIAReportAndFee.invoiceNo = liaReportAndFee.invoiceNo;
            this.selectedLIAReportAndFee.feeAmount = liaReportAndFee.feeAmount;
            this.selectedLIAReportAndFee.statusOfFeeReceipt = liaReportAndFee.statusOfFeeReceipt;
            this.selectedLIAReportAndFee.statusOfFeePaid = liaReportAndFee.statusOfFeePaid;
            this.selectedLIAReportAndFee.documentTitle = liaReportAndFee.documentTitle;
            this.selectedLIAReportAndFee.documentType = liaReportAndFee.documentType;
            this.selectedLIAReportAndFee.nextReportDate = liaReportAndFee.nextReportDate;
            this.selectedLIAReportAndFee.reportDate = liaReportAndFee.reportDate;
            this.selectedLIAReportAndFee.percentageCompletion = liaReportAndFee.percentageCompletion;
            this.selectedLIAReportAndFee.remarks = liaReportAndFee.remarks;
            this._loanMonitoringService.updateLIAReportAndFee(this.selectedLIAReportAndFee, this._dialogData.module).subscribe(() => {
                this._matSnackBar.open('LIA report updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }

    /**
     * getFileURL()
     * @param fileReference
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }  
}
