import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {PartnerModel} from "../../../../model/partner.model";
import {MatPaginator, MatSnackBar, MatSort, MatTableDataSource} from "@angular/material";
import {BehaviorSubject} from "rxjs/Rx";
import { fuseAnimations } from '@fuse/animations';
import {animate, state, style, transition, trigger} from '@angular/animations';

import {PartnerService} from "../partner.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-partner',
  templateUrl: './partnerSearch.component.html',
  styleUrls: ['./partnerSearch.component.scss'],
  animations: [
    fuseAnimations,
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class PartnerComponent implements OnInit {

  dataSource: MatTableDataSource<any>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input()
  set partnerList(partnerList: PartnerModel[]) {
    this.dataSource = new MatTableDataSource(partnerList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }


  displayedColumns = [
    'partyNumber', 'email', 'partyName1', 'contactPersonName','address'
  ];
  columnsToDisplay = ['partyNumber', 'email', 'partyName1', 'contactPersonName','address'];



  selectedPartner: PartnerModel;

  partnerSearchForm: FormGroup;
  //partnerList: PartnerModel[];
  expandPanel = true;

  pageSizeOptions: number[] = [10, 25, 50, 100];


  constructor(private _service: PartnerService,_formBuilder: FormBuilder, private _router: Router,private _matSnackBar: MatSnackBar) {
    this.partnerSearchForm = _formBuilder.group({
      partnerName: [],
      email: [],
      partnerNumberFrom: [],
      partnerNumberTo: []

    });
    _service.selectedPartnerId = undefined;

  }

  ngOnInit() {

  }

  /**
   * searchPartner()
   */
  searchPartners(): void {
    // this._service.getPartners(this.partnerSearchForm.value).subscribe((result) => {
    const searchForm = this.partnerSearchForm.value;

    let searchParameters: Array<string> = [searchForm.partnerName,
                                           searchForm.email,
                                           searchForm.partnerNumberFrom,
                                           searchForm.partnerNumberTo];


    //Check if from search parameters are is empty
    if ( searchForm.partnerName == undefined &&
         searchForm.email == undefined &&
         searchForm.partnerNumberFrom == undefined &&
         searchForm.partnerNumberTo == undefined) {
      this._matSnackBar.open('Error: Enter at least one search parameter', 'OK', { duration: 7000 });

      return;
    }
    // Check if Partner number to is lesser than partner number from
    if (searchForm.partnerNumberTo) {
      if (searchForm.partnerNumberTo < searchForm.partnerNumberFrom) {
        this._matSnackBar.open('Error: Partner Number To is less than Partner Number From', 'OK', {duration: 7000});
        return;
      }
    }


    this._service.getPartners(searchParameters).subscribe((result) => {
      const partners = new Array<PartnerModel>();
      if (result.length == 0 ){
        this._matSnackBar.open('No business partners found', 'OK', {duration: 2000});
        return;
      }

      result.map(partnerResourceModel => {
        // console.log("RESULT:" + partnerResourceModel);
        // console.log("PARTNER RESOUCRCE:" + partnerResourceModel);

        partners.push(new PartnerModel(partnerResourceModel));
      });
      console.log("Partners" + partners.values());
      this.partnerList = partners;
      this.expandPanel = true;
    });
  }




  /**
   *
   * @param enquiry
   */
  onSelect(partner: PartnerModel): void {
    this.selectedPartner = partner;
    this._service.selectedPartnerId = new BehaviorSubject(partner.partyNumber);

  }

  /**
   * Redirect to Partner Details
   */
  redirectToPartnerDetails(): void {
    if (this._service.selectedPartnerId !== undefined) {
      this._service.selectedPartnerId.next(this._service.selectedPartnerId.value);
     }
    else {
      this._service.selectedPartnerId= new BehaviorSubject(this._service.selectedPartnerId.value);

    }
   //TODO
    // this._router.navigate(['/enquiryReview']);
  }
}

export interface partnerElement {
  partyNumber: string;
  email: number;
  partyName1: number;
  contactPerson: string;

}
