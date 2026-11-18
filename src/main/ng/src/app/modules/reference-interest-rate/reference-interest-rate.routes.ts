import { Routes } from "@angular/router";
import { ReferenceInterestRateListComponent } from "./reference-interest-rate-list/reference-interest-rate-list.component";
import { routeInterceptor } from "../../route.interceptor";
import { modulePermissionGuard } from "../auth/module-permission.guard";
import { ReferenceInterestRateService } from "./reference-interest-rate.service";

export default [
    {
        path: 'reference-interest-rates',
        component: ReferenceInterestRateListComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'ReferenceInterestRates.Display' },
        resolve: {
            routeResolver: ReferenceInterestRateService
        }
    },
] as Routes;
