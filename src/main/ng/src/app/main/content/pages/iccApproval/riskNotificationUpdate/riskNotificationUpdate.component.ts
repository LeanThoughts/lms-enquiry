import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-risk-notification-update-dialog',
    templateUrl: './riskNotificationUpdate.component.html',
    styleUrls: ['./riskNotificationUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class RiskNotificationUpdateDialogComponent {

    dialogTitle = 'Add New Risk Notification';

    selectedRiskNotification: any ;

    riskNotificationUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<RiskNotificationUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedRiskNotification !== undefined) {
            this.selectedRiskNotification = Object.assign({}, _dialogData.selectedRiskNotification);
            this.dialogTitle = 'Modify Risk Notification';
        }
        else {
            this.selectedRiskNotification = {};
        }

        this.riskNotificationUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedRiskNotification.serialNumber],
            notificationDate: [this.selectedRiskNotification.notificationDate],
            remarks: [this.selectedRiskNotification.remarks || ''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        var riskNotification = this.riskNotificationUpdateForm.value;
        var dt = new Date(riskNotification.notificationDate);
        riskNotification.notificationDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        if (this._dialogData.operation === 'addRiskNotification') {
            riskNotification.loanApplicationId = this._dialogData.loanApplicationId;
            this._iccApprovalService.createRiskNotification(riskNotification).subscribe(() => {
                this._matSnackBar.open('Risk notification details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedRiskNotification.notificationDate = riskNotification.notificationDate;
            this.selectedRiskNotification.remarks = riskNotification.remarks;
            this._iccApprovalService.updateRiskNotification(this.selectedRiskNotification).subscribe(() => {
                this._matSnackBar.open('Risk notification details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }
}
