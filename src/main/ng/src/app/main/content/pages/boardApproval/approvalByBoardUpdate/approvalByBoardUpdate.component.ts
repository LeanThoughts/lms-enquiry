import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BoardApprovalService } from '../boardApproval.service';

@Component({
    selector: 'fuse-approval-by-board-update-dialog',
    templateUrl: './approvalByBoardUpdate.component.html',
    styleUrls: ['./approvalByBoardUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ApprovalByBoardUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Approval By Board';

    loanApplicationId = '';
    selectedApprovalByBoard: any;

    approvalByBoardForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<ApprovalByBoardUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected details from the dialog's data attribute.
        this.selectedApprovalByBoard = Object.assign({}, _dialogData.selectedApprovalByBoard);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedApprovalByBoard !== undefined) {
            if (_dialogData.operation === 'updateApprovalByBoard') {
                this.dialogTitle = 'Modify Approval By Board';
            }
        }
        this.approvalByBoardForm = this._formBuilder.group({
            meetingNumber: [this.selectedApprovalByBoard.meetingNumber || ''],
            meetingDate: [this.selectedApprovalByBoard.meetingDate || ''],
            details: [this.selectedApprovalByBoard.details || ''],
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
        if (this.approvalByBoardForm.valid) {
            var approvalByBoard = this.approvalByBoardForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(approvalByBoard.meetingDate);
            approvalByBoard.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addApprovalByBoard') {
                approvalByBoard.loanApplicationId = this.loanApplicationId;
                this._boardApprovalService.createApprovalByBoard(approvalByBoard).subscribe(() => {
                    this._matSnackBar.open('Approval by board added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedApprovalByBoard.meetingDate = approvalByBoard.meetingDate;
                this.selectedApprovalByBoard.meetingNumber = approvalByBoard.meetingNumber;
                this.selectedApprovalByBoard.details = approvalByBoard.details;
                this._boardApprovalService.updateApprovalByBoard(this.selectedApprovalByBoard).subscribe(() => {
                    this._matSnackBar.open('Approval by board updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
