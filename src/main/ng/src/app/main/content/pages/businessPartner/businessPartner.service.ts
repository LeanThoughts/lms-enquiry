import {BehaviorSubject, Observable, forkJoin} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import { PartnerService } from '../administration/partner/partner.service';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { PartnerModel } from '../../model/partner.model';
import { LoanMonitoringService } from '../monitoring/loanMonitoring.service';

@Injectable()
export class BusinessPartnerService {

    businessPartnerCategoryAndRole: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient, 
        private _partnerService: PartnerService,
        private _loanEnquiryService: LoanEnquiryService,
        private _loanMonitoringService: LoanMonitoringService
    ) { }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        if (this._partnerService.selectedPartner.value.id) {
            return forkJoin([
                this._partnerService.getBusinessPartnerRoleTypes(),
                this._loanEnquiryService.getStates(),
                this._loanMonitoringService.getBanks(),
                this.getIndustrySystems(),
                this.getIdentificationCategories(),
                this.getBusinessPartnerContactDetails(this._partnerService.selectedPartner.value.id),
                this.getBusinessPartnerIndustries(this._partnerService.selectedPartner.value.id),
                this.getBusinessPartnerIdentificationDetails(this._partnerService.selectedPartner.value.id),
                this.getBusinessPartnerBankDetails(this._partnerService.selectedPartner.value.id),
                this.getBusinessPartnerRoles(this._partnerService.selectedPartner.value.id),
                this.getAllIndustryTypes(),
                this.getTitles(this.businessPartnerCategoryAndRole.value.partnerCategory),
                this.getCountryCodes()
            ]);
        }
        else {
            return forkJoin([
                this._partnerService.getBusinessPartnerRoleTypes(),
                this._loanEnquiryService.getStates(),
                this._loanMonitoringService.getBanks(),
                this.getIndustrySystems(),
                this.getIdentificationCategories(),
                this.getAllIndustryTypes(),
                this.getTitles(this.businessPartnerCategoryAndRole.value.partnerCategory),
                this.getCountryCodes()
            ]);
        }
    }

    /**
     * getCountryCodes()
     */
    getCountryCodes(): Observable<any> {
        return this._http.get<any>('enquiry/api/countryCodes');
    }

    /**
     * getTitles()
     */
    getTitles(code: string): Observable<any> {
        return this._http.get<any>('enquiry/api/titles/search/findByPartnerCategory', { params: { code } });
    }

    /**
     * getBusinessPartnerRoles()
     */
    getBusinessPartnerRoles(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerRoles/search/findByPartnerId', { params: { partnerId } });
    }

    /**
     * createBusinessPartnerRole()
     */
    createBusinessPartnerRole(businessPartnerId: string, roleTypeId: string, defaultRole: boolean): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerRoles/create', {
            'businessPartnerId': businessPartnerId,
            'roleTypeId': roleTypeId,
            'defaultRole': defaultRole
        });
    }

    /**
     * createPartner()
     */
    createPartner(partner: PartnerModel): Observable<any> {
        return this._http.post<any>('enquiry/api/partners/create', partner);
    }

    /**
     * updatePartner()
     */
    updatePartner(partner: PartnerModel): Observable<any> {
        return this._http.put<any>('enquiry/api/partners/update', partner);
    }

    /**
     * getBusinessPartnerContactDetails()
     */
    getBusinessPartnerContactDetails(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerLoanContacts/search/findByPartnerIdOrderBySerialNumberDesc', { params: { partnerId } });
    }

    /**
     * createBusinessPartnerContactDetails()
     */
    createBusinessPartnerContactDetails(contactDetails: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerLoanContacts/create', contactDetails, { params: { businessPartnerId } });
    }

    /**
     * updateBusinessPartnerContactDetails()
     */
    updateBusinessPartnerContactDetails(contactDetails: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerLoanContacts/update', contactDetails);
    }

    /**
     * getBusinessPartnerBankDetails()
     */
    getBusinessPartnerBankDetails(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerBankDetails/search/findByPartnerIdOrderBySerialNumberDesc', { params: { partnerId } });
    }

    /**
     * createBankDetails()
     */
    createBusinessPartnerBankDetails(bankDetails: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerBankDetails/create', bankDetails, { params: { businessPartnerId } });
    }

    /**
     * updateBusinessPartnerBankDetails()
     */
    updateBusinessPartnerBankDetails(bankDetails: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerBankDetails/update', bankDetails);
    }

    /**
     * getIndustrySystems()
     */
    getIndustrySystems(): Observable<any> {
        return this._http.get<any>('enquiry/api/industrySystems', { params: { 'size': '1000', 'sort': 'id,asc' } });
    }

    /**
     * getAllIndustryTypes()
     */
    getAllIndustryTypes(): Observable<any> {
        return this._http.get<any>('enquiry/api/industryTypes', { params: { 'size': '1000', 'sort': 'id,asc' } });
    }

    /**
     * getIndustryTypes()
     */
    getIndustryTypes(industrySystemId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/industryTypes/search/findByIndustrySystemId', { params: { industrySystemId } });
    }

    /**
     * getBusinessPartnerIndustries()
     */
    getBusinessPartnerIndustries(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerIndustries/search/findByPartnerIdOrderBySerialNumberDesc', { params: { partnerId } });
    }

    /**
     * createBusinessPartnerIndustry()
     */
    createBusinessPartnerIndustry(industry: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerIndustries/create', industry, { params: { businessPartnerId } });
    }

    /**
     * updateBusinessPartnerIndustry()
     */
    updateBusinessPartnerIndustry(industry: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerIndustries/update', industry);
    }

    /**
     * getIdentificationCategories()
     */
    getIdentificationCategories(): Observable<any> {
        return this._http.get<any>('enquiry/api/identificationCategories?size=100&sort=value,asc');
    }

    /**
     * getBusinessPartnerIdentificationDetails()
     */
    getBusinessPartnerIdentificationDetails(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerIdentifications/search/findByPartnerIdOrderBySerialNumberDesc', { params: { partnerId } });
    }

    /**
     * createBusinessPartnerIdentificationDetails()
     */
    createBusinessPartnerIdentificationDetails(identification: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerIdentifications/create', identification, { params: { businessPartnerId } });
    }

    /**
     * updateBusinessPartnerIdentificationDetails()
     */
    updateBusinessPartnerIdentificationDetails(identification: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerIdentifications/update', identification);
    }

    /**
     * uploadVaultDocument()
     */
    public uploadVaultDocument(file: FormData): Observable<any> {
        return this._http.post('enquiry/api/upload', file);
    }

    
    /**
     * sendBusinessPartnerForWorkflowApproval()
     */
    public sendBusinessPartnerForWorkflowApproval(partnerId: string, requestorName: string, requestorEmail: string): Observable<any> {
        let requestObj = {
            'businessProcessId': partnerId,
            'requestorName': requestorName,
            'requestorEmail': requestorEmail,
            'processName': 'BusinessPartner'
        }
        return this._http.put<any>('enquiry/api/startprocess', requestObj);
    }
}
