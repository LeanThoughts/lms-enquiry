import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ICCFurtherDetailUpdateDialogComponent } from '../iccFurtherDetailUpdate/iccFurtherDetailUpdate.component';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';

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
        if (operation === 'updateFurtherDetail') {
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
     * deleteFurtherDetail()
     */
    deleteFurtherDetail(): void {
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
