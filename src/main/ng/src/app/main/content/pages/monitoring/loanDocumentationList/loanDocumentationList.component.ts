import { Component, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { LoanDocumentationUpdateDialogComponent } from '../loanDocumentationUpdate/loanDocumentationUpdate.component';
import {LoanMonitoringConstants} from '../../../model/loanMonitoringConstants';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';

@Component({
    selector: 'fuse-loan-documentation-list',
    templateUrl: './loanDocumentationList.component.html',
    styleUrls: ['./loanDocumentationList.component.scss'],
    animations: fuseAnimations
})
export class LoanDocumentationListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'documentationTypeCode', 'documentationTypeDescription', 'executionDate' , 'approvalDate', 'loanDocumentationStatusCode',
        'loanDocumentationStatusCodeDescription', 'documentType', 'documentTitle', 'remarks', 'fileReference'
    ];

    selectedLoanDocumentation: any;


    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
        console.log('this._module', this._module);
        console.log('m', m);
    }

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getLoanDocumentations(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(loanDocumentation: any): void {
        this.selectedLoanDocumentation = loanDocumentation;
        this._loanMonitoringService.selectedLoanDocumentation.next(loanDocumentation);
    }


  /**
   * getDocumentationType()
   */
  getDocumentationType(documentationType: string): string {
    const filtered = LoanMonitoringConstants.documentationTypes.filter(obj => obj.code === documentationType);
    return filtered[0].value;
  }
  /**
   * getDocumentationStatus()
   */
  getDocumentationStatus(documentationStatus: string): string {
    const filtered = LoanMonitoringConstants.documentationStatuses.filter(obj => obj.code === documentationStatus);
    return filtered[0].value;
  }

  /**
   * getDocumentType()
   */
  getDocumentType(documentType: string): string {
    const filtered = LoanMonitoringConstants.documentTypes.filter(obj => obj.code === documentType);
    return filtered[0].value;
  }

    /**
     * addLIE()
     */
    addLoanDocumentation(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LoanDocumentationUpdateDialogComponent, {
            panelClass: 'fuse-loan-documentation-dialog',
            width: '750px',
            data: {
                operation: 'addLoanDocumentation',
                loanApplicationId: this.loanApplicationId,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result.refresh) {
                this._loanMonitoringService.getLoanDocumentations(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });
    }

    /**
     * updateLIE()
     */
    updateLoanDocumentation(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LoanDocumentationUpdateDialogComponent, {
            panelClass: 'fuse-loan-documentation-dialog',
            width: '750px',
            data: {
                operation: 'updateLoanDocumentation',
                loanApplicationId: this.loanApplicationId,
                selectedLoanDocumentation: this.selectedLoanDocumentation,
                module: this._module
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result.refresh) {
                this._loanMonitoringService.getLoanDocumentations(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });
    }

    /**
     * displayLoanDocumentation()
     */
    displayLoanDocumentation(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(LoanDocumentationUpdateDialogComponent, {
            panelClass: 'fuse-loan-documentation-dialog',
            width: '750px',
            data: {
                operation: 'displayLoanDocumentation',
                loanApplicationId: this.loanApplicationId,
                selectedLoanDocumentation: this.selectedLoanDocumentation
            }
        });
    }

    /**
     * deleteLoanDocumentation()
     */
    deleteLoanDocumentation(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteLoanDocumentation(this.selectedLoanDocumentation, this._module).subscribe(() => {
                    this.selectedLoanDocumentation = undefined;
                    this._loanMonitoringService.getLoanDocumentations(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
