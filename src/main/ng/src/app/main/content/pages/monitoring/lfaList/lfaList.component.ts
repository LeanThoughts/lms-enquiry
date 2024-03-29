import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LFAUpdateDialogComponent } from '../lfaUpdate/lfaUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-lfa-list',
    templateUrl: './lfaList.component.html',
    styleUrls: ['./lfaList.component.scss'],
    animations: fuseAnimations
})
export class LFAListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    loanApplicationId: string;

    selectedLFA: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _matDialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getLendersFinancialAdvisors(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(lfa: any): void {
        this.selectedLFA = lfa;
        this._loanMonitoringService.selectedLFA.next(lfa);
    }

    /**
     * getAdvisor()
     * @param lfa
     */
    getAdvisor(lfa: any): string {
        return 'LFA';
    }

    /**
     * addLFA()
     */
    addLFA(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LFAUpdateDialogComponent, {
            panelClass: 'fuse-lfa-update-dialog',
            width: '750px',
            data: {
                operation: 'addLFA',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersFinancialAdvisors(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateLFA()
     */
    updateLFA(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(LFAUpdateDialogComponent, {
            panelClass: 'fuse-lfa-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLFA',
                loanApplicationId: this.loanApplicationId,
                selectedLFA: this.selectedLFA
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersFinancialAdvisors(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }    
}
