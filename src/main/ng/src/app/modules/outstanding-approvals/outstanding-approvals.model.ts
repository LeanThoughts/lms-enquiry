/** Outstanding Workflow Approvals report (Administration). */

export interface ApprovalRow {
    taskId: string;
    taskName: string;
    processName: string;
    processLabel: string;
    businessProcessId: string;
    loanContractId: string;
    projectName: string;
    requestorName: string;
    requestorEmail: string;
    approverName: string;
    approverEmail: string;
    assignee: string;
    createdOn: string;
    ageDays: number;
    priority: number;
    processInstanceId: string;
}

export interface ApprovalCount {
    key: string;
    label: string;
    count: number;
}

export interface ApprovalSummary {
    total: number;
    distinctApprovers: number;
    oldestAgeDays: number;
    averageAgeDays: number;
    overdue: number;
    overdueThresholdDays: number;
    byProcess: ApprovalCount[];
    byApprover: ApprovalCount[];
}

export interface ApprovalReportPage {
    rows: ApprovalRow[];
    summary: ApprovalSummary;
    page: number;
    size: number;
    total: number;
    processes: ApprovalCount[];
    approvers: string[];
    generatedOn: string;
    canView: boolean;
    userRole: string | null;
}

export interface ApprovalFilter {
    processName: string;
    approver: string;
    requestor: string;
    search: string;
    minAgeDays: number | null;
}

export const APPROVAL_PAGE_SIZES = [10, 25, 50, 100];
