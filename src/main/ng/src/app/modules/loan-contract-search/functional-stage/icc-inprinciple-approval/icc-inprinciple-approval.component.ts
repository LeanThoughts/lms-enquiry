import { Component, OnDestroy, OnInit } from '@angular/core';
import { ComponentNgxComponent } from '../../../../common/component-ngx/component-ngx.component';
import { ButtonComponent, LayoutGridModule } from '@fundamental-ngx/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { IconTabBarComponent, IconTabBarTabComponent } from '@fundamental-ngx/platform/icon-tab-bar';
import { LoanContractSearchService } from '../../loan-contract-search.service';
import { IccInprincipleApprovalService } from './icc-inprinciple-approval.service';
import { GenericListComponent } from '../../../generic/generic-list/generic-list.component';

@Component({
    selector: 'app-icc-inprinciple-approval',
    imports: [
        ButtonComponent,
        ComponentNgxComponent,
        LayoutGridModule,
        IconTabBarComponent,
        IconTabBarTabComponent,
        GenericListComponent
    ],
    templateUrl: './icc-inprinciple-approval.component.html'
})
export class ICCInprincipleApprovalComponent implements OnInit, OnDestroy {

    disableSendForApproval: boolean = false;

    title: string = '';

    private destroy$ = new Subject<void>();

    loanApplicationId: string = '';
    iccInprincipleApprovalId: string = '';

    selectedEnquiry: any;
    selectedIccInprincipleApproval: any;

    /**
     * Constructor
     */
    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private loanContractSearchService: LoanContractSearchService,
        private iccInprincipleApprovalService: IccInprincipleApprovalService
    ) 
    {
        this.loanApplicationId = this.route.snapshot.params['loanApplicationId'];
        this.iccInprincipleApprovalId = this.route.snapshot.params['iccInprincipleApprovalId'];
        console.log('loanApplicationId is', this.loanApplicationId);
        console.log('iccInprincipleApprovalId is', this.iccInprincipleApprovalId);
        
        this.selectedEnquiry = this.loanContractSearchService.selectedEnquiry$.value.loanApplication;
        this.iccInprincipleApprovalService.selectedEntity$.pipe(takeUntil(this.destroy$)).subscribe((entity) => {
            this.selectedIccInprincipleApproval = entity;
        });
    }

    /**
     * On init
     */
    ngOnInit(): void {
        // Set the title
        this.title = this.getTitle();
    }

    /**
     * Get the title for the page
     */
    private getTitle(): string {
        let title = 'ICC In-principle Approval';
        title += (this.selectedEnquiry.loanContractId) ? ` : ${this.selectedEnquiry.loanContractId}` : ` : ${this.selectedEnquiry.enquiryNo}`;
        title += ` / ${this.selectedEnquiry.projectName}`;
        return title;
    }

    /**
     * Send for approval
     */
    sendForApproval() {
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
