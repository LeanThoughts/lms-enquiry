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
    selector: 'fuse-cla-report-fee-update-dialog',
    templateUrl: './claReportAndFeeUpdate.component.html',
    styleUrls: ['./claReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class CLAReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add CLA Report Submission';

    selectedCLA: LIEModel;
    selectedCLAReportAndFee: LIEReportAndFeeModel;

    claUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<CLAReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedCLA = _dialogData.selectedCLA;
        if (_dialogData.selectedCLAReportAndFee !== undefined) {
            this.selectedCLAReportAndFee = Object.assign({}, _dialogData.selectedCLAReportAndFee);
            this.dialogTitle = 'Modify CLA Report Submission';
        }
        else {
            this.selectedCLAReportAndFee = new LIEReportAndFeeModel({});
        }

        this.claUpdateForm = _formBuilder.group({
            reportType: [this.selectedCLAReportAndFee.reportType],
            dateOfReceipt: [this.selectedCLAReportAndFee.dateOfReceipt || ''], 
            invoiceDate: [this.selectedCLAReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedCLAReportAndFee.invoiceNo],
            feeAmount: [this.selectedCLAReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedCLAReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedCLAReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedCLAReportAndFee.documentTitle],
            nextReportDate: [this.selectedCLAReportAndFee.nextReportDate || ''],
            documentType: [this.selectedCLAReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedCLAReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedCLAReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedCLAReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedCLAReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedCLAReportAndFee.percentageCompletion || ''],
            remarks: [this.selectedCLAReportAndFee.remarks || '']
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
            this.claUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.claUpdateForm.valid) {
            // To solve the utc time zone issue
            var claReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.claUpdateForm.value);
            var dt = new Date(claReportAndFee.dateOfReceipt);
            claReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(claReportAndFee.invoiceDate);
            claReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(claReportAndFee.nextReportDate);
            claReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(claReportAndFee.reportDate);
            claReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addCLAReportAndFee') {
                if (this.claUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.claUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            claReportAndFee.fileReference = response.fileReference;
                            this._loanMonitoringService.saveCLAReportAndFee(claReportAndFee, this.selectedCLA.id).subscribe((data) => {
                                this._matSnackBar.open('CLA report added successfully.', 'OK', { duration: 7000 });
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
                    this._loanMonitoringService.saveCLAReportAndFee(claReportAndFee, this.selectedCLA.id).subscribe((data) => {
                        this._matSnackBar.open('CLA report added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
            else {
                if (this.claUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.claUpdateForm.get('file').value);      
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedCLAReportAndFee.reportType = claReportAndFee.reportType;
                            this.selectedCLAReportAndFee.dateOfReceipt = claReportAndFee.dateOfReceipt;
                            this.selectedCLAReportAndFee.invoiceDate = claReportAndFee.invoiceDate;
                            this.selectedCLAReportAndFee.invoiceNo = claReportAndFee.invoiceNo;
                            this.selectedCLAReportAndFee.feeAmount = claReportAndFee.feeAmount;
                            this.selectedCLAReportAndFee.statusOfFeeReceipt = claReportAndFee.statusOfFeeReceipt;
                            this.selectedCLAReportAndFee.statusOfFeePaid = claReportAndFee.statusOfFeePaid;
                            this.selectedCLAReportAndFee.documentTitle = claReportAndFee.documentTitle;
                            this.selectedCLAReportAndFee.documentType = claReportAndFee.documentType;
                            this.selectedCLAReportAndFee.nextReportDate = claReportAndFee.nextReportDate;
                            this.selectedCLAReportAndFee.fileReference = response.fileReference;
                            this.selectedCLAReportAndFee.reportDate = response.reportDate;
                            this.selectedCLAReportAndFee.percentageCompletion = response.percentageCompletion;
                            this.selectedCLAReportAndFee.remarks = response.remarks;
                            this._loanMonitoringService.updateCLAReportAndFee(this.selectedCLAReportAndFee).subscribe(() => {
                                this._matSnackBar.open('CLA report updated successfully.', 'OK', { duration: 7000 });
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
                    this.selectedCLAReportAndFee.reportType = claReportAndFee.reportType;
                    this.selectedCLAReportAndFee.dateOfReceipt = claReportAndFee.dateOfReceipt;
                    this.selectedCLAReportAndFee.invoiceDate = claReportAndFee.invoiceDate;
                    this.selectedCLAReportAndFee.invoiceNo = claReportAndFee.invoiceNo;
                    this.selectedCLAReportAndFee.feeAmount = claReportAndFee.feeAmount;
                    this.selectedCLAReportAndFee.statusOfFeeReceipt = claReportAndFee.statusOfFeeReceipt;
                    this.selectedCLAReportAndFee.statusOfFeePaid = claReportAndFee.statusOfFeePaid;
                    this.selectedCLAReportAndFee.documentTitle = claReportAndFee.documentTitle;
                    this.selectedCLAReportAndFee.documentType = claReportAndFee.documentType;
                    this.selectedCLAReportAndFee.nextReportDate = claReportAndFee.nextReportDate;
                    this.selectedCLAReportAndFee.reportDate = claReportAndFee.reportDate;
                    this.selectedCLAReportAndFee.percentageCompletion = claReportAndFee.percentageCompletion;
                    this.selectedCLAReportAndFee.remarks = claReportAndFee.remarks;
                    this._loanMonitoringService.updateCLAReportAndFee(this.selectedCLAReportAndFee).subscribe(() => {
                        this._matSnackBar.open('CLA report updated successfully.', 'OK', { duration: 7000 });
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
