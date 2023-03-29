import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { ConfirmationDialogComponent } from '../../../appraisal/confirmationDialog/confirmationDialog.component';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { SecurityComplianceUpdateDialogComponent } from '../securityComplianceUpdate/securityComplianceUpdate.component';

@Component({
    selector: 'fuse-security-compliance-list',
    templateUrl: './securityComplianceList.component.html',
    styleUrls: ['./securityComplianceList.component.scss'],
    animations: fuseAnimations
})
export class SecurityComplianceListComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'particulars', 'qty', 'faceValue','percentage', 'applicability', 'timelines', 'dateOfCreation', 'validityDate', 'value',
            'securityPerfectionDate', 'remarks'
    ];

    loanApplicationId: string;

    selectedSecurityCompliance: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getSecurityCompliances(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     * @param securityCompliance
     */
    onSelect(securityCompliance: any): void {
        this.selectedSecurityCompliance = securityCompliance;
    }

    /**
     * getParticulars()
     * @param collateralObjectTypeCode 
     */
    getParticulars(collateralObjectTypeCode: any): string {
        const collateralObjectType = LoanMonitoringConstants.collateralObjectTypes.filter(f => f.code === collateralObjectTypeCode)[0];
        if (collateralObjectType !== undefined)
            return collateralObjectType.value;
        else
            return '';
    }

    /**
     * getApplicability()
     * @param applicabilityCode 
     */
    getApplicability(applicabilityCode: any): string {
        return LoanMonitoringConstants.applicability.filter(f => f.code === applicabilityCode)[0].value;
    }

    /**
     * updateSecurityCompliance()
     * @param operation 
     */
    updateSecurityCompliance(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedSecurityCompliance': undefined
        };
        if (operation === 'updateSecurityCompliance') {
            data.selectedSecurityCompliance = this.selectedSecurityCompliance;
        }
        const dialogRef = this._dialog.open(SecurityComplianceUpdateDialogComponent, {
            panelClass: 'fuse-security-compliance-update-dialog',
            width: '1000px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getSecurityCompliances(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });    
    }

    /**
     * deleteSecurityCompliance()
     */
    deleteSecurityCompliance(): void {
        const dialogRef = this._dialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._loanMonitoringService.deleteSecurityCompliance(this.selectedSecurityCompliance).subscribe(() => {
                    this.selectedSecurityCompliance = undefined;
                    this._loanMonitoringService.getSecurityCompliances(this.loanApplicationId).subscribe(data => {
                        this.dataSource.data = data;
                    });
                });
            }
        });
    }

}
