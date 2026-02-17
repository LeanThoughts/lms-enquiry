import { Injectable } from '@angular/core';
import { BehaviorSubject, forkJoin, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class BusinessPartnerSearchService implements Resolve<any> {

    businessPartnerSearchParameters$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    selectedEntity$: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    /**
     * Constructor
     */
    constructor(private http: HttpClient) { }

    /**
     * Resolve the business partner data
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
        const forkJoinObjects: any = {};
        if (route.params && route.params['id']) {
            forkJoinObjects.businessPartnerContacts = this.getBusinessPartnerContacts(route.params['id']);
            forkJoinObjects.bankKey = this.getBanks();
            forkJoinObjects.bankCountry = this.getCountryCodes();
            forkJoinObjects.identificationCategoryCode = this.getIdentificationCategories();
            forkJoinObjects.documentType = this.getDocumentTypes();
            forkJoinObjects.country = this.getCountryCodes();
            forkJoinObjects.industrySystemId = this.getIndustrySystems();
            forkJoinObjects.businessPartnerRoleTypes = this.getBusinessPartnerRoleTypes();
            forkJoinObjects.legalForms = this.getLegalForms();
            forkJoinObjects.legalEntities = this.getLegalEntities();
            forkJoinObjects.houseBanks = this.getHouseBanks();
            forkJoinObjects.businessPartnerRoles = this.getBusinessPartnerRoles(route.params['id']);
            forkJoinObjects.roleTypeId = this.getBusinessPartnerRoleTypes(route.params['defaultPartnerRole']);
            forkJoinObjects.planningGroups = this.getPlanningGroups();         
            forkJoinObjects.sortKeys = this.getSortKeys();         
            forkJoinObjects.dunningProcedures = this.getDunningProcedures();         
            forkJoinObjects.paymentTerms = this.getPaymentTerms();         
            forkJoinObjects.paymentMethods = this.getPaymentMethods();         
            forkJoinObjects.businessPartner = this.getBusinesPartner(route.params['id']);
            forkJoinObjects.partnerGroups = this.getPartnerGroups();
        }
        else {
            forkJoinObjects.businessPartnerContacts = of(null);
            forkJoinObjects.businessPartnerRoleTypes = this.getBusinessPartnerRoleTypes();
            forkJoinObjects.legalForms = this.getLegalForms();
            forkJoinObjects.legalEntities = this.getLegalEntities();
            forkJoinObjects.houseBanks = this.getHouseBanks();
            forkJoinObjects.partnerGroups = this.getPartnerGroups();
        }
        return forkJoin(forkJoinObjects);
    }

    /**
     * Update business partner customer details
     */
    updateBusinessPartnerCustomerDetails(customerDetails: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/partners/updateFICustomerVendorDetails', customerDetails);
    }

    /**
     * Get business partner
     */
    getBusinesPartner(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/partners/' + partnerId).subscribe(result => {
                observer.next([result]);
                observer.complete();
            });
        });
    }

    /**
     * Get business partner KYC details
     */
    getBusinessPartnerKYCDetails(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerKYCDetails', { params: { partnerId } }).subscribe(result => {
                observer.next(result._embedded.businessPartnerKYCDetails);
                observer.complete();
            });
        });
    }

    /**
     * Create business partner KYC details
     */
    createBusinessPartnerKYCDetails(kycDetails: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerKYCDetails/create', kycDetails, { params: { businessPartnerId } });
    }

    /**
     * Update business partner KYC details
     */
    updateBusinessPartnerKYCDetails(kycDetails: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerKYCDetails/update', kycDetails);
    }    
    /**
     * Get planning groups
     */
    getPlanningGroups(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/planningGroups', { params: { 'size': '1000', 'sort': 'description,asc' } });
    }

    /**
     * Get sort keys
     */
    getSortKeys(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/sortKeys', { params: { 'size': '1000', 'sort': 'description,asc' } });
    }

    /**
     * Get dunning procedures
     */
    getDunningProcedures(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/dunningProcedures', { params: { 'size': '1000', 'sort': 'description,asc' } });
    }

    /**
     * Get payment terms
     */
    getPaymentTerms(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/paymentTermses', { params: { 'size': '1000', 'sort': 'description,asc' } });
    }

    /**
     * Get payment methods
     */
    getPaymentMethods(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/paymentMethods', { params: { 'size': '1000', 'sort': 'description,asc' } });
    }

    /**
     * Get business partner customer details
     */
    getBusinessPartnerCustomerDetailsFieldConfig(defaultPartnerRole: string, partnerGroup: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/bupaRoleCustomerFieldValueses/search/findByBupaRoleCodeAndPartnerGroup', 
            { params: { roleType: defaultPartnerRole, partnerGroup: partnerGroup } });
    }

    /**
     * Get business partner financials
     */
    getBusinessPartnerFinancials(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerFinancials/search/findByPartnerIdOrderByFiscalYearAsc', 
                        { params: { partnerId } }).subscribe({
                next: (result: any) => {
                    const businessPartnerFinancials = result._embedded.businessPartnerFinancials;
                    observer.next(businessPartnerFinancials);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Create business partner financials
     */
    createBusinessPartnerFinancials(financials: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerFinancials/create', financials, 
                { params: { businessPartnerId } });
    }

    /**
     * Update business partner financials
     */
    updateBusinessPartnerFinancials(financials: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerFinancials/update', financials);
    }

    /**
     * Get business partner role field configuration
     */
    getBusinessPartnerRoleFieldConfig(businessPartnerRoleCode: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/bupaRoleFieldStatus/' + businessPartnerRoleCode);
    }

    /**
     * Search business partners
     */
    searchBusinessPartners(requestParameters: any): Observable<any> {
        return new Observable(observer => {
            const partners = new Array<any>();
            this.http.get<any[]>(environment.primaryApiHost + '/partner/queryParams?query=' + requestParameters).subscribe(result => {
                result.map(partnerModel => {
                    partners.push(partnerModel);
                });
                observer.next(partners);
                observer.complete();
            });
        });
    }

    /**
     * Get document types
     */
    getDocumentTypes(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/documentTypes');
    }
    
    /** 
     * Get business partner role types
     */
    getBusinessPartnerRoleTypes(defaultPartnerRole?: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerRoleTypes', { params: { 'size': '500', 'sort': 'value,asc' } })
            .subscribe({
                next: (result: any) => {
                const businessPartnerRoleTypes = result._embedded.businessPartnerRoleTypes;
                    if (defaultPartnerRole) {
                        const index = businessPartnerRoleTypes.findIndex((roleType: any) => roleType.code === defaultPartnerRole);
                        if (index !== -1) {
                            businessPartnerRoleTypes.splice(index, 1);
                        }
                    }
                    observer.next(businessPartnerRoleTypes);
                    observer.complete();
                }
            });
        });
    }

    /**
     * Get legal forms
     */
    getLegalForms(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/legalForms?sort=code&size=300');
    }

    /**
     * Get legal entities
     */
    getLegalEntities(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/legalEntities?sort=code&size=300');
    }

    /**
     * Get house banks
     */
    getHouseBanks(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/houseBanks?sort=houseBankId&size=300');
    }

    /** 
     * Get banks
     */
    getBanks(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/bankmasters/all');
    }

    /**
     * Get countries
     */
    // getCountries(): Observable<any> {
    //     return new Observable(observer => {
    //         this.http.get<any>(environment.primaryApiHost + '/countries?sort=countryCode&size=300').subscribe(result => {
    //             const countries = result._embedded.countries;
    //             observer.next(countries);
    //             observer.complete();
    //         });
    //     });
    // }
    
    /**
     * Get countries
     */
    getCountryCodes(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/countryCodes');
    }

    /**
     * Get regions
     */
    getRegions(countryCode: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/regions/search/findByCountryCode?countryCode=' + countryCode + 
                '&sort=regionCode&size=300').subscribe(result => {
                    const regions = result._embedded.regions;
                    observer.next(regions);
                    observer.complete();
                });
        });
    }

    /**
     * Get titles
     */
    getTitles(code: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/titles/search/findByPartnerCategory', { params: { code } });
    }
    
    /**
     * Create business partner
     */
    createBusinessPartner(partner: any): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/partners/create', partner);
    }

    /**
     * Update business partner
     */
    updateBusinessPartner(partner: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/partners/update', partner);
    }

    /**
     * Get business partner contacts
     */
    getBusinessPartnerContacts(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerLoanContacts/search/findByPartnerIdOrderBySerialNumberDesc', 
                { params: { partnerId } }).subscribe(result => {
                    const partnerContacts = result._embedded.businessPartnerLoanContacts;
                    observer.next(partnerContacts);
                    observer.complete();
                });
        });
    }

    /**
     * Create business partner contact details
     */
    createBusinessPartnerContactDetails(contactDetails: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerLoanContacts/create', contactDetails, 
            { params: { businessPartnerId } });
    }

    /**
     * Update business partner contact details
     */
    updateBusinessPartnerContactDetails(contactDetails: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerLoanContacts/update', contactDetails);
    }

    /**
     * Get business partner bank details
     */
    getBusinessPartnerBankDetails(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerBankDetails/search/findByPartnerIdOrderBySerialNumberDesc', 
                { params: { partnerId } }).subscribe(result => {
                    const bankDetails = result._embedded.businessPartnerBankDetails;
                    observer.next(bankDetails);
                    observer.complete();
                });
        });
    }
    
    /**
     * Create business partner bank details
     */
    createBusinessPartnerBankDetails(bankDetails: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerBankDetails/create', bankDetails, 
            { params: { businessPartnerId } });
    }

    /**
     * Update business partner bank details
     */
    updateBusinessPartnerBankDetails(bankDetails: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerBankDetails/update', bankDetails);
    }

    /**
     * Get identification categories
     */
    getIdentificationCategories(): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/identificationCategories?size=100&sort=value,asc').subscribe(result => {
                const identificationCategories = result._embedded.identificationCategories;
                observer.next(identificationCategories);
                observer.complete();
            });
        });
    }

    /**
     * Get business partner identification details
     */
    getBusinessPartnerIdentificationDetails(partnerId: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/businessPartnerIdentifications/findByPartnerId', { params: { partnerId } });
    }

    /**
     * Create business partner identification details
     */
    createBusinessPartnerIdentificationDetails(identification: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerIdentifications/create', identification, 
            { params: { businessPartnerId } });
    }

    /**
     * Update business partner identification details
     */
    updateBusinessPartnerIdentificationDetails(identification: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerIdentifications/update', identification);
    }

    /**
     * Get industry systems
     */
    getIndustrySystems(): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/industrySystems', { params: { 'size': '1000', 'sort': 'id,asc' } }).subscribe({
                next: (result: any) => {
                    const industrySystems = result._embedded.industrySystems;
                    observer.next(industrySystems);
                    observer.complete();
                }
            });
        });
    }
    
    /**
     * Get industry types for a given industry system
     */
    getIndustryTypes(industrySystemId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/industryTypes/search/findByIndustrySystemId', { params: { industrySystemId } })
                .subscribe({
                    next: (result: any) => {
                        const industryTypes = result._embedded.industryTypes;
                        observer.next(industryTypes);
                        observer.complete();
                    }
                });
        });
    }

    /**
     * Get business partner industries
     */
    getBusinessPartnerIndustryDetails(partnerId: string): Observable<any> {
        console.log('getBusinessPartnerIndustryDetails is called for partnerId', partnerId);
        return this.http.get<any>(environment.primaryApiHost + '/businessPartnerIndustries/findByPartnerId', { params: { partnerId } });
    }
    
    /**
     * Create business partner industry details
     */
    createBusinessPartnerIndustryDetails(industry: any, businessPartnerId: string): Observable<any> {
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerIndustries/create', industry, { params: { businessPartnerId } });
    }
    
    /**
     * Update business partner industry details
     */
    updateBusinessPartnerIndustryDetails(industry: any): Observable<any> {
        return this.http.put<any>(environment.primaryApiHost + '/businessPartnerIndustries/update', industry);
    }
    
    /**
     * Get business partner roles
     */
    getBusinessPartnerRoles(partnerId: string): Observable<any> {
        return new Observable(observer => {
            this.http.get<any>(environment.primaryApiHost + '/businessPartnerRoles/search/findByPartnerId', { params: { partnerId } }).subscribe(result => {
                const businessPartnerRoles = new Array<any>();
                result._embedded.businessPartnerRoles.forEach((bpr: any) => {
                    businessPartnerRoles.push(bpr.roleType);
                });
                observer.next(businessPartnerRoles);
                observer.complete();
            });
        });
    }
    
    /**
     * Create business partner role
     */
    createBusinessPartnerRole(businessPartnerRole: any): Observable<any> {
        console.log('createBusinessPartnerRole is called for businessPartnerRole', businessPartnerRole);
        return this.http.post<any>(environment.primaryApiHost + '/businessPartnerRoles/create', {
            'businessPartnerId': businessPartnerRole.businessPartnerId,
            'roleTypeId': businessPartnerRole.roleTypeId,
            'defaultRole': businessPartnerRole.defaultRole
        });
    }
    
    /**
     * Upload vault document
     */
    uploadVaultDocument(file: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/upload', file);
    }

    /**
     * Send business partner for workflow approval
     */
    sendBusinessPartnerForWorkflowApproval(partnerId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': partnerId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'BusinessPartner'
        }
        return this.http.put<any>(environment.primaryApiHost + '/startprocess', requestObj);
    }

    /**
     * Get partner groups
     */
    getPartnerGroups(): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/partnerGroups?size=1000&sort=value,asc');
    }

    /**
     * Get partner groups for role type
     */
    getPartnerGroupsForRoleType(roleType: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/businessPartnerRoleTypePartnerGroups/search/findByRoleType', { params: { roleType } });
    }

    /**
     * Get partners by role type
     */
    getPartnersByRoleType(roleType: string): Observable<any> {
        return this.http.get<any>(environment.primaryApiHost + '/partners/role/' + roleType);
    }
}
