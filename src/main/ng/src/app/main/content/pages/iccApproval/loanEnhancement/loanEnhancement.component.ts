import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnhancementUpdateDialogComponent } from '../loanEnhancementUpdate/loanEnhancementUpdate.component';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-loan-enhancement',
    templateUrl: './loanEnhancement.component.html',
    styleUrls: ['./loanEnhancement.component.scss'],
    animations: fuseAnimations
})
export class LoanEnhancementComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: any;

    displayedColumns = [
        'serialNumber', 'iccMeetingNumber', 'iccClearanceDate', 'revisedProjectCost', 'revisedEquity', 'revisedContractAmount', 
                'revisedCommercialOperationsDate', 'reviseRepaymentStartDate', 'remarks'
    ];

    selectedLoanEnhancement: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _dialog: MatDialog, 
            private _activatedRoute: ActivatedRoute) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[1]._embedded.loanEnhancements);
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._iccApprovalService.getLoanEnhancements(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.loanEnhancements);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onSelect()
     */
    onSelect(loanEnhancement: any): void {
        this.selectedLoanEnhancement = loanEnhancement;
    }

    /**
     * updateLoanEnhancement()
     */
    updateLoanEnhancement(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedLoanEnhancement': undefined
        };
        if (operation === 'updateLoanEnhancement') {
            data.selectedLoanEnhancement = this.selectedLoanEnhancement;
        }
        const dialogRef = this._dialog.open(LoanEnhancementUpdateDialogComponent, {
            panelClass: 'fuse-loan-enhancement-update-dialog',
            width: '800px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                    this._iccApprovalService._iccApproval.next(data);
                    this.refreshTable();
                });
            }
        });    
    }

    /**
     * deleteLoanEnhancement()
     */
    deleteLoanEnhancement(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._iccApprovalService.deleteLoanEnhancement(this.selectedLoanEnhancement.id).subscribe(() => {
                    this.selectedLoanEnhancement = undefined;
                    this.refreshTable();
                });
            }
        });
    }
}
