import { Component } from '@angular/core';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../confirmationDialog/confirmationDialog.component';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { TermLoanRiskRatingUpdateComponent } from '../term-loan-risk-rating-update/term-loan-risk-rating-update.component';

@Component({
    selector: 'fuse-term-loan-risk-rating-list',
    templateUrl: './term-loan-risk-rating-list.component.html',
    styleUrls: ['./term-loan-risk-rating-list.component.scss'],
    animations: fuseAnimations
})
export class TermLoanRiskRatingListComponent {

    private _loanApplicationId: any;
    private _loanAppraisalId: any;

    private selectedRating: any;

    dataSource: MatTableDataSource<any>;

    displayedColumns = [
        'year', 'financialRatio', 'approvalRisk', 'offTakeRisk', 'fuelRisk', 'reputationRisk', 'financingStructure', 'sponsorSupport',
                'securityPackage', 'constructionRisk', 'exposure', 'technologyRisk', 'overallRisk'
    ];

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

        this._loanAppraisalService.getTermLoanRiskRatings(this._loanAppraisalId).subscribe(response => {
            this.dataSource = new MatTableDataSource(response._embedded.termLoanRiskRatings);
        });
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
        const dialogRef = this._matDialog.open(TermLoanRiskRatingUpdateComponent, {
            panelClass: 'fuse-term-loan-risk-rating-update',
            width: '950px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getTermLoanRiskRatings(this._loanAppraisalId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.termLoanRiskRatings);
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
    getValue(risk: any): string {
        if (risk === '0') {
            return 'Low';
        }
        else if (risk === '1') {
            return 'Medium';
        }
        else {
            return 'High';
        }
    }

    /**
     * deleteRating()
     */
    deleteRating(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            this._loanAppraisalService.deleteTermLoanRiskRating(this.selectedRating.id).subscribe(() => {
                this._loanAppraisalService.getTermLoanRiskRatings(this._loanAppraisalId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.termLoanRiskRatings);
                    this.selectedRating = undefined;
                });
            });
        });
    }
}
