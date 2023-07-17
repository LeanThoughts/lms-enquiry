import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort } from '@angular/material';
import { BehaviorSubject } from 'rxjs';
import { fuseAnimations } from '@fuse/animations';
import { EnquiryAlertsService } from '../enquiryAlerts.service';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import { log } from 'console';

@Component({
    selector: 'fuse-enquiry-alerts-list',
    templateUrl: './enquiryAlertsList.component.html',
    styleUrls: ['./enquiryAlertsList.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class EnquiryAlertsListComponent implements OnInit {

    dataSource: MatTableDataSource<EnquiryApplicationModel>;
    @ViewChild(MatSort) sort: MatSort;

    selectedEnquiry: EnquiryApplicationModel;

    pageSizeOptions: number[] = [10, 25, 50, 100];


  displayedColumns = [
        'functionalStatusDescription', 'technicalStatusDescription','createdOn', 'enquiryNumber', 'projectName', 'projectLocationState', 'projectTypeDescription',
        'loanClassDescription', 'projectCapacity', 'assistanceTypeDescription', 'projectCost', 'loanAmount'
    ];

    constructor(private _service: EnquiryAlertsService) {
        this.dataSource = new MatTableDataSource(_service.loanApplications.value);


    }

    ngOnInit(): void {
        /**
         * this.sort will not be initialized in the constructor phase. It will be undefined and hence sorting
         * will not work. The below line has to be in ngOnInit() which is executed after all initializations.
         */
        this.dataSource.sort = this.sort;
    }

    onSelect(enquiry: EnquiryApplicationModel): void {
        this.selectedEnquiry = enquiry;
        this._service.selectedLoanApplicationId = new BehaviorSubject(enquiry.id);
    }
}
