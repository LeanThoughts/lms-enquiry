import { Component, OnDestroy } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../../enquiryAction.service';
import { ProjectProposalUpdateComponent } from '../projectProposalUpdate/projectProposalUpdate.component';
import { Subscription } from 'rxjs';

@Component({
    selector: 'fuse-project-proposal',
    templateUrl: './projectProposalList.component.html',
    styleUrls: ['./projectProposalList.component.scss'],
    animations: fuseAnimations
})
export class ProjectProposalListComponent implements OnDestroy {

    _loanApplicationId: string;
    _enquiryActionId: string;

    _selectedProjectProposal: any;

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'serialNumber', 'proposalFormSharingDate', 'proposalStatus'
    ];

    subscriptions = new Subscription();
    /**
     * constructor()
     */
    constructor(private _dialogRef: MatDialog, 
                _loanEnquiryService: LoanEnquiryService,
                _activatedRoute: ActivatedRoute,
                private _enquiryActionService: EnquiryActionService) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._enquiryActionId = _enquiryActionService._enquiryAction.value.id;

        this.subscriptions.add(
            _enquiryActionService._enquiryAction.subscribe(data => {
                this._enquiryActionId = data.id;
            })
        );

        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[5]._embedded.projectProposals);
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    /**
     * onRowSelect()
     */
    onRowSelect(projectProposal: any): void {
        this._selectedProjectProposal = projectProposal;
    }

    /**
     * openUpdateDialog()
     */
     openUpdateDialog(operation: string) {
        // Open the dialog.
        var data = {
            'loanApplicationId': this._loanApplicationId,
            'enquiryActionId': this._enquiryActionId,
            'projectProposal': {}
        };
        if (operation === 'modifyProjectProposal') {
            data.projectProposal = this._selectedProjectProposal;
        }
        const dialogRef = this._dialogRef.open(ProjectProposalUpdateComponent, {
            width: '90%',
            height: '85%',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(() => {
            this._enquiryActionService.getProjectProposals(this._enquiryActionId).subscribe(response => {
                this.dataSource.data = response._embedded.projectProposals;
            });
        });    
    }

    /**
     * generateTeaser()
     */
    generateTeaser(): void {       
        (window as any).open('enquiry/api/teaser/excel/generate?projectProposalId=' + this._selectedProjectProposal.id, '_blank');
    }
}
