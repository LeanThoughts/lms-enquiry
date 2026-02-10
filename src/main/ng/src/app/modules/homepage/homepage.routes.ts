import { Routes } from "@angular/router";
import { HomepageComponent } from "./homepage.component";
import { routeInterceptor } from "../../route.interceptor";

export default [
    {
        path: 'homepage',
        component: HomepageComponent,
        // canActivate: [routeInterceptor],
    },
] as Routes;
