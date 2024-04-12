import { Component } from '@angular/core';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../confirmationDialog/confirmationDialog.component';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { ExternalRatingUpdateComponent } from '../external-rating-update/external-rating-update.component';
import { log } from 'console';

@Component({
    selector: 'fuse-external-rating-list',
    templateUrl: './external-rating-list.component.html',
    styleUrls: ['./external-rating-list.component.scss'],
    animations: fuseAnimations
})
export class ExternalRatingListComponent {

    private _loanApplicationId: any;
    private _loanAppraisalId: any;

    public selectedRating: any;

    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'serialNumber', 'validityDate', 'rating', 'ratingAgency', 'creditStanding', 'creditStandingInstruction', 'creditStandingText'
    ];

    ratings: any;

    /**
     * constructor()
     * @param _dialogRef 
     * @param _loanAppraisalService 
     */
    constructor(_loanEnquiryService: LoanEnquiryService, 
                private _loanAppraisalService: LoanAppraisalService,
                private _matSnackBar: MatSnackBar,
                private _matDialog: MatDialog) { 

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;

        this._loanAppraisalService.getRatings().subscribe(response => {
            this.ratings = response._embedded.externalRatingTypes;
            this._loanAppraisalService.getExternalRatings(this._loanAppraisalId).subscribe(response => {
                this.dataSource = new MatTableDataSource(response._embedded.externalRatings);
            });
        })
        
    }

    /**
     * updateRating()
     */
    updateRating(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'selectedRating': undefined
        };
        if (operation === 'modifyRating') {
            data.selectedRating = this.selectedRating;
        }
        const dialogRef = this._matDialog.open(ExternalRatingUpdateComponent, {
            panelClass: 'fuse-external-rating-update',
            width: '950px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getExternalRatings(this._loanAppraisalId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.externalRatings);
                    this.selectedRating = undefined;
                });
            };
        });    
    }

    /**
     * onRowSelect()
     */
    onRowSelect(obj: any): void {
        this.selectedRating = obj;
    }

    /**
     * getValue()
     */
    getValue(rating: any): string {
        const obj = this.ratings.filter(r => r.code === rating);
        if (obj.length > 0)
            return obj[0].value;
        else
            return '';
    }

    /**
     * deleteRating()
     */
    deleteRating(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            this._loanAppraisalService.deleteExternalRating(this.selectedRating.id).subscribe(() => {
                this._loanAppraisalService.getExternalRatings(this._loanAppraisalId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.externalRatings);
                    this.selectedRating = undefined;
                });
            });
        });
    }
}
