import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { OperatingParameterPLFModel } from 'app/main/content/model/operatingParameterPLF';

@Component({
    selector: 'fuse-operating-parameter-plf-update-dialog',
    templateUrl: './operatingParameterPLFUpdate.component.html',
    styleUrls: ['./operatingParameterPLFUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class OperatingParameterPLFUpdateDialogComponent {

    dialogTitle = 'Add New Operating Parameter PLF';

    selectedOperatingParameterPLF: OperatingParameterPLFModel;

    operatingParameterPLFUpdateForm: FormGroup;
  
    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<OperatingParameterPLFUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected operating parameter details from the dialog's data attribute.
        if (_dialogData.selectedOperatingParameterPLF !== undefined) {
            this.selectedOperatingParameterPLF = Object.assign({}, _dialogData.selectedOperatingParameterPLF);
            this.dialogTitle = 'Modify Operating Parameter PLF';
        }
        else {
            this.selectedOperatingParameterPLF = new OperatingParameterPLFModel({});
        }

        this.operatingParameterPLFUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedOperatingParameterPLF.serialNumber],
            year: [this.selectedOperatingParameterPLF.year, [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            miUnGenerated: [this.selectedOperatingParameterPLF.miUnGenerated, [Validators.pattern(MonitoringRegEx.environmentParameters)]],
            remarks: [this.selectedOperatingParameterPLF.remarks],
            actualYearlyAveragePlfCuf: [this.selectedOperatingParameterPLF.actualYearlyAveragePlfCuf, 
                [Validators.pattern(MonitoringRegEx.environmentParameters)]],
            designPlfCuf: [this.selectedOperatingParameterPLF.designPlfCuf]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.operatingParameterPLFUpdateForm.valid) {
            console.log(this.operatingParameterPLFUpdateForm.value);
            var operatingParameterPLF: OperatingParameterPLFModel = new OperatingParameterPLFModel(this.operatingParameterPLFUpdateForm.value);
            console.log(operatingParameterPLF);
            operatingParameterPLF.miUnGenerated = +operatingParameterPLF.miUnGenerated; // convert from string to number using unary operator
            console.log(operatingParameterPLF);
            if (this._dialogData.operation === 'addOperatingParameterPLF') {
                this._loanMonitoringService.saveOperatingParameterPLF(operatingParameterPLF, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('Operating Parameter PLF details added successfully.', 'OK', { duration: 5000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                operatingParameterPLF.id = this.selectedOperatingParameterPLF.id;
                this._loanMonitoringService.updateOperatingParameterPLF(operatingParameterPLF).subscribe(() => {
                    this._matSnackBar.open('Operating Parameter PLF details updated successfully.', 'OK', { duration: 5000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }         
        }
    }
}
