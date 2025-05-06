import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-icc-further-detail-update-dialog',
    templateUrl: './iccFurtherDetailUpdate.component.html',
    styleUrls: ['./iccFurtherDetailUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCFurtherDetailUpdateDialogComponent {

    dialogTitle = 'Add New Further Details';

    disableSubmitButton = false;

    selectedICCFurtherDetail: any ;

    iccFurtherDetailUpdateForm: FormGroup;

    enquiryCompletion: any;

    loanApplicationId: any;

    today = new Date();
    public selectedLoanEnquiry: any;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCFurtherDetailUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanEnquiryService: LoanEnquiryService) {

        this.selectedLoanEnquiry = this._loanEnquiryService.selectedEnquiry.value;
        this.enquiryCompletion = _dialogData.enquiryCompletion;
        this.loanApplicationId = _dialogData.loanApplicationId;

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.operation !== 'addICCFurtherDetail') {
            this.disableSubmitButton = _dialogData.operation === 'viewICCFurtherDetail';
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

        if (this.disableSubmitButton) {
            this.iccFurtherDetailUpdateForm.disable();
        }
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
            this._iccApprovalService.createFurtherDetail(furtherDetail).subscribe(() => {
                this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                    this._iccApprovalService._iccApproval.next(data);
                });
                this._matSnackBar.open('Further details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedICCFurtherDetail.iccMeetingNumber = furtherDetail.iccMeetingNumber;
            this.selectedICCFurtherDetail.iccMeetingDate = furtherDetail.iccMeetingDate;
            this.selectedICCFurtherDetail.detailsRequired = furtherDetail.detailsRequired;
            this._iccApprovalService.updateFurtherDetail(this.selectedICCFurtherDetail).subscribe(() => {
                this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                    this._iccApprovalService._iccApproval.next(data);
                });
                this._matSnackBar.open('Further details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
    }
}
