import { Component, Input } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { CollateralDetailUpdateComponent } from '../collateralDetailUpdate/collateralDetailUpdate.component';
import { PromoterBorrowerFinancialUpdateComponent } from '../promoterBorrowerFinancialUpdate/promoterBorrowerFinancialUpdate.component';

@Component({
    selector: 'fuse-promoter-borrower-financial-list',
    templateUrl: './promoterBorrowerFinancialList.component.html',
    styleUrls: ['./promoterBorrowerFinancialList.component.scss'],
    animations: fuseAnimations
})
export class PromoterBorrowerFinancialListComponent {

    _selectedFinancial: any;

    _projectProposal: any;

    _financials: any;

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getFinancials(this._projectProposal.id).subscribe(response => {
                this._financials = response._embedded.promoterBorrowerFinancials;
                this.dataSource = new MatTableDataSource(response._embedded.promoterBorrowerFinancials);
            });
        }
    }

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'fiscalPeriod'
    ];

    /**
     * constructor()
     */
    constructor(private _dialogRef: MatDialog, 
                private _enquiryActionService: EnquiryActionService) {
    }

    /**
     * onRowSelect()
     */
    onRowSelect(financial: any): void {
        this._selectedFinancial = financial;
    }

    /**
     * openUpdateDialog()
     */
    openUpdateDialog(operation: string) {
        // Open the dialog.
        var data = {
            'projectProposalId': this._projectProposal.id,
            'financial': {},
            'financials': this._financials
        };
        if (operation === 'modifyFinancial') {
            data.financial = this._selectedFinancial;
        }
        const dialogRef = this._dialogRef.open(PromoterBorrowerFinancialUpdateComponent, {
            data: data,
            width: '90%',
            height: '85%'
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((data) => {
            if (data.refresh === true) {
                this._enquiryActionService.getFinancials(this._projectProposal.id).subscribe(response => {
                    this.dataSource.data = response._embedded.promoterBorrowerFinancials;
                    this._financials = response._embedded.promoterBorrowerFinancials;
                });
            }
        });    
    }
}
