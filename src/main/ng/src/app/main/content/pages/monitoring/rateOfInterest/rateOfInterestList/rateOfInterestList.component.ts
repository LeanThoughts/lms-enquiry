import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { RateOfInterestUpdateDialogComponent } from '../rateOfInterestUpdate/rateOfInterestUpdate.component';

@Component({
    selector: 'fuse-rate-of-interest-list',
    templateUrl: './rateOfInterestList.component.html',
    styleUrls: ['./rateOfInterestList.component.scss'],
    animations: fuseAnimations
})
export class RateOfInterestListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'conditionType', 'validFromDate', 'interestTypeIndicator','referenceInterestRate', 'refInterestSign', 'interestRate',
                'calculationDate', 'dueDate', 'interestPaymentFrequency', 'paymentForm'
    ];

    loanApplicationId: string;
    selectedRateOfInterest: any;

    conditionTypes: any;
    referenceInterestRates: any;
    paymentForms: any;

    /**
     * constructor()
     */
    constructor(_enquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {

        _loanMonitoringService.getConditionTypes().subscribe(data => {
            this.conditionTypes = data;
        });

        _loanMonitoringService.getReferenceInterestRates().subscribe(data => {
            this.referenceInterestRates = data;
        });

        _loanMonitoringService.getPaymentForms().subscribe(data => {
            this.paymentForms = data;
        });

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        _loanMonitoringService.getRateOfInterests(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     * @param selectedRateOfInterest
     */
    onSelect(selectedRateOfInterest: any): void {
        this.selectedRateOfInterest = selectedRateOfInterest;
    }

    
    /**
     * updateRateOfInterest()
     * @param operation 
     */
    updateRateOfInterest(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedRateOfInterest': undefined
        };
        if (operation === 'updateRateOfInterest') {
            data.selectedRateOfInterest = this.selectedRateOfInterest;
        }
        const dialogRef = this._dialog.open(RateOfInterestUpdateDialogComponent, {
            panelClass: 'fuse-rate-of-interest-update-dialog',
            width: '850px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getRateOfInterests(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                })
            }
        });    
    }

    /**
     * getConditionType()
     */
    getConditionType(conditionType: string): string {
        const obj = this.conditionTypes.filter(f => f.code === conditionType)[0];
        if (obj !== undefined)
            return obj.description;
        else
            return '';
    }

    /**
     * getReferenceInterestRate()
     */
    getReferenceInterestRate(referenceInterestRate: string): string {
        const obj = this.referenceInterestRates.filter(f => f.code === referenceInterestRate)[0];
        if (obj !== undefined)
            return obj.description;
        else
            return '';
    }

    /**
     * getPaymentForms()
     */
    getPaymentForms(paymentForm: string): string {
        const obj = this.paymentForms.filter(f => f.code === paymentForm)[0];
        if (obj !== undefined)
            return obj.description;
        else
            return '';
    }

    /**
     * deleteRateOfInterest()
     */
    deleteRateOfInterest(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteRateOfInterest(this.selectedRateOfInterest).subscribe(() => {
                    this.selectedRateOfInterest = undefined;
                    this._loanMonitoringService.getRateOfInterests(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
