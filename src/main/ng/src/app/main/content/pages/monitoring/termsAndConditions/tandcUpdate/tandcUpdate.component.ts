import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { TandCModel } from 'app/main/content/model/tandc.model';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { forkJoin, Observable } from 'rxjs';

@Component({
    selector: 'fuse-tandc-update-dialog',
    templateUrl: './tandcUpdate.component.html',
    styleUrls: ['./tandcUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class TandCUpdateDialogComponent {

    dialogTitle = 'Add New T&C';

    selectedTandC: TandCModel;

    tandcUpdateForm: FormGroup;

    communications = LoanMonitoringConstants.communications;
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<TandCUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedTandC !== undefined) {
            this.selectedTandC = new TandCModel(_dialogData.selectedTandC);
            this.dialogTitle = 'Modify T&C';
        }
        else {
            this.selectedTandC = new TandCModel({});
        }

        this.tandcUpdateForm = _formBuilder.group({
            documentType: [this.selectedTandC.documentType || ''],
            leadBankerDocumentType: [this.selectedTandC.leadBankerDocumentType || ''],
            documentTitle: [this.selectedTandC.documentTitle || ''],
            leadBankerDocumentTitle: [this.selectedTandC.leadBankerDocumentTitle || ''],
            communication: [this.selectedTandC.communication || ''],
            remarks: [this.selectedTandC.remarks || ''],
            reasonsForAmendment: [this.selectedTandC.reasonsForAmendment || ''],
            borrowerRequestLetterDate: [this.selectedTandC.borrowerRequestLetterDate || ''],
            dateOfIssueOfAmendedSanctionLetter: [this.selectedTandC.dateOfIssueOfAmendedSanctionLetter || ''],
            file: [''],
            leadBankerFile: [''],
            amendedDocumentType: [this.selectedTandC.amendedDocumentType || ''],
            dateOfIssueOfAmendedDocument: [this.selectedTandC.dateOfIssueOfAmendedDocument || ''],
            amendedDocumentRemarks: [this.selectedTandC.amendedDocumentRemarks || ''],
            amendedDocumentTitle: [this.selectedTandC.amendedDocumentTitle || ''],
            amendedFile: [''],
            internalFile: [''],
            internalDocumentType: [this.selectedTandC.internalDocumentType || ''],
            dateOfInternalDocument: [this.selectedTandC.dateOfInternalDocument || ''],
            internalDocumentRemarks: [this.selectedTandC.internalDocumentRemarks || ''],
            internalDocumentTitle: [this.selectedTandC.internalDocumentTitle || ''],
        });
        
        // Sort document types array
        this.documentTypes = this.documentTypes.sort((doc1, doc2) => {
            return doc1.value.localeCompare(doc2.value);
        })
    }

    /**
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.tandcUpdateForm.get('file').setValue(file);
        }
    }

    onLeadBankerFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.tandcUpdateForm.get('leadBankerFile').setValue(file);
        }
    }

    /**
     * onAmendedFileSelect()
     * @param event 
     */
    onAmendedFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.tandcUpdateForm.get('amendedFile').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.tandcUpdateForm.valid) {
            forkJoin([
                this.uploadRegularFile(),
                this.uploadAmendedFile(),
                this.uploadLeadBankerFile(),
                this.uploadInternalFile()
            ]).subscribe(response => {
                var tandc: TandCModel = new TandCModel(this.tandcUpdateForm.value);

                var dt1 = new Date(tandc.borrowerRequestLetterDate);
                tandc.borrowerRequestLetterDate = new Date(Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate()));
                var dt2 = new Date(tandc.dateOfIssueOfAmendedSanctionLetter);
                tandc.dateOfIssueOfAmendedSanctionLetter = new Date(Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()));
                var dt3 = new Date(tandc.dateOfIssueOfAmendedDocument);
                tandc.dateOfIssueOfAmendedDocument = new Date(Date.UTC(dt3.getFullYear(), dt3.getMonth(), dt3.getDate()));

                if (response[0] !== {}) {
                    tandc.fileReference = response[0].fileReference;
                }
                if (response[1] !== {}) {
                    tandc.amendedDocumentFileReference = response[1].fileReference;
                }
                if (response[2] !== {}) {
                    tandc.leadBankerDocumentFileReference = response[2].fileReference;
                }
                if (response[3] !== {}) {
                    tandc.internalDocumentFileReference = response[2].fileReference;
                }

                if (this._dialogData.operation === 'addT&C') {
                    this._loanMonitoringService.saveTandC(tandc, this._dialogData.loanApplicationId).subscribe(() => {
                        this._matSnackBar.open('T&C added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
                else {
                    this.selectedTandC.leadBankerDocumentTitle = tandc.leadBankerDocumentTitle;
                    this.selectedTandC.leadBankerDocumentType = tandc.leadBankerDocumentType;
                    this.selectedTandC.documentType  = tandc.documentType;
                    this.selectedTandC.documentTitle  = tandc.documentTitle;
                    this.selectedTandC.communication  = tandc.communication;
                    this.selectedTandC.borrowerRequestLetterDate  = tandc.borrowerRequestLetterDate;
                    this.selectedTandC.dateOfIssueOfAmendedSanctionLetter  = tandc.dateOfIssueOfAmendedSanctionLetter;
                    this.selectedTandC.remarks = tandc.remarks;
                    this.selectedTandC.reasonsForAmendment = tandc.reasonsForAmendment;
                    this.selectedTandC.amendedDocumentType = tandc.amendedDocumentType;
                    this.selectedTandC.dateOfIssueOfAmendedDocument = tandc.dateOfIssueOfAmendedDocument;
                    this.selectedTandC.amendedDocumentTitle = tandc.amendedDocumentTitle;
                    this.selectedTandC.amendedDocumentRemarks = tandc.amendedDocumentRemarks;

                    this.selectedTandC.internalDocumentType = tandc.internalDocumentType;
                    this.selectedTandC.dateOfInternalDocument = tandc.dateOfInternalDocument;
                    this.selectedTandC.internalDocumentTitle = tandc.internalDocumentTitle;
                    this.selectedTandC.internalDocumentRemarks = tandc.internalDocumentRemarks;

                    if (tandc.internalDocumentFileReference !== undefined) {
                        this.selectedTandC.internalDocumentFileReference = tandc.internalDocumentFileReference;
                    }
                    if (tandc.leadBankerDocumentFileReference !== undefined) {
                        this.selectedTandC.leadBankerDocumentFileReference = tandc.leadBankerDocumentFileReference;
                    }
                    if (tandc.amendedDocumentFileReference !== undefined) {
                        this.selectedTandC.amendedDocumentFileReference = tandc.amendedDocumentFileReference;
                    }
                    if (tandc.fileReference !== undefined) {
                        this.selectedTandC.fileReference = tandc.fileReference;
                    }
                    this._loanMonitoringService.updateTandC(this.selectedTandC).subscribe(() => {
                        this._matSnackBar.open('T&C updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            });
        }
    }

    /**
     * uploadRegularFile()
     */
    uploadRegularFile(): Observable<any> {
        if (this.tandcUpdateForm.get('file').value === '') {
            return Observable.of({});
        }
        else {
            var formData = new FormData();
            formData.append('file', this.tandcUpdateForm.get('file').value);      
            return this._loanMonitoringService.uploadVaultDocument(formData);
        }
    }

    /**
     * uploadAmendedFile()
     */
    uploadAmendedFile(): Observable<any> {
        if (this.tandcUpdateForm.get('amendedFile').value === '') {
            return Observable.of({});
        }
        else {
            var formData = new FormData();
            formData.append('file', this.tandcUpdateForm.get('amendedFile').value);      
            return this._loanMonitoringService.uploadVaultDocument(formData);
        }
    }

    uploadLeadBankerFile(): Observable<any> {
        if (this.tandcUpdateForm.get('leadBankerFile').value === '') {
            return Observable.of({});
        }
        else {
            var formData = new FormData();
            formData.append('file', this.tandcUpdateForm.get('leadBankerFile').value);      
            return this._loanMonitoringService.uploadVaultDocument(formData);
        }
    }

    uploadInternalFile(): Observable<any> {
        if (this.tandcUpdateForm.get('internalFile').value === '') {
            return Observable.of({});
        }
        else {
            var formData = new FormData();
            formData.append('file', this.tandcUpdateForm.get('internalFile').value);      
            return this._loanMonitoringService.uploadVaultDocument(formData);
        }
    }

    /**
     * closeClick()
     */
    closeClick(): void {
        this._dialogRef.close({ 'refresh': false });
    }
    
    /**
     * getFileURL()
     * @param fileReference
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }    
}
