import { Component, OnInit, ViewChild } from '@angular/core';
import { InboxService } from './inbox.service';
import { MatDialog, MatSnackBar, MatSnackBarModule } from '@angular/material';
import { Router } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { InboxItemsComponent } from './inbox-items/inbox-items.component';
import { EnquiryAlertsService } from '../enquiry/enquiryAlerts/enquiryAlerts.service';
import { BehaviorSubject } from 'rxjs';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { RejectMessageDialogComponent } from './rejectMessageDialog/rejectMessageDialog.component';
import { EnquiryApplicationModel } from '../../model/enquiryApplication.model';

@Component({
    selector: 'app-inbox',
    templateUrl: './inbox.component.html',
    styleUrls: ['./inbox.component.scss'],
    animations: fuseAnimations
})
export class InboxComponent implements OnInit {

    @ViewChild(InboxItemsComponent) inboxItemsComponent: InboxItemsComponent;
    
    constructor(private _inboxService: InboxService ,
                private _matSnackBar: MatSnackBar,
                private _router: Router, 
                private _loanEnquiryService: LoanEnquiryService,
                private _dialogRef: MatDialog) {
        
    }

    ngOnInit(): void {
    }

    /**
     * reviewTask()
     */
     reviewTask(): void {
        this._matSnackBar.open('Review in Process.', 'OK', { duration: 10000 });
        this._loanEnquiryService.getLoanApplicationByLoanContractId(this.inboxItemsComponent.selectedItem.lanContractId).subscribe(response => {
            this._loanEnquiryService.selectedEnquiry.next(new EnquiryApplicationModel(response));
            if (this._loanEnquiryService.selectedLoanApplicationId !== undefined) {
                this._loanEnquiryService.selectedLoanApplicationId.next(response.loanApplication.id);
                this._loanEnquiryService.selectedLoanApplicationPartyNumber.next(response.loanApplication.busPartnerNumber);
            }
            else {
                this._loanEnquiryService.selectedLoanApplicationId = new BehaviorSubject(response.loanApplication.id);
                this._loanEnquiryService.selectedLoanApplicationPartyNumber = new BehaviorSubject(response.loanApplication.busPartnerNumber);
            }

            let selectedInboxItem = this.inboxItemsComponent.selectedItem;
            if (selectedInboxItem.processName === 'EnquiryAction')
                this._router.navigate(['/enquiryAction']);
            else if (selectedInboxItem.processName === 'ICCApproval')
                this._router.navigate(['/iccApprovalStage']);
            else if (selectedInboxItem.processName === 'Prelim Risk Assessment')
                this._router.navigate(['/riskAssessment']);
            else if (selectedInboxItem.processName === 'ApplicationFee')
                this._router.navigate(['/applicationFee']);
            else if (selectedInboxItem.processName === 'Appraisal')
                this._router.navigate(['/loanAppraisal']);
            else if (selectedInboxItem.processName === 'BoardApproval')
                this._router.navigate(['/boardApproval']);
            else if (selectedInboxItem.processName === 'Sanction')
                this._router.navigate(['/sanction']);
            else if (selectedInboxItem.processName === 'Monitoring')
                this._router.navigate(['/loanMonitoring']);
        });
    }

    /**
     * rejectTask()
     */
    rejectTask(): void {
        // Open the dialog.
        const dialogRef = this._dialogRef.open(RejectMessageDialogComponent, {
            panelClass: 'fuse-rejection-reason-dialog',
            width: '750px'
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed().subscribe((result) => { 
            if (!result.cancel) {
                let selectedInboxItem = this.inboxItemsComponent.selectedItem;
                let workFlowProcessRequestResource = {
                    'businessProcessId': selectedInboxItem.businessProcessId,
                    'processName': selectedInboxItem.processName,
                    'processInstanceId': selectedInboxItem.id,
                    'rejectionReason': result.rejectionReason
                }        
                this._matSnackBar.open('Reject in Process.', 'OK', { duration: 25000 });
                this._inboxService.rejectTask(workFlowProcessRequestResource).subscribe(response => {
                    this._matSnackBar.open( 'Selected task is rejected and email notification was sent to requestor', 'Ok', { duration: 7000 });
                    this.inboxItemsComponent.refreshList();
                });            
            }
        }); 
    }

    /**
     * approveTask()
     */
    approveTask(): void {
        let selectedInboxItem = this.inboxItemsComponent.selectedItem;
        let workFlowProcessRequestResource = {
            'businessProcessId': selectedInboxItem.businessProcessId,
            'processName': selectedInboxItem.processName,
            'processInstanceId': selectedInboxItem.id,
            'rejectionReason': ''
        }
        this._matSnackBar.open('Approval in Process.', 'OK', { duration: 25000 });
        this._inboxService.approveTask(workFlowProcessRequestResource).subscribe(response => {
            this._matSnackBar.open( 'Selected task is approved and email notification was sent to requestor', 'Ok', { duration: 7000 });
            this.inboxItemsComponent.refreshList();
        });
    }

    /**
     * refreshTasks()
     */
    refreshTasks(): void {
        this._inboxService.fetchTasks().subscribe(response => {
            this.inboxItemsComponent.inboxItems = response;
        });
        this.inboxItemsComponent.selectedItem = undefined;   
    }
}
