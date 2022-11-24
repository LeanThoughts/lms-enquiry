import { Component, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from 'app/main/content/pages/enquiry/enquiryApplication.service';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { CreditRatingUpdateComponent } from '../creditRatingUpdate/creditRatingUpdate.component';

@Component({
    selector: 'fuse-project-proposal-other-detail-update',
    templateUrl: './projectProposalOtherDetailUpdate.component.html',
    styleUrls: ['./projectProposalOtherDetailUpdate.component.scss'],
    animations: fuseAnimations
})
export class ProjectProposalOtherDetailUpdateComponent {

    _otherDetailForm: FormGroup;
    _otherDetail: any = {};

    _projectProposal: any;

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getProjectProposalOtherDetails(this._projectProposal.id).subscribe(response => {
                this._otherDetail = response;
                this.initializeFormValues();
            });
            // this._enquiryActionService.getCreditRatings(this._projectProposal.id).subscribe(response => {
            //     this.dataSource = new MatTableDataSource(response._embedded.creditRatings);
            // });
        }
    }

    _selectedCreditRating: any;
    dataSource: MatTableDataSource<any>;
    displayedColumns = [
        'creditRating', 'creditRatingAgency', 'creditStandingInstruction', 'creditStandingText'
    ];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                private _dialogRef: MatDialog, 
                private _matSnackBar: MatSnackBar,
                public _enquiryActionService: EnquiryActionService,
                public _enquiryApplicationService: LoanEnquiryService) { 

        this._otherDetailForm = this._formBuilder.group({
            sourceAndCashFlow: new FormControl(''),
            optimumDateOfLoan: new FormControl(''),
            consolidatedGroupLeverage: new FormControl(''),
            totalDebtTNW: new FormControl(''),
            tolTNW: new FormControl(''),
            totalDebtTNWStage: new FormControl(''),
            tolTNWPercentage: new FormControl(''),
            delayInDebtServicing: new FormControl('')
        });
        
        this.initializeFormValues();
    }

    /**
     * submit()
     */
    submit(): void {        
        console.log(this._otherDetailForm.value);
        if (this._otherDetailForm.valid) {
            var formValues = this._otherDetailForm.value;

            var dt = new Date(formValues.optimumDateOfLoan);
            formValues.date = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (JSON.stringify(this._otherDetail) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._projectProposal.id;
                this._enquiryActionService.createProjectProposalOtherDetails(formValues).subscribe(response => {
                    this._otherDetail = response;
                    this._matSnackBar.open('Other details created successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                console.log('updating existing record');
                this._otherDetail.sourceAndCashFlow = formValues.sourceAndCashFlow;
                this._otherDetail.optimumDateOfLoan = formValues.optimumDateOfLoan;
                this._otherDetail.consolidatedGroupLeverage = formValues.consolidatedGroupLeverage;
                this._otherDetail.totalDebtTNW = formValues.totalDebtTNW;
                this._otherDetail.tolTNW = formValues.tolTNW;
                this._otherDetail.totalDebtTNWStage = formValues.totalDebtTNWStage;
                this._otherDetail.tolTNWPercentage = formValues.tolTNWPercentage;
                this._otherDetail.delayInDebtServicing = formValues.delayInDebtServicing;
                this._enquiryActionService.updateProjectProposalOtherDetails(this._otherDetail).subscribe(response => {
                    this._otherDetail = response;
                    this._matSnackBar.open('Other details updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * initializeFormValues()
     */
    initializeFormValues(): void {
        this._otherDetailForm.setValue({
            'sourceAndCashFlow': this._otherDetail.sourceAndCashFlow || '',
            'optimumDateOfLoan': this._otherDetail.optimumDateOfLoan || '',
            'consolidatedGroupLeverage': this._otherDetail.consolidatedGroupLeverage || '',
            'totalDebtTNW': this._otherDetail.totalDebtTNW || '',
            'tolTNW': this._otherDetail.tolTNW || '',
            'totalDebtTNWStage': this._otherDetail.totalDebtTNWStage || '',
            'tolTNWPercentage': this._otherDetail.tolTNWPercentage || '',
            'delayInDebtServicing': this._otherDetail.delayInDebtServicing || ''
        });
    }

    /**
     * onRowSelect()
     */
    onRowSelect(creditRating: any): void {
        this._selectedCreditRating = creditRating;
    }

    /**
     * openUpdateDialog()
     */
    openUpdateDialog(operation: string) {
        // Open the dialog.
        var data = {
            'projectProposalId': this._projectProposal.id,
            'otherDetail': {}
        };
        if (operation === 'modifyCreditRating') {
            data.otherDetail = this._selectedCreditRating;
        }
        const dialogRef = this._dialogRef.open(CreditRatingUpdateComponent, {
            data: data,
            width: '750px'
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((data) => {
            if (data.refresh === true) {
                this._enquiryActionService.getCreditRatings(this._projectProposal.id).subscribe(response => {
                    this.dataSource.data = response._embedded.creditRatings;
                });
            }
        });    
    }
}