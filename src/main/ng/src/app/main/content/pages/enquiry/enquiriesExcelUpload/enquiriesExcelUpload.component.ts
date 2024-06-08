import {Component} from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { LoanEnquiryService } from '../enquiryApplication.service';
import { fuseAnimations } from '@fuse/animations';
import {ActivatedRoute, Router} from '@angular/router';
import { EnquiryAlertsService } from '../enquiryAlerts/enquiryAlerts.service';
import {MatSnackBar} from "@angular/material";
import { log } from 'console';

@Component({
    selector: 'fuse-enquiry-excel-upload',
    templateUrl: './enquiriesExcelUpload.component.html',
    styleUrls: ['./enquiriesExcelUpload.component.scss'],
    animations: fuseAnimations
})
export class EnquiriesExcelUploadComponent {

    excelUploadForm: FormGroup;

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
                    this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                        'OK', { duration: 7000 });
                }
            );
        }
        else
        {
            this._matSnackBar.open('Please select a file to upload.', 'OK', { duration: 7000 });
        };
    }
}
