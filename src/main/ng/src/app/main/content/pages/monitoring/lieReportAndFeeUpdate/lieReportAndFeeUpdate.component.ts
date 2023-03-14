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
    selector: 'fuse-lie-report-fee-update-dialog',
    templateUrl: './lieReportAndFeeUpdate.component.html',
    styleUrls: ['./lieReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LIEReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add LIE Report Submission';

    selectedLIE: LIEModel;
    selectedLIEReportAndFee: LIEReportAndFeeModel;

    lieUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<LIEReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedLIE = _dialogData.selectedLIE;
        if (_dialogData.selectedLIEReportAndFee !== undefined) {
            this.selectedLIEReportAndFee = Object.assign({}, _dialogData.selectedLIEReportAndFee);
            this.dialogTitle = 'Modify LIE Report Submission';
        }
        else {
            this.selectedLIEReportAndFee = new LIEReportAndFeeModel({});
        }

        this.lieUpdateForm = _formBuilder.group({
            reportType: [this.selectedLIEReportAndFee.reportType],
            dateOfReceipt: [this.selectedLIEReportAndFee.dateOfReceipt || ''], 
            invoiceDate: [this.selectedLIEReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedLIEReportAndFee.invoiceNo],
            feeAmount: [this.selectedLIEReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedLIEReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedLIEReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedLIEReportAndFee.documentTitle],
            nextReportDate: [this.selectedLIEReportAndFee.nextReportDate || ''],
            documentType: [this.selectedLIEReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedLIEReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedLIEReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedLIEReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedLIEReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedLIEReportAndFee.percentageCompletion || ''],
            remarks: [this.selectedLIEReportAndFee.remarks || '']
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
            this.lieUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        // if (this.lieUpdateForm.valid) {
        //     // To solve the utc time zone issue
        //     var lieReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.lieUpdateForm.value);
        //     var dt = new Date(lieReportAndFee.dateOfReceipt);
        //     lieReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        //     dt = new Date(lieReportAndFee.invoiceDate);
        //     lieReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        //     dt = new Date(lieReportAndFee.nextReportDate);
        //     lieReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        //     dt = new Date(lieReportAndFee.reportDate);
        //     lieReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        //     if (this._dialogData.operation === 'addLIEReportAndFee') {
        //         if (this.lieUpdateForm.get('file').value !== '') {
        //             var formData = new FormData();
        //             formData.append('file', this.lieUpdateForm.get('file').value);      
        //             this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
        //                 (response) => {
        //                     lieReportAndFee.fileReference = response.fileReference;
        //                     this._loanMonitoringService.saveLIEReportAndFee(lieReportAndFee, this.selectedLIE.id, this._dialogData.module).subscribe((data) => {
        //                         this._matSnackBar.open('LIE report added successfully.', 'OK', { duration: 7000 });
        //                         this._dialogRef.close({ 'refresh': true });
        //                     });
        //                 },
        //                 (error) => {
        //                     this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
        //                         'OK', { duration: 7000 });
        //                 }
        //             );
        //         }
        //         else
        //         {
        //             this._loanMonitoringService.saveLIEReportAndFee(lieReportAndFee, this.selectedLIE.id, this._dialogData.module).subscribe((data) => {
        //                 this._matSnackBar.open('LIE report added successfully.', 'OK', { duration: 7000 });
        //                 this._dialogRef.close({ 'refresh': true });
        //             });
        //         }
        //     }
        //     else {
        //         if (this.lieUpdateForm.get('file').value !== '') {
        //             var formData = new FormData();
        //             formData.append('file', this.lieUpdateForm.get('file').value);      
        //             this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
        //                 (response) => {
        //                     this.selectedLIEReportAndFee.reportType = lieReportAndFee.reportType;
        //                     this.selectedLIEReportAndFee.dateOfReceipt = lieReportAndFee.dateOfReceipt;
        //                     this.selectedLIEReportAndFee.invoiceDate = lieReportAndFee.invoiceDate;
        //                     this.selectedLIEReportAndFee.invoiceNo = lieReportAndFee.invoiceNo;
        //                     this.selectedLIEReportAndFee.feeAmount = lieReportAndFee.feeAmount;
        //                     this.selectedLIEReportAndFee.statusOfFeeReceipt = lieReportAndFee.statusOfFeeReceipt;
        //                     this.selectedLIEReportAndFee.statusOfFeePaid = lieReportAndFee.statusOfFeePaid;
        //                     this.selectedLIEReportAndFee.documentTitle = lieReportAndFee.documentTitle;
        //                     this.selectedLIEReportAndFee.documentType = lieReportAndFee.documentType;
        //                     this.selectedLIEReportAndFee.nextReportDate = lieReportAndFee.nextReportDate;
        //                     this.selectedLIEReportAndFee.fileReference = response.fileReference;
        //                     this.selectedLIEReportAndFee.reportDate = response.reportDate;
        //                     this.selectedLIEReportAndFee.percentageCompletion = response.percentageCompletion;
        //                     this.selectedLIEReportAndFee.remarks = response.remarks;
        //                     this._loanMonitoringService.updateLIEReportAndFee(this.selectedLIEReportAndFee, this._dialogData.module).subscribe(() => {
        //                         this._matSnackBar.open('LIE report updated successfully.', 'OK', { duration: 7000 });
        //                         this._dialogRef.close({ 'refresh': true });
        //                     });
        //                 },
        //                 (error) => {
        //                     this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
        //                         'OK', { duration: 7000 });
        //                 }
        //             );
        //         }
        //         else
        //         {
        //             this.selectedLIEReportAndFee.reportType = lieReportAndFee.reportType;
        //             this.selectedLIEReportAndFee.dateOfReceipt = lieReportAndFee.dateOfReceipt;
        //             this.selectedLIEReportAndFee.invoiceDate = lieReportAndFee.invoiceDate;
        //             this.selectedLIEReportAndFee.invoiceNo = lieReportAndFee.invoiceNo;
        //             this.selectedLIEReportAndFee.feeAmount = lieReportAndFee.feeAmount;
        //             this.selectedLIEReportAndFee.statusOfFeeReceipt = lieReportAndFee.statusOfFeeReceipt;
        //             this.selectedLIEReportAndFee.statusOfFeePaid = lieReportAndFee.statusOfFeePaid;
        //             this.selectedLIEReportAndFee.documentTitle = lieReportAndFee.documentTitle;
        //             this.selectedLIEReportAndFee.documentType = lieReportAndFee.documentType;
        //             this.selectedLIEReportAndFee.nextReportDate = lieReportAndFee.nextReportDate;
        //             this.selectedLIEReportAndFee.reportDate = lieReportAndFee.reportDate;
        //             this.selectedLIEReportAndFee.percentageCompletion = lieReportAndFee.percentageCompletion;
        //             this.selectedLIEReportAndFee.remarks = lieReportAndFee.remarks;
        //             this._loanMonitoringService.updateLIEReportAndFee(this.selectedLIEReportAndFee, this._dialogData.module).subscribe(() => {
        //                 this._matSnackBar.open('LIE report updated successfully.', 'OK', { duration: 7000 });
        //                 this._dialogRef.close({ 'refresh': true });
        //             });
        //         }
        //     }
        // }

        if (this.lieUpdateForm.valid) {
            if (this.lieUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.lieUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLIEReportAndFee(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addLIEReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveLIEReportAndFee('');
                }
            }
        }
    }

    /**
     * saveLIEReportAndFee()
     */
    saveLIEReportAndFee(fileReference: string): void {
        // To solve the utc time zone issue
        var lieReportAndFee: LIEReportAndFeeModel = new LIEReportAndFeeModel(this.lieUpdateForm.value);
        var dt = new Date(lieReportAndFee.dateOfReceipt);
        lieReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(lieReportAndFee.invoiceDate);
        lieReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(lieReportAndFee.nextReportDate);
        lieReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(lieReportAndFee.reportDate);
        lieReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        if (this._dialogData.operation === 'addLIEReportAndFee') {
            lieReportAndFee.fileReference = fileReference;
            this._loanMonitoringService.saveLIEReportAndFee(lieReportAndFee, this.selectedLIE.id, this._dialogData.module).subscribe((data) => {
                this._matSnackBar.open('LIE report added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            if (fileReference !== '' ) {
                this.selectedLIEReportAndFee.fileReference = fileReference;
            }
            this.selectedLIEReportAndFee.reportType = lieReportAndFee.reportType;
            this.selectedLIEReportAndFee.dateOfReceipt = lieReportAndFee.dateOfReceipt;
            this.selectedLIEReportAndFee.invoiceDate = lieReportAndFee.invoiceDate;
            this.selectedLIEReportAndFee.invoiceNo = lieReportAndFee.invoiceNo;
            this.selectedLIEReportAndFee.feeAmount = lieReportAndFee.feeAmount;
            this.selectedLIEReportAndFee.statusOfFeeReceipt = lieReportAndFee.statusOfFeeReceipt;
            this.selectedLIEReportAndFee.statusOfFeePaid = lieReportAndFee.statusOfFeePaid;
            this.selectedLIEReportAndFee.documentTitle = lieReportAndFee.documentTitle;
            this.selectedLIEReportAndFee.documentType = lieReportAndFee.documentType;
            this.selectedLIEReportAndFee.nextReportDate = lieReportAndFee.nextReportDate;
            this.selectedLIEReportAndFee.reportDate = lieReportAndFee.reportDate;
            this.selectedLIEReportAndFee.percentageCompletion = lieReportAndFee.percentageCompletion;
            this.selectedLIEReportAndFee.remarks = lieReportAndFee.remarks;
            this._loanMonitoringService.updateLIEReportAndFee(this.selectedLIEReportAndFee, this._dialogData.module).subscribe(() => {
                this._matSnackBar.open('LIE report updated successfully.', 'OK', { duration: 7000 });
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
