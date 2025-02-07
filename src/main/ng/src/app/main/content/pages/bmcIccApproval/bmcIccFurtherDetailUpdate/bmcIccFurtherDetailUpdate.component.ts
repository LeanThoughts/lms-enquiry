import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-bmc-icc-further-detail-update-dialog',
    templateUrl: './bmcIccFurtherDetailUpdate.component.html',
    styleUrls: ['./bmcIccFurtherDetailUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BMCICCFurtherDetailUpdateDialogComponent {

    dialogTitle = 'Add New Further Details';

    disableSubmitButton = false;

    selectedICCFurtherDetail: any ;

    iccFurtherDetailUpdateForm: FormGroup;

    enquiryCompletion: any;

    today = new Date();
    public selectedLoanEnquiry: any;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _bmcApprovalService: BMCApprovalService,
        public _dialogRef: MatDialogRef<BMCICCFurtherDetailUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanEnquiryService: LoanEnquiryService) {

        this.selectedLoanEnquiry = this._loanEnquiryService.selectedEnquiry.value;
        this.enquiryCompletion = _dialogData.enquiryCompletion;
        
        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedICCFurtherDetail !== undefined) {
            this.selectedICCFurtherDetail = Object.assign({}, _dialogData.selectedICCFurtherDetail);
            this.dialogTitle = 'Modify Further Details';
        }
        else {
            this.selectedICCFurtherDetail = {};
        }

        this.iccFurtherDetailUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedICCFurtherDetail.serialNumber],
            iccMeetingNumber: [this.selectedICCFurtherDetail.iccMeetingNumber],
            iccMeetingDate: [this.selectedICCFurtherDetail.iccMeetingDate || ''],
            detailsRequired: [this.selectedICCFurtherDetail.detailsRequired || ''],
        });

    }

    /**
     * submit()
     */
    submit(): void {
        this.disableSubmitButton = true;
        var furtherDetail = this.iccFurtherDetailUpdateForm.value;
        var dt = new Date(furtherDetail.iccMeetingDate);
        furtherDetail.iccMeetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        if (this._dialogData.operation === 'addICCFurtherDetail') {
            furtherDetail.loanApplicationId = this._dialogData.loanApplicationId;
            this._bmcApprovalService.createBmcFurtherDetail(furtherDetail).subscribe(() => {
                this._matSnackBar.open('Further details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedICCFurtherDetail.iccMeetingNumber = furtherDetail.iccMeetingNumber;
            this.selectedICCFurtherDetail.iccMeetingDate = furtherDetail.iccMeetingDate;
            this.selectedICCFurtherDetail.detailsRequired = furtherDetail.detailsRequired;
            this._bmcApprovalService.updateBmcFurtherDetail(this.selectedICCFurtherDetail).subscribe(() => {
                this._matSnackBar.open('Further details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }
}
