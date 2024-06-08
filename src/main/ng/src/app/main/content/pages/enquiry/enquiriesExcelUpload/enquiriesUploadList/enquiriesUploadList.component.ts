import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {MatTableDataSource, MatSort, MatPaginator, MatDialog, MatSnackBar} from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiryApplication.service';
import { EnquiryApplicationModel } from 'app/main/content/model/enquiryApplication.model';

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
    set enquiryList(enquiryList: EnquiryApplicationModel[]) {
        this.dataSource = new MatTableDataSource(enquiryList);
        this.dataSource.sort = this.sort
        // this.dataSource.paginator = this.paginator;
    }

    // pageSizeOptions: number[] = [10, 25, 50, 100];

    displayedColumns = [
        'comments', 'serialNumber', 'borrowerName', 'groupName', 'projectType', 'typeOfAssistance', 'proposalType', 'dateOfLeadGeneration',
            'amountRequested', 'borrowerRequestedROI', 'iccReadinessStatus', 'remarksOnIccReadiness', 'presentedInIcc', 'iccStatus',
            'reasonForIccStatus', 'iccClearanceDate', 'iccMeetingNumber', 'amountApproved', 'iccApprovedRoi', 'remarksForIccApproval'
    ];

    selectedEnquiry: any;

    /**
     * constructor()
     */
    constructor(private _service: LoanEnquiryService, private _dialogRef: MatDialog, private _matSnackBar: MatSnackBar) {
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
        this._service.createExcelEnquiries().subscribe();
    }

    /**
     * viewRejectedReasons()
     */
    viewRejectedReasons(comments: string): void {
        // Open the dialog.
        // const arr = [];
        // comments.split(';').forEach(comment => arr.push({'comment': comment}))
        // var data = {
        //     'comments': arr
        // };
        // this._dialogRef.open(EnquiriesUploadReasonsDialogComponent, {
        //     width: '750px',
        //     data: data
        // });

        // // Subscribe to the dialog close event to intercept the action taken.
        // dialogRef.afterClosed().subscribe(result => {
        //     if (result && result.refresh === true) {
        //         this._projectAppraisalCompletion = result.projectAppraisalCompletion;
        //         this.populateDisplayTables();
        //     }
        // });

        // this._matSnackBar.open(comments, 'OK', { duration: 15000 });
        this._matSnackBar.open(comments, 'OK', {panelClass: ['success-snackbar']});
    }
}
