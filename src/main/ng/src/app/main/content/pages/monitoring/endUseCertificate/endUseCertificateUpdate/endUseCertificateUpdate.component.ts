import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-end-use-certificate-update-dialog',
    templateUrl: './endUseCertificateUpdate.component.html',
    styleUrls: ['./endUseCertificateUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class EndUseCertificateUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add End Use Certificate Details';

    selectedEndUseCertificate: any;

    endUseCertificateUpdateForm: FormGroup;

    allowUpdates = true;

    documentTypes = LoanMonitoringConstants.documentTypes;
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<EndUseCertificateUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedEndUseCertificate = Object.assign({}, _dialogData.selectedEndUseCertificate);

        if (_dialogData.selectedEndUseCertificate !== undefined) {
            if (_dialogData.operation === 'updateEndUseCertificate') {
                this.dialogTitle = 'Modify End Use Certificate Details';
            }
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.endUseCertificateUpdateForm = this._formBuilder.group({
            endUseCertificateDate: [this.selectedEndUseCertificate.endUseCertificateDate || ''],
            eventDate: [this.selectedEndUseCertificate.eventDate || ''],
            endUseCertificateDueDate: [this.selectedEndUseCertificate.endUseCertificateDueDate || ''],
            documentType: [this.selectedEndUseCertificate.documentType || ''],
            documentTitle: [this.selectedEndUseCertificate.documentTitle || ''],
            remarks: [this.selectedEndUseCertificate.remarks || ''],
            file: ['']
        }); 
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.endUseCertificateUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.endUseCertificateUpdateForm.valid) {
            if (this.endUseCertificateUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.endUseCertificateUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveEndUseCertificateDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addEndUseCertificate') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveEndUseCertificateDetails('');
                }
            }
        }
    }

    saveEndUseCertificateDetails(fileReference: string) {
        if (this.endUseCertificateUpdateForm.valid) {

            // To solve the utc time zone issue
            var endUseCertificate = this.endUseCertificateUpdateForm.value;
            var dt = new Date(endUseCertificate.eventDate);
            endUseCertificate.eventDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(endUseCertificate.endUseCertificateDate);
            endUseCertificate.endUseCertificateDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(endUseCertificate.endUseCertificateDueDate);
            endUseCertificate.endUseCertificateDueDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addEndUseCertificate') {
                endUseCertificate.fileReference = fileReference;
                this._loanMonitoringService.saveEndUseCertificate(endUseCertificate, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('End Use Certificate details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedEndUseCertificate.fileReference = fileReference;
                }
                this.selectedEndUseCertificate.endUseCertificateDate  = endUseCertificate.endUseCertificateDate;
                this.selectedEndUseCertificate.eventDate = endUseCertificate.eventDate;
                this.selectedEndUseCertificate.endUseCertificateDueDate = endUseCertificate.endUseCertificateDueDate;
                this.selectedEndUseCertificate.documentTitle = endUseCertificate.documentTitle;
                this.selectedEndUseCertificate.documentType = endUseCertificate.documentType;
                this.selectedEndUseCertificate.remarks = endUseCertificate.remarks;
                this._loanMonitoringService.updateEndUseCertificate(this.selectedEndUseCertificate).subscribe(() => {
                    this._matSnackBar.open('End Use Certificate details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
