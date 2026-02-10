import { Routes } from "@angular/router";
import { InboxComponent } from "./inbox.component";
import { InboxService } from "./inbox.service";
import { routeInterceptor } from "../../route.interceptor";

export default [
    {
        path: 'inbox',
        component: InboxComponent,
        resolve: {
            routeResolver: InboxService
        },
        // canActivate: [routeInterceptor],
    },
] as Routes;
