import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-loan-enhancement-update-dialog',
    templateUrl: './loanEnhancementUpdate.component.html',
    styleUrls: ['./loanEnhancementUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LoanEnhancementUpdateDialogComponent {

    dialogTitle = 'Add Loan Enhancement Details';

    selectedLoanEnhancement: any ;

    loanEnhancementForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<LoanEnhancementUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedLoanEnhancement !== undefined) {
            this.selectedLoanEnhancement = Object.assign({}, _dialogData.selectedLoanEnhancement);
            this.dialogTitle = 'Modify Loan Enhancement Details';
        }
        else {
            this.selectedLoanEnhancement = {};
        }

        this.loanEnhancementForm = _formBuilder.group({
            serialNumber: [this.selectedLoanEnhancement.serialNumber],
            iccMeetingNumber: [this.selectedLoanEnhancement.iccMeetingNumber],
            iccClearanceDate: [this.selectedLoanEnhancement.iccClearanceDate || ''],
            revisedProjectCost: [this.selectedLoanEnhancement.revisedProjectCost || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]],
            revisedEquity: [this.selectedLoanEnhancement.revisedEquity || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]],
            revisedContractAmount: [this.selectedLoanEnhancement.revisedContractAmount || '', [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]],
            revisedCommercialOperationsDate: [this.selectedLoanEnhancement.revisedCommercialOperationsDate || ''],
            reviseRepaymentStartDate: [this.selectedLoanEnhancement.reviseRepaymentStartDate || ''],
            remarks: [this.selectedLoanEnhancement.remarks || '']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.loanEnhancementForm.valid) {
            var loanEnhancement = this.loanEnhancementForm.value;
            var dt = new Date(loanEnhancement.iccClearanceDate);
            loanEnhancement.iccClearanceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(loanEnhancement.revisedCommercialOperationsDate);
            loanEnhancement.revisedCommercialOperationsDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(loanEnhancement.reviseRepaymentStartDate);
            loanEnhancement.reviseRepaymentStartDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLoanEnhancement') {
                loanEnhancement.loanApplicationId = this._dialogData.loanApplicationId;
                this._iccApprovalService.createLoanEnhancement(loanEnhancement).subscribe(() => {
                    this._matSnackBar.open('Loan Enhancement details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedLoanEnhancement.iccMeetingNumber = loanEnhancement.iccMeetingNumber;
                this.selectedLoanEnhancement.iccClearanceDate = loanEnhancement.iccClearanceDate;
                this.selectedLoanEnhancement.revisedProjectCost = loanEnhancement.revisedProjectCost;
                this.selectedLoanEnhancement.revisedEquity = loanEnhancement.revisedEquity;
                this.selectedLoanEnhancement.revisedContractAmount = loanEnhancement.revisedContractAmount;
                this.selectedLoanEnhancement.revisedCommercialOperationsDate  = loanEnhancement.revisedCommercialOperationsDate;
                this.selectedLoanEnhancement.reviseRepaymentStartDate  = loanEnhancement.reviseRepaymentStartDate;
                this.selectedLoanEnhancement.remarks = loanEnhancement.remarks;
                this._iccApprovalService.updateLoanEnhancement(this.selectedLoanEnhancement).subscribe(() => {
                    this._matSnackBar.open('Loan Enhancement details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
        }
    }
}
