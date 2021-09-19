import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
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

    banks: any;

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'bankName', 'sanctionedAmount', 'currency', 'lead', 'approvalStatus', 'documentStage', 'disbursementStatus', 'disbursedAmount'
    ];

    selectedSyndicateConsortium: any;
    subscriptions = new Subscription();

    _loanApplicationId: string;

    /**
     * constructor()
     */
    constructor(private _dialogRef: MatDialog, 
                private _loanEnquiryService: LoanEnquiryService,
                private _loanAppraisalService: LoanAppraisalService,
                _activatedRoute: ActivatedRoute) { 

        // Fetch list of banks and syndicate consortiums ...
        this.subscriptions.add(_activatedRoute.data.subscribe(data => {
            this.banks = data.routeResolvedData[2];
            this.dataSource = new MatTableDataSource(data.routeResolvedData[3]._embedded.syndicateConsortiums);
        }));
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
    }

    /**
     * openUpdateDialog()
     */
    openUpdateDialog(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanEnquiryService.selectedLoanApplicationId.value,
            'syndicateConsortium': {},
            'banks': this.banks
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
                this.selectedSyndicateConsortium = undefined;
            }
        });
    }

    /**
     * getSyndicateConsortiums()
     */
     getSyndicateConsortiums(): void {
        this._loanAppraisalService.getSyndicateConsortiums(this._loanAppraisalService._loanAppraisal.id).subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.syndicateConsortiums);
        });
    }

    /**
     * onRowSelect()
     */
    onRowSelect(syndicateConsortium: any): void {
        this.selectedSyndicateConsortium = syndicateConsortium;
    }

    /**
     * getApprovalStatus()
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
