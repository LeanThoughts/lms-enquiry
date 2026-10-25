import { Page, Request, Route } from '@playwright/test';
import valueLists from '../fixtures/value-lists.json';

/** A request the app sent to the (mocked) backend. */
export interface RecordedRequest {
    method: string;
    path: string;
    body: any;
    /** Decoded query parameters of the call */
    query?: Record<string, string>;
}

export interface MockOptions {
    /** Role of the signed-in user; ZLM023, ZLM018 and ZLM035 may change collaterals. */
    role?: string;
    /** Workflow status of the checklist: 0 not sent, 2 sent for approval, 3 approved, 4 rejected. */
    workFlowStatusCode?: number;
    rejectionReason?: string | null;
}

type Row = Record<string, any>;

const WRITE_ROLES = ['ZLM023', 'ZLM018', 'ZLM035'];
const CHILD_KEYS: Record<string, string> = {
    coverages: 'coverages', roc: 'rocEvents', cersai: 'cersaiEvents', nesl: 'neslEvents',
    documents: 'documents', securities: 'securitiesPositions'
};

/**
 * In-memory stand-in for the Collateral Management REST API (/enquiry/api/...). It keeps one loan (L1, contract
 * 0000010003200) with collaterals 912 and 913, applies creates / changes / deletes like the backend (numbering,
 * required fields, workflow lock) and records every request so tests can check what the app sent.
 */
/** ICC Approval Status Report sample (SAP MISIccApprovalStatRpt), two loan proposals. */
const ICC_APPROVAL_ROWS = [
    { fyear: '2026', serialNo: '0001', quarter: '2', ranl: '0000010003814', status: 'Active', bupaId: '0001000234',
        borrowerName: 'Sunrise Power Ltd', projectName: 'Solar Park Phase 2', zzsanctionedAmt: '1250000000.00',
        commitCapital: '800000000.00', loanType: 'Term Loan', sector: 'Renewables', projectType: 'Greenfield',
        grpCompName: 'Sunrise Group', feePct: '1.00', roi: '9.75', iccStatusDesc: 'Approved by ICC',
        iccApprDate: '2026-07-14', ziccRemarks: 'Approved subject to security creation', bodDate: '2026-07-28',
        bodStatusDesc: 'Approved', zboardAppRemarks: 'Noted' },
    { fyear: '2026', serialNo: '0002', quarter: '3', ranl: '0000010003900', status: 'Pending', bupaId: '0001000300',
        borrowerName: 'Himalaya Hydro Pvt Ltd', projectName: 'Run-of-river 60MW', zzsanctionedAmt: '640000000.00',
        commitCapital: '0.00', loanType: 'Bridge Loan', sector: 'Hydro', projectType: 'Brownfield',
        grpCompName: 'Himalaya Group', feePct: '0.75', roi: '10.25', iccStatusDesc: 'Pending with ICC',
        iccApprDate: null, ziccRemarks: null, bodDate: null, bodStatusDesc: 'Awaiting board', zboardAppRemarks: null }
];

/** Loans Disbursement report sample: the table the portal builds from the SAP cells (RowNo, Fieldlabel, Value). */
const DISBURSEMENT_COLUMNS = [
    { key: 'c0', label: 'Loan Contract' }, { key: 'c1', label: 'Borrower' }, { key: 'c2', label: 'Apr-2026' },
    { key: 'c3', label: 'May-2026' }, { key: 'c4', label: 'Total' }
];
const DISBURSEMENT_ROWS = [
    { c0: '0000010003814', c1: 'Sunrise Power Ltd', c2: '1,000.00', c3: '250.50', c4: '1,250.50' },
    { c0: '0000010003900', c1: 'Himalaya Hydro Pvt Ltd', c2: null, c3: '75.00', c4: '75.00' },
    { c0: '0000010004100', c1: 'Coastal Wind Energy', c2: '9,500.00', c3: '125.25-', c4: '9,374.75' }
];

/** Loans Balance List sample (SAP ZFNR7Report), two loan contracts. */
const BALANCE_ROWS = [
    { ranl: '0000010003814', rantrag: '0000200001', bukrs: 'PFS', gsart: '100', santwhr: 'INR', rrefkont: 'REF00001',
        moratorium: 'X', status: 'Disbursed', projectName: 'Solar Park Phase 2', anzwhr: 'INR', sanctionedAmt: '50000000.000',
        bzusage: '0.000', nomkap: '32500000.000', effkap: '31200000.500', tilgng: '1250000.000', zinsen: '845000.250',
        versch: '0.000', ueberz: '0.000', briwr: '0.000', bvzwr: '0.000', bvkwr: '0.000', rbear: 'SAJEEV' },
    { ranl: '0000010003900', rantrag: '0000200002', bukrs: 'PFS', gsart: '200', santwhr: 'USD', rrefkont: 'REF00002',
        moratorium: null, status: 'Sanctioned', projectName: 'Run-of-river 60MW', anzwhr: 'INR', sanctionedAmt: '1000000.000',
        bzusage: '250000.000', nomkap: '0.000', effkap: '0.000', tilgng: '0.000', zinsen: '0.000',
        versch: '0.000', ueberz: '0.000', briwr: '0.000', bvzwr: '0.000', bvkwr: '0.000', rbear: 'ADMIN' }
];

/** Loans KYC Report sample (SAP KycReportSet), two loan contracts. */
const KYC_ROWS = [
    { contractNo: '0000010003814', status: '60', serialNo: '0001', partnerId: '0001000234', partnerName: 'Sunrise Power Ltd',
        role: 'Borrower', projectName: 'Solar Park Phase 2', loanAgmtDate: '2025-03-14', kycStatus: 'Compliant',
        kycComplianceDate: '2026-04-10', reKycComplianceDate: '2028-04-10', ckycComplDate: '2026-04-12', ckycRefNo: 'CK0001234',
        pan: 'Yes', panNumber: 'AAACS1234A', coi: 'Yes', moa: 'Yes', aoa: 'Yes', cin: 'U40100DL2010PLC123456',
        lei: '335800ABCDEF1234567', addrProof: 'Yes', boardRes: 'Yes', authSignatory: 'Yes',
        kycRiskCategoryDesc: 'Low', reKycRiskCategoryDesc: 'Low' },
    { contractNo: '0000010003900', status: '60', serialNo: '0002', partnerId: '0001000300', partnerName: 'Himalaya Hydro Pvt Ltd',
        role: 'Borrower', projectName: 'Run-of-river 60MW', loanAgmtDate: '2025-08-01', kycStatus: 'Pending',
        kycComplianceDate: null, reKycComplianceDate: null, ckycComplDate: null, ckycRefNo: null,
        pan: 'Yes', panNumber: 'AAACH5678B', coi: 'Yes', moa: 'No', aoa: 'No', cin: 'U40100HP2012PTC654321',
        lei: null, addrProof: 'No', boardRes: 'No', authSignatory: 'No',
        kycRiskCategoryDesc: 'Medium', reKycRiskCategoryDesc: null }
];

/** Workflow Status Report sample (SAP WorkflowStatusSet), three workflows of two loans. */
const WORKFLOW_STATUS_ROWS = [
    { rowId: '0000000001', bukrs: 'PFS', relobj: 'VDARL', partner: '0001000234', datum: '2026-10-01', relObject: 'Loan Master Data',
        ranl: '0000010003814', parnter: '0001000234', boId: 'PFS0000010003814', initiatorId: 'SAJEEV', initiatorPosition: 'MGR',
        initiatorName: 'Sajeev Khan', initiationDate: '2026-10-01', initiationTime: '09:06:12', wfReferenceNo: 'WF00000123',
        taskText: 'Approve loan master data', approverId: 'ANITA', approverPosition: 'GM', approverName: 'Anita Rao',
        approvalDate: '2026-10-03', approvalTime: '14:20:05', releaseStatus: '02', releaseStatusDesc: 'Released',
        releaseIndicator: 'X', releaseIndicatorDesc: 'Approved' },
    { rowId: '0000000002', bukrs: 'PFS', relobj: 'VDARL', partner: '0001000300', datum: '2026-10-08', relObject: 'Loan Master Data',
        ranl: '0000010003900', parnter: '0001000300', boId: 'PFS0000010003900', initiatorId: 'RAHUL', initiatorPosition: 'AM',
        initiatorName: 'Rahul Verma', initiationDate: '2026-10-08', initiationTime: '11:45:00', wfReferenceNo: 'WF00000131',
        taskText: 'Approve loan master data', approverId: 'ANITA', approverPosition: 'GM', approverName: 'Anita Rao',
        approvalDate: null, approvalTime: null, releaseStatus: '01', releaseStatusDesc: 'Pending',
        releaseIndicator: null, releaseIndicatorDesc: 'Awaiting approval' },
    { rowId: '0000000003', bukrs: 'PFS', relobj: 'VDARL', partner: '0001000234', datum: '2026-09-15', relObject: 'Loan Master Data',
        ranl: '0000010003814', parnter: '0001000234', boId: 'PFS0000010003814', initiatorId: 'SAJEEV', initiatorPosition: 'MGR',
        initiatorName: 'Sajeev Khan', initiationDate: '2026-09-15', initiationTime: '16:02:40', wfReferenceNo: 'WF00000118',
        taskText: 'Change interest conditions', approverId: 'MEERA', approverPosition: 'CFO', approverName: 'Meera Iyer',
        approvalDate: '2026-09-16', approvalTime: '10:00:00', releaseStatus: '03', releaseStatusDesc: 'Rejected',
        releaseIndicator: 'R', releaseIndicatorDesc: 'Rejected' }
];

/** SAP Integration Log sample: one success, one SAP error, one media upload, one transport failure. */
const SAP_LOG_ROWS = [
    { id: 4, requestedAt: '2026-10-12 13:56:15.086', durationMs: 762, httpMethod: 'PUT',
        url: "http://192.168.1.205:8000/sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/DocumentSet(ZidNo='0000001037',ItemId=guid'1589f4c0-8071-4bbc-ae0c-86c6a8af40a7')?sap-client=300",
        serviceName: 'ZPFS_LMS_COLLATERAL_SRV', entitySet: 'DocumentSet', responseStatus: 400, responseStatusText: 'Bad Request', success: false,
        errorMessage: 'HTTP 400 Bad Request: Invalid key predicate [005056A509B11EE1B9A8FEC11C22378E]', sapTransactionId: '6AB73D1A84A1F08AE1000000C0A801CD',
        businessProcess: 'Collateral Management', subProcess: 'Collateral Documents', businessObjectId: '1589f4c0-8071-4bbc-ae0c-86c6a8af40a7', pointerId: 118,
        caller: 'CollateralSapIntegrationService.send', userName: null, requestSize: 312, responseSize: 455 },
    { id: 3, requestedAt: '2026-10-12 13:56:14.410', durationMs: 1310, httpMethod: 'PUT',
        url: "http://192.168.1.205:8000/sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/DocumentAttachmentSet(BusinessObjectName='Document',ZidNo='0000001037')/$value?sap-client=300",
        serviceName: 'ZPFS_LMS_COLLATERAL_SRV', entitySet: 'DocumentAttachmentSet', responseStatus: 204, responseStatusText: 'No Content', success: true,
        errorMessage: null, sapTransactionId: null, businessProcess: 'Collateral Management', subProcess: 'Collateral Documents',
        businessObjectId: '1589f4c0-8071-4bbc-ae0c-86c6a8af40a7', pointerId: 118, caller: 'CollateralSapIntegrationService.sendAttachment', userName: null,
        requestSize: 184320, responseSize: 0 },
    { id: 2, requestedAt: '2026-10-12 13:56:13.902', durationMs: 298, httpMethod: 'POST',
        url: 'http://192.168.1.205:8000/sap/opu/odata/sap/ZPFS_LMS_COLLATERAL_SRV/ChecklistHeaderSet?sap-client=300',
        serviceName: 'ZPFS_LMS_COLLATERAL_SRV', entitySet: 'ChecklistHeaderSet', responseStatus: 201, responseStatusText: 'Created', success: true,
        errorMessage: null, sapTransactionId: null, businessProcess: 'Collateral Management', subProcess: 'Collateral Item',
        businessObjectId: '4e37e4f3-2c9c-4b21-8a35-25e58e90a7d1', pointerId: 117, caller: 'CollateralSapIntegrationService.send', userName: null,
        requestSize: 1480, responseSize: 2210 },
    { id: 1, requestedAt: '2026-10-12 09:02:37.169', durationMs: 21, httpMethod: 'GET',
        url: 'http://192.168.1.205:8000/sap/opu/odata/sap/ZPFS_LOAN_ENQ_PORTAL_SRV/LoanApplicationSet?sap-client=300',
        serviceName: 'ZPFS_LOAN_ENQ_PORTAL_SRV', entitySet: 'LoanApplicationSet', responseStatus: 0, responseStatusText: null, success: false,
        errorMessage: 'ResourceAccessException: I/O error on GET request: Connection refused', sapTransactionId: null,
        businessProcess: null, subProcess: null, businessObjectId: null, pointerId: null, caller: 'SAPIntegrationService.getLoanApplication',
        userName: 'test@pfs.local', requestSize: 0, responseSize: 0 }
];

/** SAP SELECT-OPTIONS semantics for the mock: the JSON lines of a query parameter against a value (includes OR, excludes AND). */
function selected(value: string, json: string | null): boolean {
    if (!json) {
        return true;
    }
    const lines: { sign: string, option: string, low: string, high: string }[] = JSON.parse(json);
    const test = (l: { option: string, low: string, high: string }) => {
        switch (l.option) {
            case 'EQ': return value === l.low;
            case 'NE': return value !== l.low;
            case 'BT': return value >= l.low && value <= l.high;
            case 'NB': return value < l.low || value > l.high;
            case 'GE': return value >= l.low;
            case 'GT': return value > l.low;
            case 'LE': return value <= l.low;
            case 'LT': return value < l.low;
            case 'CP': return l.low.endsWith('*') ? value.startsWith(l.low.slice(0, -1)) : value.endsWith(l.low.replace(/^\*/, ''));
            default: return true;
        }
    };
    const includes = lines.filter(l => l.sign === 'I');
    return (includes.length === 0 || includes.some(test)) && lines.filter(l => l.sign === 'E').every(l => !test(l));
}

export class MockApi {
    readonly requests: RecordedRequest[] = [];
    /** Report variants saved through the mocked /reports/{report}/variants API: { report, ...variant } */
    readonly variants: any[] = [];
    readonly loan = {
        loanApplicationId: 'L1', loanContractId: '0000010003200', enquiryNo: 4711, projectName: 'Solar Park Rajasthan',
        borrowerName: 'PTC India Fin. Serv. Ltd.', borrowerNumber: '1000234', functionalStatusDescription: 'Loan Monitoring Stage'
    };
    readonly checklist: Row;
    readonly items: Row[];
    readonly children = new Map<string, Record<string, Row[]>>();
    readonly numberRange: Row = {
        sapHighestNumber: null, nextNumber: 914, highestNumberInPortal: 913, highestMigratedNumber: 913,
        startNumber: 1, maximumNumber: 9999999999, changedBy: null, changedAt: null
    };
    private nextId = 100;
    private readonly role: string;

    constructor(options: MockOptions = {}) {
        this.role = options.role ?? 'ZLM023';
        this.checklist = {
            id: 'C1', workFlowStatusCode: options.workFlowStatusCode ?? 0,
            workFlowStatusDescription: statusText(options.workFlowStatusCode ?? 0),
            rejectionReason: options.rejectionReason ?? null,
            // technical data (Technical Info)
            createdOn: '2026-10-01', createdAt: '08:15:30.123456', createdByUserName: 'writer@pfs.local',
            changedOn: '2026-10-09', changedAt: '17:45:05', changedByUserName: 'legal@pfs.local'
        };
        this.items = [
            item('I912', 912, { conditionDescription: 'Collateral', collateralObjectType: 'Z00007',
                collateralObjectDescription: 'Personal Guarantee 100000 CR', collateralAgreementType: 'Z00007',
                complianceStatus: '1', complianceDate: '2021-06-01', collateralValue: 1000000000, collateralValueCurrency: 'INR',
                penalChargesApplicable: true, penalChargesPercentage: 2.5 }),
            item('I913', 913, { conditionDescription: 'RE Condition', collateralObjectType: 'ZRE001', complianceStatus: '2' })
        ];
        this.children.set('I912', {
            coverages: [{ id: 'V1', serialNumber: 1, effectiveFromDate: '2001-01-01', expectedCoverageAmount: 1212,
                expectedCoveragePercentage: 12, coverageBasis: '2', coverageBasisDescription: 'Effective Capital',
                createdOn: '2026-10-03', createdAt: '11:02:44', createdByUserName: 'writer@pfs.local',
                changedOn: null, changedAt: null, changedByUserName: null }],
            rocEvents: [], cersaiEvents: [], neslEvents: [],
            documents: [{ id: 'D1', serialNumber: 1, documentType: 'ZPFSLM101', documentTypeDescription: 'Legal Counsel Report',
                documentStage: '2', documentStageDescription: 'Perfection', documentTitle: 'LLC opinion',
                bdsDocumentId: '005056A1B2C31EDFAB000000ABCDEF01' }],
            securitiesPositions: []
        });
        this.children.set('I913', emptyChildren());
    }

    get canWrite(): boolean {
        return WRITE_ROLES.includes(this.role);
    }

    /** Routes all calls to the backend (any host, path /enquiry/api/...) to this mock. */
    async install(page: Page): Promise<void> {
        await page.route(/\/enquiry\/api\//, route => this.handle(route));
    }

    /** Requests of a method whose path matches. */
    sent(method: string, path: RegExp): RecordedRequest[] {
        return this.requests.filter(request => request.method === method && path.test(request.path));
    }

    private async handle(route: Route): Promise<void> {
        const request = route.request();
        const headers = cors(request);
        if (request.method() === 'OPTIONS') {
            await route.fulfill({ status: 204, headers });
            return;
        }
        const path = new URL(request.url()).pathname.replace(/^.*\/enquiry\/api/, '');
        let body: any = null;
        try {
            body = request.postDataJSON();
        } catch {
            body = request.postData();
        }
        this.requests.push({ method: request.method(), path, body, query: Object.fromEntries(new URL(request.url()).searchParams) });
        const [status, response] = this.respond(request.method(), path, body, new URL(request.url()).searchParams);
        await route.fulfill({ status, headers, contentType: 'application/json', body: JSON.stringify(response ?? null) });
    }

    private respond(method: string, path: string, body: any, query: URLSearchParams): [number, any] {
        let match: RegExpMatchArray | null;
        if (path === '/me') {
            return [200, { firstName: 'Test', lastName: 'User', email: 'test@pfs.local', role: this.role }];
        }
        if (path === '/collaterals/access') {
            return [200, { canWrite: this.canWrite, userName: 'test@pfs.local', role: this.role, writeRoles: WRITE_ROLES }];
        }
        // Report variants (saved selection screens), kept in memory for the signed-in test user
        const variantUrl = /^\/reports\/([a-z_-]+)\/variants(?:\/([\w-]+))?$/.exec(path);
        if (variantUrl) {
            const [, report, id] = variantUrl;
            const dto = (v: any) => { const { report: _r, ...rest } = v; return rest; };
            if (method === 'GET') {
                return [200, this.variants.filter(v => v.report === report).map(dto)];
            }
            if (method === 'POST') {
                let v = this.variants.find(x => x.report === report && x.name === body.name && x.mine);
                if (!v) {
                    v = { report, id: 'v' + (this.variants.length + 1), owner: 'tester@test.local', mine: true };
                    this.variants.push(v);
                }
                if (body.defaultVariant) { this.variants.filter(x => x.report === report).forEach(x => x.defaultVariant = false); }
                Object.assign(v, { name: body.name, description: body.description || null, shared: !!body.shared,
                    defaultVariant: !!body.defaultVariant, parameters: body.parameters, changedOn: new Date().toISOString() });
                return [200, dto(v)];
            }
            if (method === 'DELETE') {
                const i = this.variants.findIndex(x => x.id === id);
                if (i >= 0) { this.variants.splice(i, 1); }
                return [204, null];
            }
        }
        // Reports > Loans Management > Loans : Disbursement - Periodwise and Comparison (SAP DisbursementCompRptSet)
        if (path === '/reports/disbursement/export') {
            return [200, { excel: true, rows: (body?.rows ?? []).length }];
        }
        if (path === '/reports/disbursement') {
            const rows = DISBURSEMENT_ROWS.filter(r => selected(r.c0, query.get('contract')));
            return [200, { columns: DISBURSEMENT_COLUMNS, rows, total: rows.length, generatedOn: new Date().toISOString(),
                query: 'http://sap/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV/DisbursementCompRptSet?$format=json&$filter=…' }];
        }
        // Reports > Loans Management > Loans : Balance List (SAP ZPFS_LMS_REPORTS_SRV · ZFNR7Report)
        if (path === '/reports/balance-list/export') {
            return [200, { excel: true, rows: (body?.rows ?? []).length }];
        }
        if (path === '/reports/balance-list') {
            const rows = BALANCE_ROWS.filter(r => selected(r.ranl, query.get('contract')) && selected(r.santwhr, query.get('currency')));
            return [200, { rows, total: rows.length, generatedOn: new Date().toISOString(),
                query: 'http://sap/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV/ZFNR7Report?$format=json&$filter=…' }];
        }
        // Reports > Loans Management > Loans KYC Report (SAP ZPFS_LMS_REPORTS_SRV · KycReportSet)
        if (path === '/reports/kyc/export') {
            return [200, { excel: true, rows: (body?.rows ?? []).length }];
        }
        if (path === '/reports/kyc') {
            const rows = KYC_ROWS.filter(r => selected(r.contractNo, query.get('contract')) && selected(r.status, query.get('status')));
            return [200, { rows, total: rows.length, generatedOn: new Date().toISOString(),
                query: 'http://sap/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV/KycReportSet?$format=json&$filter=…' }];
        }
        // Reports > Loans Management > ICC Approval Status Report (SAP ZPFS_LMS_REPORTS_SRV)
        if (path === '/reports/icc-approval/export') {
            return [200, { excel: true, rows: (body?.rows ?? []).length }];
        }
        if (path === '/reports/icc-approval') {
            const rows = ICC_APPROVAL_ROWS.filter(r => (!query.get('fiscalYear') || r.fyear === query.get('fiscalYear'))
                && (!query.get('quarter') || r.quarter === query.get('quarter')));
            return [200, { rows, total: rows.length, generatedOn: new Date().toISOString(),
                query: 'http://sap/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV/MISIccApprovalStatRpt?$format=json&$filter=…' }];
        }
        // Reports > Miscellaneous > Workflow Status Report (SAP ZPFS_LMS_REPORTS_SRV)
        if (path === '/reports/workflow-status/export') {
            return [200, { excel: true, rows: (body?.rows ?? []).length }];
        }
        if (path === '/reports/workflow-status') {
            const rows = WORKFLOW_STATUS_ROWS.filter(r => selected(r.ranl, query.get('contract')));
            return [200, { rows, total: rows.length, generatedOn: new Date().toISOString(),
                query: 'http://sap/sap/opu/odata/sap/ZPFS_LMS_REPORTS_SRV/WorkflowStatusSet?$format=json&$filter=…' }];
        }
        // Administration > SAP Integration Log (sap_http_log)
        if (path === '/administration/sap-http-log/export') {
            return [200, 'Id,Requested At,Method,URL\r\n'];
        }
        const sapLogDetail = /^\/administration\/sap-http-log\/(\d+)$/.exec(path);
        if (sapLogDetail) {
            const row = SAP_LOG_ROWS.find(r => r.id === Number(sapLogDetail[1]));
            return row ? [200, { ...row,
                host: '192.168.1.205:8000',
                requestHeaders: 'Accept: application/json\nAuthorization: Basic ***\nX-Requested-With: X\nContent-Type: application/json',
                requestContentType: 'application/json',
                requestBody: row.httpMethod === 'GET' ? null : '{"ZidNo":"0000001037","DocTitle":"Deed of Pledge","PtlId":"1589f4c0-8071-4bbc-ae0c-86c6a8af40a7"}',
                responseContentType: 'application/json',
                responseBody: row.success ? '{"d":{"ZidNo":"0000001037"}}'
                    : '{"error":{"code":"005056A509B11EE1B9A8FEC11C22378E","message":{"lang":"en","value":"Invalid key predicate"},"innererror":{"transactionid":"6AB73D1A84A1F08AE1000000C0A801CD"}}}',
                threadName: 'scheduling-1' }] : [404, { message: 'SAP call not found.' }];
        }
        if (path === '/administration/sap-http-log') {
            if (method === 'DELETE') {
                return [200, { deleted: 2, olderThanDays: Number(query.get('olderThanDays') ?? 0) }];
            }
            const outcome = query.get('outcome');
            const search = (query.get('search') ?? '').toLowerCase();
            const rows = SAP_LOG_ROWS
                .filter(r => !outcome || (outcome === 'failed' ? !r.success : r.success))
                .filter(r => !search || r.url.toLowerCase().includes(search) || (r.errorMessage ?? '').toLowerCase().includes(search));
            const failed = rows.filter(r => !r.success).length;
            return [200, {
                rows, page: 0, size: 25, total: rows.length,
                summary: { total: rows.length, failed, succeeded: rows.length - failed, averageDurationMs: 412, maxDurationMs: 1310,
                    lastCallAt: rows[0]?.requestedAt ?? null, lastFailureAt: rows.find(r => !r.success)?.requestedAt ?? null,
                    byMethod: [{ key: 'POST', count: 1 }, { key: 'PUT', count: 2 }, { key: 'GET', count: 1 }],
                    byStatus: [{ key: '201', count: 1 }, { key: '204', count: 1 }, { key: '400', count: 1 }, { key: 'No response', count: 1 }],
                    byEntitySet: [{ key: 'ChecklistHeaderSet', count: 1 }, { key: 'DocumentSet', count: 1 }, { key: 'DocumentAttachmentSet', count: 1 }, { key: 'LoanApplicationSet', count: 1 }] },
                businessProcesses: ['Collateral Management'], serviceNames: ['ZPFS_LMS_COLLATERAL_SRV', 'ZPFS_LOAN_ENQ_PORTAL_SRV'],
                generatedOn: new Date().toISOString(), canView: this.role !== 'ZLM014', canPurge: this.role === 'ZLM023',
                loggingEnabled: true, retentionDays: 90, userRole: this.role
            }];
        }
        if (path === '/collaterals/value-lists') {
            return [200, valueLists];
        }
        if (path === '/collaterals/agreement-types/by-object') {
            // The whole SAP table (CollateralObjectTypeAgreementType) keyed by object type: a guarantee object is its
            // own agreement type, Z30001 gets the first three; other objects have no row
            const all: { code: string; description: string }[] = (valueLists as any).AGREEMENT_TYPE;
            const table: Record<string, { code: string; description: string }[]> = { Z30001: all.slice(0, 3) };
            for (const type of all) {
                table[type.code] = [type];
            }
            return [200, table];
        }
        if (path === '/collaterals/agreement-types') {
            // As SAP derives it (CollateralObjectTypeAgreementType): a guarantee object type is its own agreement
            // type; other objects get the first three; no object type -> nothing
            const objectType = query.get('collateralObjectType');
            const all: { code: string; description: string }[] = (valueLists as any).AGREEMENT_TYPE;
            if (!objectType) {
                return [200, []];
            }
            return [200, all.some(type => type.code === objectType)
                ? all.filter(type => type.code === objectType)
                : all.slice(0, 3)];
        }
        if (path === '/collaterals/partner-roles') {
            return [200, [{ code: 'ZLM010', description: 'Security Trustee' }]];
        }
        if (path === '/collaterals/partners') {
            return [200, [{ id: 'P1', partyNumber: 1000234, partyName1: 'SBICAP Trustee Company', partyName2: 'Ltd',
                defaultPartnerRole: 'ZLM010', defaultPartnerRoleText: 'Security Trustee', searchTerm1: 'SBICAP', searchTerm2: 'TRUSTEE' }]];
        }
        if (/^\/collaterals\/(loans\/L1|checklists\/C1)$/.test(path) && method === 'GET') {
            return [200, this.checklistDto()];
        }
        if (path === '/collaterals/workflow/startprocess' && method === 'PUT') {
            if (this.checklist['workFlowStatusCode'] === 2) {
                return [409, { message: 'The checklist is already waiting for approval.' }];
            }
            this.setStatus(2);
            this.checklist['rejectionReason'] = null;
            return [200, this.checklistDto()];
        }
        if ((match = path.match(/^\/collaterals\/items\/([^/]+)$/))) {
            return this.itemCall(method, match[1], body);
        }
        if ((match = path.match(/^\/collaterals\/loans\/L1\/items$/)) && method === 'POST') {
            return this.createItem(body);
        }
        if ((match = path.match(/^\/collaterals\/items\/([^/]+)\/(coverages|roc|cersai|nesl|documents|securities)$/)) && method === 'POST') {
            return this.createChild(match[1], match[2], body);
        }
        if ((match = path.match(/^\/collaterals\/(coverages|roc|cersai|nesl|documents|securities)\/([^/]+)$/))) {
            return this.childCall(method, match[1], match[2], body);
        }
        if (path === '/upload' && method === 'POST') {
            return [200, { fileReference: '3f2b8c1e-9a4d-4e6f-8b1a-2c3d4e5f6a7b' }];
        }
        if (path === '/collaterals/configuration/checklist-id') {
            if (method === 'PUT') {
                if (this.role !== 'ZLM023') {
                    return [403, { message: `No edit access for the user with the role ${this.role}.` }];
                }
                const value = Number(body?.sapHighestNumber);
                if (value < this.numberRange['highestMigratedNumber']) {
                    return [412, { message: `Highest ZID_NO in SAP cannot be below ${this.numberRange['highestMigratedNumber']}.` }];
                }
                this.numberRange['sapHighestNumber'] = value;
                this.numberRange['nextNumber'] = Math.max(this.numberRange['nextNumber'], value + 1);
                this.numberRange['changedBy'] = 'test@pfs.local';
                this.numberRange['changedAt'] = '2026-10-05T20:41:00';
            }
            return [200, { ...this.numberRange, canChange: this.role === 'ZLM023', userRole: this.role }];
        }
        return [200, []];
    }

    // ------------------------------------------------------------------------------------------- collaterals

    private checklistDto(): Row {
        return { ...this.checklist, loan: this.loan, items: this.items };
    }

    private itemCall(method: string, id: string, body: any): [number, any] {
        const index = this.items.findIndex(candidate => candidate['id'] === id);
        if (index < 0) {
            return [404, { message: 'Collateral not found.' }];
        }
        if (method === 'GET') {
            return [200, { item: this.items[index], loan: this.loan, ...this.statusFields(), ...this.children.get(id), partnerNames: {} }];
        }
        if (this.locked()) {
            return [409, { message: 'The checklist is waiting for approval.' }];
        }
        if (method === 'PUT') {
            const missing = required(body);
            if (missing) {
                return [412, { message: `${missing} is required.` }];
            }
            this.items[index] = { ...this.items[index], ...body, id, checklistIdNo: this.items[index]['checklistIdNo'] };
            return [200, this.items[index]];
        }
        if (method === 'DELETE') {
            this.items.splice(index, 1);
            return [200, null];
        }
        return [405, null];
    }

    private createItem(body: any): [number, any] {
        if (this.locked()) {
            return [409, { message: 'The checklist is waiting for approval.' }];
        }
        const missing = required(body);
        if (missing) {
            return [412, { message: `${missing} is required.` }];
        }
        const created = item(`I${this.nextId++}`, this.numberRange['nextNumber']++, body);
        this.items.push(created);
        this.children.set(created['id'], emptyChildren());
        return [201, created];
    }

    private createChild(itemId: string, type: string, body: any): [number, any] {
        if (this.locked()) {
            return [409, { message: 'The checklist is waiting for approval.' }];
        }
        const rows = this.children.get(itemId)?.[CHILD_KEYS[type]];
        if (!rows) {
            return [404, { message: 'Collateral not found.' }];
        }
        if (type === 'documents' && (!body?.documentStage || !body?.documentType)) {
            return [412, { message: 'Document Stage and Document Type are required.' }];
        }
        const row: Row = { ...body, id: `R${this.nextId++}` };
        if (['coverages', 'documents', 'securities'].includes(type)) {
            row['serialNumber'] = rows.length + 1;
        }
        if (type === 'securities') {
            row['nominalValueCurrency'] = row['nominalValueCurrency'] ?? 'INR';
        }
        rows.push(row);
        return [201, row];
    }

    private childCall(method: string, type: string, id: string, body: any): [number, any] {
        for (const children of this.children.values()) {
            const rows = children[CHILD_KEYS[type]];
            const index = rows.findIndex(row => row['id'] === id);
            if (index >= 0) {
                if (this.locked()) {
                    return [409, { message: 'The checklist is waiting for approval.' }];
                }
                if (method === 'DELETE') {
                    rows.splice(index, 1);
                    return [200, null];
                }
                rows[index] = { ...rows[index], ...body, id };
                return [200, rows[index]];
            }
        }
        return [404, { message: 'Row not found.' }];
    }

    private locked(): boolean {
        return this.checklist['workFlowStatusCode'] === 2;
    }

    private setStatus(code: number): void {
        this.checklist['workFlowStatusCode'] = code;
        this.checklist['workFlowStatusDescription'] = statusText(code);
    }

    private statusFields(): Row {
        return { workFlowStatusCode: this.checklist['workFlowStatusCode'], workFlowStatusDescription: this.checklist['workFlowStatusDescription'] };
    }
}

function item(id: string, checklistIdNo: number, values: Row): Row {
    return {
        id, checklistId: 'C1', checklistIdNo, conditionGroup: '04', conditionCategory: '04',
        createdOn: '2026-10-05', createdAt: '09:06:12.5', createdByUserName: 'test@pfs.local',
        changedOn: '2026-10-08', changedAt: '14:30:00', changedByUserName: 'writer@pfs.local', ...values
    };
}

function emptyChildren(): Record<string, Row[]> {
    return { coverages: [], rocEvents: [], cersaiEvents: [], neslEvents: [], documents: [], securitiesPositions: [] };
}

function required(body: any): string | null {
    if (!body?.conditionGroup) {
        return 'Condition Group';
    }
    if (!body?.conditionCategory) {
        return 'Condition Category';
    }
    if (!body?.collateralObjectType) {
        return 'Collateral Object';
    }
    return null;
}

function statusText(code: number): string {
    return { 0: 'Not Sent for Approval', 2: 'Sent for Approval', 3: 'Approved', 4: 'Rejected' }[code] ?? '';
}

function cors(request: Request): Record<string, string> {
    return {
        'Access-Control-Allow-Origin': request.headers()['origin'] ?? '*',
        'Access-Control-Allow-Credentials': 'true',
        'Access-Control-Allow-Headers': 'x-requested-with, authorization, content-type, x-xsrf-token',
        'Access-Control-Allow-Methods': 'GET,POST,PUT,PATCH,DELETE,OPTIONS'
    };
}
