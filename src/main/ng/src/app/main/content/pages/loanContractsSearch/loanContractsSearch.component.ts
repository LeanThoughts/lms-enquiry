import {Component, OnInit, ViewChild} from '@angular/core';
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

@Component({
    selector: 'fuse-loancontracts-search',
    templateUrl: './loanContractsSearch.component.html',
    styleUrls: ['./loanContractsSearch.component.scss'],
    animations: fuseAnimations
})
export class LoanContractsSearchComponent {

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
                private _matSnackBar: MatSnackBar) {

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
        this.redirect('/loanMonitoring');
    }

    /**
     * redirectToLoanAppraisal()
     */
    redirectToLoanAppraisal(): void {
        this._loanAppraisalService.getLaonAppraisal(this._loanEnquiryService.selectedLoanApplicationId.value).subscribe(response => {
            this._loanAppraisalService._loanAppraisal = response;
            this.redirect('/loanAppraisal');
        })
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
}
