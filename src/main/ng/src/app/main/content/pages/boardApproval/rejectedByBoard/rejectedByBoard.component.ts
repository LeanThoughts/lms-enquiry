import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BoardApprovalService } from '../boardApproval.service';
import { RejectedByBoardUpdateDialogComponent } from '../rejectedByBoardUpdate/rejectedByBoardUpdate.component';

@Component({
    selector: 'fuse-rejected-by-board',
    templateUrl: './rejectedByBoard.component.html',
    styleUrls: ['./rejectedByBoard.component.scss'],
    animations: fuseAnimations
})
export class RejectedByBoardComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'details'
    ];

    loanApplicationId: string;

    selectedRejectedByBoard: any;

    disableAdd = true;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _boardApprovalService: BoardApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._boardApprovalService.getRejectedByBoards().subscribe(data => {
            if (data._embedded.rejectedByBoards.length === 0)
                this.disableAdd = false;
            else
                this.disableAdd = true;
            this.dataSource = new MatTableDataSource(data._embedded.rejectedByBoards);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedRejectedByBoard = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(RejectedByBoardUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'addRejectedByBoard',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._boardApprovalService._boardApproval.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._boardApprovalService.getBoardApproval(this.loanApplicationId).subscribe(data => {
                        this._boardApprovalService._boardApproval.next(data);
                        this.refreshTable(); 
                    });
                }
            }
        });    
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(RejectedByBoardUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'updateRejectedByBoard',
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByBoard: this.selectedRejectedByBoard
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.refreshTable();
            }
        });
    }

    /**
     * delete()
     */
    delete(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._boardApprovalService.deleteRejectedByBoard(this.selectedRejectedByBoard.id).subscribe(() => {
                    this.selectedRejectedByBoard = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected rejected by board details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
