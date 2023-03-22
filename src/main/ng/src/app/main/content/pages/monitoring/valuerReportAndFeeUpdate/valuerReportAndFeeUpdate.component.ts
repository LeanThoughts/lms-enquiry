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
    selector: 'fuse-valuer-report-fee-update-dialog',
    templateUrl: './valuerReportAndFeeUpdate.component.html',
    styleUrls: ['./valuerReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ValuerReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add Valuer Report Submission';

    selectedValuer: LIEModel;
    selectedValuerReportAndFee: LIEReportAndFeeModel;

    valuerUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<ValuerReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedValuer = _dialogData.selectedValuer;
        if (_dialogData.selectedValuerReportAndFee !== undefined) {
            this.selectedValuerReportAndFee = Object.assign({}, _dialogData.selectedValuerReportAndFee);
            this.dialogTitle = 'Modify Valuer Report Submission';
        }
        else {
            this.selectedValuerReportAndFee = new LIEReportAndFeeModel({});
        }

        this.valuerUpdateForm = _formBuilder.group({
            reportType: [this.selectedValuerReportAndFee.reportType],
            dateOfReceipt: [this.selectedValuerReportAndFee.dateOfReceipt || ''], 
            invoiceDate: [this.selectedValuerReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedValuerReportAndFee.invoiceNo],
            feeAmount: [this.selectedValuerReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedValuerReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedValuerReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedValuerReportAndFee.documentTitle],
            nextReportDate: [this.selectedValuerReportAndFee.nextReportDate || ''],
            documentType: [this.selectedValuerReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedValuerReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedValuerReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedValuerReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedValuerReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedValuerReportAndFee.percentageCompletion || ''],
            remarks: [this.selectedValuerReportAndFee.remarks || '']
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
            this.valuerUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.valuerUpdateForm.valid) {
            if (this.valuerUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.valuerUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveValuerReportAndFee(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addValuerReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveValuerReportAndFee('');
                }
            }
        }
    }
    
    /**
     * saveValuerReportAndFee()
     */
    saveValuerReportAndFee(fileReference: string): void {
        // To solve the utc time zone issue
        var valuerReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.valuerUpdateForm.value);
        var dt = new Date(valuerReportAndFee.dateOfReceipt);
        valuerReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(valuerReportAndFee.invoiceDate);
        valuerReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(valuerReportAndFee.nextReportDate);
        valuerReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(valuerReportAndFee.reportDate);
        valuerReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        if (this._dialogData.operation === 'addValuerReportAndFee') {
            valuerReportAndFee.fileReference = fileReference;
            this._loanMonitoringService.saveValuerReportAndFee(valuerReportAndFee, this.selectedValuer.id, this._dialogData.module).subscribe((data) => {
                this._matSnackBar.open('Valuer report added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            if (fileReference !== '' ) {
                this.selectedValuerReportAndFee.fileReference = fileReference;
            }
            this.selectedValuerReportAndFee.reportType = valuerReportAndFee.reportType;
            this.selectedValuerReportAndFee.dateOfReceipt = valuerReportAndFee.dateOfReceipt;
            this.selectedValuerReportAndFee.invoiceDate = valuerReportAndFee.invoiceDate;
            this.selectedValuerReportAndFee.invoiceNo = valuerReportAndFee.invoiceNo;
            this.selectedValuerReportAndFee.feeAmount = valuerReportAndFee.feeAmount;
            this.selectedValuerReportAndFee.statusOfFeeReceipt = valuerReportAndFee.statusOfFeeReceipt;
            this.selectedValuerReportAndFee.statusOfFeePaid = valuerReportAndFee.statusOfFeePaid;
            this.selectedValuerReportAndFee.documentTitle = valuerReportAndFee.documentTitle;
            this.selectedValuerReportAndFee.documentType = valuerReportAndFee.documentType;
            this.selectedValuerReportAndFee.nextReportDate = valuerReportAndFee.nextReportDate;
            this.selectedValuerReportAndFee.reportDate = valuerReportAndFee.reportDate;
            this.selectedValuerReportAndFee.percentageCompletion = valuerReportAndFee.percentageCompletion;
            this.selectedValuerReportAndFee.remarks = valuerReportAndFee.remarks;
            this._loanMonitoringService.updateValuerReportAndFee(this.selectedValuerReportAndFee, this._dialogData.module).subscribe(() => {
                this._matSnackBar.open('Valuer report updated successfully.', 'OK', { duration: 7000 });
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
