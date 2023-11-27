import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ICCApprovalService } from '../iccApproval.service';
import { ICCRejectedByCustomerUpdateDialogComponent } from '../iccRejectedByCustomerUpdate/iccRejectedByCustomerUpdate.component';

@Component({
    selector: 'fuse-icc-rejected-by-customer',
    templateUrl: './iccRejectedByCustomer.component.html',
    styleUrls: ['./iccRejectedByCustomer.component.scss'],
    animations: fuseAnimations
})
export class ICCRejectedByCustomerComponent {
    
    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'particulars', 'value'
    ];

    loanApplicationId: string;

    selectedRejectedByCustomer: any;

    disableAdd = false;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._iccApprovalService.getRejectedByCustomer(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.selectedRejectedByCustomer = data;
            let tableData = [];
            tableData.push({particulars: 'ICC Meeting Number', value: this.selectedRejectedByCustomer.meetingNumber});
            tableData.push({particulars: 'Rejection Category', value: this.selectedRejectedByCustomer.rejectionCategory});
            tableData.push({particulars: 'Date of Rejection', value: this.selectedRejectedByCustomer.dateOfRejection});
            tableData.push({particulars: 'Rejection Reason', value: this.selectedRejectedByCustomer.remarks});
            this.dataSource = new MatTableDataSource(tableData);
        });
    }

    /**
     * update()
     */
    update(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(ICCRejectedByCustomerUpdateDialogComponent, {
            panelClass: 'fuse-icc-rejected-by-customer-update-dialog',
            width: '750px',
            data: {
                loanApplicationId: this.loanApplicationId,
                selectedRejectedByCustomer: this.selectedRejectedByCustomer
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.refreshTable();
            }
        });
    }    
}
