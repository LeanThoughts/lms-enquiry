import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { SanctionService } from '../sanction.service';
import { BoardApprovalService } from '../../boardApproval/boardApproval.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-sanction-letter-update-dialog',
    templateUrl: './sanctionLetterUpdate.component.html',
    styleUrls: ['./sanctionLetterUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SanctionLetterUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Sanction Letter';

    loanApplicationId = '';
    selectedSanctionLetter: any;

    sanctionLetterForm: FormGroup;

    documentTypes = LoanMonitoringConstants.documentTypes;

    sanctionTypes = [];
    
    revisedSanctionAmountHidden = false;
    revisedInterestRateHidden = false;
    dateOfAmendmentHidden = false;

    originalSanctionAmountRequired = false;
    dateOfAmendmentRequired = false;
    revisedInterestRateRequired = false;
    revisedSanctionAmountRequired = false;

    originalSanctionAmountReadonly = false;
    revisedSanctionAmountReadonly = false;
    originalInterestRateReadonly = false;
    revisedInterestRateReadonly = false;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _sanctionService: SanctionService, private _boardApprovalService: BoardApprovalService,
        public _dialogRef: MatDialogRef<SanctionLetterUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        console.log('in constructor');
        // Fetch selected reason details from the dialog's data attribute.
        this.selectedSanctionLetter = Object.assign({}, _dialogData.selectedSanctionLetter);
        this.loanApplicationId = _dialogData.loanApplicationId;

        if (_dialogData.selectedSanctionLetter !== undefined) {
            if (_dialogData.operation === 'updateSanctionLetter') {
                this.dialogTitle = 'Modify Sanction Letter';
            }
        }
        
        this._sanctionService.getSanctionTypes().subscribe(data => {
            this.sanctionTypes = data._embedded.sanctionTypes;
        });

        this.sanctionLetterForm = this._formBuilder.group({
            serialNumber: [this.selectedSanctionLetter.serialNumber || ''],
            sanctionLetterIssueDate: [this.selectedSanctionLetter.sanctionLetterIssueDate || ''],
            borrowerRequestLetterDate: [this.selectedSanctionLetter.borrowerRequestLetterDate || ''],
            sanctionLetterAcceptanceDate: [this.selectedSanctionLetter.sanctionLetterAcceptanceDate || ''],
            documentType: [this.selectedSanctionLetter.documentType || ''],
            documentTitle: [this.selectedSanctionLetter.documentTitle || ''],
            fileReference: [this.selectedSanctionLetter.fileReference || ''],
            remarks: [this.selectedSanctionLetter.remarks || ''],
            file: [''],
            type: [this.selectedSanctionLetter.type || ''],
            dateOfAmendment: [this.selectedSanctionLetter.dateOfAmendment || ''],
            originalSanctionAmount: [this.selectedSanctionLetter.originalSanctionAmount || '', [Validators.pattern(MonitoringRegEx.sixteenCommaTwo)]],
            originalInterestRate: [this.selectedSanctionLetter.originalInterestRate || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)]],
            revisedSanctionAmount: [this.selectedSanctionLetter.revisedSanctionAmount || '', [Validators.pattern(MonitoringRegEx.sixteenCommaTwo)]],
            revisedInterestRate: [this.selectedSanctionLetter.revisedInterestRate || '', [Validators.pattern(MonitoringRegEx.fiveCommaTwo)]],
            sanctionLetterValidToDate: [this.selectedSanctionLetter.sanctionLetterValidToDate || '']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        // to trigger selection change event on type dropdown
        if (this._dialogData.selectedSanctionLetter !== undefined) {
            console.log('trying to set type');
            this.enableControls(null, this.sanctionLetterForm.controls['type'].value);
        }
    }

    /**
     * onFileSelect()
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.sanctionLetterForm.get('file').setValue(file);
        }
    }

    /**
     * getFileURL()
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }  

    /**
     * submit()
     */
    submit(): void {
        if (this.sanctionLetterForm.valid) {
            if (this.sanctionLetterForm.get('file').value !== '') {
                var formData = new FormData();
                formData.append('file', this.sanctionLetterForm.get('file').value);      
                this._sanctionService.uploadVaultDocument(formData).subscribe(
                    (response) => {
                        this.saveSanctionLetter(response.fileReference);
                    },
                    (error) => {
                        this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                            'OK', { duration: 7000 });
                    }
                );
            }
            else {
                if (this._dialogData.operation === 'addCLAReportAndFee') {
                    this._matSnackBar.open('Please select a file to upload', 'OK', { duration: 7000 });
                }
                else {
                    this.saveSanctionLetter('');
                }
            }
        }
    }

    saveSanctionLetter(fileReference: string): void {
        if (this.sanctionLetterForm.valid) {
            var sanctionLetter = this.sanctionLetterForm.value;
                
            // To solve the utc time zone issue
            var dt = new Date(sanctionLetter.sanctionLetterIssueDate);
            sanctionLetter.sanctionLetterIssueDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.borrowerRequestLetterDate);
            sanctionLetter.borrowerRequestLetterDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.sanctionLetterAcceptanceDate);
            sanctionLetter.sanctionLetterAcceptanceDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.dateOfAmendment);
            sanctionLetter.dateOfAmendment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(sanctionLetter.sanctionLetterValidToDate);
            sanctionLetter.sanctionLetterValidToDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addSanctionLetter') {
                sanctionLetter.loanApplicationId = this.loanApplicationId;
                sanctionLetter.fileReference = fileReference;
                this._sanctionService.createSanctionLetter(sanctionLetter).subscribe(() => {
                    this._matSnackBar.open('Sanction letter added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                if (fileReference !== '' ) {
                    this.selectedSanctionLetter.fileReference = fileReference;
                }
                this.selectedSanctionLetter.sanctionLetterIssueDate = sanctionLetter.sanctionLetterIssueDate;
                this.selectedSanctionLetter.borrowerRequestLetterDate = sanctionLetter.borrowerRequestLetterDate;
                this.selectedSanctionLetter.sanctionLetterAcceptanceDate = sanctionLetter.sanctionLetterAcceptanceDate;
                this.selectedSanctionLetter.documentType = sanctionLetter.documentType;
                this.selectedSanctionLetter.documentTitle = sanctionLetter.documentTitle;
                this.selectedSanctionLetter.fileReference = sanctionLetter.fileReference;
                this.selectedSanctionLetter.remarks = sanctionLetter.remarks;
                this.selectedSanctionLetter.type = sanctionLetter.type;
                this.selectedSanctionLetter.dateOfAmendment = sanctionLetter.dateOfAmendment;
                this.selectedSanctionLetter.originalSanctionAmount = sanctionLetter.originalSanctionAmount;
                this.selectedSanctionLetter.originalInterestRate = sanctionLetter.originalInterestRate;
                this.selectedSanctionLetter.revisedSanctionAmount = sanctionLetter.revisedSanctionAmount;
                this.selectedSanctionLetter.revisedInterestRate = sanctionLetter.revisedInterestRate;
                this.selectedSanctionLetter.sanctionLetterValidToDate = sanctionLetter.sanctionLetterValidToDate;
                this._sanctionService.updateSanctionLetter(this.selectedSanctionLetter).subscribe(() => {
                    this._matSnackBar.open('Sanction letter updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * enableControls()
     */
    enableControls(event: any, value?:string): void {
        console.log('in enable controls');
        console.log('value is', value, 'test');
        // set defaults
        this.revisedSanctionAmountHidden = false;
        this.revisedInterestRateHidden = false;
        this.dateOfAmendmentHidden = false;
        this.originalSanctionAmountRequired = false;
        this.dateOfAmendmentRequired = false;
        this.revisedInterestRateRequired = false;
        this.revisedSanctionAmountRequired = false;
        this.originalSanctionAmountReadonly = false;
        this.revisedSanctionAmountReadonly = false;
        this.originalInterestRateReadonly = false;
        this.revisedInterestRateReadonly = false;
    
        if ((event !== null && event.value === ' ') || value === ' ') {
            this.revisedSanctionAmountHidden = true;
            this.revisedInterestRateHidden = true;
            this.originalSanctionAmountRequired = true;
            this.dateOfAmendmentHidden = true;
        }
        else if ((event !== null && event.value === '1') || value === '1') {
            this.revisedSanctionAmountRequired = true;
            this.revisedInterestRateHidden = true;
            this.originalSanctionAmountReadonly = true;
            this.dateOfAmendmentRequired = true;
        }
        else if ((event !== null && event.value === '2') || value === '2') {
            this.originalSanctionAmountReadonly = true;
            this.revisedSanctionAmountReadonly = true;
            this.originalInterestRateReadonly = true;
            this.revisedInterestRateReadonly = true;
            this.dateOfAmendmentRequired = true;
        }
        else if ((event !== null && event.value === '5') || value === '5') {
            this.originalSanctionAmountReadonly = true;
            this.revisedSanctionAmountReadonly = true;
            this.originalInterestRateReadonly = true;
            this.revisedInterestRateRequired = true;
            this.dateOfAmendmentRequired = true;
        }
    }
}
