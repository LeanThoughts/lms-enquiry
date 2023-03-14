import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { CLAUpdateDialogComponent } from '../claUpdate/claUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-cla-list',
    templateUrl: './claList.component.html',
    styleUrls: ['./claList.component.scss'],
    animations: fuseAnimations
})
export class CLAListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    selectedCLA: any;

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getCommonLoanAgreements(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(cla: any): void {
        this.selectedCLA = cla;
        this._loanMonitoringService.selectedCLA.next(cla);
    }

    /**
     * getAdvisor()
     */
    getAdvisor(cla: any): string {
        return 'CLA';
    }

    /**
     * addCLA()
     */
    addCLA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(CLAUpdateDialogComponent, {
            panelClass: 'fuse-cla-update-dialog',
            width: '750px',
            data: {
                operation: 'addCLA',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getCommonLoanAgreements(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateCLA()
     */
    updateCLA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(CLAUpdateDialogComponent, {
            panelClass: 'fuse-cla-update-dialog',
            width: '750px',
            data: {
                operation: 'updateCLA',
                loanApplicationId: this.loanApplicationId,
                selectedCLA: this.selectedCLA,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getCommonLoanAgreements(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    displayCLA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(CLAUpdateDialogComponent, {
            panelClass: 'fuse-cla-update-dialog',
            width: '750px',
            data: {
                operation: 'displayCLA',
                loanApplicationId: this.loanApplicationId,
                selectedCLA: this.selectedCLA
            }
        });
    }
}
