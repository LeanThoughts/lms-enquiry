import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { MatDialog, MatSnackBar } from '@angular/material';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { AppService } from 'app/app.service';
import { RiskAssessmentService } from './riskAssessment.service';
import { Location } from '@angular/common';

@Component({
    selector: 'fuse-riskAssessment',
    templateUrl: './riskAssessment.component.html',
    styleUrls: ['./riskAssessment.component.scss'],
    animations: fuseAnimations
})
export class RiskAssessmentComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;

    selectedEnquiry: any;
    selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;
    expandPanel2 = false;

    riskAssessment: any;
    loanContractId: any;
    functionalStatus: string;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _service 
     * @param _router 
     * @param _dialogRef 
     */
    constructor(private _formBuilder: FormBuilder, 
                public _loanEnquiryService: LoanEnquiryService, 
                public _activatedRoute: ActivatedRoute, 
                private _dialogRef: MatDialog,
                public _appService: AppService,
                public _riskAssessmentService: RiskAssessmentService,
                private _matSnackBar: MatSnackBar,
                private _location: Location) {

        this.riskAssessment = _riskAssessmentService._riskAssessment;
        console.log('riskAssessment in riskAssesment component constructor is', this.riskAssessment);

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
            console.log('this.selectedEnquiry', this.selectedEnquiry);
            if (this.selectedEnquiry.loanContractId === null)
                this.loanContractId = this.selectedEnquiry.enquiryNumber;
            else
                this.loanContractId = this.selectedEnquiry.loanContractId;
            this.functionalStatus = _loanEnquiryService.getFunctionalStatusDescription(this.selectedEnquiry.functionalStatus);

        }));          
        
        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                this.loanApplicationId = data;
            })
        );

        this.subscriptions.add(
            _riskAssessmentService._riskAssessment.subscribe(data => {
                this.riskAssessment = data;
                console.log('riskAssessment in riskAssesment component constructor is', this.riskAssessment);
            })
        );
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.selectedEnquiryForm = this._formBuilder.group({
            busPartnerNumber: [this.selectedEnquiry.busPartnerNumber || ''],
            projectLocationState: [this.selectedEnquiry.projectLocationState || ''],
            projectType: [this.selectedEnquiry.projectType || ''],
            loanClassDescription: [this.selectedEnquiry.loanClassDescription || ''],
            projectCapacity: [this.selectedEnquiry.projectCapacity || ''],
            assistanceTypeDescription: [this.selectedEnquiry.assistanceTypeDescription || ''],
            projectCost: [this.selectedEnquiry.projectCost || ''],
            loanAmount: [this.selectedEnquiry.loanAmount || ''],
            financingTypeDescription: [this.selectedEnquiry.financingTypeDescription || ''],
            leadFI: [this.selectedEnquiry.leadFI || ''],
            stage: [this.selectedEnquiry.stage || '']
        });
    }

    /**
     * sendPrelimRiskAssessmentForApproval()
     */
     sendPrelimRiskAssessmentForApproval(): void {
        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        let email = this._appService.currentUser.email;
        this._matSnackBar.open('Please wait while attempting to send the Prelim Risk Assessment for approval.', 'OK', { duration: 25000 });
        this._riskAssessmentService.sendRiskAssessmentForApproval(this.riskAssessment.id, name, email).subscribe(
            response => {
                this.riskAssessment = response;
                this._matSnackBar.dismiss();
                this._matSnackBar.open('Risk assessment is sent for approval.', 'OK', { duration: 7000 });
            },
            error => {
                this.disableSendForApproval = false;
                this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator', 
                    'OK', { duration: 7000 });
            });
        this.disableSendForApproval = true;
        this._location.back();
    }    
}
