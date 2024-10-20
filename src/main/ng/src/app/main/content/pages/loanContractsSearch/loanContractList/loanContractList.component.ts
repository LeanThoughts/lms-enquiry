import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {MatTableDataSource, MatSort, MatPaginator} from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { BehaviorSubject } from 'rxjs';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-loancontracts-search-list',
    templateUrl: './loanContractList.component.html',
    styleUrls: ['./loanContractList.component.scss'],
    animations: fuseAnimations
})
export class LoanContractListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;
    @ViewChild(MatPaginator) paginator: MatPaginator;

    @Input()
    set loanContractList(loanContractList: EnquiryApplicationModel[]) {
        this.dataSource = new MatTableDataSource(loanContractList);
        this.dataSource.sort = this.sort
        this.dataSource.paginator = this.paginator;
    }

    pageSizeOptions: number[] = [10, 25, 50, 100];


    displayedColumns = [
        'functionalStatusDescription', 'enquiryNumber', 'busPartnerNumber', 'borrowerName', 'loanContractId', 'projectName', 'projectLocationState', 
            'projectTypeDescription', 'loanClassDescription', 'projectCapacity', 'assistanceTypeDescription', 'projectCost'
    ];

    selectedEnquiry: EnquiryApplicationModel;

    projectTypes: any[] = [];

    /**
     * constructor()
     */
    constructor(private _service: LoanEnquiryService) {
        this._service.getProjectTypes().subscribe(response => {
            this.projectTypes = response._embedded.projectTypes;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        /**
         * this.sort will not be initialized in the constructor phase. It will be undefined and hence sorting
         * will not work. The below line has to be in ngOnInit() which is executed after all initializations.
         */
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;

    }

    /**
     *
     * @param enquiry
     */
    onSelect(enquiry: EnquiryApplicationModel): void {
        this.selectedEnquiry = enquiry;
        this._service.selectedLoanApplicationId = new BehaviorSubject(enquiry.id);
        this._service.selectedLoanApplicationPartyNumber = new BehaviorSubject(enquiry.busPartnerNumber);
        this._service.selectedEnquiry.next(enquiry);
    }

    /**
     * getProjectTypeDescription()
    */
    getProjectTypeDescription(projectType: string): string {
        if (projectType) {
            return this.projectTypes.filter(pt => pt.code === projectType)[0].value;
        }
        return '';
    }
}
