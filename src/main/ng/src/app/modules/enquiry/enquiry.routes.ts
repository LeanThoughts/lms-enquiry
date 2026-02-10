import { Routes } from "@angular/router";
import { EnquiryUploadComponent } from "./enquiry-upload/enquiry-upload.component";
import { routeInterceptor } from "../../route.interceptor";

export default [
    {
        path: 'enquiry-upload',
        component: EnquiryUploadComponent,
        canActivate: [routeInterceptor],
    },
] as Routes;
