import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SiteVisitModel } from 'app/main/content/model/siteVisit.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanMonitoringService } from '../../../monitoring/loanMonitoring.service';

@Component({
    selector: 'fuse-further-details-site-visit-update-dialog',
    templateUrl: './furtherDetailsSiteVisitUpdate.component.html',
    styleUrls: ['./furtherDetailsSiteVisitUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class FurtherDetailsSiteVisitUpdateComponent {

    dialogTitle = 'Add New Site Visit Details';

    documentTypes = LoanMonitoringConstants.siteVisitDocumentTypes;
    selectedSiteVisit: SiteVisitModel ;
    siteVisitUpdateForm: FormGroup;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanMonitoringService: LoanMonitoringService,
                public _dialogRef: MatDialogRef<FurtherDetailsSiteVisitUpdateComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedSiteVisit !== undefined) {
            this.selectedSiteVisit = Object.assign({}, _dialogData.selectedSiteVisit);
            this.dialogTitle = 'Modify Site Visit Details';
        }
        else {
            this.selectedSiteVisit = new SiteVisitModel({});
            this.selectedSiteVisit.siteVisitType = 'Site Visit';
        }

        this.siteVisitUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedSiteVisit.serialNumber],
            siteVisitType: [this.selectedSiteVisit.siteVisitType],
            dateOfSiteVisit: [this.selectedSiteVisit.dateOfSiteVisit || ''],
            documentTitle: [this.selectedSiteVisit.documentTitle || ''],
            fiscalYear: [''],
            file: ['']
        });
    }

    /**
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.siteVisitUpdateForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.siteVisitUpdateForm.valid) {
            if (this.siteVisitUpdateForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.siteVisitUpdateForm.get('file').value);      
                this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveSiteVisitDetails(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                this.saveSiteVisitDetails('');
            }
        }
    }

    /**
     * saveSiteVisitDetails()
     * @param fileReference 
     */
    saveSiteVisitDetails(fileReference: string): void {
        var siteVisit: SiteVisitModel = new SiteVisitModel(this.siteVisitUpdateForm.value);
        const dt = new Date(siteVisit.dateOfSiteVisit);
        siteVisit.dateOfSiteVisit = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        siteVisit.fileReference = fileReference;

        if (this._dialogData.operation === 'addSiteVisit') {
            this._loanMonitoringService.saveSiteVisit(siteVisit, this._dialogData.loanApplicationId).subscribe(() => {
                this._matSnackBar.open('Site Visit details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedSiteVisit.serialNumber = siteVisit.serialNumber;
            this.selectedSiteVisit.siteVisitType = siteVisit.siteVisitType;
            this.selectedSiteVisit.dateOfSiteVisit  = siteVisit.dateOfSiteVisit;
            this.selectedSiteVisit.documentTitle = siteVisit.documentTitle;
            // this.selectedSiteVisit.fiscalYear = siteVisit.fiscalYear;
            if (siteVisit.fileReference !== '') {
                this.selectedSiteVisit.fileReference = siteVisit.fileReference;
            }
            this._loanMonitoringService.updateSiteVisit(this.selectedSiteVisit).subscribe(() => {
                this._matSnackBar.open('Site Visit details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });            
        }
    }
       
    /**
     * getFileURL()
     * @param fileReference
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }    
}
