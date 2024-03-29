import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../confirmationDialog/confirmationDialog.component';
import { businessPartnerRoleTypes } from '../../loanAppraisal.constants';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { LoanPartnerUpdateComponent } from '../loan-partner-update/loan-partner-update.component';

@Component({
    selector: 'fuse-loan-partners',
    templateUrl: './loan-partner-list.component.html',
    styleUrls: ['./loan-partner-list.component.scss'],
    animations: fuseAnimations
})
export class LoanPartnerListComponent {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'businessPartnerId', 'businessPartnerName', 'roleType','startDate'
    ];

    selectedLoanOfficer: any;

    _loanApplicationId: string;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog,
                _loanEnquiryService: LoanEnquiryService,
                private _loanAppraisalService: LoanAppraisalService,
                _activatedRoute: ActivatedRoute) { 

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;

        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[0]._embedded.loanPartners);
    }

    /**
     * openUpdateDialog()
     * @param operation 
     */
    openUpdateDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'loanOfficer': {},
        };
        if (operation === 'modifyOfficer') {
            data.loanOfficer = this.selectedLoanOfficer;
        }
        const dialogRef = this._dialogRef.open(LoanPartnerUpdateComponent, {
            panelClass: 'loan-partner-update',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.getLoanOfficers();
            }
        });       
    }

    /**
     * openDeleteDialog()
     */
    openDeleteDialog(): void {
        const dialogRef = this._dialogRef.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.response) {
                this._loanAppraisalService.deleteLoanOfficer(this.selectedLoanOfficer.id).subscribe(() => {
                    this.getLoanOfficers();
                });
            }
        });
    }

    /**
     * getLoanOfficers()
     */
    getLoanOfficers(): void {
        this._loanAppraisalService.getLoanOfficers(this._loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.loanPartners);
        });
    }

    /**
     * onRowSelect()
     * @param loanOfficer 
     */
    onRowSelect(loanOfficer: any): void {
        this.selectedLoanOfficer = loanOfficer;
    }

    /**
     * getRoleDescription()
     * @param role 
     */
    getRoleDescription(role: string): string {
        let rv = '';
        businessPartnerRoleTypes.forEach(roleType => {
            if (roleType.code === role) {
                rv = roleType.value;
            }
        });
        return rv;
    }
}
