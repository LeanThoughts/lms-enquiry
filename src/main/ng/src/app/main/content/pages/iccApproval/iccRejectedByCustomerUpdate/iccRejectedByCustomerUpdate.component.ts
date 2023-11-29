import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BoardApprovalService } from '../../boardApproval/boardApproval.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCApprovalUpdateDialogComponent } from '../iccApprovalUpdate/iccApprovalUpdate.component';

@Component({
    selector: 'fuse-icc-rejected-by-customer-update-dialog',
    templateUrl: './iccRejectedByCustomerUpdate.component.html',
    styleUrls: ['./iccRejectedByCustomerUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCRejectedByCustomerUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Rejected By Customer';

    loanApplicationId = '';
    selectedRejectedByCustomer: any;

    rejectedByCustomerForm: FormGroup;

    approvalByBoards = [];
    customerRejectionReasons = [];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCApprovalUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
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
            rejectionCategory: [this.selectedRejectedByCustomer.rejectionCategory || '']
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
            var rejectedByCustomer = this.rejectedByCustomerForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(rejectedByCustomer.dateOfRejection);
            rejectedByCustomer.dateOfRejection = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this.selectedRejectedByCustomer.id === undefined) {
                rejectedByCustomer.loanApplicationId = this.loanApplicationId;
                this._iccApprovalService.createRejectedByCustomer(rejectedByCustomer).subscribe(() => {
                    this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                        this._iccApprovalService._iccApproval.next(data);
                        this._matSnackBar.open('Rejected by Customer details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                this.selectedRejectedByCustomer.meetingDate = rejectedByCustomer.meetingDate;
                this.selectedRejectedByCustomer.dateOfRejection = rejectedByCustomer.dateOfRejection;
                this.selectedRejectedByCustomer.remarks = rejectedByCustomer.remarks;
                this._iccApprovalService.updateRejectedByCustomer(this.selectedRejectedByCustomer).subscribe(() => {
                    this._matSnackBar.open('Rejected by Customer details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
