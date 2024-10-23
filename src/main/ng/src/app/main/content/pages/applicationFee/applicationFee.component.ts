import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { AppService } from 'app/app.service';
import { MatSnackBar } from '@angular/material';
import { Location } from '@angular/common';
import { ApplicationFeeService } from './applicationFee.service';
import { log } from 'console';
import { ActivatedRoute } from '@angular/router';


@Component({
    selector: 'fuse-application-fee',
    templateUrl: './applicationFee.component.html',
    animations: fuseAnimations
})
export class ApplicationFeeComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    selectedEnquiry: any;
    selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;

    applicationFee: any;
    loanContractId: any;
    functionalStatus: string;

    meetingNumbers = [];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, public _appService: AppService, private _matSnackBar: MatSnackBar,
                public _loanEnquiryService: LoanEnquiryService, private _applicationFeeService: ApplicationFeeService,
                private _location: Location, private _activatedRoute: ActivatedRoute) {

        this.meetingNumbers = _activatedRoute.snapshot.data.routeResolvedData[3];

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
            _applicationFeeService._applicationFee.subscribe(data => {
                this.applicationFee = data;
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
            stage: [this.selectedEnquiry.stage || this.selectedEnquiry.functionalStatusDescription],
            enquiryNumber: [this.selectedEnquiry.enquiryNumber || ''],
            iccMeetingNumber: [''],
        });

        const projectType = this._loanEnquiryService.projectTypes.filter(pt => pt.code === this.selectedEnquiry.projectType)[0];
        this.selectedEnquiryForm.get('projectType').setValue(projectType ? projectType.value : '');

        const financingType = this._loanEnquiryService.financingTypes.filter(ft => ft.code === this.selectedEnquiry.financingType)[0];
        this.selectedEnquiryForm.get('financingTypeDescription').setValue(financingType ? financingType.value : '');

        let iccMeetingNumber = this.meetingNumbers.filter(obj => obj.moduleName === 'Approval By ICC')[0];
        console.log('iccMeetingNumber', iccMeetingNumber);
        this.selectedEnquiryForm.get('iccMeetingNumber').setValue(iccMeetingNumber ? iccMeetingNumber.meetingNumber : '');
    }

    
    /**
     * sendAppraisalForApproval()
     */
    sendForApproval(): void {
        if (this.applicationFee.id) {
            this._applicationFeeService.getInvoicingDetails(this.applicationFee.id).subscribe(
                (response) => {
                    console.log('response is unbelievable', response);
                    if (Object.keys(response).length === 0) {
                        this._matSnackBar.open('Invoicing details not found. Please complete Customer and Invoicing details before sending for approval.', 'OK', { duration: 7000 });
                        return;
                    }
                    const name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
                    let email = this._appService.currentUser.email;
                    this._matSnackBar.open('Please wait while attempting to send application fee details for approval.', 'OK', { duration: 25000 });
                    this._applicationFeeService.sendApplicationFeeForApproval(this.applicationFee.id, name, email).subscribe(
                        (response) => {
                            this.applicationFee = response;
                            this._matSnackBar.dismiss();
                            this._matSnackBar.open('Application Fee is sent for approval.', 'OK', { duration: 7000 });
                            this.disableSendForApproval = true;
                            this._location.back();
                        },
                        (error) => {
                            this.disableSendForApproval = false;
                            this._matSnackBar.open('Errors occurred while sending for approval. Please try again later or contact your system administrator.',
                                'OK', { duration: 7000 });
                        }
                    );
                },
                (error) => {
                    if (error.status === 404) {
                        this._matSnackBar.open('Invoicing details not found. Please complete Customer and Invoicing details before sending for approval.', 'OK', { duration: 7000 });
                    } else {
                        this._matSnackBar.open('An error occurred while fetching invoicing details. Please try again later or contact your system administrator.', 'OK', { duration: 7000 });
                    }
                }
            );
        }
        else {
            this._matSnackBar.open('Please complete Customer and Invoicing details before sending for approval', 'OK', { duration: 7000 });
        }
    }
}
