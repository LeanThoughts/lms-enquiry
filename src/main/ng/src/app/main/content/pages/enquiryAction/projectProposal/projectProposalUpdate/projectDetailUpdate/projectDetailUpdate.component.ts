import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatTableDataSource } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { StateModel } from 'app/main/content/model/state.model';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanEnquiryService } from 'app/main/content/pages/enquiry/enquiryApplication.service';
import { productTypes } from '../../../enquiryAction.constants';
import { EnquiryActionService } from '../../../enquiryAction.service';
import { CreditRatingUpdateComponent } from '../creditRatingUpdate/creditRatingUpdate.component';
import { ConfirmationDialogComponent } from 'app/main/content/pages/appraisal/confirmationDialog/confirmationDialog.component';
import { Subscription } from 'rxjs';

@Component({
    selector: 'fuse-project-detail-update',
    templateUrl: './projectDetailUpdate.component.html',
    styleUrls: ['./projectDetailUpdate.component.scss'],
    animations: fuseAnimations
})
export class ProjectDetailUpdateComponent implements OnInit, OnDestroy {

    _projectDetailForm: FormGroup;
    _projectDetail: any = {};

    _projectProposal: any;

    unitOfMeasures = [];
    states = StateModel.getStates();
    loanClasses = [];
    assistanceTypes = [];
    financingTypes = [];

    projectTypes = [];
    loanTypes = [];
    projectTypeCoreSectors = [];
    purposeOfLoans = [];

    today = new Date(); // Today's date

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        // if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
    }

    _selectedCreditRating: any;
    dataSource: MatTableDataSource<any>;
    displayedColumns = [
        'creditRating', 'creditRatingAgency', 'creditStandingInstruction', 'creditStandingText'
    ];

    subscriptions = new Subscription();

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                private _dialogRef: MatDialog,
                private _matSnackBar: MatSnackBar,
                public _enquiryActionService: EnquiryActionService,
                public _enquiryApplicationService: LoanEnquiryService, _loanEnquiryService: LoanEnquiryService) {

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

        this._enquiryApplicationService.getLoanTypes().subscribe(response => {
            this.loanTypes = response._embedded.loanTypes;
        });
        this._enquiryApplicationService.getProjectTypeCoreSectors().subscribe(response => {
            this.projectTypeCoreSectors = response._embedded.projectTypeCoreSectors;
        });
        this._enquiryApplicationService.getPurposeOfLoans().subscribe(response => {
            this.purposeOfLoans = response._embedded.purposeOfLoans;
        });
        this._enquiryApplicationService.getProjectTypes().subscribe(response => {
            this.projectTypes = response._embedded.projectTypes;
        });

        console.log('this._enquiryActionService._loanApplication', this._enquiryActionService._loanApplication);
        this._projectDetailForm = this._formBuilder.group({
            projectName: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectName),
            borrowerName: new FormControl(this.getBorrowerName()),
            promoterName: new FormControl(this._enquiryActionService._loanApplication.loanApplication.groupCompany),
            loanPurpose: new FormControl(this._enquiryActionService._loanApplication.loanApplication.loanPurpose),
            projectCapacity: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectCapacity,
                [Validators.pattern(MonitoringRegEx.sevenCommaTwo)]),
            // projectCapacityUnit: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectCapacityUnit),
            projectCapacityUnit: new FormControl(''),
            state: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectLocationState),
            district: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectDistrict),
            loanType: new FormControl(this._enquiryActionService._loanApplication.loanApplication.loanType),
            loanClass: new FormControl(this._enquiryActionService._loanApplication.loanApplication.loanClass),
            assistanceType: new FormControl(this._enquiryActionService._loanApplication.loanApplication.assistanceType),
            financingType: new FormControl(this._enquiryActionService._loanApplication.loanApplication.financingType),
            endUseOfFunds: new FormControl(this._enquiryActionService._loanApplication.loanApplication.endUseOfFunds),
            roi: new FormControl(Number(this._enquiryActionService._loanApplication.loanApplication.expectedInterestRate).toFixed(2), 
                [Validators.pattern(MonitoringRegEx.holdingPercentage)]),
            fees: new FormControl(this._enquiryActionService._loanApplication.loanApplication.fees,
                [Validators.pattern(MonitoringRegEx.fifteenCommaTwo)]),
            tenorYear: new FormControl(this._enquiryActionService._loanApplication.loanApplication.tenorYear,
                [Validators.pattern(MonitoringRegEx.digitsOnly)]),
            tenorMonths: new FormControl(this._enquiryActionService._loanApplication.loanApplication.tenorMonth,
                [Validators.pattern(MonitoringRegEx.digitsOnly)]),
            moratoriumPeriod: new FormControl(this._enquiryActionService._loanApplication.loanApplication.moratoriumPeriod,
                [Validators.pattern(MonitoringRegEx.digitsOnly)]),
            moratoriumPeriodUnit: new FormControl(this._enquiryActionService._loanApplication.loanApplication.moratoriumPeriodUnit),
            constructionPeriod: new FormControl(this._enquiryActionService._loanApplication.loanApplication.constructionPeriod,
                [Validators.pattern(MonitoringRegEx.digitsOnly)]),
            constructionPeriodUnit: new FormControl(this._enquiryActionService._loanApplication.loanApplication.constructionPeriodUnit),
            status: new FormControl(null),

            projectTypeCoreSector: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectTypeCoreSector),
            purposeOfLoan: new FormControl(this._enquiryActionService._loanApplication.loanApplication.purposeOfLoan),
            projectType: new FormControl(this._enquiryActionService._loanApplication.loanApplication.projectType),
            loanEnquiryDate: new FormControl(this._enquiryActionService._loanApplication.loanApplication.loanEnquiryDate),
        });

        // console.log('this._projectDetail', this._projectDetail);
        // console.log('JSON.stringify(this._projectDetail) !== JSON.stringify({})', JSON.stringify(this._projectDetail) !== JSON.stringify({}));
        // if (this._projectDetail.id) // update mode, initialize form values ...
        //     this.initializeFormValues();
    }

    ngOnInit(): void {
        if (this._projectProposal !== undefined && this._projectProposal.id) {
            this._projectDetailForm.controls['status'].setValue(this._projectProposal.proposalStatus);
            this.subscriptions.add(this._enquiryActionService.getProjectDetail(this._projectProposal.id).subscribe(projectDetail => {
                console.log('got project detail', projectDetail);
                this._projectDetail = projectDetail;
                this.initializeFormValues();
            }));
            this.subscriptions.add(this._enquiryActionService.getCreditRatings(this._projectProposal.id).subscribe(creditRatings => {
                console.log('got credit ratings', creditRatings);
                this.dataSource = new MatTableDataSource(creditRatings._embedded.creditRatings);
            }));
        }
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    /**
     * getBorrowerName()
     */
    getBorrowerName(): string {
        let name = this._enquiryActionService._loanApplication.partner.partyName1 + ' ';
        if (this._enquiryActionService._loanApplication.partner.partyName2) {
            name += this._enquiryActionService._loanApplication.partner.partyName2;
        }
        console.log('borrower name is', name);
        return name.trim();
    }

    /**
     * submit()
     */
    submit(): void {
        console.log(this._projectDetailForm.value);
        if (this._projectDetailForm.valid) {
            var formValues = this._projectDetailForm.value;
            var dt = new Date(formValues.loanEnquiryDate);
            formValues.loanEnquiryDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            
            // formValues.roi = formValues.roi !== '' ? formValues.roi * 100 : '';
            console.log('Object.keys(this._projectDetail).length', Object.keys(this._projectDetail).length);
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
                this._projectDetail.loanType = formValues.loanType;
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
                this._projectDetail.status = formValues.status;
                this._projectDetail.projectTypeCoreSector = formValues.projectTypeCoreSector;
                this._projectDetail.purposeOfLoan = formValues.purposeOfLoan;
                this._projectDetail.projectType = formValues.projectType;
                this._projectDetail.loanEnquiryDate = formValues.loanEnquiryDate;
                this._enquiryActionService.updateProjectDetail(this._projectDetail).subscribe(response => {
                    this._projectDetail = response;
                    this._matSnackBar.open('Project details updated successfully.', 'OK', { duration: 7000 });
                });
            }
            // Set _projectDetailForm.dirty to false
            this._projectDetailForm.markAsPristine();
        }
        else {
            console.log('form is invalid');
        }
    }

    /**
     * initializeFormValues()
     */
    initializeFormValues(): void {
        console.log('Initializing form values');
        console.log('this._projectDetail', this._projectDetail);
        this._projectDetailForm.controls['projectName'].setValue(this._projectDetail.projectName);
        this._projectDetailForm.controls['borrowerName'].setValue(this._projectDetail.borrowerName);
        this._projectDetailForm.controls['promoterName'].setValue(this._projectDetail.promoterName);
        this._projectDetailForm.controls['loanPurpose'].setValue(this._projectDetail.loanPurpose);
        this._projectDetailForm.controls['projectCapacity'].setValue(this._projectDetail.projectCapacity);
        this._projectDetailForm.controls['projectCapacityUnit'].setValue(this._projectDetail.projectCapacityUnit);
        this._projectDetailForm.controls['state'].setValue(this._projectDetail.state);
        this._projectDetailForm.controls['district'].setValue(this._projectDetail.district);
        this._projectDetailForm.controls['loanType'].setValue(this._projectDetail.loanType);
        this._projectDetailForm.controls['loanClass'].setValue(this._projectDetail.loanClass);
        this._projectDetailForm.controls['assistanceType'].setValue(this._projectDetail.assistanceType);
        this._projectDetailForm.controls['financingType'].setValue(this._projectDetail.financingType);
        this._projectDetailForm.controls['endUseOfFunds'].setValue(this._projectDetail.endUseOfFunds);
        this._projectDetailForm.controls['roi'].setValue(Number(this._projectDetail.roi).toFixed(2));
        this._projectDetailForm.controls['fees'].setValue(this._projectDetail.fees);
        this._projectDetailForm.controls['tenorYear'].setValue(this._projectDetail.tenorYear);
        this._projectDetailForm.controls['tenorMonths'].setValue(this._projectDetail.tenorMonths);
        this._projectDetailForm.controls['moratoriumPeriod'].setValue(this._projectDetail.moratoriumPeriod);
        this._projectDetailForm.controls['moratoriumPeriodUnit'].setValue(this._projectDetail.moratoriumPeriodUnit);
        this._projectDetailForm.controls['constructionPeriod'].setValue(this._projectDetail.constructionPeriod);
        this._projectDetailForm.controls['constructionPeriodUnit'].setValue(this._projectDetail.constructionPeriodUnit);
        this._projectDetailForm.controls['status'].setValue(this._projectDetail.status);
        this._projectDetailForm.controls['projectTypeCoreSector'].setValue(this._projectDetail.projectTypeCoreSector);
        this._projectDetailForm.controls['purposeOfLoan'].setValue(this._projectDetail.purposeOfLoan);
        this._projectDetailForm.controls['projectType'].setValue(this._projectDetail.projectType);
        this._projectDetailForm.controls['loanEnquiryDate'].setValue(this._projectDetail.loanEnquiryDate);
        // Set _projectDetailForm.dirty to false
        this._projectDetailForm.markAsPristine();
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

    /**
     * delete()
     */
    delete(): void {
        const dialogRef = this._dialogRef.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result.response) {
                this._enquiryActionService.deleteCreditRating(this._selectedCreditRating).subscribe(() => {
                    this._enquiryActionService.getCreditRatings(this._projectProposal.id).subscribe(response => {
                        this.dataSource.data = response._embedded.creditRatings;
                    });
                    this._selectedCreditRating = undefined;
                });
            }
        });
    }
}
