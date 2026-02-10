import { Component } from '@angular/core';
import { EnquiryUploadService } from './enquiry-upload.service';
import { 
    ButtonComponent, 
    LayoutGridModule, 
    MessageStripModule, 
    MessageStripAlertService, 
    TableModule,
    FD_DATETIME_FORMATS, 
    DATE_TIME_FORMATS, 
    FdDatetimeAdapter, 
    DatetimeAdapter, 
    DatePickerModule, 
    FormItemComponent,
    FormLabelComponent
} from '@fundamental-ngx/core';
import { ComponentNgxComponent } from '../../../common/component-ngx/component-ngx.component';
import { FileUploaderModule } from '@fundamental-ngx/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
    selector: 'app-enquiry-upload',
    standalone: true,
    templateUrl: './enquiry-upload.component.html',
    styleUrls: ['./enquiry-upload.component.scss'],
    imports: [
        ButtonComponent,
        CommonModule,
        ComponentNgxComponent,
        DatePickerModule,
        FileUploaderModule,
        FormsModule,
        FormItemComponent,
        FormLabelComponent,
        LayoutGridModule,
        MessageStripModule,
        ReactiveFormsModule,
        TableModule,
    ],
    providers: [
        {
            provide: DatetimeAdapter,
            useClass: FdDatetimeAdapter
        },
        {
            provide: DATE_TIME_FORMATS,
            useValue: FD_DATETIME_FORMATS
        }
    ],
})
export class EnquiryUploadComponent {

    displayedColumns = [
        'Comments', 'Serial Number', 'SAP Enquiry ID', 'Borrower Name', 'Group Name', 'Project Type', 'Type of Assistance', 'Proposal Type', 
            'Date of Lead Generation', 'Amount Requested', 'Borrower Requested ROI', 'ICC Readiness Status', 'Remarks on ICC Readiness', 
            'Presented in ICC', 'ICC Status', 'Reason for ICC Status', 'ICC Clearance Date', 'ICC Meeting Number', 'Amount Approved', 
            'ICC Approved ROI', 'Remarks for ICC Approval'
    ];
    downloadEnquiriesForm!: FormGroup;


    enquiries: any[] = [];
    
    files: File[] = [];

    selectedEnquiry: SelectionModel<any> = new SelectionModel<any>(false, []);

    /**
     * Constructor
     */
    constructor(
        private formBuilder: FormBuilder,
        public messageStripAlertService: MessageStripAlertService, 
        private enquiryUploadService: EnquiryUploadService) 
    {
        this.downloadEnquiriesForm = this.formBuilder.group({
            enquiryDateFrom: [null],
            enquiryDateTo: [null]
        });
    }

    /**
     * Handle file selection
     */
    handleFileSelection(files: File[]): void {
    }

    /**
     * Preview enquiries
     */
    previewEnquiries(): void {
        if (this.files.length > 0) {
            var formData = new FormData();
            formData.append('file', this.files[0], this.files[0].name);
            this.enquiryUploadService.uploadExcelDocument(formData).subscribe({
                next: (response) => {
                    this.enquiries = response;
                    this.files = [];
                },
                error: (error) => {
                    const err = error.error.message ? error.error.message : error.message;
                    this.displayAlertMessage(err + ' !! Unable to upload the file. Please try again after sometime or contact your'
                            + ' system administrator');
                }
            });
        }
        else {
            this.displayAlertMessage('Please select a file to upload !!');
        }
    }

    /**
     * View rejected reasons
     */
    viewRejectedReasons(comments: string): void {
        this.displayAlertMessage(comments);
    }

    /**
     * Download enquiries
     */
    downloadEnquiries(): void {
        let dateFrom: Date = new Date();
        let dateTo: Date = new Date();
        if (this.downloadEnquiriesForm.get('enquiryDateFrom')?.value?.isDateValid())
            dateFrom = new Date(Date.UTC(this.downloadEnquiriesForm.get('enquiryDateFrom')?.value.year, 
                this.downloadEnquiriesForm.get('enquiryDateFrom')?.value.month - 1, 
                this.downloadEnquiriesForm.get('enquiryDateFrom')?.value.day));
        if (this.downloadEnquiriesForm.get('enquiryDateTo')?.value?.isDateValid())
            dateTo = new Date(Date.UTC(this.downloadEnquiriesForm.get('enquiryDateTo')?.value.year, 
                this.downloadEnquiriesForm.get('enquiryDateTo')?.value.month - 1, 
                this.downloadEnquiriesForm.get('enquiryDateTo')?.value.day));
        this.displayAlertMessage('Enquiries file download is in progress.');
        this.enquiryUploadService.downloadEnquiries(dateFrom, dateTo);
    }

    /**
     * Create enquiries
     */
    createEnquiries(): void {
        this.enquiryUploadService.createExcelEnquiries().subscribe(
            (response: any) => {
                this.enquiries = response.enquiries;
                if (response.savedCount > 0)
                    this.displayAlertMessage(response.savedCount + ' new enquirie(s) uploaded successfully.');
                else
                    this.displayAlertMessage('No new enquiries uploaded.');
            },
            (error: HttpErrorResponse) => {
                this.displayAlertMessage(error.error.message);
            }
        );
    }

    /**
     * Display alert message
     */
    displayAlertMessage(message: string): void {
        this.messageStripAlertService.open({
            content: message,
            position: 'bottom-middle',
            closeOnNavigation: true,
            messageStrip: {
                duration: 7000,
                mousePersist: true,
                type: 'error',
                dismissible: true
            }
        });
    }    
}
