import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-rejected-by-icc-update-dialog',
    templateUrl: './rejectedByICCUpdate.component.html',
    styleUrls: ['./rejectedByICCUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class RejectedByICCUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Rejected By ICC';

    loanApplicationId = '';
    selectedRejectedByICC: any;

    rejectedByICCForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<RejectedByICCUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedRejectedByICC = Object.assign({}, _dialogData.selectedRejectedByICC);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedRejectedByICC !== undefined) {
            if (_dialogData.operation === 'updateRejectedByICC') {
                this.dialogTitle = 'Modify Rejected By ICC';
            }
        }
        this.rejectedByICCForm = this._formBuilder.group({
            meetingNumber: [this.selectedRejectedByICC.meetingNumber || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            meetingDate: [this.selectedRejectedByICC.meetingDate || ''],
            details: [this.selectedRejectedByICC.details || ''],
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
        if (this.rejectedByICCForm.valid) {
            var rejectedByICC = this.rejectedByICCForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(rejectedByICC.meetingDate);
            rejectedByICC.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addRejectedByICC') {
                rejectedByICC.loanApplicationId = this.loanApplicationId;
                // this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc added successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });
            }
            else {
                this.selectedRejectedByICC.meetingDate = rejectedByICC.meetingDate;
                this.selectedRejectedByICC.meetingNumber = rejectedByICC.meetingNumber;
                this.selectedRejectedByICC.details = rejectedByICC.details;
                // this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                //     this._matSnackBar.open('Rejected by icc updated successfully.', 'OK', { duration: 7000 });
                //     this._dialogRef.close({ 'refresh': true });
                // });            
            }
        }
    }
}
