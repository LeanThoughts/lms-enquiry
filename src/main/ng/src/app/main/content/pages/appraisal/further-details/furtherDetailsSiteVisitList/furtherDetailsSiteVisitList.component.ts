import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MatSnackBar, MatSort, MatTableDataSource, } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { ConfirmationDialogComponent } from '../../confirmationDialog/confirmationDialog.component';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { FurtherDetailsSiteVisitUpdateComponent } from '../furtherDetailsSiteVisitUpdate/furtherDetailsSiteVisitUpdate.component';

@Component({
    selector: 'fuse-further-details',
    templateUrl: './furtherDetailsSiteVisitList.component.html',
    styleUrls: ['./furtherDetailsSiteVisitList.component.scss'],
    animations: fuseAnimations
})
export class FurtherDetailsSiteVisitListComponent {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    _furtherDetail: any;
    _furtherDetailForm: FormGroup;

    displayedColumns = [
        'serialNumber', 'siteVisitType', 'dateOfSiteVisit', 'documentTitle', 'download'
    ];

    selectedSiteVisit: any;

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;
    
    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService,
                _activatedRoute: ActivatedRoute,
                private _loanAppraisalService: LoanAppraisalService,
                _formBuilder: FormBuilder,
                private _matSnackBar: MatSnackBar,
                private _matDialog: MatDialog) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        this._furtherDetail = _activatedRoute.snapshot.data.routeResolvedData[5];

        console.log('route data', _activatedRoute.snapshot.data);
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[6]._embedded.siteVisits);
        this.dataSource.sort = this.sort;

        this._furtherDetailForm = _formBuilder.group({
            furtherDetails: [ this._furtherDetail.furtherDetails || '' ],
            date: [ this._furtherDetail.date || undefined ]
        });
    }

    /**
     * saveFurtherDetails()
     */
    saveFurtherDetails(): void {
        if (this._furtherDetailForm.valid) {

            var formValues = this._furtherDetailForm.value;

            var dt = new Date(formValues.date);
            formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._furtherDetail) === JSON.stringify({})) {
                formValues.loanApplicationId = this._loanApplicationId;
                this._loanAppraisalService.updateFurtherDetail(formValues).subscribe(() => {
                    this._matSnackBar.open('Further details added successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                this._furtherDetail.loanApplicationId = this._loanApplicationId;
                this._furtherDetail.furtherDetails = formValues.furtherDetails;
                this._furtherDetail.date = formValues.date;
                this._loanAppraisalService.updateFurtherDetail(this._furtherDetail).subscribe(() => {
                    this._matSnackBar.open('Proposal details updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * onSelect()
     */
    onSelect(selectedSiteVisit: any): void {
        this.selectedSiteVisit = selectedSiteVisit;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * updatSiteVisit()
     */
    updateSiteVisit(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'selectedSiteVisit': undefined
        };
        if (operation === 'updateSiteVisit') {
            data.selectedSiteVisit = this.selectedSiteVisit;
        }
        const dialogRef = this._matDialog.open(FurtherDetailsSiteVisitUpdateComponent, {
            panelClass: 'fuse-further-details-site-visit-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSiteVisits(this._loanApplicationId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.siteVisits);
                });
            }
        });    
    }

    /**
     * openDeleteDialog()
     */
    openDeleteDialog(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.response) {
                this._loanAppraisalService.deleteSiteVisit(this.selectedSiteVisit.id).subscribe(() => {
                    this._loanAppraisalService.getSiteVisits(this._loanApplicationId).subscribe(response => {
                        this.dataSource = new MatTableDataSource(response._embedded.siteVisits);
                    });
                });
            }
        });
    }
}
