import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ConfirmationDialogComponent } from '../confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { SecurityTrusteeUpdateDialogComponent } from '../securityTrusteeUpdate/securityTrusteeUpdate.component';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
    selector: 'fuse-securityTrustee-list',
    templateUrl: './securityTrusteeList.component.html',
    styleUrls: ['./securityTrusteeList.component.scss'],
    animations: fuseAnimations
})
export class SecurityTrusteeListComponent {

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'advisor', 'bpCode','name', 'dateOfAppointment', 'contractPeriodFrom', 'contractPeriodTo', 'contactNumber', 'email'
    ];

    loanApplicationId: string;

    selectedSecurityTrustee: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanAppraisalService: LoanAppraisalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanAppraisalService.getSecurityTrustees(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     *
     * @param enquiry
     */
    onSelect(securityTrustee: any): void {
        this.selectedSecurityTrustee = securityTrustee;
        this._loanAppraisalService._selectedSecurityTrustee.next(securityTrustee);
    }

    /**
     * addSecurityTrustee()
     */
    addSecurityTrustee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(SecurityTrusteeUpdateDialogComponent, {
            panelClass: 'fuse-securityTrustee-update-dialog',
            width: '750px',
            data: {
                operation: 'addSecurityTrustee',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSecurityTrustees(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanAppraisalService.getLaonAppraisal(this.loanApplicationId).subscribe(data => {
                    this._loanAppraisalService._loanAppraisalBehaviourSubject.next(data);
                });
            }
        });
    }

    /**
     * updateSecurityTrustee()
     */
    updateSecurityTrustee(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(SecurityTrusteeUpdateDialogComponent, {
            panelClass: 'fuse-securityTrustee-update-dialog',
            width: '750px',
            data: {
                operation: 'updateSecurityTrustee',
                loanApplicationId: this.loanApplicationId,
                selectedSecurityTrustee: this.selectedSecurityTrustee,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSecurityTrustees(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });
    }

    displaySecurityTrustee(): void {
        // Open the dialog.
        this._matDialog.open(SecurityTrusteeUpdateDialogComponent, {
            panelClass: 'fuse-securityTrustee-update-dialog',
            width: '750px',
            data: {
                operation: 'displaySecurityTrustee',
                loanApplicationId: this.loanApplicationId,
                selectedSecurityTrustee: this.selectedSecurityTrustee
            }
        });
    }

    /**
     * deleteSecurityTrustee()
     */
    deleteSecurityTrustee(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanAppraisalService.deleteSecurityTrustee(this.selectedSecurityTrustee, this._module).subscribe(() => {
                    this.selectedSecurityTrustee = undefined;
                    this._loanAppraisalService.getSecurityTrustees(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected Security Trustee. Please check if there are Report and Fee entries.', 'OK', 
                            { duration: 7000 });
                });
            }
        });
    }
}
