import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { MatDialog, MatSnackBar } from '@angular/material';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from './loanMonitoring.service';
import { AppService } from 'app/app.service';

@Component({
    selector: 'fuse-loanmonitoring',
    templateUrl: './loanMonitoring.component.html',
    styleUrls: ['./loanMonitoring.component.scss'],
    animations: fuseAnimations
})
export class LoanMonitoringComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    loanApplicationId: string;
    loanContractExtension: any = {};
    loanMonitor: any;
    
    selectedEnquiry: any;

    selectedEnquiryForm: FormGroup;
    boardApprovalDetailsForm: FormGroup;

    subscriptions = new Subscription()

    expandPanel1 = true;
    expandPanel2 = false;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _service 
     * @param _router 
     * @param _dialogRef 
     */
    constructor(private _formBuilder: FormBuilder, public _loanEnquiryService: LoanEnquiryService, private _router: Router, private _dialogRef: MatDialog,
                private _loanMonitoringService: LoanMonitoringService, public _appService: AppService, private _matSnackBar: MatSnackBar,
                private _activatedRoute: ActivatedRoute) {
        
        _activatedRoute.data.subscribe((data) => {
            console.log('loan monitoring route resolved data', data);
            if (data.routeResolvedData !== null) {
                this.loanContractExtension = data.routeResolvedData;
                _loanMonitoringService.loanContractExtension = data.routeResolvedData;
                console.log('loanContractExtension', this.loanContractExtension);
            }
        });

        this.subscriptions.add(this._loanMonitoringService.loanMonitor.subscribe(data => {
            this.loanMonitor = data;
        }));

        this.subscriptions.add(this._loanEnquiryService.selectedEnquiry.subscribe(data => {
            this.selectedEnquiry = data;
        }));          
        
        this.subscriptions.add(
            _loanEnquiryService.selectedLoanApplicationId.subscribe(data => {
                // set loanApplicationId
                this.loanApplicationId = data;
                // getLoanMonitor
                _loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
                    this.loanMonitor = data;
                })
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

        this.boardApprovalDetailsForm = this._formBuilder.group({
            boardMeetingNumber: [this.loanContractExtension.boardMeetingNumber || ''],
            boardApprovalDate: [this.loanContractExtension.boardApprovalDate || ''],
            loanNumber: [this.loanContractExtension.loanNumber || ''],
            sanctionLetterDate: [this.loanContractExtension.sanctionLetterDate || ''],
            loanDocumentationDate: [this.loanContractExtension.loanDocumentationDate || ''],
            firstDistributionDate: [this.loanContractExtension.firstDisbursementDate || ''],
            sanctionAmount: [this.loanContractExtension.sanctionAmount || ''],
            discributionStatus: [this.loanContractExtension.disbursementStatus || ''],
            scheduledCOD: [this.loanContractExtension.scheduledCOD || '']
        });
    }

    /**
     * redirectToMonitorLoan()
     */
    redirectToMonitorLoan(): void {
        this._router.navigate(['/enquiryReview']);
    }

    /**
     * sendMonitoringForApproval()
     */
     sendMonitoringForApproval(): void {
        let name = this._appService.currentUser.firstName + ' ' + this._appService.currentUser.lastName;
        let email = this._appService.currentUser.email;
        this._matSnackBar.open('Please wait while attempting to send monitoring for approval.', 'OK', { duration: 25000 });
        this._loanMonitoringService.sendMonitoringForApproval(this.loanMonitor.id, name, email).subscribe(
            response => {
                this.loanMonitor = response;
                this._matSnackBar.dismiss();
                this._matSnackBar.open('Monitoring is sent for approval.', 'OK', { duration: 7000 });
            },
            error => {
                this.disableSendForApproval = false;
                this._matSnackBar.open('Errors occured. Pls try again after sometime or contact your system administrator', 
                    'OK', { duration: 7000 });
            });
        this.disableSendForApproval = true;
    }

    /**
     * getLoanMonitor()
     */
    getLoanMonitor(): void {
        this._loanMonitoringService.getLoanMonitor(this.loanApplicationId).subscribe(data => {
            this.loanMonitor = data;
        })
    }
}
