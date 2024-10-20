import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {MatTableDataSource, MatSort, MatPaginator, MatDialog, MatSnackBar} from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiryApplication.service';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'fuse-enquiry-upload-list',
    templateUrl: './enquiriesUploadList.component.html',
    styleUrls: ['./enquiriesUploadList.component.scss'],
    animations: fuseAnimations
})
export class EnquiriesUploadListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;
    @ViewChild(MatPaginator) paginator: MatPaginator;

    @Input()
    set enquiryList(enquiryList: any[]) {
        this.dataSource = new MatTableDataSource(enquiryList);
        this.dataSource.sort = this.sort
        this.disableUploadButton = false;
        // this.dataSource.paginator = this.paginator;
    }

    // pageSizeOptions: number[] = [10, 25, 50, 100];

    displayedColumns = [
        'comments', 'serialNumber', 'sapEnquiryId', 'borrowerName', 'groupName', 'projectType', 'typeOfAssistance', 'proposalType', 
            'dateOfLeadGeneration', 'amountRequested', 'borrowerRequestedROI', 'iccReadinessStatus', 'remarksOnIccReadiness', 'presentedInIcc', 
            'iccStatus', 'reasonForIccStatus', 'iccClearanceDate', 'iccMeetingNumber', 'amountApproved', 'iccApprovedRoi', 
            'remarksForIccApproval'
    ];

    selectedEnquiry: any;

    disableUploadButton = false;

    /**
     * constructor()
     */
    constructor(private _service: LoanEnquiryService, private _dialogRef: MatDialog, private _matSnackBar: MatSnackBar) {
        console.log('setting datasource to undefined', this.dataSource);
        this.dataSource = undefined;
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
    }

    /**
     * createEnquiries()
     */
    createEnquiries(): void {
        if (this.dataSource === undefined)
            this._matSnackBar.open('Upload the excel file with enquiries first.', 'OK', {panelClass: ['success-snackbar']});
        else {
            this._service.createExcelEnquiries().subscribe(
                (response: any) => {
                    this._matSnackBar.open('Created enquiries successfully', 'OK', {panelClass: ['success-snackbar']});
                    this.dataSource = new MatTableDataSource(response);
                    this.dataSource.sort = this.sort
                    this.disableUploadButton = true;
                },
                (error: HttpErrorResponse) => {
                    this._matSnackBar.open(error.error.message, 'OK', {panelClass: ['success-snackbar']});
                }
            );
        }
    }

    /**
     * viewRejectedReasons()
     */
    viewRejectedReasons(comments: string): void {
        this._matSnackBar.open(comments, 'OK', {panelClass: ['success-snackbar']});
    }
}
