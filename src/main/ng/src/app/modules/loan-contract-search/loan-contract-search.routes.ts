import { Routes } from "@angular/router";
import { LoanContractSearchComponent } from "./loan-contract-search.component";
import { LoanContractSearchService } from "./loan-contract-search.service";
import { routeInterceptor } from "../../route.interceptor";
import { ProcessEnquiryComponent } from "./functional-stage/process-enquiry/process-enquiry.component";
import { ProcessEnquiryService } from "./functional-stage/process-enquiry/process-enquiry.service";
import { ProjectProposalComponent } from "./functional-stage/process-enquiry/project-proposal/project-proposal.component";
import { ProjectProposalService } from "./functional-stage/process-enquiry/project-proposal/project-proposal.service";
import { IccInprincipleApprovalService } from "./functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";
import { ICCInprincipleApprovalComponent } from "./functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.component";

export default [
    {
        path: 'loan-contract-search',
        component: LoanContractSearchComponent,
        resolve: {
            routeResolver: LoanContractSearchService
        },
        canActivate: [routeInterceptor],
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId',
        component: ProcessEnquiryComponent,
        resolve: {
            routeResolvedData: ProcessEnquiryService
        },
        canActivate: [routeInterceptor],
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/create-project-proposal',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor],
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/update-project-proposal/:projectProposalId',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor],
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/view-project-proposal/:projectProposalId',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor],
    },
    {
        path: 'icc-inprinciple-approval/:iccInprincipleApprovalId/loanApplication/:loanApplicationId',
        component: ICCInprincipleApprovalComponent,
        canActivate: [routeInterceptor],
    },
] as Routes;
