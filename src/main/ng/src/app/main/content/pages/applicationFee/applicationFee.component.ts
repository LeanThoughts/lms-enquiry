import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { AppService } from 'app/app.service';
import { MatSnackBar } from '@angular/material';
import { Location } from '@angular/common';
import { ApplicationFeeService } from './applicationFee.service';


@Component({
    selector: 'fuse-application-fee',
    templateUrl: './applicationFee.component.html',
    animations: fuseAnimations
})
export class ApplicationFeeComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    selectedEnquiry: any;
    // selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;

    iccApproval: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, public _appService: AppService, private _matSnackBar: MatSnackBar,
                public _loanEnquiryService: LoanEnquiryService, private _applicationFeeService: ApplicationFeeService,
                private _location: Location) {

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
            console.log('this.selectedEnquiry', this.selectedEnquiry);
        }));          
        
        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                this.loanApplicationId = data;
            })
        );

        // this.subscriptions.add(
        //     _loanAppraisalService._loanAppraisalBS.subscribe(data => {
        //         this.loanAppraisal = data;
        //         console.log('loan appraisal in appraisal component subscription is', this.loanAppraisal);
        //     })
        // );
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
        // this.selectedEnquiryForm = this._formBuilder.group({
        //     busPartnerNumber: [this.selectedEnquiry.busPartnerNumber || ''],
        //     projectLocationState: [this.selectedEnquiry.projectLocationState || ''],
        //     projectType: [this.selectedEnquiry.projectType || ''],
        //     loanClassDescription: [this.selectedEnquiry.loanClassDescription || ''],
        //     projectCapacity: [this.selectedEnquiry.projectCapacity || ''],
        //     assistanceTypeDescription: [this.selectedEnquiry.assistanceTypeDescription || ''],
        //     projectCost: [this.selectedEnquiry.projectCost || ''],
        //     loanAmount: [this.selectedEnquiry.loanAmount || ''],
        //     financingTypeDescription: [this.selectedEnquiry.financingTypeDescription || ''],
        //     leadFI: [this.selectedEnquiry.leadFI || ''],
        //     stage: [this.selectedEnquiry.stage || '']
        // });
    }

    /**
     * sendAppraisalForApproval()
     */
    sendForApproval(): void {
        // let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        // let email = this._appService.currentUser.email;
        // this._matSnackBar.open('Please wait while attempting to send the board approval for approval.', 'OK', { duration: 25000 });
        // this._iccApprovalService.sendBoardApprovalForWorkflowApproval(this.boardApproval.id, name, email).subscribe(
        //     response => {
        //         this.boardApproval = response;
        //         this._matSnackBar.dismiss();
        //         this._matSnackBar.open('Board approval is sent for approval.', 'OK', { duration: 7000 });
        //     },
        //     error => {
        //         this.disableSendForApproval = false;
        //         this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator',
        //             'OK', { duration: 7000 });
        //     });
        // this.disableSendForApproval = true;
        // this._location.back();
    }
}
