import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { NestedListItem, ProductSwitchItem, SideNavigationModel } from "@fundamental-ngx/core";
import { AuthService } from "../../../../modules/auth/auth.service";

const BUSINESS_PARTNERS_DISPLAY = 'BusinessPartners.Display';
const USER_MANAGEMENT_DISPLAY = 'UserManagement.Display';
const WORKFLOW_APPROVERS_DISPLAY = 'WorkflowApprovers.Display';
const OUTSTANDING_APPROVALS_DISPLAY = 'OutstandingApprovals.Display';
const CONFIGURATION_DISPLAY = 'Configuration.Display';
const REFERENCE_INTEREST_RATES_DISPLAY = 'ReferenceInterestRates.Display';
const UPLOAD_LOAN_ENQUIRIES_DISPLAY = 'UploadLoanEnquiries.Display';

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
     */
    public sideNavigationConfiguration(): SideNavigationModel {
        const items: NestedListItem[] = [
            // Folders in menu order. A folder opens and closes when its title is clicked;
            // the third argument is whether it is open when the application starts.
            this.folder('Home', 'home', true, [
                { link: { icon: 'home', title: 'Home', routerLink: 'homepage' } },
                { link: { icon: 'inbox', title: 'Inbox', routerLink: 'inbox' } },
            ]),
        ];

        if (this.authService.hasModulePermission(BUSINESS_PARTNERS_DISPLAY)) {
            items.push(this.folder('Master Data', 'database', true, [
                // { link: { icon: 'home', title: 'New Loan Enquiry', routerLink: 'homepage' } },
                // { link: { icon: 'home', title: 'Enquiry Alerts', routerLink: 'homepage' } },
                // { link: { icon: 'home', title: 'Enquiry List', routerLink: 'homepage' } },
                { link: { icon: 'leads', title: 'Business Partners', routerLink: 'business-partners' } },
            ]));
        }

        items.push(
            this.folder('Loan Processes', 'loan', true, [
                { link: { icon: 'loan', title: 'Loan Contracts List', routerLink: 'loan-contract-search' } },
            ]),
        );

        if (this.authService.hasModulePermission(UPLOAD_LOAN_ENQUIRIES_DISPLAY)) {
            items.push(this.folder('Business Development', 'opportunities', true, [
                { link: { icon: 'upload-to-cloud', title: 'Upload Loan Enquiries', routerLink: 'enquiry-upload' } },
            ]));
        }

        if (this.authService.hasModulePermission(REFERENCE_INTEREST_RATES_DISPLAY)) {
            items.push(this.folder('Risk Department', 'trend-up', true, [
                { link: { icon: 'trend-up', title: 'Reference Interest Rates', routerLink: 'reference-interest-rates' } },
            ]));
        }

        // Reports app: the catalogue of reports by module, each report launched from its module's list
        items.push({ link: { icon: 'bar-chart', title: 'Reports', routerLink: 'reports' } });

        const administrationItems: NestedListItem[] = [
            { link: { icon: 'connected', title: 'SAP Integration Log', routerLink: 'sap-integration-log' } },
        ];
        if (this.authService.hasModulePermission(USER_MANAGEMENT_DISPLAY)) {
            administrationItems.push({ link: { icon: 'user-settings', title: 'User Management', routerLink: 'user-management' } });
        }
        if (this.authService.hasModulePermission(WORKFLOW_APPROVERS_DISPLAY)) {
            administrationItems.push({ link: { icon: 'workflow-tasks', title: 'Workflow Approvers', routerLink: 'workflow-approvers' } });
        }
        if (this.authService.hasModulePermission(OUTSTANDING_APPROVALS_DISPLAY)) {
            administrationItems.push({ link: { icon: 'task', title: 'Outstanding Approvals', routerLink: 'outstanding-approvals' } });
        }
        // { link: { icon: 'home', title: 'Email Events', routerLink: 'homepage' } },
        if (administrationItems.length > 0) {
            items.push(this.folder('Administration', 'user-settings', false, administrationItems));
        }

        // Configuration workspace: its apps are on the tabs of the workspace (by module)
        if (this.authService.hasModulePermission(CONFIGURATION_DISPLAY)) {
            items.push({ link: { icon: 'action-settings', title: 'Configuration', routerLink: 'configuration' } });
        }

        return {
            condensed: false,
            mainNavigation: { items }
        };
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

    /**
     * Menu folder: opens and closes from its arrow and from its title.
     */
    private folder(title: string, icon: string, expanded: boolean, items: NestedListItem[]): NestedListItem {
        const folder: NestedListItem = {
            link: { icon, title, callback: () => folder.expanded = !folder.expanded },
            expanded,
            list: { items }
        };
        return folder;
    }

    /**
     * Side Navigation and Product Switcher Callback function
     */
    callbackFunction(message: string): void {
        alert('Link Clicked ' + message);
    }

}