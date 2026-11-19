import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { ProductSwitchItem } from "@fundamental-ngx/core";
import { AuthService } from "../../../../modules/auth/auth.service";

const BUSINESS_PARTNERS_DISPLAY = 'BusinessPartners.Display';
const USER_MANAGEMENT_DISPLAY = 'UserManagement.Display';
const WORKFLOW_APPROVERS_DISPLAY = 'WorkflowApprovers.Display';
const OUTSTANDING_APPROVALS_DISPLAY = 'OutstandingApprovals.Display';
const CONFIGURATION_DISPLAY = 'Configuration.Display';
const REFERENCE_INTEREST_RATES_DISPLAY = 'ReferenceInterestRates.Display';
const UPLOAD_LOAN_ENQUIRIES_DISPLAY = 'UploadLoanEnquiries.Display';
const LOAN_CONTRACTS_DISPLAY = 'LoanContracts.Display';
const SAP_INTEGRATION_LOG_DISPLAY = 'SapIntegrationLog.Display';

/** One entry in the BTP side navigation. A parent without routerLink only expands and collapses. */
export interface SideNavigationItem {
    title: string;
    icon: string;
    routerLink?: string;
    expanded?: boolean;
    children?: SideNavigationItem[];
}

@Injectable({
    providedIn: 'root'
})
export class SideNavigationService {

    /**
     * Constructor
     */
    constructor(
        private router: Router,
        private authService: AuthService
    ) { }

    /**
     * Main navigation for the signed-in user. Business Partners is included only with BusinessPartners.Display.
     * Selection follows the route, so opening Home or Inbox clears Business Partners.
     */
    public navigationItems(): SideNavigationItem[] {
        const items: SideNavigationItem[] = [
            {
                title: 'Home',
                icon: 'home',
                expanded: true,
                children: [
                    { title: 'Home', icon: 'home', routerLink: '/homepage' },
                    { title: 'Inbox', icon: 'inbox', routerLink: '/inbox' },
                ]
            }
        ];

        if (this.authService.hasModulePermission(BUSINESS_PARTNERS_DISPLAY)) {
            items.push({
                title: 'Master Data',
                icon: 'database',
                expanded: true,
                children: [
                    { title: 'Business Partners', icon: 'leads', routerLink: '/business-partners' },
                ]
            });
        }

        if (this.hasLoanContractAccess()) {
            items.push({
                title: 'Loan Processes',
                icon: 'loan',
                expanded: true,
                children: [
                    { title: 'Loan Contracts List', icon: 'loan', routerLink: '/loan-contract-search' },
                ]
            });
        }

        if (this.authService.hasModulePermission(UPLOAD_LOAN_ENQUIRIES_DISPLAY)) {
            items.push({
                title: 'Business Development',
                icon: 'opportunities',
                expanded: true,
                children: [
                    { title: 'Upload Loan Enquiries', icon: 'upload-to-cloud', routerLink: '/enquiry-upload' },
                ]
            });
        }

        if (this.authService.hasModulePermission(REFERENCE_INTEREST_RATES_DISPLAY)) {
            items.push({
                title: 'Risk Department',
                icon: 'trend-up',
                expanded: true,
                children: [
                    { title: 'Reference Interest Rates', icon: 'trend-up', routerLink: '/reference-interest-rates' },
                ]
            });
        }

        items.push({ title: 'Reports', icon: 'bar-chart', routerLink: '/reports' });

        const administrationChildren: SideNavigationItem[] = [];
        if (this.authService.hasModulePermission(SAP_INTEGRATION_LOG_DISPLAY)) {
            administrationChildren.push({ title: 'SAP Integration Log', icon: 'connected', routerLink: '/sap-integration-log' });
        }
        if (this.authService.hasModulePermission(USER_MANAGEMENT_DISPLAY)) {
            administrationChildren.push({ title: 'User Management', icon: 'user-settings', routerLink: '/user-management' });
        }
        if (this.authService.hasModulePermission(WORKFLOW_APPROVERS_DISPLAY)) {
            administrationChildren.push({ title: 'Workflow Approvers', icon: 'workflow-tasks', routerLink: '/workflow-approvers' });
        }
        if (this.authService.hasModulePermission(OUTSTANDING_APPROVALS_DISPLAY)) {
            administrationChildren.push({ title: 'Outstanding Approvals', icon: 'task', routerLink: '/outstanding-approvals' });
        }
        if (administrationChildren.length > 0) {
            items.push({
                title: 'Administration',
                icon: 'user-settings',
                expanded: false,
                children: administrationChildren
            });
        }

        if (this.authService.hasModulePermission(CONFIGURATION_DISPLAY)) {
            items.push({ title: 'Configuration', icon: 'action-settings', routerLink: '/configuration' });
        }

        return items;
    }

    /**
     * Product switcher for the signed-in user. Business Partners is included only with BusinessPartners.Display.
     */
    public productSwitcher(): ProductSwitchItem[] {
        const products: ProductSwitchItem[] = [
            {
                title: 'Home',
                subtitle: 'Homepage',
                icon: 'home',
                callback: () => this.router.navigate(['/homepage']),
                disabledDragAndDrop: true,
                stickToPosition: true
            },
        ];

        if (this.authService.hasModulePermission(BUSINESS_PARTNERS_DISPLAY)) {
            products.push({
                title: 'Business Partners',
                subtitle: 'Manage Business Partners',
                icon: 'customer-and-contacts',
                callback: () => this.router.navigate(['/business-partners']),
                disabledDragAndDrop: true,
                stickToPosition: true
            });
        }

        return products;
    }

    /** The loan list is available with LoanContracts.Display or any one stage Display permission. */
    private hasLoanContractAccess(): boolean {
        return [
            LOAN_CONTRACTS_DISPLAY,
            'LoanContracts.ProcessEnquiry.Display',
            'LoanContracts.IccApproval.Display',
            'LoanContracts.RiskAssessment.Display',
            'LoanContracts.ApplicationFee.Display',
            'LoanContracts.BmcApproval.Display',
            'LoanContracts.BoardApproval.Display',
            'LoanContracts.Sanction.Display',
            'LoanContracts.Collaterals.Display'
        ].some(permission => this.authService.hasModulePermission(permission));
    }

}
