import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-icc-approval-update-dialog',
    templateUrl: './iccApprovalUpdate.component.html',
    styleUrls: ['./iccApprovalUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCApprovalUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Rejected By ICC';

    loanApplicationId = '';
    selectedICCApproval: any;

    iccApprovalForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCApprovalUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedICCApproval = Object.assign({}, _dialogData.selectedRejectedByICC);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedRejectedByICC !== undefined) {
            if (_dialogData.operation === 'updateICCApproval') {
                this.dialogTitle = 'Modify ICC Approval Details';
            }
        }
        this.iccApprovalForm = this._formBuilder.group({
            meetingNumber: [this.selectedICCApproval.meetingNumber || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            dateOfICCClearance: [this.selectedICCApproval.dateOfICCClearance || ''],
            remarks: [this.selectedICCApproval.remarks || ''],
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.iccApprovalForm.valid) {
            var iccApproval = this.iccApprovalForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(iccApproval.dateOfICCClearance);
            iccApproval.dateOfICCClearance = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addICCApproval') {
                iccApproval.loanApplicationId = this.loanApplicationId;
                // this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc added successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });
            }
            else {
                this.selectedICCApproval.dateOfICCClearance = iccApproval.dateOfICCClearance;
                this.selectedICCApproval.meetingNumber = iccApproval.meetingNumber;
                this.selectedICCApproval.remarks = iccApproval.remarks;
                // this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc updated successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });            
            }
        }
    }
}
