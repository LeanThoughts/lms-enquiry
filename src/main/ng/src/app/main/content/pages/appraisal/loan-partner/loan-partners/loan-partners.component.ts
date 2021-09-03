import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { LoanPartnerUpdateComponent } from '../loan-partner-update/loan-partner-update.component';

@Component({
    selector: 'fuse-loan-partners',
    templateUrl: './loan-partners.component.html',
    styleUrls: ['./loan-partners.component.scss'],
    animations: fuseAnimations
})
export class LoanPartnersComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'businessPartnerId', 'businessPartnerName', 'roleType','startDate'
    ];

    selectedLoanOfficer: any;

    _loanApplicationId: string;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(private _dialogRef: MatDialog,
                _loanEnquiryService: LoanEnquiryService,
                private _loanAppraisalService: LoanAppraisalService) { 

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this.dataSource = new MatTableDataSource([]);
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
    }

    /**
     * openUpdateDialog()
     * @param operation 
     */
    openUpdateDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'loanOfficer': {},
        };
        if (operation === 'modifyOfficer') {
            data.loanOfficer = this.selectedLoanOfficer;
        }
        const dialogRef = this._dialogRef.open(LoanPartnerUpdateComponent, {
            panelClass: 'loan-partner-update',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.getLoanOfficers();
            }
        });       
    }

    /**
     * getLoanOfficers()
     */
    getLoanOfficers(): void {
        this._loanAppraisalService.getLoanOfficers(this._loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.loanPartners);
        });
    }

    /**
     * onRowSelect()
     * @param loanOfficer 
     */
    onRowSelect(loanOfficer: any): void {
        this.selectedLoanOfficer = loanOfficer;
    }
}
