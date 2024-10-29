import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BusinessPartnerService } from '../businessPartner.service';

@Component({
    selector: 'fuse-business-partner-industry-update',
    templateUrl: './businessPartnerIndustryUpdate.component.html',
    styleUrls: ['./businessPartnerIndustryUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class BusinessPartnerIndustryUpdateComponent implements OnInit {

    dialogTitle = 'Add Industry';

    selectedIndustry: any;

    industryUpdateForm: FormGroup;
    industrySystems: any;
    industryTypes: any;
    
    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, 
                private _businessPartnerService: BusinessPartnerService,
                public _dialogRef: MatDialogRef<BusinessPartnerIndustryUpdateComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch list of industry systems and other details from the dialog's data attribute.
        this.industrySystems = this._dialogData.industrySystems;
        if (_dialogData.selectedIndustry !== undefined) {
            this.selectedIndustry = Object.assign({}, _dialogData.selectedIndustry);
            this.dialogTitle = 'Modify Industry';
        }
        else {
            this.selectedIndustry = {};
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.industryUpdateForm = this._formBuilder.group({
            serialNumber: [this.selectedIndustry.serialNumber || null],
            industrySystemId: [this.selectedIndustry.industrySystemId || null],
            industryTypeId: [this.selectedIndustry.industryTypeId || null],
        });
    }
    
    fetchIndustryTypes(event: any): void {
        console.log('in fetch industry types');
        console.log('event is', event);
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.industryUpdateForm.valid) {
            if (this._dialogData.operation === 'addIndustry') {
                this._businessPartnerService.createBusinessPartnerIndustry(this.selectedIndustry, this._dialogData.businessPartnerId).
                        subscribe(() => {
                    this._matSnackBar.open('Industry details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedIndustry.industrySystemId = this.industryUpdateForm.value.industrySystemId;
                this.selectedIndustry.industryTypeId = this.industryUpdateForm.value.industryTypeId;
                this._businessPartnerService.updateBusinessPartnerIndustry(this.selectedIndustry).subscribe(() => {
                    this._matSnackBar.open('Industry details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
