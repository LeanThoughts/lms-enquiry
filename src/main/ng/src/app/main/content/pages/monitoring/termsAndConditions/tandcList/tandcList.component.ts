import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { TandCUpdateDialogComponent } from '../tandcUpdate/tandcUpdate.component';

@Component({
    selector: 'fuse-tandc-list',
    templateUrl: './tandcList.component.html',
    styleUrls: ['./tandcList.component.scss'],
    animations: fuseAnimations
})
export class TandCListComponent implements OnInit {

    loanApplicationId: string;

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'communication', 'remarks','borrowerRequestLetterDate', 'dateofIssueofAmendedSanctionLetter', 'documentType', 'download'
    ];

    selectedTandC: any;

    /**
     * constructor()
     */
    constructor(_enquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {
        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;
        _loanMonitoringService.getTermsAndConditions(this.loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     *
     * @param enquiry
     */
    onSelect(tandc: any): void {
        this.selectedTandC = tandc;
    }

    /**
     * getCommunication()
     * @param communication 
     */
    getCommunication(communication: string) {
        if (communication === '0')
            return 'Original';
        else if (communication === '1')
            return 'Amended';
        else
            return 'Others';
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getDocumentType()
     * @param documentType 
     */
    getDocumentType(documentType: string): string {
        const filtered = LoanMonitoringConstants.documentTypes.filter(obj => obj.code === documentType);
        return filtered[0].value;    
    }

    
    /**
     * updateTermsAndConditions()
     * @param operation 
     */
     updateTermsAndConditions(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this.loanApplicationId,
            'selectedTandC': undefined
        };
        if (operation === 'updateT&C') {
            data.selectedTandC = this.selectedTandC;
        }
        const dialogRef = this._dialog.open(TandCUpdateDialogComponent, {
            panelClass: 'fuse-tandc-update-dialog',
            width: '900px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getTermsAndConditions(this.loanApplicationId).subscribe(data => {
                    this.dataSource.data = data;
                });
                this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this._loanMonitoringService.loanMonitor.next(data);
                });
            }
        });
    }
}
