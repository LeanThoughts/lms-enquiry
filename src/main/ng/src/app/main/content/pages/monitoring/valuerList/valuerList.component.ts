import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { ValuerUpdateDialogComponent } from '../valuerUpdate/valuerUpdate.component';

@Component({
    selector: 'fuse-valuer-list',
    templateUrl: './valuerList.component.html',
    styleUrls: ['./valuerList.component.scss'],
    animations: fuseAnimations
})
export class ValuerListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    selectedValuer: any;

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog,
                        private _matSnackBar: MatSnackBar) {
                            
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getValuers(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(valuer: any): void {
        this.selectedValuer = valuer;
        this._loanMonitoringService.selectedValuer.next(valuer);
    }

    /**
     * getAdvisor()
     */
    getAdvisor(valuer: any): string {
        return 'Valuer';
    }

    /**
     * addValuer()
     */
    addValuer(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ValuerUpdateDialogComponent, {
            panelClass: 'fuse-valuer-update-dialog',
            width: '750px',
            data: {
                operation: 'addValuer',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getValuers(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateValuer()
     */
    updateValuer(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ValuerUpdateDialogComponent, {
            panelClass: 'fuse-valuer-update-dialog',
            width: '750px',
            data: {
                operation: 'updateValuer',
                loanApplicationId: this.loanApplicationId,
                selectedValuer: this.selectedValuer,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getValuers(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    displayValuer(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(ValuerUpdateDialogComponent, {
            panelClass: 'fuse-valuer-update-dialog',
            width: '750px',
            data: {
                operation: 'displayValuer',
                loanApplicationId: this.loanApplicationId,
                selectedValuer: this.selectedValuer
            }
        });
    }

    /**
     * deleteValuer()
     */
    deleteValuer(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteValuer(this.selectedValuer, this._module).subscribe(() => {
                    this.selectedValuer = undefined;
                    this._loanMonitoringService.getValuers(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected Valuer. Please check if there are Valuer Report and Fee entries.', 'OK', 
                            { duration: 7000 });
                });
            }
        });
    }

}
