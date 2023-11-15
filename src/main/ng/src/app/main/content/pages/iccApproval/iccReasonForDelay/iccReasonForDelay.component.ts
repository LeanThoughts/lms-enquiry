import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCReasonForDelayUpdateDialogComponent } from '../iccReasonForDelayUpdate/iccReasonForDelayUpdate.component';

@Component({
    selector: 'fuse-icc-reason-for-delay',
    templateUrl: './iccReasonForDelay.component.html',
    styleUrls: ['./iccReasonForDelay.component.scss'],
    animations: fuseAnimations
})
export class ICCReasonForDelayComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'reasonForDelay', 'date'
    ];

    loanApplicationId: string;

    selectedReasonForDelay: any;

    disableAdd = false;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        // this._iccApprovalService.getRejectedByBoards().subscribe(data => {
        //     if (data._embedded.rejectedByBoards.length === 0)
        //         this.disableAdd = false;
        //     else
        //         this.disableAdd = true;
        //     this.dataSource = new MatTableDataSource(data._embedded.rejectedByBoards);
        //     this.dataSource.sort = this.sort;
        // });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedReasonForDelay = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-icc-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'addReasonForDelay',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._iccApprovalService._iccApproval.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    // this._iccApprovalService.getBoardApproval(this.loanApplicationId).subscribe(data => {
                    //     this._iccApprovalService._boardApproval.next(data);
                    //     this.refreshTable(); 
                    // });
                }
            }
        });    
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCReasonForDelayUpdateDialogComponent, {
            panelClass: 'fuse-icc-reason-for-delay-update-dialog',
            width: '750px',
            data: {
                operation: 'updateReasonForDelay',
                loanApplicationId: this.loanApplicationId,
                selectedReasonForDelay: this.selectedReasonForDelay
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
            // if (response) {
            //     this._iccApprovalService.deleteRejectedByBoard(this.selectedICCApproval.id).subscribe(() => {
            //         this.selectedICCApproval = undefined;
            //         this.refreshTable();
            //     },
            //     (error) => {
            //         this._matSnackBar.open('Unable to delete selected rejected by icc details.', 'OK', { duration: 7000 });
            //     });
            // }
        });
    }
}
