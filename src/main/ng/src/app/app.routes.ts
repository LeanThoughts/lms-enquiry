import { Routes } from '@angular/router';
import homepageRoutes from './modules/homepage/homepage.routes';
import enquiryRoutes from './modules/enquiry/enquiry.routes';
import { LayoutComponent } from './common/layout/layout.component';
import loginRoutes from './modules/login/login.routes';
import loanContractSearchRoutes from './modules/loan-contract-search/loan-contract-search.routes';
import inboxRoutes from './modules/inbox/inbox.routes';
import referenceInterestRateRoutes from './modules/reference-interest-rate/reference-interest-rate.routes';
import businessPartnerSearchRoutes from './modules/business-partner-search/business-partner-search.routes';

export const routes: Routes = [

    // Unauthenticated routes
    {
        path: '',
        redirectTo: '/homepage',
        pathMatch: 'full'
    },
    ...loginRoutes,

    // Authenticated routes
    {
        path: '',
        component: LayoutComponent,
        children: [
            ...homepageRoutes,
            ...enquiryRoutes,
            ...loanContractSearchRoutes,
            ...inboxRoutes,
            ...referenceInterestRateRoutes,
            ...businessPartnerSearchRoutes,
        ]
    },

    // {
    //     path: '',
    //     children: [
    //         ...loginRoutes,
    //     ]
    // }
];
