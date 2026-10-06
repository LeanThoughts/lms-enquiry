import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, shareReplay, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { skipBusyIndicator } from '../../busy-indicator.service';
import {
    ChecklistIdConfiguration,
    ChildType,
    CollateralAccess,
    CollateralChecklist,
    CollateralItem,
    CollateralItemDetail,
    PartnerSearchCriteria,
    PartnerSearchResult,
    ValueEntry,
    ValueLists
} from './collateral.model';

/**
 * Backend calls of Collateral Management. The pages show their own message and hourglass while waiting,
 * so the calls skip the global busy indicator.
 */
@Injectable({
    providedIn: 'root'
})
export class CollateralService {

    private readonly baseUrl = environment.primaryApiHost + '/collaterals';

    private access$?: Observable<CollateralAccess>;
    private valueLists$?: Observable<ValueLists>;
    private partnerRoles$?: Observable<ValueEntry[]>;

    constructor(private http: HttpClient) {}

    /** What the signed-in user may do; loaded once per session. Falls back to display only on errors. */
    getAccess(): Observable<CollateralAccess> {
        if (!this.access$) {
            this.access$ = this.http.get<CollateralAccess>(this.baseUrl + '/access', { context: skipBusyIndicator() }).pipe(
                catchError(() => of({ canWrite: false, userName: null, role: null, writeRoles: [] } as CollateralAccess)),
                shareReplay(1)
            );
        }
        return this.access$;
    }

    /** All dropdown value lists; loaded once per session. */
    getValueLists(): Observable<ValueLists> {
        if (!this.valueLists$) {
            this.valueLists$ = this.http.get<ValueLists>(this.baseUrl + '/value-lists', { context: skipBusyIndicator() }).pipe(
                tap({ error: () => this.valueLists$ = undefined }),
                shareReplay(1)
            );
        }
        return this.valueLists$;
    }

    /** Agreement types allowed for a collateral object type. */
    getAgreementTypes(collateralObjectType: string | null): Observable<ValueEntry[]> {
        let params = new HttpParams();
        if (collateralObjectType) {
            params = params.set('collateralObjectType', collateralObjectType);
        }
        return this.http.get<ValueEntry[]>(this.baseUrl + '/agreement-types', { params, context: skipBusyIndicator() });
    }

    getChecklist(loanApplicationId: string): Observable<CollateralChecklist> {
        return this.http.get<CollateralChecklist>(`${this.baseUrl}/loans/${loanApplicationId}`, { context: skipBusyIndicator() });
    }

    /** Collateral list of the loan a checklist belongs to (e.g. from a workflow task). */
    getChecklistById(checklistId: string): Observable<CollateralChecklist> {
        return this.http.get<CollateralChecklist>(`${this.baseUrl}/checklists/${checklistId}`, { context: skipBusyIndicator() });
    }

    /** Sends the checklist for approval (CollateralWorkFlowController startprocess). */
    sendForApproval(checklistId: string): Observable<CollateralChecklist> {
        return this.http.put<CollateralChecklist>(`${this.baseUrl}/workflow/startprocess`,
            { businessProcessId: checklistId }, { context: skipBusyIndicator() });
    }

    getItem(itemId: string): Observable<CollateralItemDetail> {
        return this.http.get<CollateralItemDetail>(`${this.baseUrl}/items/${itemId}`, { context: skipBusyIndicator() });
    }

    createItem(loanApplicationId: string, item: CollateralItem): Observable<CollateralItem> {
        return this.http.post<CollateralItem>(`${this.baseUrl}/loans/${loanApplicationId}/items`, item,
            { context: skipBusyIndicator() });
    }

    updateItem(itemId: string, item: CollateralItem): Observable<CollateralItem> {
        return this.http.put<CollateralItem>(`${this.baseUrl}/items/${itemId}`, item, { context: skipBusyIndicator() });
    }

    deleteItem(itemId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/items/${itemId}`, { context: skipBusyIndicator() });
    }

    /** Creates a row of a child table (coverages, roc, cersai, nesl) of a collateral. */
    createChild<T>(type: ChildType, itemId: string, row: T): Observable<T> {
        return this.http.post<T>(`${this.baseUrl}/items/${itemId}/${type}`, row, { context: skipBusyIndicator() });
    }

    updateChild<T>(type: ChildType, id: string, row: T): Observable<T> {
        return this.http.put<T>(`${this.baseUrl}/${type}/${id}`, row, { context: skipBusyIndicator() });
    }

    deleteChild(type: ChildType, id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${type}/${id}`, { context: skipBusyIndicator() });
    }

    /** Business partners matching the criteria (at most 200), for Security Trustee, Security Agent and Custodian. */
    searchPartners(criteria: PartnerSearchCriteria): Observable<PartnerSearchResult[]> {
        let params = new HttpParams();
        for (const [key, value] of Object.entries(criteria)) {
            if (typeof value === 'string' && value.trim()) {
                params = params.set(key, value.trim());
            }
        }
        return this.http.get<PartnerSearchResult[]>(this.baseUrl + '/partners', { params, context: skipBusyIndicator() });
    }

    /** Business partner roles for the partner search; loaded once per session. */
    getPartnerRoles(): Observable<ValueEntry[]> {
        if (!this.partnerRoles$) {
            this.partnerRoles$ = this.http.get<ValueEntry[]>(this.baseUrl + '/partner-roles', { context: skipBusyIndicator() }).pipe(
                catchError(() => {
                    this.partnerRoles$ = undefined;
                    return of([] as ValueEntry[]);
                }),
                shareReplay(1)
            );
        }
        return this.partnerRoles$;
    }

    /** Stores a file in the portal's file storage (as for business partner documents) and returns its reference. */
    uploadFile(file: File): Observable<string> {
        const body = new FormData();
        body.append('file', file, file.name);
        return this.http.post<{ fileReference: string }>(environment.primaryApiHost + '/upload', body,
            { context: skipBusyIndicator() }).pipe(map(result => result.fileReference));
    }

    /** Download link of a stored file. */
    downloadUrl(fileReference: string, fileName: string | null | undefined): string {
        const name = encodeURIComponent(fileName || 'document');
        return `${environment.primaryApiHost}/download/${fileReference}/${name}`;
    }

    /** Checklist ID number range (Configuration app). */
    getChecklistIdConfiguration(): Observable<ChecklistIdConfiguration> {
        return this.http.get<ChecklistIdConfiguration>(this.baseUrl + '/configuration/checklist-id',
            { context: skipBusyIndicator() });
    }

    /** Sets the highest ZID_NO used in SAP; portal numbers continue above it. */
    setSapHighestNumber(sapHighestNumber: number): Observable<ChecklistIdConfiguration> {
        return this.http.put<ChecklistIdConfiguration>(this.baseUrl + '/configuration/checklist-id',
            { sapHighestNumber }, { context: skipBusyIndicator() });
    }

    /** Readable error text from a backend error (LmsException message) or a fallback. */
    errorMessage(error: unknown, fallback: string): string {
        if (error instanceof HttpErrorResponse) {
            const body = error.error;
            if (body && typeof body === 'object' && typeof body.message === 'string' && body.message) {
                return body.message;
            }
            if (typeof body === 'string' && body && body.length < 300 && !body.trim().startsWith('<')) {
                return body;
            }
            if (error.status === 0) {
                return 'The server cannot be reached. Please try again.';
            }
        }
        return fallback;
    }
}
