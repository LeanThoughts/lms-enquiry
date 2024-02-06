import { Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog, MatSnackBar } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanEnquiryService } from '../../enquiry/enquiryApplication.service';
import { DocumentationService } from '../documentation.service';
import { ConfirmationDialogComponent } from '../../appraisal/confirmationDialog/confirmationDialog.component';
import { NodalOfficerUpdateDialogComponent } from '../nodalOfficerUpdate/nodalOfficerUpdate.component';

@Component({
    selector: 'fuse-nodal-officers',
    templateUrl: './nodalOfficers.component.html',
    styleUrls: ['./nodalOfficers.component.scss'],
    animations: fuseAnimations
})
export class NodalOfficersComponent {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    loanApplicationId: string;

    displayedColumns = [
        'serialNumber', 'bpCode', 'name', 'startDate', 'endDate'
    ];

    selectedNodalOfficer: any;

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _documentationService: DocumentationService, private _matDialog: MatDialog,
            private _matSnackBar: MatSnackBar) {
                
        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;
        _documentationService.getNodalOfficers().subscribe(data => {
            this.dataSource = new MatTableDataSource(data._embedded.nodalOfficers);
            this.dataSource.sort = this.sort;
        });
    }

    /**
     * onSelect()
     */
    onSelect(counsel: any): void {
        this.selectedNodalOfficer = counsel;
    }

    /**
     * addNodalOfficer()
     */
    addNodalOfficer(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(NodalOfficerUpdateDialogComponent, {
            panelClass: 'fuse-nodal-officer-update-dialog',
            width: '750px',
            data: {
                operation: 'addNodalOfficer',
                loanApplicationId: this.loanApplicationId
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                if (this._documentationService._documentation.value.id !== '') {
                    this._documentationService.getNodalOfficers().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.nodalOfficers);
                        this.dataSource.sort = this.sort;
                    });    
                }
                else {
                    this._documentationService.getDocumentation(this.loanApplicationId).subscribe(data => {
                        this._documentationService._documentation.next(data);
                        this._documentationService.getNodalOfficers().subscribe(data => {
                            this.dataSource = new MatTableDataSource(data._embedded.nodalOfficers);
                            this.dataSource.sort = this.sort;
                        });  
                    });
                }
            }
        });    
    }

    /**
     * updateNodalOfficer()
     */
    updateNodalOfficer(): void {
        // Open the dialog.
        const dialogRef = this._matDialog.open(NodalOfficerUpdateDialogComponent, {
            panelClass: 'fuse-nodal-officer-update-dialog',
            width: '750px',
            data: {
                operation: 'updateNodalOfficer',
                loanApplicationId: this.loanApplicationId,
                selectedNodalOfficer: this.selectedNodalOfficer
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._documentationService.getNodalOfficers().subscribe(data => {
                    this.dataSource = new MatTableDataSource(data._embedded.nodalOfficers);
                    this.dataSource.sort = this.sort;
                });    
            }
        });    
    }

    delete(): void {
        const dialogRef = this._matDialog.open(ConfirmationDialogComponent);
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((response) => {
            if (response) {
                this._documentationService.deleteNodalOfficer(this.selectedNodalOfficer.id).subscribe(() => {
                    this.selectedNodalOfficer = undefined;
                    this._documentationService.getNodalOfficers().subscribe(data => {
                        this.dataSource = new MatTableDataSource(data._embedded.nodalOfficers);
                        this.dataSource.sort = this.sort;
                    });
                },
                (error) => {
                    this._matSnackBar.open('Unable to delete selected nodal officer', 'OK', { duration: 7000 });
                });
            }
        });
    }
}
