import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { SiteVisitUpdateDialogComponent } from '../siteVisitUpdate/siteVisitUpdate.component';

@Component({
    selector: 'fuse-site-visit-list',
    templateUrl: './siteVisitList.component.html',
    styleUrls: ['./siteVisitList.component.scss'],
    animations: fuseAnimations
})
export class SiteVisitListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: any;

    displayedColumns = [
        'serialNumber', 'siteVisitType', 'actualCOD', 'dateOfSiteVisit','dateOfLendersMeet', 'documentType', 'download'
    ];

    selectedSiteVisit: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getSiteVisits(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onSelect()
     */
    onSelect(selectedSiteVisit: any): void {
        this.selectedSiteVisit = selectedSiteVisit;
    }

    /**
     * updatSiteVisit()
     */
    updateSiteVisit(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedSiteVisit': undefined
        };
        if (operation === 'updateSiteVisit') {
            data.selectedSiteVisit = this.selectedSiteVisit;
        }
        const dialogRef = this._dialog.open(SiteVisitUpdateDialogComponent, {
            panelClass: 'fuse-site-visit-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getSiteVisits(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                })
            }
        });    
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
