import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { OperatingParameterUpdateDialogComponent } from '../operatingParameterUpdate/operatingParameterUpdate.component';

@Component({
    selector: 'fuse-operating-parameter-list',
    templateUrl: './operatingParameterList.component.html',
    styleUrls: ['./operatingParameterList.component.scss'],
    animations: fuseAnimations
})
export class OperatingParameterListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'month', 'year', 'exportNetGeneration', 'plfCufActual', 'applicableTariff', 'revenue', 'dateOfInvoice', 
                'dateOfPaymentReceipt', 'carbonDiOxideEmission', 'waterSaved'
    ];

    loanApplicationId: string;

    selectedOperatingParameter: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getOperatingParameters(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort
        })
    }

    /**
     * onSelect()
     */
    onSelect(selectedOperatingParameter: any): void {
        this.selectedOperatingParameter = selectedOperatingParameter;
    }

    /**
     * updateOperatingParameter()
     * @param operation 
     */
    updateOperatingParameter(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedOperatingParameter': undefined
        };
        if (operation === 'updateOperatingParameter') {
            data.selectedOperatingParameter = this.selectedOperatingParameter;
        }
        const dialogRef = this._dialog.open(OperatingParameterUpdateDialogComponent, {
            panelClass: 'fuse-operating-parameter-update-dialog',
            width: '1000px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getOperatingParameters(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * deleteOperatingParameter()
     */
    deleteOperatingParameter(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteOperatingParameter(this.selectedOperatingParameter).subscribe(() => {
                    this.selectedOperatingParameter = undefined;
                    this._loanMonitoringService.getOperatingParameters(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
