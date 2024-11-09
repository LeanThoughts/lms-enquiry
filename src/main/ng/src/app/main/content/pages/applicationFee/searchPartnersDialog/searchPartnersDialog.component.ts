import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { ApplicationFeeService } from '../applicationFee.service';

@Component({
    selector: 'fuse-search-partners-dialog',
    templateUrl: './searchPartnersDialog.component.html',
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SearchPartnersDialogComponent implements OnInit {

    dataSource: any;
    displayedColumns: string[] = ['partyName1', 'partyNumber', 'addressLine1', 'addressLine2', 'city', 'state'];
    dialogTitle = 'Search Partners';

    selectedPartner: any;

    partnerForm: FormGroup;

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder, private _applicationFeeService: ApplicationFeeService,
        public _dialogRef: MatDialogRef<SearchPartnersDialogComponent>, @Inject(MAT_DIALOG_DATA) public _dialogData: any,
        private _matSnackBar: MatSnackBar) {

        // Fetch selected reason details from the dialog's data attribute.
        this.partnerForm = this._formBuilder.group({
            partyName1: [''],
            partyNumber: [''],
            searchTerm1: [''],
            searchTerm2: ['']
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * selectRow()
     */
    selectRow(partner: any): void {
        this.selectedPartner = partner;
    }

    /**
     * selectPartner()
     */
    selectPartner() {
        if (this.selectedPartner) {
            this._dialogRef.close({ 'selectedPartner': this.selectedPartner });
        }
        else {
            this._matSnackBar.open('Please select a partner from the table.', 'OK', { duration: 7000 });
        }
    }

    /**
     * submit()
     */
    submit(): void {
        const partner = this.partnerForm.value;
        if ((partner.partyNumber.trim() !== '' || partner.partyName1.trim() !== '' || partner.searchTerm1.trim() !== '' || 
                partner.searchTerm2.trim() !== '') && (partner.partyName1.length >= 3 || partner.partyNumber.length >= 3 || 
                partner.searchTerm1.length >= 3 || partner.searchTerm2.length >= 3)) {
            
            this._applicationFeeService.searchPartners(partner).subscribe(response => {
                if (response.length > 0) {
                    this.dataSource = response;
                }
                else {
                    this._matSnackBar.open('The search criteria did not match any records.', 'OK', { duration: 7000 });
                }
            });
        }
        else {
            this._matSnackBar.open('Please enter valid search criteria (Minimum 3 characters in any field).', 'OK', { duration: 7000 });
        }
    }
}
