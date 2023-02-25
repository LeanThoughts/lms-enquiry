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
    selector: 'fuse-llc-report-fee-update-dialog',
    templateUrl: './llcReportAndFeeUpdate.component.html',
    styleUrls: ['./llcReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LLCReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add LLC Report Submission';

    selectedLLC: LIEModel;
    selectedLLCReportAndFee: LIEReportAndFeeModel;

    llcUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<LLCReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedLLC = _dialogData.selectedLLC;
        if (_dialogData.selectedLLCReportAndFee !== undefined) {
            this.selectedLLCReportAndFee = Object.assign({}, _dialogData.selectedLLCReportAndFee);
            this.dialogTitle = 'Modify LLC Report Submission';
        }
        else {
            this.selectedLLCReportAndFee = new LIEReportAndFeeModel({});
        }

        this.llcUpdateForm = _formBuilder.group({
            reportType: [this.selectedLLCReportAndFee.reportType],
            dateOfReceipt: [this.selectedLLCReportAndFee.dateOfReceipt || ''], 
            invoiceDate: [this.selectedLLCReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedLLCReportAndFee.invoiceNo],
            feeAmount: [this.selectedLLCReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedLLCReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedLLCReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedLLCReportAndFee.documentTitle],
            nextReportDate: [this.selectedLLCReportAndFee.nextReportDate || ''],
            documentType: [this.selectedLLCReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedLLCReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedLLCReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedLLCReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedLLCReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedLLCReportAndFee.percentageCompletion || ''],
            remarks: [this.selectedLLCReportAndFee.remarks || '']
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
            this.llcUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.llcUpdateForm.valid) {
            // To solve the utc time zone issue
            var llcReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.llcUpdateForm.value);
            var dt = new Date(llcReportAndFee.dateOfReceipt);
            llcReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(llcReportAndFee.invoiceDate);
            llcReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(llcReportAndFee.nextReportDate);
            llcReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(llcReportAndFee.reportDate);
            llcReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLLCReportAndFee') {
                if (this.llcUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.llcUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            llcReportAndFee.fileReference = response.fileReference;
                            this._loanMonitoringService.saveLLCReportAndFee(llcReportAndFee, this.selectedLLC.id).subscribe((data) => {
                                this._matSnackBar.open('LLC report added successfully.', 'OK', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this._loanMonitoringService.saveLLCReportAndFee(llcReportAndFee, this.selectedLLC.id).subscribe((data) => {
                        this._matSnackBar.open('LLC report added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
            else {
                if (this.llcUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.llcUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedLLCReportAndFee.reportType = llcReportAndFee.reportType;
                            this.selectedLLCReportAndFee.dateOfReceipt = llcReportAndFee.dateOfReceipt;
                            this.selectedLLCReportAndFee.invoiceDate = llcReportAndFee.invoiceDate;
                            this.selectedLLCReportAndFee.invoiceNo = llcReportAndFee.invoiceNo;
                            this.selectedLLCReportAndFee.feeAmount = llcReportAndFee.feeAmount;
                            this.selectedLLCReportAndFee.statusOfFeeReceipt = llcReportAndFee.statusOfFeeReceipt;
                            this.selectedLLCReportAndFee.statusOfFeePaid = llcReportAndFee.statusOfFeePaid;
                            this.selectedLLCReportAndFee.documentTitle = llcReportAndFee.documentTitle;
                            this.selectedLLCReportAndFee.documentType = llcReportAndFee.documentType;
                            this.selectedLLCReportAndFee.nextReportDate = llcReportAndFee.nextReportDate;
                            this.selectedLLCReportAndFee.fileReference = response.fileReference;
                            this.selectedLLCReportAndFee.reportDate = response.reportDate;
                            this.selectedLLCReportAndFee.percentageCompletion = response.percentageCompletion;
                            this.selectedLLCReportAndFee.remarks = response.remarks;
                            this._loanMonitoringService.updateLLCReportAndFee(this.selectedLLCReportAndFee).subscribe(() => {
                                this._matSnackBar.open('LLC report updated successfully.', 'OK', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this.selectedLLCReportAndFee.reportType = llcReportAndFee.reportType;
                    this.selectedLLCReportAndFee.dateOfReceipt = llcReportAndFee.dateOfReceipt;
                    this.selectedLLCReportAndFee.invoiceDate = llcReportAndFee.invoiceDate;
                    this.selectedLLCReportAndFee.invoiceNo = llcReportAndFee.invoiceNo;
                    this.selectedLLCReportAndFee.feeAmount = llcReportAndFee.feeAmount;
                    this.selectedLLCReportAndFee.statusOfFeeReceipt = llcReportAndFee.statusOfFeeReceipt;
                    this.selectedLLCReportAndFee.statusOfFeePaid = llcReportAndFee.statusOfFeePaid;
                    this.selectedLLCReportAndFee.documentTitle = llcReportAndFee.documentTitle;
                    this.selectedLLCReportAndFee.documentType = llcReportAndFee.documentType;
                    this.selectedLLCReportAndFee.nextReportDate = llcReportAndFee.nextReportDate;
                    this.selectedLLCReportAndFee.reportDate = llcReportAndFee.reportDate;
                    this.selectedLLCReportAndFee.percentageCompletion = llcReportAndFee.percentageCompletion;
                    this.selectedLLCReportAndFee.remarks = llcReportAndFee.remarks;
                    this._loanMonitoringService.updateLLCReportAndFee(this.selectedLLCReportAndFee).subscribe(() => {
                        this._matSnackBar.open('LLC report updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
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
