import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { SanctionService } from '../sanction.service';
import { SanctionLetterUpdateDialogComponent } from '../sanctionLetterUpdate/sanctionLetterUpdate.component';

@Component({
    selector: 'fuse-sanction-letter',
    templateUrl: './sanctionLetter.component.html',
    styleUrls: ['./sanctionLetter.component.scss'],
    animations: fuseAnimations
})
export class sanctionLetterComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'sanctionLetterIssueDate', 'borrowerRequestLetterDate', 'sanctionLetterAcceptanceDate', 'documentType', 'documentTitle'
    ];

    loanApplicationId: string;

    selectedSanctionLetter: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _sanctionService: SanctionService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._sanctionService.getSanctionLetters().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.sanctionLetters);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(row: any): void {
        this.selectedSanctionLetter = row;
    }

    /**
     * add()
     */
    add(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(SanctionLetterUpdateDialogComponent, {
            panelClass: 'fuse-sanction-letter-update-dialog',
            width: '850px',
            data: {
                operation: 'addSanctionLetter',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._sanctionService._sanction.value.id !== '') {
                    this.refreshTable();
                }
                else {
                    this._sanctionService.getSanction(this.loanApplicationId).subscribe(data => {
                        this._sanctionService._sanction.next(data);
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
        const dialogRef = this._matDialog.open(SanctionLetterUpdateDialogComponent, {
            panelClass: 'fuse-sanction-letter-update-dialog',
            width: '850px',
            data: {
                operation: 'updateSanctionLetter',
                loanApplicationId: this.loanApplicationId,
                selectedSanctionLetter: this.selectedSanctionLetter
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
                this._sanctionService.deleteSanctionLetter(this.selectedSanctionLetter.id).subscribe(() => {
                    this.selectedSanctionLetter = undefined;
                    this.refreshTable();
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected sanction letter details.', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
