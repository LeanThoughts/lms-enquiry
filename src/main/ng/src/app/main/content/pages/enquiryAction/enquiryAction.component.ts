import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { MatDialog, MatSnackBar, MatTabGroup } from '@angular/material';
import { AppService } from 'app/app.service';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { EnquiryActionService } from './enquiryAction.service';
import { Location } from '@angular/common';


@Component({
    selector: 'fuse-enquiryaction',
    templateUrl: './enquiryAction.component.html',
    styleUrls: ['./enquiryAction.component.scss'],
    animations: fuseAnimations
})
export class EnquiryActionComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;

    selectedEnquiry: any;
    selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;
    expandPanel2 = false;

    enquiryAction: any;
    loanContractId: any;
    functionalStatus: string;
    
    // Declare and initialize tabGroup
    @ViewChild(MatTabGroup) tabGroup: MatTabGroup;

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
                public _enquiryActionService: EnquiryActionService,
                private _matSnackBar: MatSnackBar, 
                private _location: Location) {

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
            if (this.selectedEnquiry.loanContractId === null)
                this.loanContractId = this.selectedEnquiry.enquiryNumber;
            else
                this.loanContractId = this.selectedEnquiry.loanContractId;
            this.functionalStatus = _loanEnquiryService.getFunctionalStatusDescription(this.selectedEnquiry.functionalStatus);
        }));

        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                this.loanApplicationId = data;
                _enquiryActionService.getLoanApplication(data).subscribe(loanApplication => {
                    _enquiryActionService._loanApplication = loanApplication;
                });
            })
        );

        this.subscriptions.add(
            _enquiryActionService._enquiryAction.subscribe(data => {
                this.enquiryAction = data;
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
            stage: [this.selectedEnquiry.stage || ''],
            enquiryNumber: [this.selectedEnquiry.enquiryNumber || '']
        });
    }

    /**
     * sendEnquiryActionForApproval()
     */
    sendEnquiryActionForApproval(): void {
        this._enquiryActionService.getEnquiryCompletion(this.enquiryAction.id).subscribe(data => {
            if (Object.keys(data).length > 0) {
                this._enquiryActionService.getProjectProposalByStatus(this.enquiryAction.id, 'Final').subscribe(pp => {
                    if (pp._embedded.projectProposals.length > 0) {
                        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
                        let email = this._appService.currentUser.email;
                        this._matSnackBar.open('Please wait while attempting to send enquiry for approval.', 'OK', { duration: 25000 });
                        this._enquiryActionService.sendEnquiryActionForApproval(this.enquiryAction.id, name, email).subscribe(
                            response => {
                                this.enquiryAction = response;
                                this._matSnackBar.dismiss();
                                this._matSnackBar.open('Enquiry is sent for approval.', 'OK', { duration: 7000 });
                            },
                            error => {
                                this.disableSendForApproval = false;
                                this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator',
                                    'OK', { duration: 7000 });
                            });
                        this.disableSendForApproval = true;
                        this._location.back();
                    }
                    else {
                        this._matSnackBar.open('Project Proposal with status Final not found. Cannot send enquiry for approval.',
                        'OK', { duration: 7000 });
                    }
                });
            }
            else {
                // Activate the 5th tab (index 4) before showing the snackbar
                if (this.tabGroup) {
                    this.tabGroup.selectedIndex = 4;
                }
                this._matSnackBar.open('Data for enquiry completion is missing. Cannot send enquiry for approval.',
                'OK', { duration: 7000 });
            }
        })
    }
}
