import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BoardApprovalService } from '../boardApproval.service';

@Component({
    selector: 'fuse-deferred-by-board-update-dialog',
    templateUrl: './deferredByBoardUpdate.component.html',
    styleUrls: ['./deferredByBoardUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class DeferredByBoardUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Deferred By Board';

    loanApplicationId = '';
    selectedDeferredByBoard: any;

    deferredByBoardForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<DeferredByBoardUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected details from the dialog's data attribute.
        this.selectedDeferredByBoard = Object.assign({}, _dialogData.selectedDeferredByBoard);
        this.loanApplicationId = _dialogData.loanApplicationId;
        if (_dialogData.selectedDeferredByBoard !== undefined) {
            if (_dialogData.operation === 'updateDeferredByBoard') {
                this.dialogTitle = 'Modify Deferred By Board';
            }
        }
        this.deferredByBoardForm = this._formBuilder.group({
            meetingNumber: [this.selectedDeferredByBoard.meetingNumber || ''],
            meetingDate: [this.selectedDeferredByBoard.meetingDate || ''],
            details: [this.selectedDeferredByBoard.details || ''],
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
        if (this.deferredByBoardForm.valid) {
            var deferredByBoard = this.deferredByBoardForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(deferredByBoard.meetingDate);
            deferredByBoard.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addDeferredByBoard') {
                deferredByBoard.loanApplicationId = this.loanApplicationId;
                this._boardApprovalService.createDeferredByBoard(deferredByBoard).subscribe(() => {
                    this._matSnackBar.open('Deferred by board added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedDeferredByBoard.meetingDate = deferredByBoard.meetingDate;
                this.selectedDeferredByBoard.meetingNumber = deferredByBoard.meetingNumber;
                this.selectedDeferredByBoard.details = deferredByBoard.details;
                this._boardApprovalService.updateDeferredByBoard(this.selectedDeferredByBoard).subscribe(() => {
                    this._matSnackBar.open('Deferred by board updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
