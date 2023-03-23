import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LLCUpdateDialogComponent } from '../llcUpdate/llcUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-llc-list',
    templateUrl: './llcList.component.html',
    styleUrls: ['./llcList.component.scss'],
    animations: fuseAnimations
})
export class LLCListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    selectedLLC: any;

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
        _loanMonitoringService.getLendersLegalCouncils(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(llc: any): void {
        this.selectedLLC = llc;
        this._loanMonitoringService.selectedLLC.next(llc);
    }

    /**
     * getAdvisor()
     */
    getAdvisor(llc: any): string {
        return 'LLC';
    }

    /**
     * addLLC()
     */
    addLLC(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LLCUpdateDialogComponent, {
            panelClass: 'fuse-llc-update-dialog',
            width: '750px',
            data: {
                operation: 'addLLC',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersLegalCouncils(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateLLC()
     */
    updateLLC(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LLCUpdateDialogComponent, {
            panelClass: 'fuse-llc-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLLC',
                loanApplicationId: this.loanApplicationId,
                selectedLLC: this.selectedLLC,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersLegalCouncils(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    displayLLC(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LLCUpdateDialogComponent, {
            panelClass: 'fuse-llc-update-dialog',
            width: '750px',
            data: {
                operation: 'displayLLC',
                loanApplicationId: this.loanApplicationId,
                selectedLLC: this.selectedLLC
            }
        });
    }
}
