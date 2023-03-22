import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';

@Component({
    selector: 'fuse-valuer-update-dialog',
    templateUrl: './valuerUpdate.component.html',
    styleUrls: ['./valuerUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class ValuerUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Valuer';

    selectedValuer: LIEModel;

    valuerUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<ValuerUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedValuer !== undefined) {
            this.selectedValuer = Object.assign({}, _dialogData.selectedValuer);
            if (_dialogData.operation === 'displayValuer') {
                this.dialogTitle = 'View Valuer Details';
                this.allowUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify Valuer';
            }
        }
        else {
            this.selectedValuer = new LIEModel({});
        }

        _loanMonitoringService.getAllValuers().subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.valuerUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedValuer.bpCode],
            name: [this.selectedValuer.name || ''],
            dateOfAppointment: [this.selectedValuer.dateOfAppointment || ''],
            contactPerson: [this.selectedValuer.contactPerson],
            contactNumber: [this.selectedValuer.contactNumber],
            contractPeriodFrom: [this.selectedValuer.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedValuer.contractPeriodTo || ''],
            email: [this.selectedValuer.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.valuerUpdateForm.valid) {
            // To solve the utc time zone issue
            var valuer: LIEModel = new LIEModel(this.valuerUpdateForm.value);
            var dt = new Date(valuer.dateOfAppointment);
            valuer.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(valuer.contractPeriodFrom);
            valuer.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(valuer.contractPeriodTo);
            valuer.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addValuer') {
                this._loanMonitoringService.saveValuer(valuer, this._dialogData.loanApplicationId, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('Valuer added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedValuer.bpCode  = valuer.bpCode;
                this.selectedValuer.name = valuer.name;
                this.selectedValuer.dateOfAppointment = valuer.dateOfAppointment;
                this.selectedValuer.contactPerson = valuer.contactPerson;
                this.selectedValuer.contractPeriodFrom = valuer.contractPeriodFrom;
                this.selectedValuer.contractPeriodTo = valuer.contractPeriodTo;
                this.selectedValuer.contactNumber = valuer.contactNumber;
                this.selectedValuer.email = valuer.email;

                this._loanMonitoringService.updateValuer(this.selectedValuer, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('Valuer updated successfully.', 'OK', { duration: 7000 });
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
        this.valuerUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
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
        const value = this.valuerUpdateForm.value;
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
