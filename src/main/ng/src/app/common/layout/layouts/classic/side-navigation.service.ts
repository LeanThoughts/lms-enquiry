import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { ProductSwitchItem, SideNavigationModel } from "@fundamental-ngx/core";

@Injectable({
    providedIn: 'root'
})
export class SideNavigationService {

    public sideNavigationConfiguration: SideNavigationModel = {
        condensed: false,
        mainNavigation: {
            items: [
                { headerTitle: 'Home' },
                // { link: { icon: 'home', title: 'Home', callback: () => this.callbackFunction('homepage') } },
                { link: { icon: 'home', title: 'Home', routerLink: 'homepage' } },
                { link: { icon: 'inbox', title: 'Inbox', routerLink: 'inbox' } },
                
                // { headerTitle: 'Administration' },
                // { link: { icon: 'home', title: 'User Management', routerLink: 'homepage' } },
                // { link: { icon: 'home', title: 'Email Events', routerLink: 'homepage' } },

                { headerTitle: 'Applications' },
                // { link: { icon: 'home', title: 'New Loan Enquiry', routerLink: 'homepage' } },
                // { link: { icon: 'home', title: 'Enquiry Alerts', routerLink: 'homepage' } },
                // { link: { icon: 'home', title: 'Enquiry List', routerLink: 'homepage' } },
                { link: { icon: 'leads', title: 'Business Partners', routerLink: 'business-partners' } },

                { headerTitle: 'Loan Processes' },
                { link: { icon: 'loan', title: 'Loan Contracts List', routerLink: 'loan-contract-search' } },
                
                // { headerTitle: 'Business Development' },
                // { link: { icon: 'upload-to-cloud', title: 'Upload Loan Enquiries', routerLink: 'enquiry-upload' } },
                
                // { headerTitle: 'Risk Department' },
                // { link: { icon: 'home', title: 'Reference Interest Rates', routerLink: 'reference-interest-rates' } },
                
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
     * Side Navigation and Product Switcher Callback function
     */
    callbackFunction(message: string): void {
        alert('Link Clicked ' + message);
    }

}