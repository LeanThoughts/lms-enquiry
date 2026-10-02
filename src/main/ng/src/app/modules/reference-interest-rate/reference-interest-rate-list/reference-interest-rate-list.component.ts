import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { 
    ButtonComponent, 
    DynamicPageComponent, 
    DynamicPageContentComponent, 
    DynamicPageGlobalActionsComponent, 
    DynamicPageHeaderComponent, 
    FormModule, 
    SelectModule, 
    ToolbarComponent 
} from '@fundamental-ngx/core';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { ReferenceInterestRateService } from '../reference-interest-rate.service';
import { GenericListComponent } from '../../generic/generic-list/generic-list.component';
import { MessageService } from '../../../message.service';
import { AuthService } from '../../auth/auth.service';

@Component({
    selector: 'app-reference-interest-rate-list',
    imports: [
        // Dynamic Page Components
        DynamicPageComponent,
        DynamicPageContentComponent,
        DynamicPageGlobalActionsComponent,
        DynamicPageHeaderComponent,
        ToolbarComponent,
        // Other Components and Modules
        ButtonComponent,
        FormModule,
        SelectModule,
        GenericListComponent
    ],
    templateUrl: './reference-interest-rate-list.component.html'
})
export class ReferenceInterestRateListComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    @ViewChild(GenericListComponent) referenceInterestValueList?: GenericListComponent;

    referenceRateTypes: any[] = [];
    selectedReferenceRateType: any = null;
    sendingForApproval: boolean = false;
    
    /**
     * Constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private authService: AuthService,
        private cdr: ChangeDetectorRef,
        private referenceInterestRateService: ReferenceInterestRateService,
        private messageService: MessageService
    ) {}

    /**
     * On init
     */
    ngOnInit(): void {
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
            this.referenceRateTypes = data['routeResolver'].referenceRateTypes;
            // The inbox sets the rate type code of the task being reviewed before navigating here
            const referenceInterestRateTypeCode = this.referenceInterestRateService.referenceInterestRateTypeCode;
            if (referenceInterestRateTypeCode) {
                this.referenceInterestRateService.referenceInterestRateTypeCode = null;
                this.selectedReferenceRateType = this.referenceRateTypes.find((type: any) => type.code === referenceInterestRateTypeCode) ?? null;
            }
        });
    }

    /**
     * Id of the selected reference rate type, used by the generic list to fetch its values
     */
    get selectedReferenceRateTypeId(): string {
        return this.selectedReferenceRateType ? String(this.selectedReferenceRateType.id) : '';
    }

    /**
     * Reload the values when another reference rate type is selected. The first selection creates the list, which loads on init.
     */
    onReferenceRateTypeChange(): void {
        const list = this.referenceInterestValueList;
        if (list) {
            this.cdr.detectChanges();
            list.clearSelection();
            list.fetchData();
        }
    }

    /**
     * Whether the selected value can be sent for approval. Only changed values that are not already awaiting approval can be sent.
     */
    isSendForApprovalDisabled(): boolean {
        const selected = this.referenceInterestValueList?.selectedObject;
        return !selected || selected.workFlowStatusCode === 1 || selected.modificationStatus === 0 || this.sendingForApproval;
    }

    /**
     * Send the selected reference interest value for approval
     */
    sendForApproval(): void {
        const list = this.referenceInterestValueList;
        if (!list?.selectedObject) return;

        this.sendingForApproval = true;
        const { firstName = '', lastName = '', email = '' } = this.authService.currentUser ?? {};
        const name = `${firstName} ${lastName}`.trim();
        this.messageService.showInfo('Please wait while attempting to send reference interest value for approval.', 15000);

        this.referenceInterestRateService.sendReferenceInterestValueForApproval(list.selectedObject.id, name, email).subscribe({
            next: () => {
                this.sendingForApproval = false;
                this.messageService.showSuccess('Reference interest value is sent for approval.');
                list.clearSelection();
                list.fetchData();
            },
            error: (error: any) => {
                this.sendingForApproval = false;
                this.messageService.showError(error.message + '!! Error sending reference interest value for approval. Please try '
                    + 'again. If the problem persists, please contact the administrator.');
            }
        });
    }
    
    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
