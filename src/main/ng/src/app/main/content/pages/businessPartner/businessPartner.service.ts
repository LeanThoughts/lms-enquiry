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
        return forkJoin([
            this._partnerService.getBusinessPartnerRoleTypes(),
            this._loanEnquiryService.getStates(),
            this._loanMonitoringService.getBanks(),
            this.getIndustrySystems()
        ]);
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
     * getBankDetails()
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
     * updateBankDetails()
     */
    updateBusinessPartnerBankDetails(bankDetails: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerBankDetails/update', bankDetails);
    }

    /**
     * getIndustrySystems()
     */
    getIndustrySystems(): Observable<any> {
        return this._http.get<any>('enquiry/api/industrySystems');
    }

    /**
     * getIndustries()
     */
    getBusinessPartnerIndustries(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerIndustries/search/findByPartnerIdOrderBySerialNumberDesc', { params: { partnerId } });
    }

    /**
     * createIndustry()
     */
    createBusinessPartnerIndustry(industry: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerIndustries/create', industry, { params: { businessPartnerId } });
    }

    /**
     * updateIndustry()
     */
    updateBusinessPartnerIndustry(industry: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerIndustries/update', industry);
    }
}
