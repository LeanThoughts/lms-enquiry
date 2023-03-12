import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { LoanMonitoringService } from '../../../monitoring/loanMonitoring.service';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';

@Component({
    selector: 'fuse-sub-location-update-dialog',
    templateUrl: './subLocationUpdate.component.html',
    styleUrls: ['./subLocationUpdate.component.scss'],
    animations: fuseAnimations,
    encapsulation: ViewEncapsulation.None
})
export class SubLocationUpdateComponent {

    dialogTitle = 'Add New Sub Location Details';

    selectedSubLocation: any;
    subLocationUpdateForm: FormGroup;

    /**
     * constructor()
     * @param _formBuilder 
     * @param _loanAppraisalService 
     * @param _dialogRef 
     * @param _dialogData 
     * @param _matSnackBar 
     */
    constructor(_formBuilder: FormBuilder, 
                private _loanAppraisalService: LoanAppraisalService,
                public _dialogRef: MatDialogRef<SubLocationUpdateComponent>, 
                @Inject(MAT_DIALOG_DATA) public _dialogData: any,
                private _matSnackBar: MatSnackBar) {

        // Fetch selected user details from the dialog's data attribute.
        this.selectedSubLocation = Object.assign({}, _dialogData.selectedSubLocation);

        if (this._dialogData.operation === 'updateSubLocation') {
            this.dialogTitle = 'Modify Sub Location Details';
        }

        this.subLocationUpdateForm = _formBuilder.group({
            serialNumber: [this.selectedSubLocation.serialNumber],
            location: [ this.selectedSubLocation.location || '' ],
            state: [ this.selectedSubLocation.state || '' ],
            district: [ this.selectedSubLocation.district || '' ],
            region: [ this.selectedSubLocation.region || '' ],
            nearestVillage: [ this.selectedSubLocation.nearestVillage || '' ],
            nearestVillageDistance: [ this.selectedSubLocation.nearestVillageDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            nearestRailwayStation: [ this.selectedSubLocation.nearestRailwayStation || '' ],
            nearestRailwayStationDistance: [ this.selectedSubLocation.nearestRailwayStationDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestAirport: [ this.selectedSubLocation.nearestAirport || '' ],
            nearestAirportDistance: [ this.selectedSubLocation.nearestAirportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestSeaport: [ this.selectedSubLocation.nearestSeaport || '' ],
            nearestSeaportDistance: [ this.selectedSubLocation.nearestSeaportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestFunctionalAirport: [ this.selectedSubLocation.nearestFunctionalAirport || '' ],
            nearestFunctionalAirportDistance: [ this.selectedSubLocation.nearestFunctionalAirportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ]
        });
    }

    /**
     * submit()
     */
    submit(): void {
        if (this.subLocationUpdateForm.valid) {
            this.saveSubLocationDetails();
        }
    }

    /**
     * saveSiteVisitDetails()
     */
    saveSubLocationDetails(): void {
        var subLocation = this.subLocationUpdateForm.value;
        subLocation.loanApplicationId = this._dialogData.loanApplicationId;
        if (this._dialogData.operation === 'addSubLocation') {
            this._loanAppraisalService.createSubLocationDetail(subLocation).subscribe(() => {
                this._matSnackBar.open('Sub location details added successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });
        }
        else {
            this.selectedSubLocation.loanApplicationId = this._dialogData.loanApplicationId;
            this.selectedSubLocation.location = subLocation.location;
            this.selectedSubLocation.state = subLocation.state;
            this.selectedSubLocation.district = subLocation.district;
            this.selectedSubLocation.region = subLocation.region;
            this.selectedSubLocation.nearestVillage = subLocation.nearestVillage;
            this.selectedSubLocation.nearestVillageDistance = subLocation.nearestVillageDistance;
            this.selectedSubLocation.nearestRailwayStation = subLocation.nearestRailwayStation;
            this.selectedSubLocation.nearestRailwayStationDistance = subLocation.nearestRailwayStationDistance;
            this.selectedSubLocation.nearestAirport = subLocation.nearestAirport;
            this.selectedSubLocation.nearestAirportDistance = subLocation.nearestAirportDistance;
            this.selectedSubLocation.nearestSeaport = subLocation.nearestSeaport;
            this.selectedSubLocation.nearestSeaportDistance = subLocation.nearestSeaportDistance;
            this.selectedSubLocation.nearestFunctionalAirport = subLocation.nearestFunctionalAirport;
            this.selectedSubLocation.nearestFunctionalAirportDistance = subLocation.nearestFunctionalAirportDistance;
            this._loanAppraisalService.updateSubLocationDetail(this.selectedSubLocation).subscribe(() => {
                this._matSnackBar.open('Sub location details updated successfully.', 'OK', { duration: 7000 });
                this._dialogRef.close({ 'refresh': true });
            });            
        }
    }
}
