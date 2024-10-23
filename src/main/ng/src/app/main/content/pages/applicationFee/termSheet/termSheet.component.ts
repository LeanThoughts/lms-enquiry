import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ApplicationFeeService } from '../applicationFee.service';
import { TermSheetUpdateDialogComponent } from '../termSheetUpdate/termSheetUpdate.component';

@Component({
    selector: 'fuse-term-sheet',
    templateUrl: './termSheet.component.html',
    styleUrls: ['./termSheet.component.scss'],
    animations: fuseAnimations
})
export class TermSheetComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'status', 'issuanceDate', 'acceptanceDate', 'download'
    ];

    loanApplicationId: string;

    selectedTermSheet: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, 
                private _applicationFeeService: ApplicationFeeService, 
                private _matDialog: MatDialog,
                private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._applicationFeeService.getTermSheets(this._applicationFeeService._applicationFee.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.termSheets);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedTermSheet = row;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * add()
     */
    add(mode: string): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(TermSheetUpdateDialogComponent, {
            panelClass: 'fuse-term-sheet-update-dialog',
            width: '750px',
            data: {
                operation: mode,
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._applicationFeeService._applicationFee.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                        this._applicationFeeService._applicationFee.next(data);
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
        let mode = '';
        if (this.selectedTermSheet.status === 'Draft')
            mode = 'modifyDraft';
        else
            mode = 'modifyAcceptance';
        // Open the dialog.
        const dialogRef = this._matDialog.open(TermSheetUpdateDialogComponent, {
            panelClass: 'fuse-term-sheet-update-dialog',
            width: '750px',
            data: {
                operation: mode,
                loanApplicationId: this.loanApplicationId,
                selectedTermSheet: this.selectedTermSheet
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
                this._applicationFeeService.deleteTermSheet(this.selectedTermSheet.id).subscribe(() => {
                    this.selectedTermSheet = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected term sheet details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
