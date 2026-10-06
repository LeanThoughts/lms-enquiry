import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { skipBusyIndicator } from '../../../busy-indicator.service';
import {
    EntityField,
    EntitySetField,
    EntitySetFieldStatus,
    EntitySetOverview,
    FieldStatusChanges,
    FieldStatusOverview,
    RoleFieldStatus
} from './bupa-field-status.model';

/** REST calls of the Configuration app "Business Partner Field Status". */
@Injectable({ providedIn: 'root' })
export class BupaFieldStatusService {

    private readonly baseUrl = environment.primaryApiHost + '/configuration/bupa-field-status';
    private readonly setUrl = environment.primaryApiHost + '/configuration/bupa-entity-set-field-status';

    constructor(private http: HttpClient) {}

    getOverview(): Observable<FieldStatusOverview> {
        return this.http.get<FieldStatusOverview>(this.baseUrl, { context: skipBusyIndicator() });
    }

    getRole(roleCode: string): Observable<RoleFieldStatus> {
        return this.http.get<RoleFieldStatus>(`${this.baseUrl}/${encodeURIComponent(roleCode)}`, { context: skipBusyIndicator() });
    }

    saveChanges(roleCode: string, changes: FieldStatusChanges): Observable<RoleFieldStatus> {
        return this.http.put<RoleFieldStatus>(`${this.baseUrl}/${encodeURIComponent(roleCode)}`, changes,
            { context: skipBusyIndicator() });
    }

    addEntityField(roleCode: string, field: Partial<EntityField>): Observable<RoleFieldStatus> {
        return this.http.post<RoleFieldStatus>(`${this.baseUrl}/${encodeURIComponent(roleCode)}/entity-fields`, field,
            { context: skipBusyIndicator() });
    }

    addEntitySetField(roleCode: string, field: Partial<EntitySetField>): Observable<RoleFieldStatus> {
        return this.http.post<RoleFieldStatus>(`${this.baseUrl}/${encodeURIComponent(roleCode)}/entity-set-fields`, field,
            { context: skipBusyIndicator() });
    }

    /** Copies the missing fields of another role; entityFieldsOnly leaves the entity sets alone. */
    copyFromRole(roleCode: string, sourceRoleCode: string, entityFieldsOnly = false): Observable<RoleFieldStatus> {
        return this.http.post<RoleFieldStatus>(`${this.baseUrl}/${encodeURIComponent(roleCode)}/copy`,
            { sourceRoleCode, entityFieldsOnly },
            { context: skipBusyIndicator() });
    }

    deleteEntityField(id: number): Observable<RoleFieldStatus> {
        return this.http.delete<RoleFieldStatus>(`${this.baseUrl}/entity-fields/${id}`, { context: skipBusyIndicator() });
    }

    deleteEntitySetField(id: number): Observable<RoleFieldStatus> {
        return this.http.delete<RoleFieldStatus>(`${this.baseUrl}/entity-set-fields/${id}`, { context: skipBusyIndicator() });
    }

    // ---------------------------------------------------------------- by role and entity set (BP Entity Set Fields)

    getEntitySetOverview(): Observable<EntitySetOverview> {
        return this.http.get<EntitySetOverview>(this.setUrl, { context: skipBusyIndicator() });
    }

    getEntitySet(roleCode: string, entitySet: string): Observable<EntitySetFieldStatus> {
        return this.http.get<EntitySetFieldStatus>(this.setPath(roleCode, entitySet), { context: skipBusyIndicator() });
    }

    saveEntitySet(roleCode: string, entitySet: string, changes: Partial<EntitySetField>[]): Observable<EntitySetFieldStatus> {
        return this.http.put<EntitySetFieldStatus>(this.setPath(roleCode, entitySet), changes, { context: skipBusyIndicator() });
    }

    addFieldToEntitySet(roleCode: string, entitySet: string, field: Partial<EntitySetField>): Observable<EntitySetFieldStatus> {
        return this.http.post<EntitySetFieldStatus>(this.setPath(roleCode, entitySet) + '/fields', field,
            { context: skipBusyIndicator() });
    }

    copyEntitySet(roleCode: string, entitySet: string, sourceRoleCode: string): Observable<EntitySetFieldStatus> {
        return this.http.post<EntitySetFieldStatus>(this.setPath(roleCode, entitySet) + '/copy', { sourceRoleCode },
            { context: skipBusyIndicator() });
    }

    deleteFieldOfEntitySet(id: number): Observable<EntitySetFieldStatus> {
        return this.http.delete<EntitySetFieldStatus>(`${this.setUrl}/fields/${id}`, { context: skipBusyIndicator() });
    }

    private setPath(roleCode: string, entitySet: string): string {
        return `${this.setUrl}/${encodeURIComponent(roleCode)}/${encodeURIComponent(entitySet)}`;
    }

    /** Readable error text from a backend error (LmsException message) or a fallback. */
    errorMessage(error: unknown, fallback: string): string {
        if (error instanceof HttpErrorResponse) {
            const body = error.error;
            if (body && typeof body === 'object' && typeof body.message === 'string' && body.message) {
                return body.message;
            }
            if (error.status === 0) {
                return 'The server cannot be reached. Please try again.';
            }
        }
        return fallback;
    }
}
