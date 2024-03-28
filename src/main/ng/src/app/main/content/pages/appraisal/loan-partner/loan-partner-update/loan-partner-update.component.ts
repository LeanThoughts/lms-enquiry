import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';
import { businessPartnerRoleTypes } from '../../loanAppraisal.constants';
import { LoanAppraisalService } from '../../loanAppraisal.service';

@Component({
  selector: 'fuse-loan-partner-update',
  templateUrl: './loan-partner-update.component.html',
  styleUrls: ['./loan-partner-update.component.scss']
})
export class LoanPartnerUpdateComponent implements OnInit {

    bupaRoleTypes: any;
    businessPartnerId: string;

    dialogTitle = "Add Loan Partners";

    loanOfficerForm: FormGroup;

    selectedLoanOfficer: any;

    selectedRoleTypeDescription: string;

    partners: any;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     */
    constructor(private _formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<LoanPartnerUpdateComponent>,
                @Inject(MAT_DIALOG_DATA) private _dialogData: any,
                private _matSnackBar: MatSnackBar) { 

        // Fetch selected loan officer details from the dialog's data attribute
        this.selectedLoanOfficer = Object.assign({}, _dialogData.loanOfficer);

        // Change diglog title and fetch partners based on the role assigned ...
        if (this._dialogData.operation === 'modifyOfficer') {
            this.dialogTitle = 'Modify Loan Partners';
            this._loanAppraisalService.getPartnersByRole(this.selectedLoanOfficer.roleType).subscribe(response => {
                this.partners = response;
            });
        }
          
        // Sort bupaRoleTypes array ...
        this.bupaRoleTypes = businessPartnerRoleTypes.sort((obj1, obj2) => {
            return obj1.value.localeCompare(obj2.value);
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit() {
        this.loanOfficerForm = this._formBuilder.group({
            serialNumber: [ this.selectedLoanOfficer.serialNumber || '' ],
            businessPartnerId: [ this.selectedLoanOfficer.businessPartnerId || '' ],
            displayBusinessPartnerId: [ this.selectedLoanOfficer.businessPartnerId || '' ],
            businessPartnerName: [ this.selectedLoanOfficer.businessPartnerName || '' ],
            roleType: [ this.selectedLoanOfficer.roleType || '' ],
            startDate: [ this.selectedLoanOfficer.startDate || '' ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.loanOfficerForm.valid) {
            var formData = this.loanOfficerForm.value;

            var dt = new Date(formData.startDate);
            formData.startDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            this.selectedLoanOfficer.businessPartnerId = formData.businessPartnerId;
            this.selectedLoanOfficer.businessPartnerName = formData.businessPartnerName;
            this.selectedLoanOfficer.roleType = formData.roleType;
            this.selectedLoanOfficer.startDate = formData.startDate;
            this.selectedLoanOfficer.roleDescription = this.selectedRoleTypeDescription;
            if (this._dialogData.operation === 'modifyOfficer') {
                this._loanAppraisalService.updateLoanOfficer(this.selectedLoanOfficer).subscribe(response => {
                    this._matSnackBar.open('Loan partner updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                    if (response.kycRequired == true) {
                        this._loanAppraisalService.refreshKYCPartnerList.next({'refresh': true});
                    };
                    this._loanAppraisalService.getLaonAppraisal(this._dialogData.loanApplicationId).subscribe(response => {
                        this._loanAppraisalService._loanAppraisalBehaviourSubject.next(response);
                        console.log('updated loan appraisal is', this._loanAppraisalService._loanAppraisalBehaviourSubject.value);
                    });
                });
            }
            else {
                if (this.selectedLoanOfficer.roleType !== 'TR0100') {
                    this.createLoanPartner();
                }
                else {
                    this._loanAppraisalService.getLoanOfficersByRoleType(this._dialogData.loanApplicationId, 'TR0100').subscribe(data => {
                        if (data._embedded.loanPartners.length === 0) {
                            this.createLoanPartner();
                        }
                        else {
                            this._matSnackBar.open('Only one main loan partner possible for a loan account.', 'OK', { duration: 7000 });
                        }
                    });
                }
            }
        }
    }

    /**
     * createLoanPartner()
     */
    createLoanPartner(): void {
        this.selectedLoanOfficer.loanApplicationId = this._dialogData.loanApplicationId;
        this.selectedLoanOfficer.kycStatus = "Not Started";
        this._loanAppraisalService.createLoanOfficer(this.selectedLoanOfficer).subscribe(response => {
            this._matSnackBar.open('Loan partner added successfully.', 'OK', { duration: 7000 });
            this._dialogRef.close({ 'refresh': true });
            if (response.kycRequired == true) {
                this._loanAppraisalService.refreshKYCPartnerList.next({'refresh': true});
            };
            this._loanAppraisalService.getLaonAppraisal(this._dialogData.loanApplicationId).subscribe(response => {
                this._loanAppraisalService._loanAppraisalBehaviourSubject.next(response);
                console.log('updated loan appraisal is', this._loanAppraisalService._loanAppraisalBehaviourSubject.value);
            });
        });
    }

    /**
     * closeDialog()
     */
    closeDialog(): void {
        this._dialogRef.close({ 'refresh': false });
    }

    /**
     * onBupaRoleSelect()
     * @param roleType 
     */
    onBupaRoleSelect(roleType: any): void {
        console.log('onBupaRoleSelect', roleType);
        this.selectedRoleTypeDescription = roleType.value;
        this._loanAppraisalService.getPartnersByRole(roleType.code).subscribe(response => {
            this.partners = response;
        })
    }

    /**
     * getBupaCodeAndName()
     * @param party 
     */
    getBupaCodeAndName(partner: any): string {
        return partner.partyNumber + " - " + partner.partyName1 + ' ' + partner.partyName2;
    }

    /**
     * onPartnerSelect()
     * @param event 
     */
    onPartnerSelect(partner: any): void {
        console.log("onPartnerSelect", partner);
        this.loanOfficerForm.controls.displayBusinessPartnerId.setValue(partner.partyNumber);
        this.loanOfficerForm.controls.businessPartnerName.setValue(partner.partyName1 + ' ' + partner.partyName2);
    }
}
