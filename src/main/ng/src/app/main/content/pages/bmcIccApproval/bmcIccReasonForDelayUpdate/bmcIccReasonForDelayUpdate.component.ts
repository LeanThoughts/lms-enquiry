import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-bmc-icc-reason-for-delay-update-dialog',
    templateUrl: './bmcIccReasonForDelayUpdate.component.html',
    styleUrls: ['./bmcIccReasonForDelayUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BMCICCReasonForDelayUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Reason For Delay';

    disableSubmitButton = false;

    loanApplicationId = '';
    selectedReasonForDelay: any;

    reasonForDelayForm: FormGroup;

    selectedEnquiry: any;

    today: Date = new Date();
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _bmcApprovalService: BMCApprovalService,
        public _dialogRef: MatDialogRef<BMCICCReasonForDelayUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanEnquiryService: LoanEnquiryService) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedReasonForDelay = Object.assign({}, _dialogData.selectedReasonForDelay);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (this.selectedReasonForDelay.id !== undefined) {
            this.dialogTitle = 'Modify Reason For Delay';
        }
        
        this.reasonForDelayForm = this._formBuilder.group({
            reasonForDelay: [this.selectedReasonForDelay.reasonForDelay || ''],
            date: [this.selectedReasonForDelay.date || '']
        });

        this.selectedEnquiry = this._loanEnquiryService.selectedEnquiry.value;
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
        if (this.reasonForDelayForm.valid) {
            this.disableSubmitButton = true;
            var reasonForDelay = this.reasonForDelayForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(reasonForDelay.date);
            reasonForDelay.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this.selectedReasonForDelay.id === undefined) {
                console.log('adding reason for delay');
                reasonForDelay.loanApplicationId = this.loanApplicationId;
                this._bmcApprovalService.createBmcReasonForDelay(reasonForDelay).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                        this._matSnackBar.open('Reason for Delay added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                console.log('updating reason for delay');
                this.selectedReasonForDelay.date = reasonForDelay.date;
                this.selectedReasonForDelay.reasonForDelay = reasonForDelay.reasonForDelay;
                this._bmcApprovalService.updateBmcReasonForDelay(this.selectedReasonForDelay).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                    });
                    this._matSnackBar.open('Reason for Delay updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
