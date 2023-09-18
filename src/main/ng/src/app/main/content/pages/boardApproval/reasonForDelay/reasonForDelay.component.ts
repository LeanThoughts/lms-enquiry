import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BoardApprovalService } from '../boardApproval.service';
import { BoardApprovalReasonForDelayUpdateDialogComponent } from '../reasonForDelayUpdate/reasonForDelayUpdate.component';

@Component({
    selector: 'fuse-board-approval-reason-for-delay',
    templateUrl: './reasonForDelay.component.html',
    styleUrls: ['./reasonForDelay.component.scss'],
    animations: fuseAnimations
})
export class BoardApprovalReasonForDelayComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'date', 'reason'
    ];

    loanApplicationId: string;

    selectedReason: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _boardApprovalService: BoardApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _boardApprovalService.getReasonForDelays().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(lfa: any): void {
        this.selectedReason = lfa;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(BoardApprovalReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-ba-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'addReason',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._boardApprovalService._boardApproval.value.id !== '') {
                    this._boardApprovalService.getReasonForDelays().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                        this.dataSource.sort = this.sort;
                    });    
                }
                else {
                    this._boardApprovalService.getBoardApproval(this.loanApplicationId).subscribe(data => {
                        this._boardApprovalService._boardApproval.next(data);
                        this._boardApprovalService.getReasonForDelays().subscribe(data => {
                            this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                            this.dataSource.sort = this.sort;
                        });  
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
        const dialogRef = this._matDialog.open(BoardApprovalReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-ba-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'updateReason',
                loanApplicationId: this.loanApplicationId,
                selectedReason: this.selectedReason
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._boardApprovalService.getReasonForDelays().subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                    this.dataSource.sort = this.sort;
                });
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
                this._boardApprovalService.deleteBoardApprovalReasonForDelay(this.selectedReason.id).subscribe(() => {
                    this.selectedReason = undefined;
                    this._boardApprovalService.getReasonForDelays().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.boardApprovalReasonForDelays);
                        this.dataSource.sort = this.sort;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected reason for delay.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
