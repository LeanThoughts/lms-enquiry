import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ICCApprovalService } from '../iccApproval.service';

@Component({
    selector: 'fuse-icc-further-detail-update-dialog',
    templateUrl: './iccFurtherDetailUpdate.component.html',
    styleUrls: ['./iccFurtherDetailUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ICCFurtherDetailUpdateDialogComponent {

    dialogTitle = 'Add New Further Details';

    selectedICCFurtherDetail: any ;

    iccFurtherDetailUpdateForm: FormGroup;

    /**
     * constructor()
     */
    constructor(_formBuilder: FormBuilder, private _iccApprovalService: ICCApprovalService,
        public _dialogRef: MatDialogRef<ICCFurtherDetailUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedICCFurtherDetail !== undefined) {
            this.selectedICCFurtherDetail = Object.assign({}, _dialogData.selectedICCFurtherDetail);
            this.dialogTitle = 'Modify Further Details';
        }
        else {
            this.selectedICCFurtherDetail = {};
        }

        this.iccFurtherDetailUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedICCFurtherDetail.serialNumber],
            iccMeetingNumber: [this.selectedICCFurtherDetail.iccMeetingNumber],
            iccMeetingDate: [this.selectedICCFurtherDetail.iccMeetingDate || ''],
            detailsRequired: [this.selectedICCFurtherDetail.detailsRequired || ''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.iccFurtherDetailUpdateForm.valid) {
            // if (this.iccFurtherDetailUpdateForm.get('file').value !== '') {
            //     var formData = new FormData();
            //     formData.append('file', this.iccFurtherDetailUpdateForm.get('file').value);
            //     this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
            //         (response) => {
            //             this.saveSiteVisitDetails(response.fileReference);
            //         },
            //         (error) => {
            //             this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator',
            //                 'OK', { duration: 7000 });
            //         }
            //     );
            // }
            // else {
            //     if (this._dialogData.operation === 'addSiteVisit') {
            //         this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
            //     }
            //     else {
            //         this.saveSiteVisitDetails('');
            //     }
            // }
        }
    }

    /**
     * saveICCFurtherDetails()
     */
    saveICCFurtherDetails(fileReference: string): void {
        // var siteVisit: SiteVisitModel = new SiteVisitModel(this.iccFurtherDetailUpdateForm.value);
        // var dt = new Date(siteVisit.actualCOD);
        // siteVisit.actualCOD = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        // dt = new Date(siteVisit.dateOfSiteVisit);
        // siteVisit.dateOfSiteVisit = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        // dt = new Date(siteVisit.dateOfLendersMeet);
        // siteVisit.dateOfLendersMeet = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        // dt = new Date(siteVisit.initialSCOD);
        // siteVisit.initialSCOD = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        // dt = new Date(siteVisit.revisedSCOD1);
        // siteVisit.revisedSCOD1 = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
        // dt = new Date(siteVisit.revisedSCOD2);
        // siteVisit.revisedSCOD2 = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

        // siteVisit.fileReference = fileReference;

        // if (this._dialogData.operation === 'addSiteVisit') {
        //     this._loanMonitoringService.saveSiteVisit(siteVisit, this.moduleName, this._dialogData.loanApplicationId).subscribe(() => {
        //         this._matSnackBar.open('Site Visit details added successfully.', 'OK', { duration: 7000 });
        //         this._dialogRef.close({ 'refresh': true });
        //     });
        // }
        // else {
        //     this.selectedICCFurtherDetail.businessPartnerId = siteVisit.businessPartnerId;
        //     this.selectedICCFurtherDetail.businessPartnerName = siteVisit.businessPartnerName;
        //     this.selectedICCFurtherDetail.serialNumber = siteVisit.serialNumber;
        //     this.selectedICCFurtherDetail.siteVisitType = siteVisit.siteVisitType;
        //     this.selectedICCFurtherDetail.actualCOD = siteVisit.actualCOD;
        //     this.selectedICCFurtherDetail.dateOfLendersMeet  = siteVisit.dateOfLendersMeet;
        //     this.selectedICCFurtherDetail.dateOfSiteVisit  = siteVisit.dateOfSiteVisit;
        //     this.selectedICCFurtherDetail.documentType = siteVisit.documentType;
        //     this.selectedICCFurtherDetail.documentTitle = siteVisit.documentTitle;
        //     this.selectedICCFurtherDetail.initialSCOD = siteVisit.initialSCOD;
        //     this.selectedICCFurtherDetail.revisedSCOD1 = siteVisit.revisedSCOD1;
        //     this.selectedICCFurtherDetail.revisedSCOD2 = siteVisit.revisedSCOD2;
        //     if (siteVisit.fileReference !== '') {
        //         this.selectedICCFurtherDetail.fileReference = siteVisit.fileReference;
        //     }
        //     this._loanMonitoringService.updateSiteVisit(this.selectedICCFurtherDetail, this.moduleName).subscribe(() => {
        //         this._matSnackBar.open('Site Visit details updated successfully.', 'OK', { duration: 7000 });
        //         this._dialogRef.close({ 'refresh': true });
        //     });
        // }
    }
}
