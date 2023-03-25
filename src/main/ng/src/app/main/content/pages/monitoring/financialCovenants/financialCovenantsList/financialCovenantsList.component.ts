import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { FinancialCovenantsUpdateDialogComponent } from '../financialCovenantsUpdate/financialCovenantsUpdate.component';

@Component({
    selector: 'fuse-financial-covenants-list',
    templateUrl: './financialCovenantsList.component.html',
    styleUrls: ['./financialCovenantsList.component.scss'],
    animations: fuseAnimations
})
export class FinancialCovenantsListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'financialCovenantType', 'financialYear', 'debtEquityRatio','dscr', 'tolTnw', 'remarksForDeviation', 'documentTitle', 'download'
    ];

    loanApplicationId: string;

    selectedFinancialCovenants: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getFinancialCovenants(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        })
    }

    /**
     * onSelect()
     * @param selectedFinancialCovenants
     */
    onSelect(selectedFinancialCovenants: any): void {
        this.selectedFinancialCovenants = selectedFinancialCovenants;
    }

    /**
     * getFinancialCovenantType()
     * @param financialCovenantType 
     */
    getFinancialCovenantType(financialCovenantType: any): string {
        return LoanMonitoringConstants.financialCovenantsType.filter(f => f.code === financialCovenantType)[0].value;
    }

    
    /**
     * updateFinancialCovenants()
     * @param operation 
     */
    updateFinancialCovenants(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedFinancialCovenants': undefined
        };
        if (operation === 'updateFinancialCovenants') {
            data.selectedFinancialCovenants = this.selectedFinancialCovenants;
        }
        const dialogRef = this._dialog.open(FinancialCovenantsUpdateDialogComponent, {
            panelClass: 'fuse-financial-covenants-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getFinancialCovenants(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
