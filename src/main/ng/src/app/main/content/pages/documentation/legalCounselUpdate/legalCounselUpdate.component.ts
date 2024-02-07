import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { PartnerModel } from 'app/main/content/model/partner.model';
import { DocumentationService } from '../documentation.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';

@Component({
    selector: 'fuse-legal-counsel-update-dialog',
    templateUrl: './legalCounselUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LegalCounselUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Legal Counsel';

    selectedLegalCounsel: any;

    legalCounselForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    partners: PartnerModel[] = new Array();
    documentTypes = LoanMonitoringConstants.documentTypes;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _documentationService: DocumentationService,
        public _dialogRef: MatDialogRef<LegalCounselUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected legal counsel details from the dialog's data attribute.
        if (_dialogData.selectedLegalCounsel !== undefined) {
            this.selectedLegalCounsel = Object.assign({}, _dialogData.selectedLegalCounsel);
            if (_dialogData.operation === 'addLegalCounsel')
                this.dialogTitle = 'Add Legal Counsel';
            else
                this.dialogTitle = 'Modify Legal Counsel';
        }
        else {
            this.selectedLegalCounsel = {};
        }

        // Fetch busines partners
        _documentationService.getPartnersByRoleType("ZLM006").subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })

        // Sort document types array
        this.documentTypes = this.documentTypes.sort((obj1, obj2) => {
            return obj1.value.localeCompare(obj2.value);
        });
    
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.legalCounselForm = this._formBuilder.group({
            bpCode: [this.selectedLegalCounsel.bpCode],
            name: [this.selectedLegalCounsel.name || ''],
            startDate: [this.selectedLegalCounsel.startDate || ''],
            endDate: [this.selectedLegalCounsel.endDate],
            remarks: [this.selectedLegalCounsel.remarks],
            documentName: [this.selectedLegalCounsel.documentName || ''],
            documentType: [this.selectedLegalCounsel.documentType || ''],
            file: ['']
        }); 
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.legalCounselForm.get('file').setValue(file);
        }
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.legalCounselForm.valid) {
            if (this.legalCounselForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.legalCounselForm.get('file').value);      
                this._documentationService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveLegalCounsel(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addLegalCounsel') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveLegalCounsel('');
                }
            }
        }
    }

    /**
     * saveLegalCounsel()
     */
    saveLegalCounsel(fileReference: string): void {
        if (this.legalCounselForm.valid) {
            // To solve the utc time zone issue
            var legalCounsel = this.legalCounselForm.value;
            var dt = new Date(legalCounsel.startDate);
            legalCounsel.startDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(legalCounsel.endDate);
            legalCounsel.endDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLegalCounsel') {
                legalCounsel.fileReference = fileReference;
                legalCounsel.loanApplicationId = this._dialogData.loanApplicationId;
                this._documentationService.createLegalCounsel(legalCounsel).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedLegalCounsel.fileReference = fileReference;
                }    
                this.selectedLegalCounsel.bpCode  = legalCounsel.bpCode;
                this.selectedLegalCounsel.name = legalCounsel.name;
                this.selectedLegalCounsel.startDate = legalCounsel.startDate;
                this.selectedLegalCounsel.endDate = legalCounsel.endDate;
                this.selectedLegalCounsel.remarks = legalCounsel.remarks;
                this.selectedLegalCounsel.documentName = legalCounsel.documentName;
                this.selectedLegalCounsel.documentType = legalCounsel.documentType;

                this._documentationService.updateLegalCounsel(this.selectedLegalCounsel).subscribe(() => {
                    this._matSnackBar.open('Legal Counsel updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * onPartnerSelect()
     * @param event 
     */
    onPartnerSelect(partner: PartnerModel): void {
        this.legalCounselForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
    }

    /**
     * getPartyNumberAndName()
     * @param party 
     */
    getPartyNumberAndName(party: any): string {
        return party.partyNumber + " - " + party.partyName1 + ' ' + party.partyName2;
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }
}
