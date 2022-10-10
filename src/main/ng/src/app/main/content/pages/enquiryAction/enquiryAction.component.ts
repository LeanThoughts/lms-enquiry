import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { MatDialog, MatSnackBar } from '@angular/material';
import { AppService } from 'app/app.service';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { EnquiryActionService } from './enquiryAction.service';


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
                private _matSnackBar: MatSnackBar) {

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
            console.log('this.selectedEnquiry', this.selectedEnquiry);
        }));          
        
        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                this.loanApplicationId = data;
            })
        );

        this.subscriptions.add(
            _enquiryActionService._enquiryAction.subscribe(data => {
                this.enquiryAction = data;
                console.log('enquiry action in enquiry action component constructor is', this.enquiryAction);
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
     * sendAppraisalForApproval()
     */
    sendAppraisalForApproval(): void {

    }
}
