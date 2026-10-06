import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { NestedListItem, ProductSwitchItem, SideNavigationModel } from "@fundamental-ngx/core";

@Injectable({
    providedIn: 'root'
})
export class SideNavigationService {

    public sideNavigationConfiguration: SideNavigationModel = {
        condensed: false,
        mainNavigation: {
            items: [
                // Folders in menu order. A folder opens and closes when its title is clicked;
                // the third argument is whether it is open when the application starts.
                this.folder('Home', 'home', true, [
                    { link: { icon: 'home', title: 'Home', routerLink: 'homepage' } },
                    { link: { icon: 'inbox', title: 'Inbox', routerLink: 'inbox' } },
                ]),
                this.folder('Master Data', 'database', true, [
                    // { link: { icon: 'home', title: 'New Loan Enquiry', routerLink: 'homepage' } },
                    // { link: { icon: 'home', title: 'Enquiry Alerts', routerLink: 'homepage' } },
                    // { link: { icon: 'home', title: 'Enquiry List', routerLink: 'homepage' } },
                    { link: { icon: 'leads', title: 'Business Partners', routerLink: 'business-partners' } },
                ]),
                this.folder('Loan Processes', 'loan', true, [
                    { link: { icon: 'loan', title: 'Loan Contracts List', routerLink: 'loan-contract-search' } },
                ]),
                this.folder('Business Development', 'opportunities', true, [
                    { link: { icon: 'upload-to-cloud', title: 'Upload Loan Enquiries', routerLink: 'enquiry-upload' } },
                ]),
                this.folder('Risk Department', 'trend-up', true, [
                    { link: { icon: 'trend-up', title: 'Reference Interest Rates', routerLink: 'reference-interest-rates' } },
                ]),
                this.folder('Administration', 'user-settings', false, [
                    { link: { icon: 'user-settings', title: 'User Management', routerLink: 'user-management' } },
                    { link: { icon: 'workflow-tasks', title: 'Workflow Approvers', routerLink: 'workflow-approvers' } },
                    // { link: { icon: 'home', title: 'Email Events', routerLink: 'homepage' } },
                ]),
                // Configuration workspace: its apps are on the tabs of the workspace (by module)
                { link: { icon: 'action-settings', title: 'Configuration', routerLink: 'configuration' } },

                // { headerTitle: 'Reports' },
                // { link: { icon: 'home', title: 'Change History', routerLink: 'homepage' } }
            ]
        }
        // utilityNavigation: {
        //     textOnly: true,
        //     items: [
        //         {
        //             headerTitle: 'Others'
        //         },
        //         {
        //             link: {
        //                 title: 'Link 1'
        //             }
        //         },
        //         {
        //             link: {
        //                 title: 'Link 2'
        //             }
        //         },
        //         { link: { icon: 'upload-to-cloud', title: 'Upload Enquiries', routerLink: 'enquiry-upload' } },
        //     ]
        // }
    };

    public productSwitcher: ProductSwitchItem[] = [
        {
            title: 'Home',
            subtitle: 'Homepage',
            icon: 'home',
            callback: () => this.router.navigate(['/homepage']),
            disabledDragAndDrop: true,
            stickToPosition: true
        },
        {
            title: 'Business Partners',
            subtitle: 'Manage Business Partners',
            icon: 'customer-and-contacts',
            callback: () => this.router.navigate(['/business-partners']),
            disabledDragAndDrop: true,
            stickToPosition: true
        },
    ]

    /**
     * Constructor
     */
    constructor(private router: Router) { }

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