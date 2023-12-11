import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ICCFurtherDetailUpdateDialogComponent } from '../iccFurtherDetailUpdate/iccFurtherDetailUpdate.component';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'fuse-icc-further-dtails',
    templateUrl: './iccFurtherDetails.component.html',
    styleUrls: ['./iccFurtherDetails.component.scss'],
    animations: fuseAnimations
})
export class ICCFurtherDetailsComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: any;

    displayedColumns = [
        'serialNumber', 'iccMeetingNumber', 'iccMeetingDate', 'detailsRequired'
    ];

    selectedICCFurtherDetail: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _dialog: MatDialog, 
                _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[0]);
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
        this._iccApprovalService.getICCFurtherDetails(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
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
            'selectedICCFurtherDetail': undefined
        };
        if (operation === 'updateICCFurtherDetail') {
            data.selectedICCFurtherDetail = this.selectedICCFurtherDetail;
        }
        const dialogRef = this._dialog.open(ICCFurtherDetailUpdateDialogComponent, {
            panelClass: 'fuse-icc-further-detail-update-dialog',
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
     * deleteICCFurtherDetail()
     */
    deleteICCFurtherDetail(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._iccApprovalService.deleteFurtherDetail(this.selectedICCFurtherDetail.id).subscribe(() => {
                    this.selectedICCFurtherDetail = undefined;
                    this.refreshTable();
                });
            }
        });
    }
}
