import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSnackBar, MatSort, MatTableDataSource, } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { MonitoringRegEx } from 'app/main/content/others/monitoring.regEx';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanAppraisalService } from '../../loanAppraisal.service';
import { SubLocationUpdateComponent } from '../subLocationUpdate/subLocationUpdate.component';

@Component({
    selector: 'fuse-main-location-details',
    templateUrl: './mainLocationUpdate.component.html',
    styleUrls: ['./mainLocationUpdate.component.scss'],
    animations: fuseAnimations
})
export class MainLocationUpdateComponent {

    _loanApplicationId: string;
    _loanAppraisalId: string;

    _mainLocationDetail: any;
    _mainLocationDetailForm: FormGroup;

    displayedColumns = [
        'serialNumber', 'location'
    ];

    selectedSubLocation: any;

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;
    
    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService,
                _activatedRoute: ActivatedRoute,
                private _loanAppraisalService: LoanAppraisalService,
                _formBuilder: FormBuilder,
                private _matSnackBar: MatSnackBar,
                private _matDialog: MatDialog) {

        this._loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        this._loanAppraisalId = _loanAppraisalService._loanAppraisal.id;
        this._mainLocationDetail = _activatedRoute.snapshot.data.routeResolvedData[15];

        console.log('route data', _activatedRoute.snapshot.data);
        this.dataSource = new MatTableDataSource(_activatedRoute.snapshot.data.routeResolvedData[16]._embedded.subLocationDetails);
        this.dataSource.sort = this.sort;

        this._mainLocationDetailForm = _formBuilder.group({
            location: [ this._mainLocationDetail.location || '' ],
            state: [ this._mainLocationDetail.state || '' ],
            district: [ this._mainLocationDetail.district || '' ],
            region: [ this._mainLocationDetail.region || '' ],
            nearestVillage: [ this._mainLocationDetail.nearestVillage || '' ],
            nearestVillageDistance: [ this._mainLocationDetail.nearestVillageDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)]],
            nearestRailwayStation: [ this._mainLocationDetail.nearestRailwayStation || '' ],
            nearestRailwayStationDistance: [ this._mainLocationDetail.nearestRailwayStationDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestAirport: [ this._mainLocationDetail.nearestAirport || '' ],
            nearestAirportDistance: [ this._mainLocationDetail.nearestAirportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestSeaport: [ this._mainLocationDetail.nearestSeaport || '' ],
            nearestSeaportDistance: [ this._mainLocationDetail.nearestSeaportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ],
            nearestFunctionalAirport: [ this._mainLocationDetail.nearestFunctionalAirport || '' ],
            nearestFunctionalAirportDistance: [ this._mainLocationDetail.nearestFunctionalAirportDistance || '', 
                    [Validators.pattern(MonitoringRegEx.digitsOnly)] ]
        });
    }

    /**
     * saveFurtherDetails()
     */
    saveMainLocationDetails(): void {
        if (this._mainLocationDetailForm.valid) {

            var formValues = this._mainLocationDetailForm.value;

            if (JSON.stringify(this._mainLocationDetail) === JSON.stringify({})) {
                formValues.loanApplicationId = this._loanApplicationId;
                this._loanAppraisalService.updateMainLocationDetail(formValues).subscribe(() => {
                    this._matSnackBar.open('Further details added successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                this._mainLocationDetail.loanApplicationId = this._loanApplicationId;
                this._mainLocationDetail.location = formValues.location;
                this._mainLocationDetail.state = formValues.state;
                this._mainLocationDetail.district = formValues.district;
                this._mainLocationDetail.region = formValues.region;
                this._mainLocationDetail.nearestVillage = formValues.nearestVillage;
                this._mainLocationDetail.nearestVillageDistance = formValues.nearestVillageDistance;
                this._mainLocationDetail.nearestRailwayStation = formValues.nearestRailwayStation;
                this._mainLocationDetail.nearestRailwayStationDistance = formValues.nearestRailwayStationDistance;
                this._mainLocationDetail.nearestAirport = formValues.nearestAirport;
                this._mainLocationDetail.nearestAirportDistance = formValues.nearestAirportDistance;
                this._mainLocationDetail.nearestSeaport = formValues.nearestSeaport;
                this._mainLocationDetail.nearestSeaportDistance = formValues.nearestSeaportDistance;
                this._mainLocationDetail.nearestFunctionalAirport = formValues.nearestFunctionalAirport;
                this._mainLocationDetail.nearestFunctionalAirportDistance = formValues.nearestFunctionalAirportDistance;
                this._loanAppraisalService.updateMainLocationDetail(this._mainLocationDetail).subscribe(() => {
                    this._matSnackBar.open('Proposal details updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * onSelect()
     */
    onSelect(selectedSubLocation: any): void {
        this.selectedSubLocation = selectedSubLocation;
    }

    /**
     * updateSubLocation()
     */
    updateSubLocation(operation: string): void {
        // Open the dialog.
        var data = {
            'operation': operation,
            'loanApplicationId': this._loanApplicationId,
            'selectedSubLocation': undefined
        };
        if (operation === 'updateSubLocation') {
            data.selectedSubLocation = this.selectedSubLocation;
        }
        const dialogRef = this._matDialog.open(SubLocationUpdateComponent, {
            panelClass: 'fuse-sub-location-update-dialog',
            width: '850px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanAppraisalService.getSubLocations(this._loanAppraisalId).subscribe(response => {
                    this.dataSource = new MatTableDataSource(response._embedded.subLocationDetails);
                });
            }
        });    
    }

    /**
     * openDeleteDialog()
     */
    // openDeleteDialog(): void {
    //     const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
    //     // Subscribe to the dialog close event to intercept the action taken.
    //     dialogRef.afterClosed().subscribe((result) => { 
    //         if (result.response) {
    //             this._loanAppraisalService.deleteSiteVisit(this.selectedSubLocation.id).subscribe(() => {
    //                 this._loanAppraisalService.getSiteVisits(this._loanApplicationId).subscribe(response => {
    //                     this.dataSource = new MatTableDataSource(response._embedded.siteVisits);
    //                 });
    //             });
    //         }
    //     });
    // }
}
