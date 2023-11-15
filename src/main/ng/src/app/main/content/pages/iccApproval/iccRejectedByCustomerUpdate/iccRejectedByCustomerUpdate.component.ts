import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BoardApprovalService } from '../../boardApproval/boardApproval.service';

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

    approvalByBoards: any;
    customerRejectionReasons: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<ICCRejectedByCustomerUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        this._boardApprovalService.getApprovalByBoards().subscribe(data => {
            this.approvalByBoards = data._embedded.approvalByBoards;
        });

        this._boardApprovalService.getCustomerRejectionReasons().subscribe(data => {
            this.customerRejectionReasons = data._embedded.customerRejectionReasons;
        });

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedRejectedByCustomer = Object.assign({}, _dialogData.selectedRejectedByCustomer);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedRejectedByCustomer !== undefined) {
            if (_dialogData.operation === 'updateRejectedByCustomer') {
                this.dialogTitle = 'Modify Rejected By Customer';
            }
        }
        this.rejectedByCustomerForm = this._formBuilder.group({
            iccMeetingNumber: [this.selectedRejectedByCustomer.iccMeetingNumber || ''],
            meetingDate: [this.selectedRejectedByCustomer.meetingDate || ''],
            rejectionCategory: [this.selectedRejectedByCustomer.rejectionCategory || ''],
            details: [this.selectedRejectedByCustomer.details || ''],
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
            var dt = new Date(rejectedByCustomer.meetingDate);
            rejectedByCustomer.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addRejectedByCustomer') {
                rejectedByCustomer.loanApplicationId = this.loanApplicationId;
                this._boardApprovalService.createRejectedByCustomer(rejectedByCustomer).subscribe(() => {
                    this._matSnackBar.open('Rejected by customer added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedRejectedByCustomer.meetingDate = rejectedByCustomer.meetingDate;
                this.selectedRejectedByCustomer.iccMeetingNumber = rejectedByCustomer.iccMeetingNumber;
                this.selectedRejectedByCustomer.rejectionCategory = rejectedByCustomer.rejectionCategory;
                this.selectedRejectedByCustomer.details = rejectedByCustomer.details;
                this._boardApprovalService.updateRejectedByCustomer(this.selectedRejectedByCustomer).subscribe(() => {
                    this._matSnackBar.open('Rejected by customer updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
