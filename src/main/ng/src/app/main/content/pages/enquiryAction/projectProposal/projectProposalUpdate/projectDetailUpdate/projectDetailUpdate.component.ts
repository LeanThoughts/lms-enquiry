import { Component, Input, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { StateModel } from 'app/main/content/model/state.model';
import { LoanEnquiryService } from 'app/main/content/pages/enquiry/enquiryApplication.service';
import { productTypes } from '../../../enquiryAction.constants';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { CreditRatingUpdateComponent } from '../creditRatingUpdate/creditRatingUpdate.component';

@Component({
    selector: 'fuse-project-detail-update',
    templateUrl: './projectDetailUpdate.component.html',
    styleUrls: ['./projectDetailUpdate.component.scss'],
    animations: fuseAnimations
})
export class ProjectDetailUpdateComponent {

    _projectDetailForm: FormGroup;
    _projectDetail: any = {};

    _projectProposal: any;

    unitOfMeasures = [];
    states = StateModel.getStates();
    productTypes = [];
    loanClasses = [];
    assistanceTypes = [];
    financingTypes = [];

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getProjectDetail(this._projectProposal.id).subscribe(response => {
                this._projectDetail = response;
                this.initializeFormValues();
            });
            this._enquiryActionService.getCreditRatings(this._projectProposal.id).subscribe(response => {
                this.dataSource = new MatTableDataSource(response._embedded.creditRatings);
            });
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

        // Initialize dropdown values
        this._enquiryApplicationService.getUnitOfMeasures().subscribe(response => {
            this.unitOfMeasures = response._embedded.unitOfMeasures;
        });
        this._enquiryApplicationService.getLoanClasses().subscribe(response => {
            this.loanClasses = response._embedded.loanClasses;
        });
        this._enquiryApplicationService.getAssistanceTypes().subscribe(response => {
            this.assistanceTypes = response._embedded.assistanceTypes;
        });
        this._enquiryApplicationService.getFinancingTypes().subscribe(response => {
            this.financingTypes = response._embedded.financingTypes;
        });
        this.productTypes = productTypes;

        this._projectDetailForm = this._formBuilder.group({
            projectName: new FormControl(''),
            borrowerName: new FormControl(''),
            promoterName: new FormControl(''),
            loanPurpose: new FormControl(''),
            projectCapacity: new FormControl(''),
            projectCapacityUnit: new FormControl(''),
            state: new FormControl(''),
            district: new FormControl(''),
            productType: new FormControl(''),
            loanClass: new FormControl(''),
            assistanceType: new FormControl(''),
            financingType: new FormControl(''),
            endUseOfFunds: new FormControl(''),
            roi: new FormControl(''),
            fees: new FormControl(''),
            tenorYear: new FormControl(''),
            tenorMonths: new FormControl(''),
            moratoriumPeriod: new FormControl(''),
            moratoriumPeriodUnit: new FormControl(''),
            constructionPeriod: new FormControl(''),
            constructionPeriodUnit: new FormControl(''),
        });
        
        this.initializeFormValues();
    }

    /**
     * submit()
     */
    submit(): void {        
        console.log(this._projectDetailForm.value);
        if (this._projectDetailForm.valid) {
            var formValues = this._projectDetailForm.value;
            if (JSON.stringify(this._projectDetail) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._projectProposal.id;
                this._enquiryActionService.createProjectDetail(formValues).subscribe(response => {
                    this._projectDetail = response;
                    this._matSnackBar.open('Project details created successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                console.log('updating existing record');
                this._projectDetail.projectName = formValues.projectName;
                this._projectDetail.borrowerName = formValues.borrowerName;
                this._projectDetail.promoterName = formValues.promoterName;
                this._projectDetail.loanPurpose = formValues.loanPurpose;
                this._projectDetail.projectCapacity = formValues.projectCapacity;
                this._projectDetail.projectCapacityUnit = formValues.projectCapacityUnit;
                this._projectDetail.state = formValues.state;
                this._projectDetail.district = formValues.district;
                this._projectDetail.productType = formValues.productType;
                this._projectDetail.loanClass = formValues.loanClass;
                this._projectDetail.assistanceType = formValues.assistanceType;
                this._projectDetail.financingType = formValues.financingType;
                this._projectDetail.endUseOfFunds = formValues.endUseOfFunds;
                this._projectDetail.roi = formValues.roi;
                this._projectDetail.fees = formValues.fees;
                this._projectDetail.tenorYear = formValues.tenorYear;
                this._projectDetail.tenorMonths = formValues.tenorMonths;
                this._projectDetail.moratoriumPeriod = formValues.moratoriumPeriod;
                this._projectDetail.moratoriumPeriodUnit = formValues.moratoriumPeriodUnit;
                this._projectDetail.constructionPeriod = formValues.constructionPeriod;
                this._projectDetail.constructionPeriodUnit = formValues.constructionPeriodUnit;
                this._enquiryActionService.updateProjectDetail(this._projectDetail).subscribe(response => {
                    this._projectDetail = response;
                    this._matSnackBar.open('Project details updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * initializeFormValues()
     */
    initializeFormValues(): void {
        this._projectDetailForm.setValue({
            'projectName': this._projectDetail.projectName || '',
            'borrowerName': this._projectDetail.borrowerName || '',
            'promoterName': this._projectDetail.promoterName || '',
            'loanPurpose': this._projectDetail.loanPurpose || '',
            'projectCapacity': this._projectDetail.projectCapacity || '',
            'projectCapacityUnit': this._projectDetail.projectCapacityUnit || '',
            'state': this._projectDetail.state || '',
            'district': this._projectDetail.district || '',
            'productType': this._projectDetail.productType || '',
            'loanClass': this._projectDetail.loanClass || '',
            'assistanceType': this._projectDetail.assistanceType || '',
            'financingType': this._projectDetail.financingType || '',
            'endUseOfFunds': this._projectDetail.endUseOfFunds || '',
            'roi': this._projectDetail.roi || '',
            'fees': this._projectDetail.fees || '',
            'tenorYear': this._projectDetail.tenorYear || '',
            'tenorMonths': this._projectDetail.ternorMonths || '',
            'moratoriumPeriod': this._projectDetail.moratoriumPeriod || '',
            'moratoriumPeriodUnit': this._projectDetail.moratoriumPeriodUnit || '',
            'constructionPeriod': this._projectDetail.constructionPeriod || '',
            'constructionPeriodUnit': this._projectDetail.constructionPeriodUnit || ''
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
            'creditRating': {}
        };
        if (operation === 'modifyCreditRating') {
            data.creditRating = this._selectedCreditRating;
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