import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BoardApprovalService } from '../boardApproval.service';
import { DeferredByBoardUpdateDialogComponent } from '../deferredByBoardUpdate/deferredByBoardUpdate.component';

@Component({
    selector: 'fuse-deferred-by-board',
    templateUrl: './deferredByBoard.component.html',
    styleUrls: ['./deferredByBoard.component.scss'],
    animations: fuseAnimations
})
export class DeferredByBoardComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'details'
    ];

    loanApplicationId: string;

    selectedDeferredByBoard: any;

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
        this._boardApprovalService.getDeferredByBoards().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.deferredByBoards);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedDeferredByBoard = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(DeferredByBoardUpdateDialogComponent, {
            panelClass: 'fuse-deferred-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'addDeferredByBoard',
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
        const dialogRef = this._matDialog.open(DeferredByBoardUpdateDialogComponent, {
            panelClass: 'fuse-deferred-by-board-update-dialog',
            width: '750px',
            data: {
                operation: 'updateDeferredByBoard',
                loanApplicationId: this.loanApplicationId,
                selectedDeferredByBoard: this.selectedDeferredByBoard
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
                this._boardApprovalService.deleteDeferredByBoard(this.selectedDeferredByBoard.id).subscribe(() => {
                    this.selectedDeferredByBoard = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected deferred by board details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
