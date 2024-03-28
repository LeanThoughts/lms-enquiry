import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { EnquiryApplicationRegEx } from 'app/main/content/others/enquiryApplication.regEx';
import { PartnerModel } from 'app/main/content/model/partner.model';
import { LoanAppraisalService } from '../loanAppraisal.service';

@Component({
    selector: 'fuse-securityTrustee-update-dialog',
    templateUrl: './securityTrusteeUpdate.component.html',
    styleUrls: ['./securityTrusteeUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SecurityTrusteeUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Security Trustee';

    selectedSecurityTrustee: any;

    securityTrusteeUpdateForm: FormGroup;

    //businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    partners: PartnerModel[] = new Array();
    
    allowUpdates = true;
    allowBusinessPartnerUpdates = true;

    securityTrusteeList: any;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _loanAppraisalService: LoanAppraisalService,
        public _dialogRef: MatDialogRef<SecurityTrusteeUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        if (_dialogData.selectedSecurityTrustee !== undefined) {
            this.selectedSecurityTrustee = Object.assign({}, _dialogData.selectedSecurityTrustee);
            if (_dialogData.operation === 'displaySecurityTrustee') {
                this.dialogTitle = 'View Details';
                this.allowUpdates = false;
                this.allowBusinessPartnerUpdates = false;
            }
            else {
                this.dialogTitle = 'Modify Security Trustee';
                this.allowBusinessPartnerUpdates = false;
            }
        }
        else {
            this.selectedSecurityTrustee = {};
        }

        _loanAppraisalService.getBusinessPartners('ZLM009').subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })

        this._loanAppraisalService.getSecurityTrustees(this._dialogData.loanApplicationId).subscribe(data => {
            this.securityTrusteeList = data;
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.securityTrusteeUpdateForm = this._formBuilder.group({
            bpCode: [this.selectedSecurityTrustee.bpCode || ''],
            name: [this.selectedSecurityTrustee.name || ''],
            dateOfAppointment: [this.selectedSecurityTrustee.dateOfAppointment || ''],
            contactPerson: [this.selectedSecurityTrustee.contactPerson],
            contractPeriodFrom: [this.selectedSecurityTrustee.contractPeriodFrom || ''],
            contractPeriodTo: [this.selectedSecurityTrustee.contractPeriodTo || ''],
            contactNumber: [this.selectedSecurityTrustee.contactNumber || ''],
            email: [this.selectedSecurityTrustee.email, [Validators.pattern(EnquiryApplicationRegEx.email)]]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.securityTrusteeUpdateForm.valid) {
            var securityTrustee = this.securityTrusteeUpdateForm.value;
            securityTrustee.advisor = 'Security Trustee';
            // To solve the utc time zone issue
            var dt = new Date(securityTrustee.dateOfAppointment);
            securityTrustee.dateOfAppointment = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityTrustee.contractPeriodFrom);
            securityTrustee.contractPeriodFrom = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(securityTrustee.contractPeriodTo);
            securityTrustee.contractPeriodTo = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addSecurityTrustee') {
                const filteredList = this.securityTrusteeList.filter(obj => obj.securityTrustee.bpCode  === securityTrustee.bpCode);
                if (filteredList.length == 0) {
                    this._loanAppraisalService.saveSecurityTrustee(securityTrustee, this._dialogData.loanApplicationId, this._dialogData.module).subscribe(() => {
                        this._matSnackBar.open('Security Trustee added successfully.', 'OK', { duration: 7000 });
                        this._dialogRef.close({ 'refresh': true });
                    });
                }
                else {
                    this._matSnackBar.open('Business partner ' + securityTrustee.bpCode + ' is already in the list. Please select a different business partner.', 
                            'OK', { duration: 7000 });
                }
            }
            else {
                this.selectedSecurityTrustee.bpCode  = securityTrustee.bpCode;
                this.selectedSecurityTrustee.name = securityTrustee.name;
                this.selectedSecurityTrustee.dateOfAppointment = securityTrustee.dateOfAppointment;
                this.selectedSecurityTrustee.contactPerson = securityTrustee.contactPerson;
                this.selectedSecurityTrustee.contractPeriodFrom = securityTrustee.contractPeriodFrom;
                this.selectedSecurityTrustee.contractPeriodTo = securityTrustee.contractPeriodTo;
                this.selectedSecurityTrustee.contactNumber = securityTrustee.contactNumber;
                this.selectedSecurityTrustee.email = securityTrustee.email;
                this._loanAppraisalService.updateSecurityTrustee(this.selectedSecurityTrustee, this._dialogData.module).subscribe(() => {
                    this._matSnackBar.open('Security Trustee updated successfully.', 'OK', { duration: 7000 });
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
        this.securityTrusteeUpdateForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
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
        const value = this.securityTrusteeUpdateForm.value;
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
