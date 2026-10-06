import { Page, Request, Route } from '@playwright/test';
import valueLists from '../fixtures/value-lists.json';

/** A request the app sent to the (mocked) backend. */
export interface RecordedRequest {
    method: string;
    path: string;
    body: any;
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
export class MockApi {
    readonly requests: RecordedRequest[] = [];
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
            rejectionReason: options.rejectionReason ?? null
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
                expectedCoveragePercentage: 12, coverageBasis: '2', coverageBasisDescription: 'Effective Capital' }],
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
        this.requests.push({ method: request.method(), path, body });
        const [status, response] = this.respond(request.method(), path, body);
        await route.fulfill({ status, headers, contentType: 'application/json', body: JSON.stringify(response ?? null) });
    }

    private respond(method: string, path: string, body: any): [number, any] {
        let match: RegExpMatchArray | null;
        if (path === '/me') {
            return [200, { firstName: 'Test', lastName: 'User', email: 'test@pfs.local', role: this.role }];
        }
        if (path === '/collaterals/access') {
            return [200, { canWrite: this.canWrite, userName: 'test@pfs.local', role: this.role, writeRoles: WRITE_ROLES }];
        }
        if (path === '/collaterals/value-lists') {
            return [200, valueLists];
        }
        if (path === '/collaterals/agreement-types') {
            return [200, (valueLists as any).AGREEMENT_TYPE];
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
        createdOn: '2026-10-05', createdByUserName: 'test@pfs.local', ...values
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
