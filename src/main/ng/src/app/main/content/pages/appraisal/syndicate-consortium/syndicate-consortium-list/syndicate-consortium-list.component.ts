import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { SyndicateConsortiumUpdateComponent } from '../syndicate-consortium-update/syndicate-consortium-update.component';

@Component({
    selector: 'fuse-syndicate-consortium-list',
    templateUrl: './syndicate-consortium-list.component.html',
    styleUrls: ['./syndicate-consortium-list.component.scss'],
    animations: fuseAnimations
})
export class SyndicateConsortiumListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'sanctionedAmount', 'currency', 'lead', 'approvalStatus', 'documentStage', 'disbursementStatus', 'disbursedAmount'
    ];

    selectedSyndicateConsortium: any;

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
            'syndicateConsortium': {},
        };
        if (operation === 'modifySyndicateConsortium') {
            data.syndicateConsortium = this.selectedSyndicateConsortium;
        }
        const dialogRef = this._dialogRef.open(SyndicateConsortiumUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this.getSyndicateConsortiums();
            }
        });
    }

    /**
     * getSyndicateConsortiums()
     */
     getSyndicateConsortiums(): void {
        this._loanAppraisalService.getSyndicateConsortiums(this._loanApplicationId).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.syndicateConsortiums);
        });
    }

    /**
     * onRowSelect()
     * @param syndicateConsortium 
     */
    onRowSelect(syndicateConsortium: any): void {
        this.selectedSyndicateConsortium = syndicateConsortium;
    }

    /**
     * getApprovalStatus()
     * @param value 
     */
    getApprovalStatus(value: string): string {
        if (value === '01') {
            return 'Application Submited';
        }
        else if (value === '02') {
            return 'In-principle Approved';
        }
        else if (value === '03') {
            return 'Sanctioned';
        }
        else {
            return 'Disbursed';
        }
    }

    /**
     * getDisbursementStatus()
     * @param value 
     */
    getDisbursementStatus(value: string): string {
        if (value === '1') {
            return 'Disbursed';
        }
        else {
            return 'Not Disbursed';
        }
    }
}
