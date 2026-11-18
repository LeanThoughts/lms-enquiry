import { Routes } from "@angular/router";
import { WorkflowApproverListComponent } from "./workflow-approver-list/workflow-approver-list.component";
import { routeInterceptor } from "../../route.interceptor";
import { modulePermissionGuard } from "../auth/module-permission.guard";
import { WorkflowApproverService } from "./workflow-approver.service";

export default [
    {
        path: 'workflow-approvers',
        component: WorkflowApproverListComponent,
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'WorkflowApprovers.Display' },
        resolve: {
            routeResolvedData: WorkflowApproverService
        }
    },
] as Routes;
