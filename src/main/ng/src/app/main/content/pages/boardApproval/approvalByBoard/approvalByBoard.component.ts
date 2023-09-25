import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BoardApprovalService } from '../boardApproval.service';
import { ApprovalByBoardUpdateDialogComponent } from '../approvalByBoardUpdate/approvalByBoardUpdate.component';

@Component({
    selector: 'fuse-approval-by-board',
    templateUrl: './approvalByBoard.component.html',
    styleUrls: ['./approvalByBoard.component.scss'],
    animations: fuseAnimations
})
export class ApprovalByBoardComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'details'
    ];

    loanApplicationId: string;

    selectedApprovalByBoard: any;

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
        this._boardApprovalService.getApprovalByBoards().subscribe(data => {
            if (data._embedded.approvalByBoards.length === 0)
                this.disableAdd = false;
            else
                this.disableAdd = true;
            this.dataSource = new MatTableDataSource(data._embedded.approvalByBoards);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedApprovalByBoard = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ApprovalByBoardUpdateDialogComponent, {
            panelClass: 'fuse-approval-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'addApprovalByBoard',
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
        const dialogRef = this._matDialog.open(ApprovalByBoardUpdateDialogComponent, {
            panelClass: 'fuse-approval-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'updateApprovalByBoard',
                loanApplicationId: this.loanApplicationId,
                selectedApprovalByBoard: this.selectedApprovalByBoard
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
                this._boardApprovalService.deleteApprovalByBoard(this.selectedApprovalByBoard.id).subscribe(() => {
                    this.selectedApprovalByBoard = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected approval by board details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
