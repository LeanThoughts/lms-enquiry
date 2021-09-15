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
            documentTitle: [this.selectedTandC.documentTitle || ''],
            communication: [this.selectedTandC.communication || ''],
            remarks: [this.selectedTandC.remarks || ''],
            borrowerRequestLetterDate: [this.selectedTandC.borrowerRequestLetterDate || ''],
            dateOfIssueOfAmendedSanctionLetter: [this.selectedTandC.dateOfIssueOfAmendedSanctionLetter || ''],
            file: [''],
            amendedDocumentType: [this.selectedTandC.amendedDocumentType || ''],
            dateOfIssueOfAmendedDocument: [this.selectedTandC.dateOfIssueOfAmendedDocument || ''],
            amendedDocumentRemarks: [this.selectedTandC.amendedDocumentRemarks || ''],
            amendedDocumentTitle: [this.selectedTandC.amendedDocumentTitle || ''],
            amendedFile: ['']
        });
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
                this.uploadAmendedFile()
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

                if (this._dialogData.operation === 'addT&C') {
                    this._loanMonitoringService.saveTandC(tandc, this._dialogData.loanApplicationId).subscribe(() => {
                        this._matSnackBar.open('T&C added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
                else {
                    this.selectedTandC.documentType  = tandc.documentType;
                    this.selectedTandC.documentTitle  = tandc.documentTitle;
                    this.selectedTandC.communication  = tandc.communication;
                    this.selectedTandC.borrowerRequestLetterDate  = tandc.borrowerRequestLetterDate;
                    this.selectedTandC.dateOfIssueOfAmendedSanctionLetter  = tandc.dateOfIssueOfAmendedSanctionLetter;
                    this.selectedTandC.remarks = tandc.remarks;
                    if (tandc.fileReference !== undefined) {
                        this.selectedTandC.fileReference = tandc.fileReference;
                    }
                    this.selectedTandC.amendedDocumentType = tandc.amendedDocumentType;
                    this.selectedTandC.dateOfIssueOfAmendedDocument = tandc.dateOfIssueOfAmendedDocument;
                    this.selectedTandC.amendedDocumentTitle = tandc.amendedDocumentTitle;
                    this.selectedTandC.amendedDocumentRemarks = tandc.amendedDocumentRemarks;
                    if (tandc.amendedDocumentFileReference !== undefined) {
                        this.selectedTandC.amendedDocumentFileReference = tandc.amendedDocumentFileReference;
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
