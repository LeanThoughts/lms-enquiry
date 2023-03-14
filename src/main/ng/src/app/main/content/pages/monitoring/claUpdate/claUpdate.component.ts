import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';

@Component({
    selector: 'fuse-cla-update-dialog',
    templateUrl: './claUpdate.component.html',
    styleUrls: ['./claUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class CLAUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add CLA';

    selectedCLA: LIEModel;

    claUpdateForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    partners: PartnerModel[] = new Array();

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
        public _dialogRef: MatDialogRef<CLAUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedCLA !== undefined) {
            this.selectedCLA = Object.assign({}, _dialogData.selectedCLA);
            if (_dialogData.operation === 'displayCLA') {
                this.dialogTitle = 'View CLA Details';
                this.allowUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify CLA';
            }
        }
        else {
            this.selectedCLA = new LIEModel({});
        }

        _loanMonitoringService.getCLAs().subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.claUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedCLA.bpCode],
            name: [this.selectedCLA.name || ''],
            dateOfAppointment: [this.selectedCLA.dateOfAppointment || ''],
            contactPerson: [this.selectedCLA.contactPerson],
            contactNumber: [this.selectedCLA.contactNumber],
            contractPeriodFrom: [this.selectedCLA.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedCLA.contractPeriodTo || ''],
            email: [this.selectedCLA.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.claUpdateForm.valid) {
            // To solve the utc time zone issue
            var cla: LIEModel = new LIEModel(this.claUpdateForm.value);
            var dt = new Date(cla.dateOfAppointment);
            cla.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(cla.contractPeriodFrom);
            cla.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(cla.contractPeriodTo);
            cla.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addCLA') {
                this._loanMonitoringService.saveCLA(cla, this._dialogData.loanApplicationId, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('CLA added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedCLA.bpCode  = cla.bpCode;
                this.selectedCLA.name = cla.name;
                this.selectedCLA.dateOfAppointment = cla.dateOfAppointment;
                this.selectedCLA.contactPerson = cla.contactPerson;
                this.selectedCLA.contractPeriodFrom = cla.contractPeriodFrom;
                this.selectedCLA.contractPeriodTo = cla.contractPeriodTo;
                this.selectedCLA.contactNumber = cla.contactNumber;
                this.selectedCLA.email = cla.email;

                this._loanMonitoringService.updateCLA(this.selectedCLA, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('CLA updated successfully.', 'OK', { duration: 7000 });
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
        this.claUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
    }

    /**
     * getPartyNumberAndName()
     * @param party 
     */
    getPartyNumberAndName(party: any): string {
        return party.partyNumber + " - " + party.partyName1 + ' ' + party.partyName2;
    }

    /**
     * dateOfAppointmentFilter()
     * @param d 
     */
    public dateOfAppointmentFilter = (d: Date): boolean => {
        const value = this.claUpdateForm.value;
        return (d >= this.toDate(value.contractPeriodFrom)) && (d <= this.toDate(value.contractPeriodTo));
    }

    /**
     * toDate()
     * @param d 
     */
    protected toDate(d: Date | string): Date {
        return (typeof d === 'string') ? new Date(d) : d;
    }
}
