import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { BMCApprovalService } from '../bmcIccApproval.service';
import { BMCICCRejectedByCustomerUpdateDialogComponent } from '../bmcIccRejectedByCustomerUpdate/bmcIccRejectedByCustomerUpdate.component';

@Component({
    selector: 'fuse-bmc-icc-rejected-by-customer',
    templateUrl: './bmcIccRejectedByCustomer.component.html',
    styleUrls: ['./bmcIccRejectedByCustomer.component.scss'],
    animations: fuseAnimations
})
export class BMCICCRejectedByCustomerComponent {
    
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
    constructor(_loanEnquiryService: LoanEnquiryService, private _bmcApprovalService: BMCApprovalService, private _matDialog: MatDialog,
                    private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.refreshTable();
    }

    /**
     * refreshTable()
     */
    refreshTable(): void {
        this._bmcApprovalService.getBmcRejectedByCustomer(this._bmcApprovalService._bmcIccApproval.value.id).subscribe(data => {
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
        const dialogRef = this._matDialog.open(BMCICCRejectedByCustomerUpdateDialogComponent, {
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
