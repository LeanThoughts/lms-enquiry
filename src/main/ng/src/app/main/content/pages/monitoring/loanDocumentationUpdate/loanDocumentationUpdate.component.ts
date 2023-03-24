import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
 import { LoanMonitoringService } from '../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import {LoanDocumentationModel} from '../../../model/loanDocumentation.model';
import {DocumentationTypeModel} from '../../../model/documentationType.model';
import {DocumentationStatusModel} from '../../../model/documentationStatus.model';

@Component({
    selector: 'fuse-loan-documentation-dialog',
    templateUrl: './loanDocumentationUpdate.component.html',
    styleUrls: ['./loanDocumentationUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LoanDocumentationUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Loan Documentation';

    selectedLoanDocumentation: LoanDocumentationModel;

    loanDocumentationUpdateForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    documentationTypes: DocumentationTypeModel[] = new Array();
    documentationStatuses: DocumentationStatusModel[] = new Array();
    documentTypes = LoanMonitoringConstants.documentTypes;




  allowUpdates: boolean = true;

    /**
     * constructor()
     * @param _formBuilder
     * @param _loanMonitoringService
     * @param _dialogRef
     * @param _dialogData
     * @param _matSnackBar
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<LoanDocumentationUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedLoanDocumentation !== undefined) {
            this.selectedLoanDocumentation = Object.assign({}, _dialogData.selectedLoanDocumentation);
            if (_dialogData.operation === 'displayLoanDocumentation') {
                this.dialogTitle = 'View Loan Documentation Details';
                this.allowUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify Loan Documentation';
            }
        }
        else {
            this.selectedLoanDocumentation = new LoanDocumentationModel({});
        }

        _loanMonitoringService.getDocumentationTypes().subscribe(response => {
            response.forEach(element => {
                this.documentationTypes.push(new DocumentationTypeModel(element));
            });
        });
      _loanMonitoringService.getDocumentationStatuses().subscribe(response => {
        response.forEach(element => {
          this.documentationStatuses.push(new DocumentationStatusModel(element));
        });
      });

      // Sort document types array
      this.documentTypes = this.documentTypes.sort((obj1, obj2) => {
        return obj1.value.localeCompare(obj2.value);
      });

      this.documentationTypes = LoanMonitoringConstants.documentationTypes;
      this.documentationStatuses = LoanMonitoringConstants.documentationStatuses;
    }

  /**
   * onFileSelect()
   * @param event
   */
  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.loanDocumentationUpdateForm.get('file').setValue(file);
    }
  }
    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.loanDocumentationUpdateForm = this._formBuilder.group({
          serialNumber: [this.selectedLoanDocumentation.serialNumber],
          documentationTypeCode: [this.selectedLoanDocumentation.documentationTypeCode || ''],
          documentationTypeDescription: [this.selectedLoanDocumentation.documentationTypeDescription || ''],
          executionDate: [this.selectedLoanDocumentation.executionDate],
          approvalDate: [this.selectedLoanDocumentation.approvalDate],
          loanDocumentationStatusCode: [this.selectedLoanDocumentation.loanDocumentationStatusCode || ''],
          loanDocumentationStatusCodeDescription: [this.selectedLoanDocumentation.loanDocumentationStatusCodeDescription || ''],
          documentType: [this.selectedLoanDocumentation.documentType || ''],
          documentTitle: [this.selectedLoanDocumentation.documentTitle || ''],
          fileReference: [this.selectedLoanDocumentation.fileReference || ''],
          remarks: [this.selectedLoanDocumentation.remarks || '' ]
        });
    }



    /**
     * submit()
     */
    submit(): void {
        if (this.loanDocumentationUpdateForm.valid) {
            // To solve the utc time zone issue
            var loanDocumenation: LoanDocumentationModel = new LoanDocumentationModel(this.loanDocumentationUpdateForm.value);
            var dt = new Date(loanDocumenation.executionDate);

            loanDocumenation.executionDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(loanDocumenation.executionDate);
            loanDocumenation.approvalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(loanDocumenation.approvalDate);
            loanDocumenation.approvalDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLoanDocumentation') {
                this._loanMonitoringService.saveLoanDocumentation(loanDocumenation, this._dialogData.loanApplicationId, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('Loan documentation added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedLoanDocumentation.serialNumber  = loanDocumenation.serialNumber;
                this.selectedLoanDocumentation.documentationTypeCode = loanDocumenation.documentationTypeCode;
                this.selectedLoanDocumentation.documentationTypeDescription = loanDocumenation.documentationTypeDescription;
                this.selectedLoanDocumentation.executionDate = loanDocumenation.executionDate;
                this.selectedLoanDocumentation.approvalDate = loanDocumenation.approvalDate;
                this.selectedLoanDocumentation.loanDocumentationStatusCode = loanDocumenation.loanDocumentationStatusCode;
                this.selectedLoanDocumentation.loanDocumentationStatusCodeDescription = loanDocumenation.loanDocumentationStatusCodeDescription;
                this.selectedLoanDocumentation.documentType = loanDocumenation.documentTitle;
                this.selectedLoanDocumentation.fileReference = loanDocumenation.fileReference;
                this.selectedLoanDocumentation.documentTitle = loanDocumenation.documentTitle;
                this.selectedLoanDocumentation.remarks = loanDocumenation.remarks;

                this._loanMonitoringService.updateLoanDocumentation(this.selectedLoanDocumentation, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('Loan documentation updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
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
     * toDate()
     * @param d
     */
    protected toDate(d: Date | string): Date {
        return (typeof d === 'string') ? new Date(d) : d;
    }
}
