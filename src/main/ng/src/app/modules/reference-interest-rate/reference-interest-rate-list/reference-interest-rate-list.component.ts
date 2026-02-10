import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, DialogService, FormModule, IconModule, LayoutGridModule, SelectModule, TableModule } from '@fundamental-ngx/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ReferenceInterestRateService } from '../reference-interest-rate.service';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { ReferenceInterestRateUpdateComponent } from '../reference-interest-rate-update/reference-interest-rate-update.component';
import { DatePipe } from '@angular/common';
import { CustomDialogComponent } from '../../../custom-dialog.component';
import { MessageService } from '../../../message.service';
import { AuthService } from '../../auth/auth.service';

@Component({
    selector: 'app-reference-interest-rate-list',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        DatePipe,
        FormModule,
        IconModule,
        LayoutGridModule,
        ReactiveFormsModule,
        SelectModule,
        TableModule
    ],
    templateUrl: './reference-interest-rate-list.component.html'
})
export class ReferenceInterestRateListComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    displayedColumns: string[] = ['Interest Rate Type Code', 'Interest Rate Type Description', 'Valid From Date', 'Interest Rate', 
        'Modification Status Description', 'Work Flow Status Description'];
    referenceRateTypes: any[] = [];
    referenceInterestValues: any[] = [];
    selectedReferenceRateType: any = null;
    selectedReferenceInterestValue: any = null;
    selectedReferenceInterestValueId: SelectionModel<any> = new SelectionModel<any>(false, []);
    
    /**
     * Constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private authService: AuthService,
        private dialog: DialogService,
        private referenceInterestRateService: ReferenceInterestRateService,
        private messageService: MessageService
    ) {}

    /**
     * On init
     */
    ngOnInit(): void {
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
            this.referenceRateTypes = data['routeResolver'].referenceRateTypes;
        });
    }

    /**
     * Get reference interest values
     */
    getReferenceInterestValues(): void {
        if (this.selectedReferenceRateType) {
            this.referenceInterestRateService.getReferenceInterestRates(this.selectedReferenceRateType.id).
                subscribe((response: any) => {
                    this.referenceInterestValues = response;
                    this.selectedReferenceInterestValueId.clear();
                });
        }
    }

    /**
     * Return modification status description
     */
    getModificationStatusDescription(modificationStatus: number): string {
        if (modificationStatus === 0) {
            return 'Not Changed';
        } else if (modificationStatus === 1) {
            return 'Changed';
        } else if (modificationStatus === 2) {
            return 'Marked for Deletion';
        } else {
            return '';
        }
    }
    
    /**
     * Open reference interest rate dialog
     */
    openReferenceInterestRateDialog(operation: string): void {
        const dialogRef = this.dialog.open(ReferenceInterestRateUpdateComponent, {
            data: {
                operation: operation,
                referenceRateType: this.selectedReferenceRateType,
                referenceInterestValue: this.selectedReferenceInterestValue
            },
            width: operation === 'View' ? '30rem' : '40rem'
        });
        dialogRef.afterClosed.pipe(takeUntil(this.destroy$)).subscribe({
            next: (result: any) => {
                if (result === 'Updated') {
                    this.getReferenceInterestValues();
                }
            },
            error: (error: any) => {
                console.error(error);
            }
        });
    }
    
    /**
     * Delete reference interest rate value
     */
    deleteReferenceInterestRateValue(): void {
        const dialogRef = this.dialog.open(CustomDialogComponent, {
            data: {
                title: 'Delete Reference Interest Rate Value',
                description: 'Are you sure you want to delete this reference interest rate value? This will impact cash flows of existing '
                    + 'loans in the system.',
            }
        });
        // Subscribe to the dialog close event to intercept the action taken.
        dialogRef.afterClosed.subscribe({
            next: (result: any) => {
                if (result.continue) {
                    this.referenceInterestRateService.deleteReferenceInterestRate(this.selectedReferenceInterestValue.id).subscribe({
                        next: () => {
                            this.getReferenceInterestValues();
                            this.messageService.showSuccess('Reference interest rate value deleted successfully');
                        },
                        error: (error: any) => {
                            this.messageService.showError(error.message + '!! Error deleting reference interest rate value. Please try '
                                + 'again. If the problem persists, please contact the administrator.');
                        }
                    });
                }
            }
        });
    }

    /**
     * Send reference interest value for approval
     */
    sendForApproval(): void {
        if (this.selectedReferenceInterestValue.workFlowStatusDescription === 'Not Sent for Approval') {
            if (this.selectedReferenceInterestValue.modificationStatus === 0) {
                this.messageService.showError('Reference Interest Value is already sent for approval. Only modified values can be sent '
                    + 'for approval.');
            } else {
                let name = this.authService.currentUser.firstName + ' ' + this.authService.currentUser.lastName;
                let email = this.authService.currentUser.email;
                this.messageService.showInfo('Please wait while attempting to send reference interest value for approval.', 15000);
                this.referenceInterestRateService.sendReferenceInterestValueForApproval(this.selectedReferenceInterestValue.id, name, email).subscribe({
                    next: () => {
                        this.messageService.showSuccess('Reference Interest Value is sent for approval.');
                        this.getReferenceInterestValues();
                    },
                    error: (error: any) => {
                        this.messageService.showError(error.message + '!! Error sending reference interest value for approval. Please try '
                            + 'again. If the problem persists, please contact the administrator.');
                    }
                });
            }
        }
    }
    
    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
