import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { PartnerModel } from 'app/main/content/model/partner.model';
import { DocumentationService } from '../documentation.service';

@Component({
    selector: 'fuse-nodal-officer-update-dialog',
    templateUrl: './nodalOfficerUpdate.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class NodalOfficerUpdateDialogComponent implements OnInit {

    dialogTitle = 'Add Nodal Officer';

    selectedNodalOfficer: any;

    nodalOfficerForm: FormGroup;

    // businessPartnerRoles = LoanMonitoringConstants.businessPartnerRoles;
    partners: PartnerModel[] = new Array();

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _documentationService: DocumentationService,
        public _dialogRef: MatDialogRef<NodalOfficerUpdateDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected nodal officer details from the dialog's data attribute.
        if (_dialogData.operation === 'addNodalOfficer') {
            this.dialogTitle = 'Add Nodal Officer';
            this.selectedNodalOfficer = {};
        }
        else {
            this.dialogTitle = 'Modify Nodal Officer';
            this.selectedNodalOfficer = Object.assign({}, _dialogData.selectedNodalOfficer);
        }

        // Fetch busines partners
        _documentationService.getPartnersByRoleType("ZLM018").subscribe(response => {
            response.forEach(element => {
                this.partners.push(new PartnerModel(element));
            });
        })
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.nodalOfficerForm = this._formBuilder.group({
            bpCode: [this.selectedNodalOfficer.bpCode],
            name: [this.selectedNodalOfficer.name || ''],
            startDate: [this.selectedNodalOfficer.startDate || ''],
            endDate: [this.selectedNodalOfficer.endDate]
        }); 
    }

    /**
     * submit()
     */
    submit(): void {
        this.saveNodalOfficer();
    }

    /**
     * saveLegalCounsel()
     */
    saveNodalOfficer(): void {
        if (this.nodalOfficerForm.valid) {
            // To solve the utc time zone issue
            var nodalOfficer = this.nodalOfficerForm.value;
            var dt = new Date(nodalOfficer.startDate);
            nodalOfficer.startDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));
            dt = new Date(nodalOfficer.endDate);
            nodalOfficer.endDate = new Date(Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate()));

            if (this._dialogData.operation === 'addNodalOfficer') {
                nodalOfficer.loanApplicationId = this._dialogData.loanApplicationId;
                this._documentationService.createNodalOfficer(nodalOfficer).subscribe(() => {
                    this._matSnackBar.open('Nodal Officer added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedNodalOfficer.bpCode  = nodalOfficer.bpCode;
                this.selectedNodalOfficer.name = nodalOfficer.name;
                this.selectedNodalOfficer.startDate = nodalOfficer.startDate;
                this.selectedNodalOfficer.endDate = nodalOfficer.endDate;
                this._documentationService.updateNodalOfficer(this.selectedNodalOfficer).subscribe(() => {
                    this._matSnackBar.open('Nodal Officer updated successfully.', 'OK', { duration: 7000 });
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
        this.nodalOfficerForm.controls.name.setValue(partner.partyName1 + ' ' + partner.partyName2);
    }

    /**
     * getPartyNumberAndName()
     * @param party 
     */
    getPartyNumberAndName(party: any): string {
        return party.partyNumber + " - " + party.partyName1 + ' ' + party.partyName2;
    }
}
