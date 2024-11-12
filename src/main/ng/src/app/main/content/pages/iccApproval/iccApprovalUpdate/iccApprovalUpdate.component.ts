import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-icc-approval-update-dialog',
    templateUrl: './iccApprovalUpdate.component.html',
    styleUrls: ['./iccApprovalUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCApprovalUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add ICC Approval Details';

    loanApplicationId = '';
    selectedICCApproval: any;

    iccApprovalForm: FormGroup;

    today = new Date();

    enquiryCompletion: any;
    
    fileReference1: string = '';
    fileReference2: string = '';

    documentTypes: any[] = [];

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCApprovalUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanEnquiryService: LoanEnquiryService) {

        this.documentTypes = this._loanEnquiryService.documentTypes;

        // Fetch selected reason details from the dialog's data attribute.
        this.selectedICCApproval = Object.assign({}, _dialogData.selectedICCApproval);
        this.loanApplicationId = _dialogData.loanApplicationId;
        this.enquiryCompletion = _dialogData.enquiryCompletion;

        if (this.selectedICCApproval.id !== undefined) {
            this.dialogTitle = 'Modify ICC Approval Details';
        }

        this.iccApprovalForm = this._formBuilder.group({
            meetingNumber: [this.selectedICCApproval.meetingNumber],
            meetingDate: [this.selectedICCApproval.meetingDate || ''],
            remarks: [this.selectedICCApproval.remarks || ''],
            edApprovalDate: [this.selectedICCApproval.edApprovalDate || ''],
            cfoApprovalDate: [this.selectedICCApproval.cfoApprovalDate || ''],
            documentTypeMinutes: [this.selectedICCApproval.documentTypeMinutes || ''],
            documentTypeMailFromCS: [this.selectedICCApproval.documentTypeMailFromCS || ''],
            file1: [''],
            file2: ['']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * onFile1Select()
     */
    onFile1Select(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.iccApprovalForm.get('file1').setValue(file);
        }
    }

    /**
     * onFile2Select()
     */
    onFile2Select(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.iccApprovalForm.get('file2').setValue(file);
        }
    }

    /**
     * submit()
     */
    async submit(): Promise<void> {
        if (this.iccApprovalForm.valid) {

            if (this.iccApprovalForm.get('file1').value !== '') {
                var formData = new FormData();
                formData.append('file', this.iccApprovalForm.get('file1').value);
                const response1 = await this.uploadDocument1(formData);
                console.log('received response from uploadDocument1');
                // this.selectedICCApproval.fileReference1 = response1.fileReference;
            }

            if (this.iccApprovalForm.get('file2').value !== '') {
                var formData = new FormData();
                formData.append('file', this.iccApprovalForm.get('file2').value);
                const response2 = await this.uploadDocument2(formData);
                console.log('received response from uploadDocument2');
                // this.selectedICCApproval.fileReference2 = response2.fileReference;
            }

            console.log('completing rest of the submission');

            var iccApproval = this.iccApprovalForm.value;
            iccApproval.fileReference1 = this.fileReference1;
            iccApproval.fileReference2 = this.fileReference2;

            // To solve the utc time zone issue
            var dt = new Date(iccApproval.meetingDate);
            iccApproval.meetingDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            // Convert edApprovalDate to UTC
            if (iccApproval.edApprovalDate) {
                dt = new Date(iccApproval.edApprovalDate);
                iccApproval.edApprovalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            // Convert cfoApprovalDate to UTC
            if (iccApproval.cfoApprovalDate) {
                dt = new Date(iccApproval.cfoApprovalDate);
                iccApproval.cfoApprovalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            }

            if (this.selectedICCApproval.id === undefined) {
                iccApproval.loanApplicationId = this.loanApplicationId;
                this._iccApprovalService.createApprovalByICC(iccApproval).subscribe(() => {
                    this._iccApprovalService.getICCApproval(this.loanApplicationId).subscribe(data => {
                        this._iccApprovalService._iccApproval.next(data);
                        this._matSnackBar.open('ICC Approval details created successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                });
            }
            else {
                this.selectedICCApproval.meetingDate = iccApproval.meetingDate;
                this.selectedICCApproval.meetingNumber = iccApproval.meetingNumber;
                this.selectedICCApproval.remarks = iccApproval.remarks;
                this.selectedICCApproval.edApprovalDate = iccApproval.edApprovalDate;
                this.selectedICCApproval.cfoApprovalDate = iccApproval.cfoApprovalDate;
                this.selectedICCApproval.documentTypeMinutes = iccApproval.documentTypeMinutes;
                this.selectedICCApproval.documentTypeMailFromCS = iccApproval.documentTypeMailFromCS;
                if (this.fileReference1 !== '') {
                    this.selectedICCApproval.fileReference1 = this.fileReference1;
                }
                if (this.fileReference2 !== '') {
                    this.selectedICCApproval.fileReference2 = this.fileReference2;
                }
                this._iccApprovalService.updateApprovalByICC(this.selectedICCApproval).subscribe(() => {
                    this._matSnackBar.open('ICC Approval details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * uploadDocument1()
     */
    async uploadDocument1(file: FormData): Promise<any> {
        console.log('uploading document 1');
        let httpData = await this._iccApprovalService.uploadVaultDocument(file).toPromise();
        this.fileReference1 = httpData.fileReference;
    }

    /**
     * uploadDocument2()
     */
    async uploadDocument2(file: FormData): Promise<any> {
        console.log('uploading document 2');
        let httpData = await this._iccApprovalService.uploadVaultDocument(file).toPromise();
        this.fileReference2 = httpData.fileReference;
    }
}
