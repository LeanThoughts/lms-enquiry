import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { BMCLoanEnhancementUpdateDialogComponent } from '../bmcIccLoanEnhancementUpdate/bmcIccLoanEnhancementUpdate.component';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-bmc-loan-enhancement',
    templateUrl: './bmcIccLoanEnhancement.component.html',
    styleUrls: ['./bmcIccLoanEnhancement.component.scss'],
    animations: fuseAnimations
})
export class BMCLoanEnhancementComponent implements OnInit {

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
    constructor(_loanEnquiryService: LoanEnquiryService, private _bmcApprovalService: BMCApprovalService, private _dialog: MatDialog, 
            private _activatedRoute: ActivatedRoute) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[1]);
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._bmcApprovalService.getBmcLoanEnhancements(this._bmcApprovalService._bmcIccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
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
        const dialogRef = this._dialog.open(BMCLoanEnhancementUpdateDialogComponent, {
            panelClass: 'fuse-loan-enhancement-update-dialog',
            width: '800px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                    this._bmcApprovalService._bmcIccApproval.next(data);
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
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result.response) {
                this._bmcApprovalService.deleteBmcLoanEnhancement(this.selectedLoanEnhancement.id).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                    });
                    this.selectedLoanEnhancement = undefined;
                    this.refreshTable();
                });
            }
        });
    }
}
