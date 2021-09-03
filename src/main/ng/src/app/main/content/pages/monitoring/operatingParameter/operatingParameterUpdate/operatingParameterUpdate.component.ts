import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { OperatingParameterModel } from 'app/main/content/model/operatingParameter';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';

@Component({
    selector: 'fuse-operating-parameter-update-dialog',
    templateUrl: './operatingParameterUpdate.component.html',
    styleUrls: ['./operatingParameterUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class OperatingParameterUpdateDialogComponent {

    dialogTitle = 'Add New Operating Parameter';

    selectedOperatingParameter: OperatingParameterModel ;

    operatingParameterUpdateForm: FormGroup;
  
    documentTypes = LoanMonitoringConstants.documentTypes;
    months = LoanMonitoringConstants.months;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<OperatingParameterUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected operating parameter details from the dialog's data attribute.
        if (_dialogData.selectedOperatingParameter !== undefined) {
            this.selectedOperatingParameter = Object.assign({}, _dialogData.selectedOperatingParameter);
            this.dialogTitle = 'Modify Operating Parameter Details';
        }
        else {
            this.selectedOperatingParameter = new OperatingParameterModel({});
        }

        this.operatingParameterUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedOperatingParameter.serialNumber],
            month: [this.selectedOperatingParameter.month],
            year: [this.selectedOperatingParameter.year, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            exportNetGeneration: [this.selectedOperatingParameter.exportNetGeneration, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            plfCufActual: [this.selectedOperatingParameter.plfCufActual, [Validators.pattern(MonitoringRegEx.holdingPercentage)]],
            applicableTariff: [this.selectedOperatingParameter.applicableTariff, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            revenue: [this.selectedOperatingParameter.revenue, [Validators.pattern(MonitoringRegEx.genericAmount)]],
            dateOfInvoice: [this.selectedOperatingParameter.dateOfInvoice || ''],
            dateOfPaymentReceipt: [this.selectedOperatingParameter.dateOfPaymentReceipt || ''],
            carbonDiOxideEmission: [this.selectedOperatingParameter.carbonDiOxideEmission, [Validators.pattern(MonitoringRegEx.environmentParameters)]],
            waterSaved: [this.selectedOperatingParameter.waterSaved, [Validators.pattern(MonitoringRegEx.environmentParameters)]],
            documentType: [this.selectedOperatingParameter.documentType],
            documentTitle: [this.selectedOperatingParameter.documentTitle],
            file: ['']
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.operatingParameterUpdateForm.valid) {
            var operatingParameter: OperatingParameterModel = new OperatingParameterModel(this.operatingParameterUpdateForm.value);
            var dt = new Date(operatingParameter.dateOfInvoice);
            operatingParameter.dateOfInvoice = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(operatingParameter.dateOfPaymentReceipt);
            operatingParameter.dateOfPaymentReceipt = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            if (this._dialogData.operation === 'addOperatingParameter') {
                if (this.operatingParameterUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.operatingParameterUpdateForm.get('file').value);
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            operatingParameter.fileReference = response.fileReference;
                            this._loanMonitoringService.saveOperatingParameter(operatingParameter, this._dialogData.loanApplicationId).subscribe(() => {
                                this._matSnackBar.open('Operating Parameter details added successfully.', 'OK', { duration: 7000 });
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
                    this._loanMonitoringService.saveOperatingParameter(operatingParameter, this._dialogData.loanApplicationId).subscribe(() => {
                        this._matSnackBar.open('Operating Parameter details added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
            }
            else {
                if (this.operatingParameterUpdateForm.get('file').value !== '') {
                    var formData = new FormData();
                    formData.append('file', this.operatingParameterUpdateForm.get('file').value);
                    this._loanMonitoringService.uploadVaultDocument(formData).subscribe(
                        (response) => {
                            operatingParameter.id = this.selectedOperatingParameter.id;
                            operatingParameter.fileReference = response.fileReference;
                            this._loanMonitoringService.updateOperatingParameter(operatingParameter).subscribe(() => {
                                this._matSnackBar.open('Operating Parameter details updated successfully.', 'OK', { duration: 7000 });
                                this._dialogRef.close({ 'refresh': true });
                            });            
                        },
                        (error) => {
                            this._matSnackBar.open('Unable to upload the file. Pls try again after sometime or contact your system administrator', 
                                'OK', { duration: 7000 });
                        }
                    );
                }
                else {
                    operatingParameter.id = this.selectedOperatingParameter.id;
                    operatingParameter.fileReference = this.selectedOperatingParameter.fileReference;
                    this._loanMonitoringService.updateOperatingParameter(operatingParameter).subscribe(() => {
                        this._matSnackBar.open('Operating Parameter details updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }         
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
     * onFileSelect()
     * @param event 
     */
    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.operatingParameterUpdateForm.get('file').setValue(file);
        }
    }
}
