import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { ICCFurtherDetailUpdateDialogComponent } from '../iccFurtherDetailUpdate/iccFurtherDetailUpdate.component';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { ActivatedRoute } from '@angular/router';
import { RiskNotificationUpdateDialogComponent } from '../riskNotificationUpdate/riskNotificationUpdate.component';

@Component({
    selector: 'fuse-risk-notifications',
    templateUrl: './riskNotifications.component.html',
    styleUrls: ['./riskNotifications.component.scss'],
    animations: fuseAnimations
})
export class RiskNotificationComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: any;

    displayedColumns = [
        'serialNumber', 'notificationDate', 'remarks'
    ];

    selectedRiskNotification: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService, private _dialog: MatDialog, 
                _activatedRoute: ActivatedRoute, private _matSnackBar: MatSnackBar) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[2]);
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
        this._iccApprovalService.getRiskNotifications(this._iccApprovalService._iccApproval.value.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }
    
    /**
     * onSelect()
     */
    onSelect(selectedRiskNotification: any): void {
        this.selectedRiskNotification = selectedRiskNotification;
    }

    /**
     * updateRiskNotification()
     */
    updateRiskNotification(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedRiskNotification': undefined
        };
        if (operation === 'updateRiskNotification') {
            data.selectedRiskNotification = this.selectedRiskNotification;
        }
        const dialogRef = this._dialog.open(RiskNotificationUpdateDialogComponent, {
            panelClass: 'fuse-risk-notification-update-dialog',
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
     * deleteRiskNotification()
     */
    deleteRiskNotification(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._iccApprovalService.deleteRiskNotification(this.selectedRiskNotification.id).subscribe(() => {
                    this.selectedRiskNotification = undefined;
                    this.refreshTable();
                });
            }
        });
    }

    sendNotification(): void {
        var obj = Object.assign({}, this.selectedRiskNotification);
        obj.loanApplicationId = this.loanApplicationId;
        this._iccApprovalService.sendNotification(obj).subscribe(() => {
            this._matSnackBar.open('Notification was sent successfully.', 'OK', { duration: 7000 });
        });
    }
}
