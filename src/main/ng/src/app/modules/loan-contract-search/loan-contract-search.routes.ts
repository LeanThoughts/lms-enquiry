import { Routes } from "@angular/router";
import { LoanContractSearchComponent } from "./loan-contract-search.component";
import { LoanContractSearchService } from "./loan-contract-search.service";
import { routeInterceptor } from "../../route.interceptor";
import { modulePermissionGuard } from "../auth/module-permission.guard";
import { ProcessEnquiryComponent } from "./functional-stage/process-enquiry/process-enquiry.component";
import { ProcessEnquiryService } from "./functional-stage/process-enquiry/process-enquiry.service";
import { ProjectProposalComponent } from "./functional-stage/process-enquiry/project-proposal/project-proposal.component";
import { ProjectProposalService } from "./functional-stage/process-enquiry/project-proposal/project-proposal.service";
import { IccInprincipleApprovalService } from "./functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.service";
import { ICCInprincipleApprovalComponent } from "./functional-stage/icc-inprinciple-approval/icc-inprinciple-approval.component";
import { RiskAssessmentComponent } from "./functional-stage/risk-assessment/risk-assessment.component";
import { RiskAssessmentService } from "./functional-stage/risk-assessment/risk-assessment.service";
import { ApplicationFeeComponent } from "./functional-stage/application-fee/application-fee.component";
import { ApplicationFeeService } from "./functional-stage/application-fee/application-fee.service";
import { BmcApprovalComponent } from "./functional-stage/bmc-approval/bmc-approval.component";
import { BmcApprovalService } from "./functional-stage/bmc-approval/bmc-approval.service";
import { BoardApprovalComponent } from "./functional-stage/board-approval/board-approval.component";
import { BoardApprovalService } from "./functional-stage/board-approval/board-approval.service";
import { SanctionComponent } from "./functional-stage/sanction/sanction.component";
import { SanctionService } from "./functional-stage/sanction/sanction.service";

export default [
    {
        path: 'loan-contract-search',
        component: LoanContractSearchComponent,
        resolve: {
            routeResolver: LoanContractSearchService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.Display' },
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId',
        component: ProcessEnquiryComponent,
        resolve: {
            routeResolvedData: ProcessEnquiryService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.ProcessEnquiry.Display' },
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/create-project-proposal',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.ProcessEnquiry.Display' },
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/update-project-proposal/:projectProposalId',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.ProcessEnquiry.Display' },
    },
    {
        path: 'process-enquiry/:enquiryActionId/loanApplication/:loanApplicationId/view-project-proposal/:projectProposalId',
        component: ProjectProposalComponent,
        resolve: {
            routeResolvedData: ProjectProposalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.ProcessEnquiry.Display' },
    },
    {
        path: 'icc-inprinciple-approval/:iccInprincipleApprovalId/loanApplication/:loanApplicationId',
        component: ICCInprincipleApprovalComponent,
        resolve: {
            routeResolvedData: IccInprincipleApprovalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.IccApproval.Display' },
    },
    {
        path: 'risk-assessment/:riskAssessmentId/loanApplication/:loanApplicationId',
        component: RiskAssessmentComponent,
        resolve: {
            routeResolvedData: RiskAssessmentService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.RiskAssessment.Display' },
    },
    {
        path: 'application-fee/:applicationFeeId/loanApplication/:loanApplicationId',
        component: ApplicationFeeComponent,
        resolve: {
            routeResolvedData: ApplicationFeeService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.ApplicationFee.Display' },
    },
    {
        path: 'bmc-approval/:bmcApprovalId/loanApplication/:loanApplicationId',
        component: BmcApprovalComponent,
        resolve: {
            routeResolvedData: BmcApprovalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.BmcApproval.Display' },
    },
    {
        path: 'board-approval/:boardApprovalId/loanApplication/:loanApplicationId',
        component: BoardApprovalComponent,
        resolve: {
            routeResolvedData: BoardApprovalService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.BoardApproval.Display' },
    },
    {
        path: 'sanction/:sanctionId/loanApplication/:loanApplicationId',
        component: SanctionComponent,
        resolve: {
            routeResolvedData: SanctionService
        },
        canActivate: [routeInterceptor, modulePermissionGuard],
        data: { modulePermission: 'LoanContracts.Sanction.Display' },
    },
] as Routes;
