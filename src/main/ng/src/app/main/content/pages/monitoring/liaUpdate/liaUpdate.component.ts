import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LIEModel } from 'app/main/content/model/lie.model';
import { LoanMonitoringService } from '../loanMonitoring.service';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';

@Component({
    selector: 'fuse-lia-update-dialog',
    templateUrl: './liaUpdate.component.html',
    styleUrls: ['./liaUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class LIAUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add LIA';

    selectedLIA: LIEModel;

    liaUpdateForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    partners: PartnerModel[] = new Array();

    allowUpdates: boolean = true;
    allowBusinessPartnerUpdates = true;

    liaList: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanMonitoringService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(private _formBuilder: FormBuilder, private _loanMonitoringService: LoanMonitoringService,
        public _dialogRef: MatDialogRef<LIAUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedLIA !== undefined) {
            this.selectedLIA = Object.assign({}, _dialogData.selectedLIA);
            if (_dialogData.operation === 'displayLIA') {
                this.dialogTitle = 'View LIA Details';
                this.allowUpdates = false;
                this.allowBusinessPartnerUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify LIA';
                this.allowBusinessPartnerUpdates = false;
            }
        }
        else {
            this.selectedLIA = new LIEModel({});
        }

        _loanMonitoringService.getLIAs().subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })

        this._loanMonitoringService.getLendersInsuranceAdvisors(this._dialogData.loanApplicationId).subscribe(data => {
            this.liaList = data;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.liaUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedLIA.bpCode],
            name: [this.selectedLIA.name || ''],
            dateOfAppointment: [this.selectedLIA.dateOfAppointment || ''],
            contactPerson: [this.selectedLIA.contactPerson],
            contactNumber: [this.selectedLIA.contactNumber],
            contractPeriodFrom: [this.selectedLIA.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedLIA.contractPeriodTo || ''],
            email: [this.selectedLIA.email || '', [Validators.pattern(EnquiryApplicationRegEx.email)]]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.liaUpdateForm.valid) {
            var lia: LIEModel = new LIEModel(this.liaUpdateForm.value);
            const filteredList = this.liaList.filter(obj => obj.lendersInsuranceAdvisor.bpCode  === lia.bpCode);
            if (filteredList.length == 0) {
                // To solve the utc time zone issue
                var dt = new Date(lia.dateOfAppointment);
                lia.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
                dt = new Date(lia.contractPeriodFrom);
                lia.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
                dt = new Date(lia.contractPeriodTo);
                lia.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

                if (this._dialogData.operation === 'addLIA') {
                    this._loanMonitoringService.saveLIA(lia, this._dialogData.loanApplicationId, this._dialogData.module).subscribe(() => {
                        this._matSnackBar.open('LIA added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
                else {
                    this.selectedLIA.bpCode  = lia.bpCode;
                    this.selectedLIA.name = lia.name;
                    this.selectedLIA.dateOfAppointment = lia.dateOfAppointment;
                    this.selectedLIA.contactPerson = lia.contactPerson;
                    this.selectedLIA.contractPeriodFrom = lia.contractPeriodFrom;
                    this.selectedLIA.contractPeriodTo = lia.contractPeriodTo;
                    this.selectedLIA.contactNumber = lia.contactNumber;
                    this.selectedLIA.email = lia.email;

                    this._loanMonitoringService.updateLIA(this.selectedLIA, this._dialogData.module).subscribe(() => {
                        this._matSnackBar.open('LIA updated successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });            
                }
            }
            else {
                this._matSnackBar.open('Business partner ' + lia.bpCode + ' is already in the list. Please select a different business partner.', 
                        'OK', { duration: 7000 });
            }
        }
    }

    /**
     * onPartnerSelect()
     * @param event 
     */
    onPartnerSelect(partner: PartnerModel): void {
        this.liaUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
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
        const value = this.liaUpdateForm.value;
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
