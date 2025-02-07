import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { ActivatedRoute } from '@angular/router';
import { BMCICCFurtherDetailUpdateDialogComponent } from '../bmcIccFurtherDetailUpdate/bmcIccFurtherDetailUpdate.component';

@Component({
    selector: 'fuse-bmc-icc-further-details',
    templateUrl: './bmcIccFurtherDetails.component.html',
    styleUrls: ['./bmcIccFurtherDetails.component.scss'],
    animations: fuseAnimations
})
export class BMCICCFurtherDetailsComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: any;

    displayedColumns = [
        'serialNumber', 'iccMeetingNumber', 'iccMeetingDate', 'detailsRequired'
    ];

    selectedICCFurtherDetail: any;

    enquiryCompletion: any;
    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _bmcApprovalService: BMCApprovalService, private _dialog: MatDialog, 
                _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[0]);
        this.enquiryCompletion = _activatedRoute.snapshot.data.routeResolvedData[3];
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._bmcApprovalService.getBmcICCFurtherDetails(this._bmcApprovalService._bmcIccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }
    
    /**
     * onSelect()
     */
    onSelect(selectedICCFurtherDetail: any): void {
        this.selectedICCFurtherDetail = selectedICCFurtherDetail;
    }

    /**
     * updateICCFurtherDetail()
     */
    updateICCFurtherDetail(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedICCFurtherDetail': undefined,
            'enquiryCompletion': this.enquiryCompletion
        };
        if (operation === 'updateICCFurtherDetail') {
            data.selectedICCFurtherDetail = this.selectedICCFurtherDetail;
        }
        const dialogRef = this._dialog.open(BMCICCFurtherDetailUpdateDialogComponent, {
            panelClass: 'fuse-icc-further-detail-update-dialog',
            width: '800px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                    this._bmcApprovalService._bmcIccApproval.next(data);
                });
                this.refreshTable();
            }
        });
    }

    /**
     * deleteICCFurtherDetail()
     */
    deleteICCFurtherDetail(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result.response) {
                this._bmcApprovalService.deleteBmcFurtherDetail(this.selectedICCFurtherDetail.id).subscribe(() => {
                    this._bmcApprovalService.getBmcICCApproval(this.loanApplicationId).subscribe(data => {
                        this._bmcApprovalService._bmcIccApproval.next(data);
                    });
                    this.selectedICCFurtherDetail = undefined;
                    this.refreshTable();
                });
            }
        });
    }
}
