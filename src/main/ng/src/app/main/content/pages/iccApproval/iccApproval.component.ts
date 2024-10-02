import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { AppService } from 'app/app.service';
import { MatSnackBar } from '@angular/material';
import { Location } from '@angular/common';
import { ICCApprovalService } from './iccApproval.service';
import { log } from 'console';


@Component({
    selector: 'fuse-iccApproval',
    templateUrl: './iccApproval.component.html',
    animations: fuseAnimations
})
export class ICCApprovalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    selectedEnquiry: any;
    // selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;

    iccApproval: any;
    loanContractId: any;
    functionalStatus: string;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, public _appService: AppService, private _matSnackBar: MatSnackBar,
                public _loanEnquiryService: LoanEnquiryService, private _iccApprovalService: ICCApprovalService,
                private _location: Location) {

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
            _iccApprovalService._iccApproval.subscribe(data => {
                this.iccApproval = data;
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
        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        let email = this._appService.currentUser.email;
        this._matSnackBar.open('Please wait while attempting to send the icc approval for approval.', 'OK', { duration: 25000 });
        this._iccApprovalService.sendICCApprovalForApproval(this.iccApproval.id, name, email).subscribe(
            response => {
                this.iccApproval = response;
                this._matSnackBar.dismiss();
                this._matSnackBar.open('ICC Stage is sent for approval.', 'OK', { duration: 7000 });
            },
            error => {
                console.log(error);
                this.disableSendForApproval = false;
                if (error.status === 500) {
                    this._matSnackBar.open(error.error.message, 'OK', { duration: 9000 });
                }
                else {
                    this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator',
                        'OK', { duration: 7000 });    
                }
            });
        this.disableSendForApproval = true;
        // this._location.back();
    }
}
