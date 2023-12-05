import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { fuseAnimations } from '@fuse/animations';
import {ActivatedRoute, Router} from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import {MatPaginator, MatSnackBar} from "@angular/material";
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { EnquiryAlertsService } from '../enquiry/enquiryAlerts/enquiryAlerts.service';
import { LoanMonitoringConstants } from '../../model/loanMonitoringConstants';
import { AppService } from 'app/app.service';
import { LoanAppraisalService } from '../appraisal/loanAppraisal.service';
import { HttpErrorResponse } from '@angular/common/http';
import { EnquiryActionService } from '../enquiryAction/enquiryAction.service';
import { BoardApprovalService } from '../boardApproval/boardApproval.service';
import { SanctionService } from '../sanction/sanction.service';
import { ICCApprovalService } from '../iccApproval/iccApproval.service';
import { ApplicationFeeService } from '../applicationFee/applicationFee.service';

@Component({
    selector: 'fuse-loancontracts-search',
    templateUrl: './loanContractsSearch.component.html',
    styleUrls: ['./loanContractsSearch.component.scss'],
    animations: fuseAnimations
})
export class LoanContractsSearchComponent implements OnInit, OnDestroy {

    @ViewChild(MatPaginator ) paginator: MatPaginator;

    loanContractsSearchForm: FormGroup;

    loanContractList: EnquiryApplicationModel[];

    expandPanel = true;

    accountStatuses: Array<any>;
    loanClasses: Array<any>;
    financingTypes: Array<any>;
    projectTypes: Array<any>;
    assistanceTypes: Array<any>;
    states: Array<string>;
    technicalStatuses: Array<any>;

    monitoring: boolean = false;
    appraisal: boolean = false;
    boardApproval: boolean = true; // Fix this later
    sanction: boolean = true; // fix this later


    /**
     *
     * @param _formBuilder
     * @param _service
     * @param _router
     * @param _enquiryAlertsService
     */
    constructor(_route: ActivatedRoute, _formBuilder: FormBuilder, public _appService: AppService,
                public _service: LoanEnquiryService, private _router: Router, private _loanAppraisalService: LoanAppraisalService,
                private _enquiryAlertsService: EnquiryAlertsService, private _loanEnquiryService: LoanEnquiryService,
                private _matSnackBar: MatSnackBar, private _enquiryActionService: EnquiryActionService, 
                private _boardApprovalService: BoardApprovalService, private _sanctionService: SanctionService, 
                private _iccApprovalService: ICCApprovalService, private _applicationFeeService: ApplicationFeeService) {

        this.loanContractsSearchForm = _formBuilder.group({
            accountStatus: [],
            technicalStatus:[],
            partyName: [],
            projectLocationState: [],
            loanClass: [],
            projectType: [],
            financingType: [],
            assistanceType: [],
            borrowerCodeFrom: [],
            borrowerCodeTo: [],
            loanNumberFrom: [],
            loanNumberTo: []
        });

        _service.selectedLoanApplicationId = undefined;

        // Initialize dropdowns.
        this.accountStatuses = LoanMonitoringConstants.functionalStatuses;
        this.loanClasses = _route.snapshot.data.routeResolvedData[0]._embedded.loanClasses;
        this.financingTypes = _route.snapshot.data.routeResolvedData[1]._embedded.financingTypes;
        this.projectTypes = _route.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
        this.states = _route.snapshot.data.routeResolvedData[3];
        this.assistanceTypes = _route.snapshot.data.routeResolvedData[4]._embedded.assistanceTypes;
        this.technicalStatuses = _route.snapshot.data.routeResolvedData[5];

        _route.snapshot.data.routeResolvedData[6].forEach(element => {
            if (element.authorizationObject === 'Execute Appraisal')
                this.appraisal = true;
            else if (element.authorizationObject === 'Execute Monitoring')
                this.monitoring = true;
        });

        console.log('_appService.currentUser', _appService.currentUser);
    }

    ngOnDestroy(): void {
        this._service.loanContractSearchValues = this.loanContractsSearchForm.value;
    }

    ngOnInit(): void {
        if (this._service.loanContractSearchValues !== undefined) {
            let formValues = this._service.loanContractSearchValues;
            this.loanContractsSearchForm.controls['accountStatus'].setValue(formValues.accountStatus);
            this.loanContractsSearchForm.controls['technicalStatus'].setValue(formValues.technicalStatus);
            this.loanContractsSearchForm.controls['partyName'].setValue(formValues.partyName);
            this.loanContractsSearchForm.controls['projectLocationState'].setValue(formValues.projectLocationState);
            this.loanContractsSearchForm.controls['loanClass'].setValue(formValues.loanClass);
            this.loanContractsSearchForm.controls['projectType'].setValue(formValues.projectType);
            this.loanContractsSearchForm.controls['financingType'].setValue(formValues.financingType);
            this.loanContractsSearchForm.controls['assistanceType'].setValue(formValues.assistanceType);
            this.loanContractsSearchForm.controls['borrowerCodeFrom'].setValue(formValues.borrowerCodeFrom);
            this.loanContractsSearchForm.controls['borrowerCodeTo'].setValue(formValues.borrowerCodeTo);
            this.loanContractsSearchForm.controls['loanNumberFrom'].setValue(formValues.loanNumberFrom);
            this.loanContractsSearchForm.controls['loanNumberTo'].setValue(formValues.loanNumberTo);

        }
    }

    /**
     * searchEnquiries()
     */
    searchEnquiries(): void {

        this._matSnackBar.dismiss();

        const loanContractsSearchParameters = this.loanContractsSearchForm.value;

        if (loanContractsSearchParameters.accountStatus == undefined &&
            loanContractsSearchParameters.technicalStatus == undefined &&
            loanContractsSearchParameters.partyName == undefined  &&
            loanContractsSearchParameters.projectLocationState == undefined  &&
            loanContractsSearchParameters.loanClass == undefined  &&
            loanContractsSearchParameters.projectType == undefined  &&
            loanContractsSearchParameters.financingType == undefined  &&
            loanContractsSearchParameters.assistanceType == undefined  &&
            loanContractsSearchParameters.borrowerCodeFrom == undefined  &&
            loanContractsSearchParameters.borrowerCodeTo == undefined &&
            loanContractsSearchParameters.loanNumberFrom == undefined &&
            loanContractsSearchParameters.loanNumberTo == undefined
        ) {
            this._matSnackBar.open('Error: Enter at least one search parameter', 'OK', { duration: 7000 });
        }
        else {
            this._service.searchLoanContracts(this.loanContractsSearchForm.value).subscribe((result) => {
                const enquiryApplications = new Array<EnquiryApplicationModel>();
                result.map(loanApplicationResourceModel => {
                    enquiryApplications.push(new EnquiryApplicationModel(loanApplicationResourceModel));
                });
                this.loanContractList = enquiryApplications;
                this.expandPanel = false;
            });
        }
    }

    /**
     * redirectToMonitorLoan()
     */
    redirectToMonitorLoan(): void {
        if (this._service.selectedEnquiry.value.loanContractId !== undefined) {
            this.redirect('/loanMonitoring');
        }
        else {
            this._matSnackBar.open('Loan is still in the enquiry phase. Process cannot only be executed after the enquiry is approved by BD Team',
                'OK', { duration: 7000 });
        }
    }

    /**
     * redirectToLoanAppraisal()
     */
    redirectToLoanAppraisal(): void {
        if (this._service.selectedEnquiry.value.loanContractId !== undefined) {
            this._loanAppraisalService.getLaonAppraisal(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._loanAppraisalService._loanAppraisal = response;
                this._loanAppraisalService._loanAppraisalBS.next(response);
                this.redirect('/loanAppraisal');
            }, (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._loanAppraisalService._loanAppraisal = { id: '' };
                    this.redirect('/loanAppraisal');
                }
            })
        }
        else {
            this._matSnackBar.open('Loan is still in the enquiry phase. Process cannot only be executed after the enquiry is approved by BD Team',
                'OK', { duration: 7000 });
        }
    }

    /**
     * redirectToICCApprovalStage()
     */
    redirectToICCApprovalStage(): void {
        //if (this._service.selectedEnquiry.value.loanContractId === undefined) {
            this._iccApprovalService.getICCApproval(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._iccApprovalService._iccApproval.next(response);
                this.redirect('/iccApprovalStage');
            }, (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._iccApprovalService._iccApproval.next({ id: '' });
                    this.redirect('/iccApprovalStage');
                }
            })
        //}
        // else {
        //     this._matSnackBar.open('Loan has already completed the enquiry phase ! ',
        //         'OK', { duration: 7000 });
        // }
        // this.redirect('/iccApprovalStage');
    }

    /**
     * redirectToApplicationFee()
     */
    redirectToApplicationFee(): void {
        //if (this._service.selectedEnquiry.value.loanContractId === undefined) {
            this._applicationFeeService.getApplicationFee(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._applicationFeeService._applicationFee.next(response);
                this.redirect('/applicationFee');
            }, (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._applicationFeeService._applicationFee.next({ id: '' });
                    this.redirect('/applicationFee');
                }
            })
        //}
        // else {
        //     this._matSnackBar.open('Loan has already completed the enquiry phase ! ',
        //         'OK', { duration: 7000 });
        // }
        // this.redirect('/iccApprovalStage');
    }

    /**
     * redirectToEnquiryAction()
     */
    redirectToEnquiryAction(): void {
        if (this._service.selectedEnquiry.value.loanContractId === undefined) {
            this._enquiryActionService.getEnquiryAction(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._enquiryActionService._enquiryAction.next(response);
                this.redirect('/enquiryAction');
            }, (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._enquiryActionService._enquiryAction.next({ id: '' });
                    this.redirect('/enquiryAction');
                }
            })
        }
        else {
            this._matSnackBar.open('Loan has already completed the enquiry phase ! ',
                'OK', { duration: 7000 });
        }
    }

    /**
     * redirectToBoardApproval()
     */
    redirectToBoardApproval(): void {
        this._boardApprovalService.getBoardApproval(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
            this._boardApprovalService._boardApproval.next(response);
            this.redirect('/boardApproval');
        }, 
        (error: HttpErrorResponse) => {
            if (error.status === 404) {
                this._boardApprovalService._boardApproval.next({ id: '' });
                this.redirect('/boardApproval');
            }
        })
    }

    /**
     * redirectToSanction()
     */
    redirectToSanction(): void {
        const functionalStatus: number = this._service.selectedEnquiry.value.functionalStatus;
        console.log('functional status is', functionalStatus);

        if (functionalStatus == 5) {
            // Redirect to Sanction
            this._sanctionService.getSanction(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._sanctionService._sanction.next(response);
                this.redirect('/sanction');
            }, 
            (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._sanctionService._sanction.next({ id: '' });
                    this.redirect('/sanction');
                }
            });
        }
        else if (functionalStatus == 4) {
            const loanApplicationId = this._service.selectedEnquiry.value.id;
            this._boardApprovalService.getBoardApproval(loanApplicationId).subscribe(data => {
                if (data.workFlowStatusCode = 3) {
                    // Redirect to Sanction
                    this._sanctionService.getSanction(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                        this._sanctionService._sanction.next(response);
                        this.redirect('/sanction');
                    }, 
                    (error: HttpErrorResponse) => {
                        if (error.status === 404) {
                            this._sanctionService._sanction.next({ id: '' });
                            this.redirect('/sanction');
                        }
                    });
                }
                else if (data.workFlowStatusCode == 1 || data.workFlowStatusCode == 2) {
                    this._matSnackBar.open('Board approval workflow not completed for loan.', 'OK', { duration: 7000 });
                }
                else if (data.workFlowStatusCode == 4) {
                    this._matSnackBar.open('Board Approval is rejected. Sanction not possible.', 'OK', { duration: 7000 });
                }
                else if (data.workFlowStatusCode > 5) {
                    this._matSnackBar.open('Board Approval is in ... state.', 'OK', { duration: 7000 });
                }
            })
        }
        else if (functionalStatus >= 1 && functionalStatus <= 3) {
            // this._matSnackBar.open('Loan is still in 01-Enquiry Stage /02-ICC Approval Stage/ 03-Appraisal Stage. Sanction not Possible.',
            //     'OK', { duration: 7000 });
            
            this._sanctionService.getSanction(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
                this._sanctionService._sanction.next(response);
                this.redirect('/sanction');
            }, 
            (error: HttpErrorResponse) => {
                if (error.status === 404) {
                    this._sanctionService._sanction.next({ id: '' });
                    this.redirect('/sanction');
                }
            });
        }
    }

    /**
     * redirect()
     * @param to
     */
    redirect(to: string): void {
        if (this._enquiryAlertsService.selectedLoanApplicationId !== undefined) {
            this._enquiryAlertsService.selectedLoanApplicationId.next(this._service.selectedLoanApplicationId.value);
            this._enquiryAlertsService.selectedLoanApplicationPartyNumber.next(this._service.selectedLoanApplicationPartyNumber.value);
        }
        else {
            this._enquiryAlertsService.selectedLoanApplicationId = new BehaviorSubject(this._service.selectedLoanApplicationId.value);
            this._enquiryAlertsService.selectedLoanApplicationPartyNumber = new BehaviorSubject(this._service.selectedLoanApplicationPartyNumber.value);
        }
        this._router.navigate([to]);
    }

    /**
     * reset()
     */
    reset(): void {
        this.loanContractsSearchForm.reset();
        this._service.loanContractSearchValues = undefined;
    }
}
