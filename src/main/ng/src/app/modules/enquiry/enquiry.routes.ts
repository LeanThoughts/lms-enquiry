import { Routes } from "@angular/router";
import { EnquiryUploadComponent } from "./enquiry-upload/enquiry-upload.component";
import { routeInterceptor } from "../../route.interceptor";
import { modulePermissionGuard } from "../auth/module-permission.guard";

export default [
    {
        path: 'enquiry-upload',
        component: EnquiryUploadComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'UploadLoanEnquiries.Display' },
    },
] as Routes;
