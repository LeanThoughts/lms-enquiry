import { Component, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { InsuranceUpdateDialogComponent } from '../insuranceUpdate/insuranceUpdate.component';

@Component({
    selector: 'fuse-insurance-list',
    templateUrl: './insuranceList.component.html',
    styleUrls: ['./insuranceList.component.scss'],
    animations: fuseAnimations
})
export class InsuranceListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'validFrom', 'validTo', 'documentType','documentTitle', 'download'
    ];

    selectedInsurance: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getInsurances(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * addInsurance()
     */
    addInsurance(): void {
        // Open the dialog.
        const dialogRef = this._dialog.open(InsuranceUpdateDialogComponent, {
            panelClass: 'fuse-insurance-update-dialog',
            width: '750px',
            data: {
                operation: 'addInsurance',
                loanApplicationId: this.loanApplicationId,
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getInsurances(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * updateInsurance()
     */
    updateInsurance(): void {
        //Open the dialog.
        const dialogRef = this._dialog.open(InsuranceUpdateDialogComponent, {
            panelClass: 'fuse-insurance-update-dialog',
            width: '750px',
            data: {
                operation: 'updateInsurance',
                loanApplicationId: this.loanApplicationId,
                selectedInsurance: this.selectedInsurance
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getInsurances(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * onSelect()
     */
    onSelect(selectedInsurance: any): void {
        this.selectedInsurance = selectedInsurance;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getDocumentTypeDescription()
     */
    getDocumentTypeDescription(documentType: string): string {
        const obj = LoanMonitoringConstants.documentTypes.filter(f => f.code === documentType)[0];
        if (obj !== undefined)
            return obj.value;
        else
            return '';
    }

    /**
     * deleteInsurance()
     */
    deleteInsurance(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteInsurance(this.selectedInsurance).subscribe(() => {
                    this.selectedInsurance = undefined;
                    this._loanMonitoringService.getInsurances(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }
}
