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

    selectedBusinessPartnerIndustry: any;

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
        if (_dialogData.selectedBusinessPartnerIndustry !== undefined) {
            this.selectedBusinessPartnerIndustry = Object.assign({}, _dialogData.selectedBusinessPartnerIndustry);
            this._businessPartnerService.getIndustryTypes(this.selectedBusinessPartnerIndustry.industrySystemId).subscribe((response: any) => {
                this.industryTypes = response._embedded.industryTypes;
            });
            this.dialogTitle = 'Modify Industry';
        }
        else {
            this.selectedBusinessPartnerIndustry = {};
        }
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.industryUpdateForm = this._formBuilder.group({
            serialNumber: [this.selectedBusinessPartnerIndustry.serialNumber || null],
            industrySystemId: [this.selectedBusinessPartnerIndustry.industrySystemId || null],
            industryTypeId: [this.selectedBusinessPartnerIndustry.industryTypeId || null],
        });
    }
    
    /**
     * fetchIndustryTypes()
     */
    fetchIndustryTypes(event: any): void {
        this._businessPartnerService.getIndustryTypes(event.value).subscribe((response: any) => {
            this.industryTypes = response._embedded.industryTypes;
        });
    }

    /**
     * submit()
     */
    submit(): void {
        console.log(this.selectedBusinessPartnerIndustry);
        if (this.industryUpdateForm.valid) {
            if (this._dialogData.operation === 'addBusinessPartnerIndustry') {
                this._businessPartnerService.createBusinessPartnerIndustry(this.industryUpdateForm.value, this._dialogData.businessPartnerId).
                        subscribe(() => {
                    this._matSnackBar.open('Industry details added successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });
            }
            else {
                this.selectedBusinessPartnerIndustry.industrySystemId = this.industryUpdateForm.value.industrySystemId;
                this.selectedBusinessPartnerIndustry.industryTypeId = this.industryUpdateForm.value.industryTypeId;
                this._businessPartnerService.updateBusinessPartnerIndustry(this.selectedBusinessPartnerIndustry).subscribe(() => {
                    this._matSnackBar.open('Industry details updated successfully.', 'OK', { duration: 7000 });
                    this._dialogRef.close({ 'refresh': true });
                });            
            }
        }
    }
}
