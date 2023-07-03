import { Component, Input } from '@angular/core';
import { MatDialog, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { CollateralDetailUpdateComponent } from '../collateralDetailUpdate/collateralDetailUpdate.component';
import { collateralTypes } from '../../../enquiryAction.constants';

@Component({
    selector: 'fuse-collateral-detail-list',
    templateUrl: './collateralDetailList.component.html',
    styleUrls: ['./collateralDetailList.component.scss'],
    animations: fuseAnimations
})
export class CollateralDetailListComponent {

    _selectedCollateralDetail: any;

    _projectProposal: any;

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getCollateralDetails(this._projectProposal.id).subscribe(response => {
                this.dataSource = new MatTableDataSource(response._embedded.collateralDetails);
            });
        }
    }

    dataSource: MatTableDataSource<any>;
    
    displayedColumns = [
        'collateralType', 'details'
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
    onRowSelect(collateralDetail: any): void {
        this._selectedCollateralDetail = collateralDetail;
    }

    /**
     * openUpdateDialog()
     */
    openUpdateDialog(operation: string) {
        // Open the dialog.
        var data = {
            'projectProposalId': this._projectProposal.id,
            'collateralDetail': {}
        };
        if (operation === 'modifyCollateralDetail') {
            data.collateralDetail = this._selectedCollateralDetail;
        }
        const dialogRef = this._dialogRef.open(CollateralDetailUpdateComponent, {
            data: data,
            width: '750px'
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((data) => {
            if (data.refresh === true) {
                this._enquiryActionService.getCollateralDetails(this._projectProposal.id).subscribe(response => {
                    this.dataSource.data = response._embedded.collateralDetails;
                });
            }
        });    
    }

    getCollateralTypeDescription(code: string): string {
        let description = ''
        collateralTypes.forEach(obj => {
            if (obj.Code === code) {
                description = obj.Value;
            }
        })
        return description;
    }
}
