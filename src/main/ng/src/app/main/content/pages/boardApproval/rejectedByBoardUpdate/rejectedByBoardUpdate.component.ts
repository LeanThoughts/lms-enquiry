import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BoardApprovalService } from '../boardApproval.service';

@Component({
    selector: 'fuse-rejected-by-board-update-dialog',
    templateUrl: './rejectedByBoardUpdate.component.html',
    styleUrls: ['./rejectedByBoardUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class RejectedByBoardUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Rejected By Board';

    loanApplicationId = '';
    selectedRejectedByBoard: any;

    rejectedByBoardForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<RejectedByBoardUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedRejectedByBoard = Object.assign({}, _dialogData.selectedRejectedByBoard);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedRejectedByBoard !== undefined) {
            if (_dialogData.operation === 'updateRejectedByBoard') {
                this.dialogTitle = 'Modify Rejected By Board';
            }
        }
        this.rejectedByBoardForm = this._formBuilder.group({
            meetingNumber: [this.selectedRejectedByBoard.meetingNumber || ''],
            meetingDate: [this.selectedRejectedByBoard.meetingDate || ''],
            details: [this.selectedRejectedByBoard.details || ''],
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
        if (this.rejectedByBoardForm.valid) {
            var rejectedByBoard = this.rejectedByBoardForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(rejectedByBoard.meetingDate);
            rejectedByBoard.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addRejectedByBoard') {
                rejectedByBoard.loanApplicationId = this.loanApplicationId;
                this._boardApprovalService.createRejectedByBoard(rejectedByBoard).subscribe(() => {
                    this._matSnackBar.open('Rejected by board added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedRejectedByBoard.meetingDate = rejectedByBoard.meetingDate;
                this.selectedRejectedByBoard.meetingNumber = rejectedByBoard.meetingNumber;
                this.selectedRejectedByBoard.details = rejectedByBoard.details;
                this._boardApprovalService.updateRejectedByBoard(this.selectedRejectedByBoard).subscribe(() => {
                    this._matSnackBar.open('Rejected by board updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
