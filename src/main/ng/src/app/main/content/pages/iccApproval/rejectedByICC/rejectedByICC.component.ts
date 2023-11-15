import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { RejectedByICCUpdateDialogComponent } from '../rejectedByICCUpdate/rejectedByICCUpdate.component';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-rejected-by-icc',
    templateUrl: './rejectedByICC.component.html',
    styleUrls: ['./rejectedByICC.component.scss'],
    animations: fuseAnimations
})
export class RejectedByICCComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'meetingNumber', 'meetingDate', 'details'
    ];

    loanApplicationId: string;

    selectedRejectedByICC: any;

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
        this.selectedRejectedByICC = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(RejectedByICCUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-icc-update-dialog',
            width: '750px',
            data: {
                operation: 'addRejectedByICC',
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
        const dialogRef = this._matDialog.open(RejectedByICCUpdateDialogComponent, {
            panelClass: 'fuse-rejected-by-icc-update-dialog',
            width: '750px',
            data: {
                operation: 'updateRejectedByICC',
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByICC: this.selectedRejectedByICC
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
            //     this._iccApprovalService.deleteRejectedByBoard(this.selectedRejectedByICC.id).subscribe(() => {
            //         this.selectedRejectedByICC = undefined;
            //         this.refreshTable();
            //     },
            //     (error) => {
            //         this._matSnackBar.open('Unable to delete selected rejected by icc details.', 'OK', { duration: 7000 });
            //     });
            // }
        });
    }
}
