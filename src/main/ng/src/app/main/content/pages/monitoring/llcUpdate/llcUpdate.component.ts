import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';

@Component({
    selector: 'fuse-llc-update-dialog',
    templateUrl: './llcUpdate.component.html',
    styleUrls: ['./llcUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LLCUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add LLC';

    selectedLLC: LIEModel;

    llcUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<LLCUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedLLC !== undefined) {
            this.selectedLLC = Object.assign({}, _dialogData.selectedLLC);
            if (_dialogData.operation === 'displayLLC') {
                this.dialogTitle = 'View LLC Details';
                this.allowUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify LLC';
            }
        }
        else {
            this.selectedLLC = new LIEModel({});
        }

        _loanMonitoringService.getLLCs().subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.llcUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedLLC.bpCode],
            name: [this.selectedLLC.name || ''],
            dateOfAppointment: [this.selectedLLC.dateOfAppointment || ''],
            contactPerson: [this.selectedLLC.contactPerson],
            contactNumber: [this.selectedLLC.contactNumber],
            contractPeriodFrom: [this.selectedLLC.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedLLC.contractPeriodTo || ''],
            email: [this.selectedLLC.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.llcUpdateForm.valid) {
            // To solve the utc time zone issue
            var llc: LIEModel = new LIEModel(this.llcUpdateForm.value);
            var dt = new Date(llc.dateOfAppointment);
            llc.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(llc.contractPeriodFrom);
            llc.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(llc.contractPeriodTo);
            llc.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLLC') {
                this._loanMonitoringService.saveLLC(llc, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('LLC added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedLLC.bpCode  = llc.bpCode;
                this.selectedLLC.name = llc.name;
                this.selectedLLC.dateOfAppointment = llc.dateOfAppointment;
                this.selectedLLC.contactPerson = llc.contactPerson;
                this.selectedLLC.contractPeriodFrom = llc.contractPeriodFrom;
                this.selectedLLC.contractPeriodTo = llc.contractPeriodTo;
                this.selectedLLC.contactNumber = llc.contactNumber;
                this.selectedLLC.email = llc.email;

                this._loanMonitoringService.updateLLC(this.selectedLLC).subscribe(() => {
                    this._matSnackBar.open('LLC updated successfully.', 'OK', { duration: 7000 });
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
        this.llcUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
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
        const value = this.llcUpdateForm.value;
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
