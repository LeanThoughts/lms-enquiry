import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { BoardApprovalService } from './boardApproval.service';
import { AppService } from 'app/app.service';
import { MatSnackBar } from '@angular/material';
import { Location } from '@angular/common';


@Component({
    selector: 'fuse-boardapproval',
    templateUrl: './boardApproval.component.html',
    animations: fuseAnimations
})
export class BoardApprovalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    selectedEnquiry: any;
    selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;

    boardApproval: any;
    loanContractId: any;
    functionalStatus: string;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, public _appService: AppService, private _matSnackBar: MatSnackBar,
                public _loanEnquiryService: LoanEnquiryService, private _boardApprovalService: BoardApprovalService,
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

        this.subscriptions.add(this._boardApprovalService._boardApproval.subscribe(data => {
            this.boardApproval = data;
        }));
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
            enquiryNumber: [this.selectedEnquiry.enquiryNumber || '']
        });

        this.selectedEnquiryForm.get('projectType')
            .setValue(this._loanEnquiryService.projectTypes.filter(pt => pt.code === this.selectedEnquiry.projectType)[0].value);

        this.selectedEnquiryForm.get('financingTypeDescription')
            .setValue(this._loanEnquiryService.financingTypes.filter(ft => ft.code === this.selectedEnquiry.financingType)[0].value);
    }

    /**
     * sendAppraisalForApproval()
     */
    sendForApproval(): void {
        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        let email = this._appService.currentUser.email;
        this._matSnackBar.open('Please wait while attempting to send the board approval for approval.', 'OK', { duration: 25000 });
        this._boardApprovalService.sendBoardApprovalForWorkflowApproval(this.boardApproval.id, name, email).subscribe(
            response => {
                this.boardApproval = response;
                this._matSnackBar.dismiss();
                this._matSnackBar.open('Board approval is sent for approval.', 'OK', { duration: 7000 });
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
