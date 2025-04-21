import {Component, ViewChild} from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { LoanEnquiryService } from '../enquiryApplication.service';
import { fuseAnimations } from '@fuse/animations';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from "@angular/material";
import { EnquiriesUploadListComponent } from './enquiriesUploadList/enquiriesUploadList.component';

@Component({
    selector: 'fuse-enquiry-excel-upload',
    templateUrl: './enquiriesExcelUpload.component.html',
    styleUrls: ['./enquiriesExcelUpload.component.scss'],
    animations: fuseAnimations
})
export class EnquiriesExcelUploadComponent {

    excelUploadForm: FormGroup;
    excelDownloadForm: FormGroup;
    expandPanel = true;

    enquiryList = [];

   /**
     * constructor()
     */
    constructor(_route: ActivatedRoute,_formBuilder: FormBuilder,
                public _service: LoanEnquiryService, private _router: Router, private _matSnackBar: MatSnackBar) {

        this.excelUploadForm = _formBuilder.group({
            file: [''],
        });

        this.excelDownloadForm = _formBuilder.group({
            enquiryDateFrom: [''],
            enquiryDateTo: [''],
        });
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.excelUploadForm.get('file').setValue(file);
        }
    }

    /**
     * uploadEnquiries()
     */
    uploadEnquiries(): void {
        if (this.excelUploadForm.get('file').value !== '') {
            var formData = new FormData();
            formData.append('file', this.excelUploadForm.get('file').value);
            this._service.uploadExcelDocument(formData).subscribe(
                (response) => {
                    this.enquiryList = response;
                },
                (error) => {
                    console.log(error);
                    this._matSnackBar.open(error.error.message + ' Unable to upload the file. Pls try again after sometime or contact your'
                            + ' system administrator', 'OK', { duration: 7000 });
                }
            );
        }
        else
        {
            this._matSnackBar.open('Please select a file to upload.', 'OK', { duration: 7000 });
        };
    }

    /**
     * downloadEnquiries()
     */
    downloadEnquiries(): void {

        if (this.excelDownloadForm.get('enquiryDateFrom').value && this.excelDownloadForm.get('enquiryDateTo').value) {
            var enquiryDateFrom = new Date(this.excelDownloadForm.get('enquiryDateFrom').value);
            var dateFrom = new Date(Date.UTC(enquiryDateFrom.getFullYear(), enquiryDateFrom.getMonth(), enquiryDateFrom.getDate()));
            var enquiryDateTo = new Date(this.excelDownloadForm.get('enquiryDateTo').value);
            var dateTo = new Date(Date.UTC(enquiryDateTo.getFullYear(), enquiryDateTo.getMonth(), enquiryDateTo.getDate()));
            this._matSnackBar.open('Enquiries file download in progress.', 'OK', { duration: 10000 });
            //(window as any).open('enquiry/api/enquiriesExcelDownload'  );    
            this._service.downloadEnquiries(dateFrom, dateTo);
        }
        else {
            this._matSnackBar.open('Please select a date range to download the enquiries.', 'OK', { duration: 7000 });
        }
    }
}
