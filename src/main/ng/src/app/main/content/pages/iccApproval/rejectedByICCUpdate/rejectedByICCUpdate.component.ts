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
        if (this.selectedRejectedByICC.id !== undefined) {
            this.dialogTitle = 'Modify Rejected by ICC';
        }

        this.rejectedByICCForm = this._formBuilder.group({
            meetingNumber: [this.selectedRejectedByICC.meetingNumber || '', [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            meetingDate: [this.selectedRejectedByICC.meetingDate || ''],
            reasonForRejection: [this.selectedRejectedByICC.reasonForRejection || ''],
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

            console.log('selected rejected by icc id', this.selectedRejectedByICC.id);
            if (this.selectedRejectedByICC.id === undefined) {
                console.log('adding reason for delay');
                rejectedByICC.loanApplicationId = this.loanApplicationId;
                this._iccApprovalService.createRejectedByICC(rejectedByICC).subscribe(() => {
                    this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                        this._iccApprovalService._iccApproval.next(data);
                        this._matSnackBar.open('Rejected by ICC details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                console.log('updating rejected by ICC');
                this.selectedRejectedByICC.meetingDate = rejectedByICC.meetingDate;
                this.selectedRejectedByICC.meetingNumber = rejectedByICC.meetingNumber;
                this.selectedRejectedByICC.reasonForRejection = rejectedByICC.reasonForRejection;
                this._iccApprovalService.updateRejectedByICC(this.selectedRejectedByICC).subscribe(() => {
                    this._matSnackBar.open('Rejected by ICC details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
