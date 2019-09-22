import {Component, OnInit, ViewChild} from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { LoanEnquiryService } from '../enquiryApplication.service';
import { fuseAnimations } from '@fuse/animations';
import {ActivatedRoute, Router} from '@angular/router';
import { EnquiryAlertsService } from '../enquiryAlerts/enquiryAlerts.service';
import { BehaviorSubject } from 'rxjs';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import {MatPaginator, MatSnackBar} from "@angular/material";

@Component({
    selector: 'fuse-enquiry-search',
    templateUrl: './enquirySearch.component.html',
    styleUrls: ['./enquirySearch.component.scss'],
    animations: fuseAnimations
})
export class EnquirySearchComponent {

    @ViewChild(MatPaginator ) paginator: MatPaginator;

    enquirySearchForm: FormGroup;

    enquiryList: EnquiryApplicationModel[];

    expandPanel = true;

  loanClasses: Array<any>;
  financingTypes: Array<any>;
  projectTypes: Array<any>;
  assistanceTypes: Array<any>;
  states: Array<string>;

   /**
     *
     * @param _formBuilder
     * @param _service
     * @param _router
     * @param _enquiryAlertsService
     */
    constructor(_route: ActivatedRoute,_formBuilder: FormBuilder,
                 public _service: LoanEnquiryService, private _router: Router,
                private _enquiryAlertsService: EnquiryAlertsService,private _matSnackBar: MatSnackBar) {

        this.enquirySearchForm = _formBuilder.group({
            enquiryNoFrom: [],
            enquiryNoTo: [],
            enquiryDateFrom: [],
            enquiryDateTo: [],
            partyName: [],
            projectLocationState: [],
            loanClass: [],
            projectType: [],
            financingType: [],
            assistanceType: []
        });

        _service.selectedLoanApplicationId = undefined;


     // Initialize dropdowns.
     this.loanClasses = _route.snapshot.data.routeResolvedData[0]._embedded.loanClasses;
     this.financingTypes = _route.snapshot.data.routeResolvedData[1]._embedded.financingTypes;
     this.projectTypes = _route.snapshot.data.routeResolvedData[2]._embedded.projectTypes;
     this.states = _route.snapshot.data.routeResolvedData[3];
     this.assistanceTypes = _route.snapshot.data.routeResolvedData[4]._embedded.assistanceTypes;

     }

    /**
     * searchEnquiries()
     */
    searchEnquiries(): void {

      this._matSnackBar.dismiss();

      const enquirySearchParameters = this.enquirySearchForm.value;

      if (enquirySearchParameters.enquiryDateFrom == undefined &&
        enquirySearchParameters.enquiryNoFrom == undefined  &&
        enquirySearchParameters.partyName == undefined  &&
        enquirySearchParameters.enquiryNoFrom == undefined  &&
        enquirySearchParameters.projectLocationState == undefined  &&
        enquirySearchParameters.loanClass == undefined  &&
        enquirySearchParameters.projectType == undefined  &&
        enquirySearchParameters.financingType == undefined  &&
        enquirySearchParameters.assistanceType == undefined ) {
        this._matSnackBar.open('Error: Enter at least one search parameter', 'OK', { duration: 7000 });

        return;
      }

      //Check if from date is empty
      if (enquirySearchParameters.enquiryDateFrom == '' ||
          enquirySearchParameters.enquiryDateFrom == undefined &&
          enquirySearchParameters.enquiryDateTo != undefined) {
        this._matSnackBar.open('Error: From Date is not entered. Please enter the from date', 'OK', { duration: 7000 });

        return;
      }

      //Check if To Date is greater than From Date
      let dateFrom = new Date(enquirySearchParameters.enquiryDateFrom);
      let dateTo = new Date(enquirySearchParameters.enquiryDateTo);

      if (dateTo < dateFrom){
        this._matSnackBar.open('Error: To Date is less than From Date', 'OK', { duration: 7000 });
        return;
      }



      this._service.searchLoanEnquiries(this.enquirySearchForm.value).subscribe((result) => {
            const enquiryApplications = new Array<EnquiryApplicationModel>();
            result.map(loanApplicationResourceModel => {
                enquiryApplications.push(new EnquiryApplicationModel(loanApplicationResourceModel));
            });
            this.enquiryList = enquiryApplications;
            this.expandPanel = false;
        });
    }

    /**
     *
     */
    redirectToEnquiryReview(): void {
        if (this._enquiryAlertsService.selectedLoanApplicationId !== undefined) {
            this._enquiryAlertsService.selectedLoanApplicationId.next(this._service.selectedLoanApplicationId.value);
            this._enquiryAlertsService.selectedLoanApplicationPartyNumber.next(this._service.selectedLoanApplicationPartyNumber.value);
        }
        else {
            this._enquiryAlertsService.selectedLoanApplicationId = new BehaviorSubject(this._service.selectedLoanApplicationId.value);
            this._enquiryAlertsService.selectedLoanApplicationPartyNumber = new BehaviorSubject(this._service.selectedLoanApplicationPartyNumber.value);

        }
        this._router.navigate(['/enquiryReview']);
    }
}
