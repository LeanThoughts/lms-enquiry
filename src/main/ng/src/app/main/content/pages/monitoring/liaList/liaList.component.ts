import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LIAUpdateDialogComponent } from '../liaUpdate/liaUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-lia-list',
    templateUrl: './liaList.component.html',
    styleUrls: ['./liaList.component.scss'],
    animations: fuseAnimations
})
export class LIAListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    selectedLIA: any;

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
        _loanMonitoringService.getLendersInsuranceAdvisors(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(lia: any): void {
        this.selectedLIA = lia;
        this._loanMonitoringService.selectedLIA.next(lia);
    }

    /**
     * getAdvisor()
     */
    getAdvisor(lia: any): string {
        return 'LIA';
    }

    /**
     * addLIA()
     */
    addLIA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIAUpdateDialogComponent, {
            panelClass: 'fuse-lia-update-dialog',
            width: '750px',
            data: {
                operation: 'addLIA',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersInsuranceAdvisors(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateLIA()
     */
    updateLIA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIAUpdateDialogComponent, {
            panelClass: 'fuse-lia-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLIA',
                loanApplicationId: this.loanApplicationId,
                selectedLIA: this.selectedLIA,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersInsuranceAdvisors(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    displayLIA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIAUpdateDialogComponent, {
            panelClass: 'fuse-lia-update-dialog',
            width: '750px',
            data: {
                operation: 'displayLIA',
                loanApplicationId: this.loanApplicationId,
                selectedLIA: this.selectedLIA
            }
        });
    }
}
