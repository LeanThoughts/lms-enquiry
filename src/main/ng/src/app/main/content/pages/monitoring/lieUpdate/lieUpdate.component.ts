import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';

@Component({
    selector: 'fuse-lie-update-dialog',
    templateUrl: './lieUpdate.component.html',
    styleUrls: ['./lieUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LIEUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add LIE';

    selectedLIE: LIEModel;

    lieUpdateForm: FormGroup;

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
        public _dialogRef: MatDialogRef<LIEUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedLIE !== undefined) {
            this.selectedLIE = Object.assign({}, _dialogData.selectedLIE);
            if (_dialogData.operation === 'displayLIE') {
                this.dialogTitle = 'View LIE Details';
                this.allowUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify LIE';
            }
        }
        else {
            this.selectedLIE = new LIEModel({});
        }

        _loanMonitoringService.getLIEs().subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.lieUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedLIE.bpCode],
            name: [this.selectedLIE.name || ''],
            dateOfAppointment: [this.selectedLIE.dateOfAppointment || ''],
            contactPerson: [this.selectedLIE.contactPerson],
            contactNumber: [this.selectedLIE.contactNumber],
            contractPeriodFrom: [this.selectedLIE.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedLIE.contractPeriodTo || ''],
            email: [this.selectedLIE.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.lieUpdateForm.valid) {
            // To solve the utc time zone issue
            var lie: LIEModel = new LIEModel(this.lieUpdateForm.value);
            var dt = new Date(lie.dateOfAppointment);
            lie.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(lie.contractPeriodFrom);
            lie.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(lie.contractPeriodTo);
            lie.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addLIE') {
                this._loanMonitoringService.saveLIE(lie, this._dialogData.loanApplicationId).subscribe(() => {
                    this._matSnackBar.open('LIE added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedLIE.bpCode  = lie.bpCode;
                this.selectedLIE.name = lie.name;
                this.selectedLIE.dateOfAppointment = lie.dateOfAppointment;
                this.selectedLIE.contactPerson = lie.contactPerson;
                this.selectedLIE.contractPeriodFrom = lie.contractPeriodFrom;
                this.selectedLIE.contractPeriodTo = lie.contractPeriodTo;
                this.selectedLIE.contactNumber = lie.contactNumber;
                this.selectedLIE.email = lie.email;

                this._loanMonitoringService.updateLIE(this.selectedLIE).subscribe(() => {
                    this._matSnackBar.open('LIE updated successfully.', 'OK', { duration: 7000 });
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
        this.lieUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
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
        const value = this.lieUpdateForm.value;
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
