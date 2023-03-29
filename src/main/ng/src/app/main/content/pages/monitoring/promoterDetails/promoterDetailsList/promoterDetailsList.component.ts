import { Component, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { PromoterDetailsUpdateDialogComponent } from '../promoterDetailsUpdate/promoterDetailsUpdate.component';

@Component({
    selector: 'fuse-promoter-details-list',
    templateUrl: './promoterDetailsList.component.html',
    styleUrls: ['./promoterDetailsList.component.scss'],
    animations: fuseAnimations
})
export class PromoterDetailsItemListComponent {

    loanApplicationId: string;

    selectedPromoterDetailItem: any;

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'shareHoldingCompany', 'paidupCapitalEquitySanction', 'paidupCapitalEquityCurrent','equityLinkInstrumentSanction', 
                'equityLinkInstrumentCurrent'
    ];

    /**
     * constructor()
     */
    constructor(_enquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog,
                        private _formBuilder: FormBuilder, private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        _loanMonitoringService.getPromoterDetailItems(this.loanApplicationId).subscribe(data => {
            console.log('data', data);
            if (data.length > 0) {
                this.dataSource = new MatTableDataSource(data);
                this.dataSource.sort = this.sort;
            }
        });
    }

    /**
     * onSelect()
     */
    onSelect(promoterDetailsItem: any): void {
        this.selectedPromoterDetailItem = promoterDetailsItem;
    }

    /**
     * updatePromoterDetailItem()
     */
    updatePromoterDetailItem(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedPromoterDetailItem': undefined
        };
        if (operation === 'updatePromoterDetailItem') {
            data.selectedPromoterDetailItem = this.selectedPromoterDetailItem;
        }
        const dialogRef = this._dialog.open(PromoterDetailsUpdateDialogComponent, {
            panelClass: 'fuse-promoter-details-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getPromoterDetailItems(this.loanApplicationId).subscribe(data1 => {
                    if (data1.length > 0) {
                        console.log('response data', data1);
                        this.dataSource.data = data1;
                    }
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data2 => {
                    this._loanMonitoringService.loanMonitor.next(data2);
                })
            }
        });
    }

    /**
     * deletePromoterDetailItem()
     */
    deletePromoterDetailItem(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deletePromoterDetailItem(this.selectedPromoterDetailItem).subscribe(() => {
                    this.selectedPromoterDetailItem = undefined;
                    this._loanMonitoringService.getPromoterDetailItems(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
