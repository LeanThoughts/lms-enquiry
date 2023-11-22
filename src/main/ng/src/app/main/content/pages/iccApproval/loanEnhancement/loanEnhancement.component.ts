import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnhancementUpdateDialogComponent } from '../loanEnhancementUpdate/loanEnhancementUpdate.component';

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
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        // _loanMonitoringService.getSiteVisits(this.loanApplicationId).subscribe(data => {
        //     this.dataSource = new MatTableDataSource(data);
        //     this.dataSource.sort = this.sort;
        // });
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
                // this._loanMonitoringService.getSiteVisits(this.loanApplicationId).subscribe(data => {
                //     this.dataSource.data = data;
                // });
                // this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                //     this._loanMonitoringService.loanMonitor.next(data);
                // })
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
                // this._loanMonitoringService.deleteSiteVisit(this.selectedICCFurtherDetail, this._module).subscribe(() => {
                //     this.selectedICCFurtherDetail = undefined;
                //     this._loanMonitoringService.getSiteVisits(this.loanApplicationId).subscribe(data => {
                //         this.dataSource.data = data;
                //     });
                // });
            }
        });
    }
}
