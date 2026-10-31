/** Business partner search of Master Data > Business Partners (REST GET /business-partners/search). */

export interface BusinessPartnerSearchCriteria {
    name?: string | null;
    email?: string | null;
    pan?: string | null;
    /** Search term 1 or 2 of the partner */
    searchTerm?: string | null;
    /** Default partner role (code) */
    partnerRole?: string | null;
    partnerNumberFrom?: string | null;
    partnerNumberTo?: string | null;
}

export interface BusinessPartnerRow {
    id: string;
    partyNumber: number | null;
    partyName: string;
    partyName1: string | null;
    partyName2: string | null;
    defaultPartnerRole: string | null;
    defaultPartnerRoleText: string | null;
    partnerGroup: string | null;
    partnerCategory: string | null;
    pan: string | null;
    searchTerm1: string | null;
    searchTerm2: string | null;
    contactPersonName: string | null;
    email: string | null;
    addressLine1: string | null;
    addressLine2: string | null;
    city: string | null;
    workFlowStatusDescription: string | null;
}

export interface BusinessPartnerSearchResult {
    rows: BusinessPartnerRow[];
    /** 0 based */
    page: number;
    size: number;
    total: number;
    sort: string;
}

/** What the search screen remembers when the user leaves it (e.g. to change a partner) and comes back */
export interface BusinessPartnerSearchState {
    criteria: BusinessPartnerSearchCriteria;
    page: number;
    size: number;
}

export const BP_SEARCH_PAGE_SIZES = [25, 50, 100, 200];
