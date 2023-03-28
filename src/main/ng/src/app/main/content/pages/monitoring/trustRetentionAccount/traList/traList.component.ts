import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { TRAUpdateDialogComponent } from '../traUpdate/traUpdate.component';

@Component({
    selector: 'fuse-tra-list',
    templateUrl: './traList.component.html',
    styleUrls: ['./traList.component.scss'],
    animations: fuseAnimations
})
export class TRAListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'bankKey', 'traBankName','branch', 'address', 'beneficiaryName', 'ifscCode', 'accountNumber', 'contactName', 'typeOfAccount',
            'contactNumber', 'email', 'pfsAuthorisedPerson'
    ];

    loanApplicationId: string;

    selectedTRA: any;

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
        _loanMonitoringService.getTrustRetentionaccounts(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(tra: any): void {
        this.selectedTRA = tra;
        this._loanMonitoringService.selectedTRA.next(Object.assign({}, this.selectedTRA));
    }

    /**
     * addTRA()
     */
    addTRA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(TRAUpdateDialogComponent, {
            panelClass: 'fuse-tra-update-dialog',
            width: '750px',
            data: {
                operation: 'addTRA',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getTrustRetentionaccounts(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * updateTRA()
     */
    updateTRA(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(TRAUpdateDialogComponent, {
            panelClass: 'fuse-tra-update-dialog',
            width: '900px',
            data: {
                operation: 'updateTRA',
                loanApplicationId: this.loanApplicationId,
                selectedTRA: this.selectedTRA,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getTrustRetentionaccounts(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * deleteTRA()
     */
    deleteTRA(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteTRA(this.selectedTRA, this._module).subscribe(() => {
                    this.selectedTRA = undefined;
                    this._loanMonitoringService.getTrustRetentionaccounts(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected TRA. Please check if there are TRA Statements for the selected entry.', 'OK', 
                            { duration: 7000 });
                });
            }
        });
    }
}
