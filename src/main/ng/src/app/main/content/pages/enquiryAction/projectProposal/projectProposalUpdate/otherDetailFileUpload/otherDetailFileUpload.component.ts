import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { documentTypes } from '../../../enquiryAction.constants';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-other-details-file-upload',
  templateUrl: './otherDetailFileUpload.component.html',
  styleUrls: ['./otherDetailFileUpload.component.scss']
})
export class OtherDetailsFileUploadComponent {

    dialogTitle = "Document Upload";

    otherDetailsDocumentForm: FormGroup;

    selectedDocument: any;
    projectProposal: any;

    documentTypes = documentTypes;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(private _formBuilder: FormBuilder, 
                private _enquiryActionService: EnquiryActionService,
                public _dialogRef: MatDialogRef<OtherDetailsFileUploadComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected document details and project proposal from the dialog's data attribute
        this.selectedDocument = Object.assign({}, _dialogData.selectedDocument);
        this.projectProposal = Object.assign({}, _dialogData.projectProposal);
        console.log('_dialogData', _dialogData);
        console.log('selectedDocument', this.selectedDocument);
        console.log('projectProposal', this.projectProposal);
        this.otherDetailsDocumentForm = this._formBuilder.group({
            documentType: [ this.selectedDocument.documentType || '' ],
            documentName: [ this.selectedDocument.documentName || '' ],
            file: [''],
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.otherDetailsDocumentForm.valid) {

            var otherDetailsDocument = this.otherDetailsDocumentForm.value;

            if (this._dialogData.operation === 'addDocument') {
                if (this.otherDetailsDocumentForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.otherDetailsDocumentForm.get('file').value);      
                    this._enquiryActionService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            otherDetailsDocument.fileReference = response.fileReference;
                            otherDetailsDocument.projectProposalId = this.projectProposal.id;
                            console.log('payload', otherDetailsDocument);
                            this._enquiryActionService.createOtherDetailsDocument(otherDetailsDocument).subscribe(() => {
                                this._matSnackBar.open('Document added successfully.', 'OK', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this._matSnackBar.open('Please select a document to upload', 'OK', { duration: 7000 });                
                }
            }
            else {
                if (this.otherDetailsDocumentForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.otherDetailsDocumentForm.get('file').value);      
                    this._enquiryActionService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            this.selectedDocument.documentName = otherDetailsDocument.documentName;
                            this.selectedDocument.documentType = otherDetailsDocument.documentType;
                            this.selectedDocument.fileReference = response.fileReference;
                            this._enquiryActionService.updateOtherDetailsDocument(this.selectedDocument).subscribe(() => {
                                this._matSnackBar.open('Document updated uccessfully.', 'OK', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            });
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else
                {
                    this.selectedDocument.documentName = otherDetailsDocument.documentName;
                    this.selectedDocument.documentType = otherDetailsDocument.documentType;
                    this._enquiryActionService.updateOtherDetailsDocument(this.selectedDocument).subscribe(() => {
                        this._matSnackBar.open('Document details updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
        }
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.otherDetailsDocumentForm.get('file').setValue(file);
        }
    }
}
