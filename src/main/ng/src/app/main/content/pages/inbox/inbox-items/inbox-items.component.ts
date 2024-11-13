import { Component, OnInit } from '@angular/core';
import { InboxService } from '../inbox.service';
import { ActivatedRoute, Router } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { BehaviorSubject } from 'rxjs';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { EnquiryAlertsService } from '../../enquiry/enquiryAlerts/enquiryAlerts.service';

@Component({
    selector: 'app-inbox-items',
    templateUrl: './inbox-items.component.html',
    styleUrls: ['./inbox-items.component.scss'],
    animations: fuseAnimations
})
export class InboxItemsComponent implements OnInit {

    inboxItems: any;
    
    selectedItem: any;

    displayedColumns = [
        'requestDate', 'lanContractId', 'projectName', 'processName', 'requestorName', 'approverName'
    ];

    /**
     * 
     * @param _service: InboxService
     * @param _route: ActivatedRoute
     */
    constructor(private _service: InboxService, _route: ActivatedRoute) {
        // Fetch evaluations from route resolved data.
        _route.data.subscribe((data) => {
            this.inboxItems = data.routeResolvedData;
            console.log('inboxItems', this.inboxItems);
        });
    }

    /**
     * 
     */
    ngOnInit(): void {
    }

    /**
     * onSelect()
     * @param inboxItem 
     */
    onSelect(inboxItem: any): void {
        this.selectedItem = inboxItem;
        this._service.selectedItem = new BehaviorSubject(inboxItem);
        console.log('inbox selected item', this.selectedItem);
    }

    /**
     * refreshList()
     */
    refreshList(): void {
        this._service.fetchTasks().subscribe(response => {
            this.inboxItems = response;
        })
    }
}
