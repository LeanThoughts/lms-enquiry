import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { AppService } from 'app/app.service';
import { ReasonForDelayUpdateComponent } from './reasonForDelay/reasonForDelay.component';
import { CustomerRejectionUpdateComponent } from './customerRejection/customerRejection.component';

@Component({
    selector: 'fuse-loanappraisal',
    templateUrl: './loanAppraisal.component.html',
    styleUrls: ['./loanAppraisal.component.scss'],
    animations: fuseAnimations
})
export class LoanAppraisalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    loanAppraisalId: string;

    selectedEnquiry: any;
    selectedEnquiryForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;
    expandPanel2 = false;

    _reasonForDelay: any;
    _customerRejection: any;

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
                public _appService: AppService) {
        
        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
            console.log('this.selectedEnquiry', this.selectedEnquiry);
        }));          
        
        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                this.loanApplicationId = data;
            })
        );

        this._reasonForDelay = _activatedRoute.snapshot.data.routeResolvedData[8];
        this._customerRejection = _activatedRoute.snapshot.data.routeResolvedData[9];
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
     * openReasonForDelayUpdateDialog()
     */
    openReasonForDelayUpdateDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this.loanApplicationId,
            'loanAppraisalId': this.loanAppraisalId,
            'reasonForDelay': this._reasonForDelay
        };
        const dialogRef = this._dialogRef.open(ReasonForDelayUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._reasonForDelay = result.reasonForDelay;
            }
        });        
    }

    /**
     * openCustomerRejectionUpdateDialog()
     */
    openCustomerRejectionUpdateDialog(): void {
        // Open the dialog.
        var data = {
            'loanApplicationId': this.loanApplicationId,
            'loanAppraisalId': this.loanAppraisalId,
            'customerRejection': this._customerRejection
        };
        const dialogRef = this._dialogRef.open(CustomerRejectionUpdateComponent, {
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe(result => {
            if (result && result.refresh === true) {
                this._customerRejection = result.customerRejection;
            }
        });   
    }
}
