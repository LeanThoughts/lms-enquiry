import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { ActivatedRoute } from '@angular/router';
import { StateModel } from 'app/main/content/model/state.model';
import { log } from 'console';

@Component({
    selector: 'fuse-application-fee-project-details',
    templateUrl: './projectDetails.component.html',
    styleUrls: ['./projectDetails.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ApplicationFeeProjectDetailsComponent implements OnInit {

    projectDetailForm: FormGroup;
    disableSubmitButton = false;
    loanApplicationId = '';

    states = StateModel.getStates();
    projectTypes = [];
    purposeOfLoans: any;
    unitOfMeasures: any;
    loanTypes: any;
    loanClasses: any;
    assistanceTypes: any;
    financingTypes: any;
    projectTypeCoreSectors: any;
    productTypes: any;

    projectDetails: any;

    enquiryCompletion: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        _enquiryService: LoanEnquiryService, private _matSnackBar: MatSnackBar, private _activatedRoute: ActivatedRoute) {

        this.loanApplicationId = _enquiryService.selectedLoanApplicationId.value;

        this.projectTypes = _activatedRoute.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
        this.states = _activatedRoute.snapshot.data.routeResolvedData[5];
        this.loanTypes = _activatedRoute.snapshot.data.routeResolvedData[6]._embedded.loanTypes;
        this.loanClasses = _activatedRoute.snapshot.data.routeResolvedData[7]._embedded.loanClasses;
        this.assistanceTypes = _activatedRoute.snapshot.data.routeResolvedData[8]._embedded.assistanceTypes;
        this.financingTypes = _activatedRoute.snapshot.data.routeResolvedData[9]._embedded.financingTypes;
        this.projectTypeCoreSectors = _activatedRoute.snapshot.data.routeResolvedData[10]._embedded.projectTypeCoreSectors;
        this.purposeOfLoans = _activatedRoute.snapshot.data.routeResolvedData[11]._embedded.purposeOfLoans;
        this.unitOfMeasures = _activatedRoute.snapshot.data.routeResolvedData[12]._embedded.unitOfMeasures;
        this.productTypes = _activatedRoute.snapshot.data.routeResolvedData[14]._embedded.products;

        this.projectDetails = _activatedRoute.snapshot.data.routeResolvedData[13];

        this.enquiryCompletion = _activatedRoute.snapshot.data.routeResolvedData[15];

        console.log('projectDetails', this.projectDetails);

        this.projectDetailForm = this._formBuilder.group({
            projectName: [''],
            promoterName: [''],
            loanPurpose: [''],
            projectCapacity: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            projectCapacityUnit: [''],
            state: [''],
            productTypeCode: [''],
            term: [''],
            enquiryCompletionDate: [''],
            loanType: [''],
            loanClass: [''],
            assistanceType: [''],
            financingType: [''],
            projectType: [''],
            projectTypeCoreSector: [''],
            purposeOfLoan: [''],
            projectCost: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            debt: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            promoterContributionEquity: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            debtEquityRatio: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            grantSubsidyAmount: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            debtEquityRatioWithGrant: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            pfsDebtAmount: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            rateOfInterest: ['', [Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            tenorYear: ['', [Validators.pattern(/^\d+$/)]],
            tenorMonths: ['', [Validators.pattern(/^\d+$/)]],
            moratoriumPeriod: ['', [Validators.pattern(/^\d+$/)]],
            moratoriumPeriodUnit: [''],
            constructionPeriod: ['', [Validators.pattern(/^\d+$/)]],
            constructionPeriodUnit: ['']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.populateFormValues();
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.projectDetailForm.valid) {
            this.disableSubmitButton = true;
            this.saveProjectDetails();
        }
    }

    /**
     * populateFormValues()
     */
    populateFormValues() {
        const formFields = {
            projectName: 'projectName',
            promoterName: 'promoterName',
            loanPurpose: 'loanPurpose',
            projectCapacity: 'projectCapacity',
            projectCapacityUnit: 'projectCapacityUnit',
            // state: 'state',
            // productTypeCode: 'productTypeCode',
            term: 'term',
            enquiryCompletionDate: 'enquiryCompletionDate',
            loanType: 'loanType',
            loanClass: 'loanClass',
            assistanceType: 'assistanceType',
            financingType: 'financingType',
            projectType: 'projectType',
            projectTypeCoreSector:   'projectCoreSector',  // 'projectTypeCoreSector',
            purposeOfLoan: 'purposeOfLoan',
            projectCost: 'projectCost',
            //   tslint:disable-next-line:comment-format
            debt: 'projectDebtAmount' , // 'debt',
            promoterContributionEquity:   'equity',  // 'promoterContributionEquity',
            debtEquityRatio: 'debtEquityRatio',
            grantSubsidyAmount: 'grantSubsidyAmount',
            debtEquityRatioWithGrant: 'debtEquityRatioWithGrant',
            pfsDebtAmount: 'pfsDebtAmount',
            // rateOfInterest: 'rateOfInterest',
            tenorYear: 'tenorYear',
            // tenorMonths: 'tenorMonths',
            moratoriumPeriod: 'moratoriumPeriod',
            moratoriumPeriodUnit: 'moratoriumPeriodUnit',
            constructionPeriod: 'constructionPeriod',
            constructionPeriodUnit: 'constructionPeriodUnit'
        };
        if (!this.projectDetails || Object.keys(this.projectDetails).length === 0) {

            const loanApplication = this._activatedRoute.snapshot.data.routeResolvedData[1].loanApplication;
            const formValues = {};
            Object.keys(formFields).forEach(key => {
                formValues[key] = loanApplication[formFields[key]];
            });
            console.log('loanApplication', loanApplication);
            this.projectDetailForm.patchValue(formValues);
            this.projectDetailForm.controls['state'].setValue(loanApplication.projectLocationState);
            this.projectDetailForm.controls['term'].setValue(loanApplication.term);
            this.projectDetailForm.controls['productTypeCode'].setValue(loanApplication.productCode);
            this.projectDetailForm.controls['rateOfInterest'].setValue(loanApplication.expectedInterestRate);
            this.projectDetailForm.controls['state'].setValue(loanApplication.projectLocationState);
            this.projectDetailForm.controls['tenorMonths'].setValue(loanApplication.tenorMonth);
        }
        else {
            const formValues = {};
            Object.keys(formFields).forEach(key => {
                formValues[key] = this.projectDetails[formFields[key]];
            });
            this.projectDetailForm.patchValue(formValues);
        }
        console.log('projectDetailForm', this.projectDetailForm.value);
    }

    /**
     * saveProjectDetails()
     */
    saveProjectDetails() {
        if (Object.keys(this.projectDetails).length === 0) {
            const projectDetails = this.projectDetailForm.value;
            const dt = new Date(projectDetails.enquiryCompletionDate);
            projectDetails.enquiryCompletionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            console.log('projectDetails', projectDetails);
            projectDetails.loanApplicationId = this.loanApplicationId;
            this._applicationFeeService.createProjectDetails(this.projectDetailForm.value).subscribe((data) => {
                this.projectDetails = data;
                this.disableSubmitButton = false;
                this._matSnackBar.open('Project details saved successfully.', 'OK', { duration: 7000 });
                this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                    this._applicationFeeService._applicationFee.next(data);
                });
            });
        }
        else {
            const projectDetails = this.projectDetails;
            Object.keys(this.projectDetailForm.value).forEach(key => {
                projectDetails[key] = this.projectDetailForm.value[key];
            });
            const dt = new Date(projectDetails.enquiryCompletionDate);
            projectDetails.enquiryCompletionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            console.log('projectDetails', projectDetails);
            this._applicationFeeService.updateProjectDetails(projectDetails).subscribe((data) => {
                this.projectDetails = data;
                this.disableSubmitButton = false;
                this._matSnackBar.open('Project details updated successfully.', 'OK', { duration: 7000 });
                this._applicationFeeService.getApplicationFee(this.loanApplicationId).subscribe(data => {
                    this._applicationFeeService._applicationFee.next(data);
                });
            });
        }
    }

    /**
     * calculateRatio()
     */
    calculateRatio(): void {
        // Calculate debt equity ratio without grant
        const debt = this.projectDetailForm.controls.debt.value;
        const equity = this.projectDetailForm.controls.promoterContributionEquity.value;
        if (equity > 0) {
            this.projectDetailForm.controls.debtEquityRatio.setValue((debt / equity).toFixed(2));
        }
        else if (equity == 0) {
            this.projectDetailForm.controls.debtEquityRatio.setValue(0);
             }

        // Calculate debt equity ratio with grant
        const grantAmount = this.projectDetailForm.get('grantSubsidyAmount').value;
        if (debt && equity && grantAmount && equity > 0) {
            const ratioWithGrant = (Number(grantAmount) + Number(debt)) / Number(equity);
            this.projectDetailForm.get('debtEquityRatioWithGrant').setValue(ratioWithGrant.toFixed(2));
        } else {
            this.projectDetailForm.get('debtEquityRatioWithGrant').setValue('');
        }
    }

}
