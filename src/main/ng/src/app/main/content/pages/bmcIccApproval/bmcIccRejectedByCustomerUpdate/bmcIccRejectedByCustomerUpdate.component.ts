import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BMCApprovalService } from '../bmcIccApproval.service';

@Component({
    selector: 'fuse-bmc-icc-rejected-by-customer-update-dialog',
    templateUrl: './bmcIccRejectedByCustomerUpdate.component.html',
    styleUrls: ['./bmcIccRejectedByCustomerUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BMCICCRejectedByCustomerUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Rejected By Customer';

    disableSubmitButton = false;

    loanApplicationId = '';
    selectedRejectedByCustomer: any;

    rejectedByCustomerForm: FormGroup;

    approvalByBoards = [];
    customerRejectionReasons = [];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _bmcApprovalService: BMCApprovalService,
        public _dialogRef: MatDialogRef<BMCICCRejectedByCustomerUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedRejectedByCustomer = Object.assign({}, _dialogData.selectedRejectedByCustomer);
        this.loanApplicationId = _dialogData.loanApplicationId;

        if (this.selectedRejectedByCustomer.id !== undefined) {
            this.dialogTitle = 'Modify Rejected By Customer';
        }

        this.rejectedByCustomerForm = this._formBuilder.group({
            meetingNumber: [this.selectedRejectedByCustomer.meetingNumber],
            dateOfRejection: [this.selectedRejectedByCustomer.dateOfRejection || ''],
            remarks: [this.selectedRejectedByCustomer.remarks || ''],
            rejectionCategory: [this.selectedRejectedByCustomer.rejectionCategory || 'Rejected By Customer']
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
        if (this.rejectedByCustomerForm.valid) {
            this.disableSubmitButton = true;
            var rejectedByCustomer = this.rejectedByCustomerForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(rejectedByCustomer.dateOfRejection);
            rejectedByCustomer.dateOfRejection = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this.selectedRejectedByCustomer.id === undefined) {
                rejectedByCustomer.loanApplicationId = this.loanApplicationId;
                this._bmcApprovalService.createBmcRejectedByCustomer(rejectedByCustomer).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                        this._matSnackBar.open('Rejected by Customer details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                this.selectedRejectedByCustomer.meetingDate = rejectedByCustomer.meetingDate;
                this.selectedRejectedByCustomer.dateOfRejection = rejectedByCustomer.dateOfRejection;
                this.selectedRejectedByCustomer.remarks = rejectedByCustomer.remarks;
                this._bmcApprovalService.updateBmcRejectedByCustomer(this.selectedRejectedByCustomer).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                    });
                    this._matSnackBar.open('Rejected by Customer details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
