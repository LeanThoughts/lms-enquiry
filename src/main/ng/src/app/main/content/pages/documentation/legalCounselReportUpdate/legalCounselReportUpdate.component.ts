import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { DocumentationService } from '../documentation.service';
import { LoanMonitoringService } from '../../monitoring/loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-legal-counsel-report-update-dialog',
    templateUrl: './legalCounselReportUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LegalCounselReportUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Legal Counsel Report';

    selectedLegalCounselId: any;
    selectedLegalCounselReport: any;

    legalCounselReportForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _documentationService: DocumentationService,
        public _dialogRef: MatDialogRef<LegalCounselReportUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanMonitoringService: LoanMonitoringService) {

        // Fetch selected legal counsel details from the dialog's data attribute.
        this.selectedLegalCounselId = _dialogData.selectedLegalCounselId;
        if (_dialogData.operation === 'updateLegalCounselReport') {
            console.log('in update');
            this.selectedLegalCounselReport = Object.assign({}, _dialogData.selectedLegalCounselReport);
            this.dialogTitle = 'Modify Legal Counsel Report';
        }
        else {
            console.log('in create');
            this.selectedLegalCounselId = _dialogData.selectedLegalCounselId;
            this.selectedLegalCounselReport = {};
        }

        // Sort document types array
        this.documentTypes = this.documentTypes.sort((obj1, obj2) => {
            return obj1.value.localeCompare(obj2.value);
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.legalCounselReportForm = this._formBuilder.group({
            submissionDate: [this.selectedLegalCounselReport.submissionDate || ''],
            fiscalYear: [this.selectedLegalCounselReport.name || ''],
            period: [this.selectedLegalCounselReport.startDate || ''],
            remarks: [this.selectedLegalCounselReport.remarks],
            documentName: [this.selectedLegalCounselReport.documentName || ''],
            documentType: [this.selectedLegalCounselReport.documentType || ''],
            file: ['']
        }); 
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.legalCounselReportForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.legalCounselReportForm.valid) {
            if (this.legalCounselReportForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.legalCounselReportForm.get('file').value);      
                this._documentationService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLegalCounselReport(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addLegalCounselReport') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveLegalCounselReport('');
                }
            }
        }
    }

    /**
     * saveLegalCounselReport()
     */
    saveLegalCounselReport(fileReference: string): void {
        if (this.legalCounselReportForm.valid) {
            // To solve the utc time zone issue
            var legalCounselReport = this.legalCounselReportForm.value;
            var dt = new Date(legalCounselReport.submissionDate);
            legalCounselReport.submissionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            console.log(this._dialogData.operation);
            console.log(this.selectedLegalCounselId);
            if (this._dialogData.operation === 'addLegalCounselReport') {
                legalCounselReport.fileReference = fileReference;
                legalCounselReport.legalCounselId = this.selectedLegalCounselId;
                this._documentationService.createLegalCounselReport(legalCounselReport).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel Report added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedLegalCounselReport.fileReference = fileReference;
                }
                this.selectedLegalCounselReport.submissionDate  = legalCounselReport.submissionDate;
                this.selectedLegalCounselReport.fiscalYear = legalCounselReport.fiscalYear;
                this.selectedLegalCounselReport.period = legalCounselReport.period;
                this.selectedLegalCounselReport.remarks = legalCounselReport.remarks;
                this.selectedLegalCounselReport.documentName = legalCounselReport.documentName;
                this.selectedLegalCounselReport.documentType = legalCounselReport.documentType;

                this._documentationService.updateLegalCounselReport(this.selectedLegalCounselReport).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel Report updated successfully.', 'OK', { duration: 7000 });
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
