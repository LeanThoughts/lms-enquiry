import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { TRAModel } from 'app/main/content/model/tra.model';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { map, startWith } from 'rxjs/operators';

@Component({
    selector: 'fuse-tra-update-dialog',
    templateUrl: './traUpdate.component.html',
    styleUrls: ['./traUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class TRAUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add New TRA Account';

    selectedTRA: TRAModel;

    traUpdateForm: FormGroup;

    accountTypes = LoanMonitoringConstants.accountTypes;

    banks: any;
    bankFilteredOptions: any;
    bankKeyFormControl = new FormControl();

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<TRAUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedTRA !== undefined) {
            this.selectedTRA = Object.assign({}, _dialogData.selectedTRA);
            this.dialogTitle = 'Modify TRA Account';
        }
        else {
            this.selectedTRA = new TRAModel({});
        }

        this.traUpdateForm = _formBuilder.group({
            bankKey: [this.selectedTRA.bankKey || ''],
            traBankName: [this.selectedTRA.traBankName || ''],
            branch: [this.selectedTRA.branch || ''],
            address: [this.selectedTRA.address || ''],
            ifscCode: [this.selectedTRA.ifscCode || ''],
            accountNumber: [this.selectedTRA.accountNumber || ''],
            contactName: [this.selectedTRA.contactName || ''],
            contactNumber: [this.selectedTRA.contactNumber || ''],
            typeOfAccount: [this.selectedTRA.typeOfAccount || 'TRA Account'],
            email: [this.selectedTRA.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]],
            pfsAuthorisedPersonBPCode: [this.selectedTRA.pfsAuthorisedPersonBPCode || ''],
            pfsAuthorisedPerson: [this.selectedTRA.pfsAuthorisedPerson || ''],
            beneficiaryName: [this.selectedTRA.beneficiaryName || '']
        });

        this.banks = _loanMonitoringService.banks;
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        // this.bankKeyFormControl.valueChanges.subscribe(str => {
        //     this.bankFilteredOptions = this.banks.filter(bank => bank.bankName.toLowerCase().includes(str.toLowerCase()));
        // });

        this.bankFilteredOptions = this.bankKeyFormControl.valueChanges.pipe(
            startWith(''),
            map(value => value ? this._filterStates(value) : this.banks.slice())
        );

        this.traUpdateForm.controls.typeOfAccount.setValue(this.accountTypes[0].code);
    }

    private _filterStates(value: string): any {
        const filterValue = value.toLowerCase();
        return this.banks.filter(bank => bank.bankName.toLowerCase().indexOf(filterValue) === 0);
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.traUpdateForm.valid) {
            var tra: TRAModel = new TRAModel(this.traUpdateForm.value);
            if (this._dialogData.operation === 'addTRA') {
                this._loanMonitoringService.saveTRA(tra, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('TRA added successfully.', 'OK', { duration: 5000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedTRA.bankKey  = tra.bankKey;
                this.selectedTRA.traBankName  = tra.traBankName;
                this.selectedTRA.branch  = tra.branch;
                this.selectedTRA.address  = tra.address;
                this.selectedTRA.ifscCode  = tra.ifscCode;
                this.selectedTRA.accountNumber  = tra.accountNumber;
                this.selectedTRA.contactName  = tra.contactName;
                this.selectedTRA.contactNumber  = tra.contactNumber;
                this.selectedTRA.typeOfAccount  = tra.typeOfAccount;
                this.selectedTRA.email  = tra.email;
                this.selectedTRA.pfsAuthorisedPersonBPCode  = tra.pfsAuthorisedPersonBPCode;
                this.selectedTRA.pfsAuthorisedPerson  = tra.pfsAuthorisedPerson;
                this.selectedTRA.beneficiaryName  = tra.beneficiaryName;
                this._loanMonitoringService.updateTRA(this.selectedTRA).subscribe(() => {
                    this._matSnackBar.open('TRA updated successfully.', 'OK', { duration: 5000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }

    /**
     * validateBank()
     */
     validateBank($event) {
        const filteredBanks = this.banks.filter(bank => bank.bankKey === $event.target.value);
        if (filteredBanks.length > 0) {
            this.traUpdateForm.controls.bankKey.setValue(this.bankKeyFormControl.value);
            this.traUpdateForm.controls.traBankName.setValue(filteredBanks[0].bankName);
            this.traUpdateForm.controls.branch.setValue(filteredBanks[0].bankBranch || '');
            this.traUpdateForm.controls.address.setValue(filteredBanks[0].houseNumberAndStreet);
            this.traUpdateForm.controls.ifscCode.setValue(filteredBanks[0].bankKey);
        }
        else {
            this.traUpdateForm.controls.bankKey.setValue('');
            this.traUpdateForm.controls.traBankName.setValue('');
            this.traUpdateForm.controls.branch.setValue('');
            this.traUpdateForm.controls.address.setValue('');
            this.traUpdateForm.controls.ifscCode.setValue('');
        }
    }
}
