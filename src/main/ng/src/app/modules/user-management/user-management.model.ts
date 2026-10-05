/**
 * Models for the User Management module (mirror the usermanagement DTOs in the Spring Boot layer).
 */

export interface User {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    userName?: string | null;
    role: string;
    roleDescription?: string | null;
    sapBPNumber?: string | null;
    riskDepartment?: string | null;
    riskDepartmentName?: string | null;
    departmentHead: boolean;
    riskPortalDisplayOnlyAccess: boolean;
    status: boolean;
    /** Valid from, ISO yyyy-MM-dd (creation date). */
    startDate?: string | null;
    /** Valid to, ISO yyyy-MM-dd: 9999-12-31 while active, the deactivation date once inactive. */
    endDate?: string | null;
    createdOn?: string | null;
    createdAt?: string | null;
    createdByUserName?: string | null;
    changedOn?: string | null;
    changedAt?: string | null;
    changedByUserName?: string | null;
}

export interface UserRequest {
    firstName: string;
    lastName: string;
    email: string;
    userName?: string | null;
    role: string;
    roleDescription?: string | null;
    sapBPNumber?: string | null;
    riskDepartment?: string | null;
    departmentHead: boolean;
    riskPortalDisplayOnlyAccess: boolean;
    status: boolean;
}

export interface UserRole {
    id: string;
    code: string;
    value: string;
    userCount: number;
    /** null when the role entry is fine. */
    issue?: string | null;
}

export interface Department {
    code: string;
    value: string;
}

export interface UserManagementAccess {
    allowed: boolean;
    email?: string | null;
    role?: string | null;
}

export type UserStatusFilter = 'all' | 'active' | 'inactive';

export interface UserSearchCriteria {
    query?: string | null;
    role?: string | null;
    riskDepartment?: string | null;
    status: UserStatusFilter;
}

/** Data handed to the create/edit dialog. */
export interface UserFormDialogData {
    operation: 'create' | 'edit';
    user?: User;
    roles: UserRole[];
    departments: Department[];
}

/** Data resolved for every User Management route. */
export interface UserManagementResolvedData {
    roles: UserRole[];
    departments: Department[];
}

/** Formats an ISO date (yyyy-MM-dd) as dd.MM.yyyy; '–' when empty. */
export function formatDate(value: string | null | undefined): string {
    if (!value) {
        return '–';
    }
    const match = /^(\d{4})-(\d{2})-(\d{2})/.exec(value);
    return match ? `${match[3]}.${match[2]}.${match[1]}` : value;
}

/** Display name helper. */
export function fullName(user: Pick<User, 'firstName' | 'lastName'> | null | undefined): string {
    return user ? `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim() : '';
}
