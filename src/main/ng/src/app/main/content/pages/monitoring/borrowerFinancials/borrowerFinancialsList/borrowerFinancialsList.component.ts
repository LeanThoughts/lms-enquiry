import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { BorrowerFinancialsUpdateDialogComponent } from '../borrowerFinancialsUpdate/borrowerFinancialsUpdate.component';

@Component({
    selector: 'fuse-borrower-financials-list',
    templateUrl: './borrowerFinancialsList.component.html',
    styleUrls: ['./borrowerFinancialsList.component.scss'],
    animations: fuseAnimations
})
export class BorrowerFinancialsListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    @Input()
    set borrowerFinancialsList(borrowerFinancialsList: any) {
        this.dataSource = new MatTableDataSource(borrowerFinancialsList);
        this.dataSource.sort = this.sort
    }

    displayedColumns = [
        'serialNumber', 'fiscalYear', 'turnover', 'pat','netWorth', 'overAllRating', 'pdfAnnualReport', 'pdfRating'
    ];

    loanApplicationId: string;

    selectedFinancials: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getBorrowerFinancials(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * onSelect()
     * @param selectedFinancials
     */
    onSelect(selectedFinancials: any): void {
        this.selectedFinancials = selectedFinancials;
    }

    /**
     * updateBorrowerFinancials()
     * @param operation 
     */
    updateBorrowerFinancials(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedFinancials': undefined
        };
        if (operation === 'updateFinancials') {
            data.selectedFinancials = this.selectedFinancials;
        }
        const dialogRef = this._dialog.open(BorrowerFinancialsUpdateDialogComponent, {
            panelClass: 'fuse-borrower-financials-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getBorrowerFinancials(this.loanApplicationId).subscribe(data => {
                    this.borrowerFinancialsList = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * deleteBorrowerFinancials()
     */
    deleteBorrowerFinancials(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteBorrowerFinancials(this.selectedFinancials).subscribe(() => {
                    this.selectedFinancials = undefined;
                    this._loanMonitoringService.getBorrowerFinancials(this.loanApplicationId).subscribe(data => {
                        this.borrowerFinancialsList = data;
                    });
                });
            }
        });
    }

}
