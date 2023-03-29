import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { PromoterFinancialsUpdateDialogComponent } from '../promoterFinancialsUpdate/promoterFinancialsUpdate.component';

@Component({
    selector: 'fuse-promoter-financials-list',
    templateUrl: './promoterFinancialsList.component.html',
    styleUrls: ['./promoterFinancialsList.component.scss'],
    animations: fuseAnimations
})
export class PromoterFinancialsListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

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
        _loanMonitoringService.getPromoterFinancials(this.loanApplicationId).subscribe(data => {
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
     * updatePromoterFinancials()
     */
    updatePromoterFinancials(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedFinancials': undefined
        };
        if (operation === 'updateFinancials') {
            data.selectedFinancials = this.selectedFinancials;
        }
        const dialogRef = this._dialog.open(PromoterFinancialsUpdateDialogComponent, {
            panelClass: 'fuse-promoter-financials-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getPromoterFinancials(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }    

    /**
     * deletePromoterFinancials()
     */
    deletePromoterFinancials(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deletePromoterFinancials(this.selectedFinancials).subscribe(() => {
                    this.selectedFinancials = undefined;
                    this._loanMonitoringService.getPromoterFinancials(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
