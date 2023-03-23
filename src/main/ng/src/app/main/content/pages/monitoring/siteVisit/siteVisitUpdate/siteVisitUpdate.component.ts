import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { SiteVisitModel } from 'app/main/content/model/siteVisit.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { LoanAppraisalService } from '../../../appraisal/loanAppraisal.service';

@Component({
    selector: 'fuse-site-visit-update-dialog',
    templateUrl: './siteVisitUpdate.component.html',
    styleUrls: ['./siteVisitUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SiteVisitUpdateDialogComponent {

    dialogTitle = 'Add New Site Visit Details';

    documentTypes = LoanMonitoringConstants.siteVisitDocumentTypes;
    selectedSiteVisit: SiteVisitModel ;
    siteVisitUpdateForm: FormGroup;
    siteVisitTypes = LoanMonitoringConstants.siteVisitTypes;

    partners = new Array<any>();

    /**
     * constructor()
     * @param _formBuilder
     * @param _loanMonitoringService
     * @param _dialogRef
     * @param _dialogData
     * @param _matSnackBar
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<SiteVisitUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar, private _loanAppraisalService: LoanAppraisalService) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedSiteVisit !== undefined) {
            this.selectedSiteVisit = Object.assign({}, _dialogData.selectedSiteVisit);
            this.dialogTitle = 'Modify Site Visit Details';
        }
        else {
            this.selectedSiteVisit = new SiteVisitModel({});
            if (_loanMonitoringService.loanContractExtension)
                this.selectedSiteVisit.actualCOD = _loanMonitoringService.loanContractExtension.actualCOD;
        }

        this.siteVisitUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedSiteVisit.serialNumber],
            siteVisitType: [this.selectedSiteVisit.siteVisitType],
            actualCOD: [this.selectedSiteVisit.actualCOD || ''],
            dateOfSiteVisit: [this.selectedSiteVisit.dateOfSiteVisit || ''],
            dateOfLendersMeet: [this.selectedSiteVisit.dateOfLendersMeet || ''],
            documentType: [this.selectedSiteVisit.documentType || ''],
            documentTitle: [this.selectedSiteVisit.documentTitle || ''],
            initialSCOD: [this.selectedSiteVisit.initialSCOD || ''],
            revisedSCOD1: [this.selectedSiteVisit.revisedSCOD1 || ''],
            revisedSCOD2: [this.selectedSiteVisit.revisedSCOD2 || ''],
            businessPartnerId: [ this.selectedSiteVisit.businessPartnerId || '' ],
            displayBusinessPartnerId: [ this.selectedSiteVisit.businessPartnerId || '' ],
            businessPartnerName: [ this.selectedSiteVisit.businessPartnerName || '' ],
            file: ['']
        });

        this._loanAppraisalService.getPartnersByRole('ZLM024').subscribe(response => {
            this.partners = response;
        });
        this._loanAppraisalService.getPartnersByRole('ZLM028').subscribe(response => {
            response.map((partner) => {
                this.partners.push(partner);
            });
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
                if (this._dialogData.operation === 'addSiteVisit') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveSiteVisitDetails('');
                }
            }
        }
    }

    /**
     * saveSiteVisitDetails()
     * @param fileReference
     */
    saveSiteVisitDetails(fileReference: string): void {
        var siteVisit: SiteVisitModel = new SiteVisitModel(this.siteVisitUpdateForm.value);
        var dt = new Date(siteVisit.actualCOD);
        siteVisit.actualCOD = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(siteVisit.dateOfSiteVisit);
        siteVisit.dateOfSiteVisit = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(siteVisit.dateOfLendersMeet);
        siteVisit.dateOfLendersMeet = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(siteVisit.initialSCOD);
        siteVisit.initialSCOD = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(siteVisit.revisedSCOD1);
        siteVisit.revisedSCOD1 = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        dt = new Date(siteVisit.revisedSCOD2);
        siteVisit.revisedSCOD2 = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        siteVisit.fileReference = fileReference;

        if (this._dialogData.operation === 'addSiteVisit') {
            this._loanMonitoringService.saveSiteVisit(siteVisit, 'monitoring', this._dialogData.loanApplicationId).subscribe(() => {
                this._matSnackBar.open('Site Visit details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedSiteVisit.businessPartnerId = siteVisit.businessPartnerId;
            this.selectedSiteVisit.businessPartnerName = siteVisit.businessPartnerName;
            this.selectedSiteVisit.serialNumber = siteVisit.serialNumber;
            this.selectedSiteVisit.siteVisitType = siteVisit.siteVisitType;
            this.selectedSiteVisit.actualCOD = siteVisit.actualCOD;
            this.selectedSiteVisit.dateOfLendersMeet  = siteVisit.dateOfLendersMeet;
            this.selectedSiteVisit.dateOfSiteVisit  = siteVisit.dateOfSiteVisit;
            this.selectedSiteVisit.documentType = siteVisit.documentType;
            this.selectedSiteVisit.documentTitle = siteVisit.documentTitle;
            this.selectedSiteVisit.initialSCOD = siteVisit.initialSCOD;
            this.selectedSiteVisit.revisedSCOD1 = siteVisit.revisedSCOD1;
            this.selectedSiteVisit.revisedSCOD2 = siteVisit.revisedSCOD2;
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

    /**
     * onPartnerSelect()
     */
    onPartnerSelect(partner: any): void {
        console.log("onPartnerSelect", partner);
        this.siteVisitUpdateForm.controls.displayBusinessPartnerId.setValue(partner.partyNumber);
        this.siteVisitUpdateForm.controls.businessPartnerName.setValue(partner.partyName1 + ' ' + partner.partyName2);
    }
}
