import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanAppraisalService } from '../loanAppraisal.service';
import { LoanMonitoringService } from '../../monitoring/loanMonitoring.service';

@Component({
    selector: 'fuse-securityTrustee-report-fee-update-dialog',
    templateUrl: './securityTrusteeReportAndFeeUpdate.component.html',
    styleUrls: ['./securityTrusteeReportAndFeeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SecurityTrusteeReportAndFeeUpdateDialogComponent {

    dialogTitle = 'Add Security Trustee Report Submission';

    selectedSecurityTrustee: any;
    selectedSecurityTrusteeReportAndFee: any;

    stReportUpdateForm: FormGroup;

    reportTypes = LoanMonitoringConstants.reportTypes;
    feePaidStatuses = LoanMonitoringConstants.feePaidStatuses;
    feeReceiptStatuses = LoanMonitoringConstants.feeReceiptStatuses;
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<SecurityTrusteeReportAndFeeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanAppraisalService: LoanAppraisalService) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedSecurityTrustee = _dialogData.selectedSecurityTrustee;
        if (_dialogData.selectedSecurityTrusteeReportAndFee !== undefined) {
            this.selectedSecurityTrusteeReportAndFee = Object.assign({}, _dialogData.selectedSecurityTrusteeReportAndFee);
            this.dialogTitle = 'Modify Security Trustee Report Submission';
        }
        else {
            this.selectedSecurityTrusteeReportAndFee = {};
        }

        this.stReportUpdateForm = _formBuilder.group({
            reportType: [this.selectedSecurityTrusteeReportAndFee.reportType],
            dateOfReceipt: [this.selectedSecurityTrusteeReportAndFee.dateOfReceipt || ''],
            invoiceDate: [this.selectedSecurityTrusteeReportAndFee.invoiceDate || ''],
            invoiceNo: [this.selectedSecurityTrusteeReportAndFee.invoiceNo],
            feeAmount: [this.selectedSecurityTrusteeReportAndFee.feeAmount, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            statusOfFeeReceipt: [this.selectedSecurityTrusteeReportAndFee.statusOfFeeReceipt],
            statusOfFeePaid: [this.selectedSecurityTrusteeReportAndFee.statusOfFeePaid],
            documentTitle: [this.selectedSecurityTrusteeReportAndFee.documentTitle],
            nextReportDate: [this.selectedSecurityTrusteeReportAndFee.nextReportDate || ''],
            documentType: [this.selectedSecurityTrusteeReportAndFee.documentType || ''],
            file: [''],
            sapFIInvoiceDate: [this.selectedSecurityTrusteeReportAndFee.sapFIInvoiceDate || ''],
            sapFIInvoiceNumber: [this.selectedSecurityTrusteeReportAndFee.sapFIInvoiceNumber],
            feeAmountRaisedOnCustomer: [this.selectedSecurityTrusteeReportAndFee.feeAmountRaisedOnCustomer],
            reportDate: [this.selectedSecurityTrusteeReportAndFee.reportDate || ''],
            percentageCompletion: [this.selectedSecurityTrusteeReportAndFee.percentageCompletion, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            remarks: [this.selectedSecurityTrusteeReportAndFee.remarks || '']
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
            this.stReportUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.stReportUpdateForm.valid) {
            if (this.stReportUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.stReportUpdateForm.get('file').value);
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveSecurityTrusteeReportAndFee(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addSecurityTrusteeReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveSecurityTrusteeReportAndFee('');
                }
            }
        }
    }

    /**
     * saveSecurityTrusteeReportAndFee()
     */
    saveSecurityTrusteeReportAndFee(fileReference: string): void {
        // To solve the utc time zone issue
        var securityTrusteeReportAndFee = this.stReportUpdateForm.value;
        var dt = new Date(securityTrusteeReportAndFee.dateOfReceipt);
        securityTrusteeReportAndFee.dateOfReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(securityTrusteeReportAndFee.invoiceDate);
        securityTrusteeReportAndFee.invoiceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(securityTrusteeReportAndFee.nextReportDate);
        securityTrusteeReportAndFee.nextReportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(securityTrusteeReportAndFee.reportDate);
        securityTrusteeReportAndFee.reportDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        if (this._dialogData.operation === 'addSecurityTrusteeReportAndFee') {
            securityTrusteeReportAndFee.fileReference = fileReference;
            this._loanAppraisalService.saveSecurityTrusteeReportAndFee(securityTrusteeReportAndFee, this.selectedSecurityTrustee.id, this._dialogData.module).subscribe((data) => {
                this._matSnackBar.open('Security Trustee report added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            if (fileReference !== '' ) {
                this.selectedSecurityTrusteeReportAndFee.fileReference = fileReference;
            }
            this.selectedSecurityTrusteeReportAndFee.reportType = securityTrusteeReportAndFee.reportType;
            this.selectedSecurityTrusteeReportAndFee.dateOfReceipt = securityTrusteeReportAndFee.dateOfReceipt;
            this.selectedSecurityTrusteeReportAndFee.invoiceDate = securityTrusteeReportAndFee.invoiceDate;
            this.selectedSecurityTrusteeReportAndFee.invoiceNo = securityTrusteeReportAndFee.invoiceNo;
            this.selectedSecurityTrusteeReportAndFee.feeAmount = securityTrusteeReportAndFee.feeAmount;
            this.selectedSecurityTrusteeReportAndFee.statusOfFeeReceipt = securityTrusteeReportAndFee.statusOfFeeReceipt;
            this.selectedSecurityTrusteeReportAndFee.statusOfFeePaid = securityTrusteeReportAndFee.statusOfFeePaid;
            this.selectedSecurityTrusteeReportAndFee.documentTitle = securityTrusteeReportAndFee.documentTitle;
            this.selectedSecurityTrusteeReportAndFee.documentType = securityTrusteeReportAndFee.documentType;
            this.selectedSecurityTrusteeReportAndFee.nextReportDate = securityTrusteeReportAndFee.nextReportDate;
            this.selectedSecurityTrusteeReportAndFee.reportDate = securityTrusteeReportAndFee.reportDate;
            this.selectedSecurityTrusteeReportAndFee.percentageCompletion = securityTrusteeReportAndFee.percentageCompletion;
            this.selectedSecurityTrusteeReportAndFee.remarks = securityTrusteeReportAndFee.remarks;
            this._loanAppraisalService.updateSecurityTrusteeReportAndFee(this.selectedSecurityTrusteeReportAndFee, this._dialogData.module).subscribe(() => {
                this._matSnackBar.open('Security Trustee report updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }

    /**
     * closeClick()
     */
    closeClick(): void {
        this._dialogRef.close({ 'refresh': false });
    }

    /**
     * getFileURL()
     * @param fileReference
     */
     getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
