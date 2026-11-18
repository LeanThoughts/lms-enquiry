import { Routes } from "@angular/router";
import { routeInterceptor } from "../../route.interceptor";
import { modulePermissionGuard } from "../auth/module-permission.guard";
import { BusinessPartnerUpdateComponent } from "./business-partner-update/business-partner-update.component";
import { BusinessPartnerSearchService } from "./business-partner-search.service";
import { BusinessPartnerSearchComponent } from "./business-partner-search.component";
import { PartnerDetailsUpdateComponent } from "./business-partner-update/partner-details-update/partner-details-update.component";

export default [
    {
        path: 'business-partners',
        component: BusinessPartnerSearchComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'BusinessPartners.Display' },
        resolve: {
            routeResolvedData: BusinessPartnerSearchService
        }
    },
    {
        path: 'business-partners/profile/create',
        component: PartnerDetailsUpdateComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'BusinessPartners.Display' },
        resolve: {
            routeResolvedData: BusinessPartnerSearchService
        }
    },
    {
        path: 'business-partners/profile/update/:id',
        component: PartnerDetailsUpdateComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'BusinessPartners.Display' },
        resolve: {
            routeResolvedData: BusinessPartnerSearchService
        }
    },
    {
        path: 'business-partners/:operation/:id',
        component: BusinessPartnerUpdateComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'BusinessPartners.Display' },
        resolve: {
            routeResolvedData: BusinessPartnerSearchService
        }
    }
] as Routes;
