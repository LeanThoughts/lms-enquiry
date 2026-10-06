import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { CollateralListComponent } from './collateral-list/collateral-list.component';
import { CollateralDetailComponent } from './collateral-detail/collateral-detail.component';

/**
 * Collateral Management (SAP "PFS Check List"). Opened from Loan Contract Search with the "Collaterals" action.
 * Every signed-in user can display; Create, Change and Delete are shown to the write roles only.
 */
export default [
    {
        path: 'collateral-management/loan/:loanApplicationId',
        component: CollateralListComponent,
        canActivate: [routeInterceptor]
    },
    {
        // Opened from a workflow task in the inbox (business process id = checklist id)
        path: 'collateral-management/checklist/:checklistId',
        component: CollateralListComponent,
        canActivate: [routeInterceptor]
    },
    {
        path: 'collateral-management/loan/:loanApplicationId/item/new',
        component: CollateralDetailComponent,
        canActivate: [routeInterceptor]
    },
    {
        path: 'collateral-management/item/:itemId',
        component: CollateralDetailComponent,
        canActivate: [routeInterceptor]
    }
    // Checklist ID Number Range is an app of the Configuration workspace (configuration.routes.ts, tab Collaterals)
] as Routes;
