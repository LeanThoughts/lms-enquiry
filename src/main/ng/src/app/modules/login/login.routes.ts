import { Routes } from "@angular/router";
import { LoginComponent } from "./login.component";
import { routeInterceptor } from "../../route.interceptor";

export default [
    {
        path: 'login',
        component: LoginComponent,
        // canActivate: [routeInterceptor]
    },
] as Routes;
