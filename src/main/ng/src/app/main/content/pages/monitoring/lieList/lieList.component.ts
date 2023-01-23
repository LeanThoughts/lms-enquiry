import { Component, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LIEUpdateDialogComponent } from '../lieUpdate/lieUpdate.component';
import { LoanMonitoringService } from '../loanMonitoring.service';

@Component({
    selector: 'fuse-lie-list',
    templateUrl: './lieList.component.html',
    styleUrls: ['./lieList.component.scss'],
    animations: fuseAnimations
})
export class LIEListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    selectedLIE: any;

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
        console.log('this._module', this._module);
        console.log('m', m);
    }

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getLendersIndependentEngineers(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    // ngOnChanges(changes: SimpleChanges): void {
    //     console.log(changes['module'].currentValue);
    //     this._module = changes['module'].currentValue;
    //     console.log('after ngonchanges module', this._module);
    // }

    /**
     * onSelect()
     */
    onSelect(lie: any): void {
        this.selectedLIE = lie;
        this._loanMonitoringService.selectedLIE.next(lie);
    }

    /**
     * getAdvisor()
     */
    getAdvisor(lie: any): string {
        return 'LIE';
    }

    /**
     * addLIE()
     */
    addLIE(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIEUpdateDialogComponent, {
            panelClass: 'fuse-lie-update-dialog',
            width: '750px',
            data: {
                operation: 'addLIE',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersIndependentEngineers(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateLIE()
     */
    updateLIE(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIEUpdateDialogComponent, {
            panelClass: 'fuse-lie-update-dialog',
            width: '750px',
            data: {
                operation: 'updateLIE',
                loanApplicationId: this.loanApplicationId,
                selectedLIE: this.selectedLIE
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getLendersIndependentEngineers(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    displayLIE(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LIEUpdateDialogComponent, {
            panelClass: 'fuse-lie-update-dialog',
            width: '750px',
            data: {
                operation: 'displayLIE',
                loanApplicationId: this.loanApplicationId,
                selectedLIE: this.selectedLIE
            }
        });
    }
}
