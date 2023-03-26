import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { MatTableDataSource, MatSort, MatDialog } from '@angular/material';
import { fuseAnimations } from '@fuse/animations';
import { LoanMonitoringConstants } from 'app/main/content/model/loanMonitoringConstants';
import { TRAModel } from 'app/main/content/model/tra.model';
import { Subscription } from 'rxjs';
import { LoanEnquiryService } from '../../../enquiry/enquiryApplication.service';
import { LoanMonitoringService } from '../../loanMonitoring.service';
import { TRAStatementUpdateDialogComponent } from '../traStatementUpdate/traStatementUpdate.component';

@Component({
    selector: 'fuse-tra-statement-list',
    templateUrl: './traStatementList.component.html',
    styleUrls: ['./traStatementList.component.scss'],
    animations: fuseAnimations
})
export class TRAStatementListComponent implements OnDestroy {

    dataSource: MatTableDataSource<any>;
    @ViewChild(MatSort) sort: MatSort;

    displayedColumns = [
        'serialNumber', 'documentType', 'periodQuarter','periodYear', 'remarks', 'download'
    ];

    loanApplicationId: string;

    selectedTRA: any;
    selectedTRAStatement: any;

    subscriptions = new Subscription();

    _module = '';

    @Input()
    set module(m: string) {
        this._module = m;
    }

    /**
     * constructor()
     */
    constructor(_loanEnquiryService: LoanEnquiryService, private _loanMonitoringService: LoanMonitoringService, private _dialog: MatDialog) {

        this.loanApplicationId = _loanEnquiryService.selectedLoanApplicationId.value;

        this.subscriptions.add(_loanMonitoringService.selectedTRA.subscribe(data => {
            this.selectedTRA = new TRAModel(data);
            if (this.selectedTRA.id !== '') {
                _loanMonitoringService.getTRAStatements(this.selectedTRA.id).subscribe(data => {
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                });
            }
        }));
    }

    /**
     * @param enquiry
     */
    onSelect(traStatement: any): void {
        this.selectedTRAStatement = traStatement;
    }

    /**
     * getFileURL()
     * @param fileReference 
     */
    getFileURL(fileReference: string): string {
        return 'enquiry/api/download/' + fileReference;
    }

    /**
     * getDocumentType()
     * @param documentType 
     */
    getDocumentType(documentType: string): string {
        const filtered = LoanMonitoringConstants.documentTypes.filter(obj => obj.code === documentType);
        return filtered[0].value;    
    }

    /**
     * updateTRAStatement()
     * @param operation 
     */
    updateTRAStatement(operation: string): void {
        // Open the dialog.
        var data: any;
        if (operation === 'addTRAStatement') {
            data = {
                'operation': operation,
                'loanApplicationId': this.loanApplicationId,
                'selectedTRA': this.selectedTRA,
                'module': this._module
            }
        }
        else {
            data = {
                'operation': operation,
                'selectedTRA': this.selectedTRA,
                'selectedTRAStatement': this.selectedTRAStatement,
                'module': this._module
            }
        }
        const dialogRef = this._dialog.open(TRAStatementUpdateDialogComponent, {
            panelClass: 'fuse-tra-statement-update-dialog',
            width: '750px',
            data: data
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (result.refresh) {
                this._loanMonitoringService.getTRAStatements(this.selectedTRA.id).subscribe(data => {
                    this.dataSource.data = data;
                });
            }
        });    
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }
}
