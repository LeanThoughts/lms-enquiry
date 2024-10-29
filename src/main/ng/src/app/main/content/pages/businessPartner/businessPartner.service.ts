import {BehaviorSubject, Observable, forkJoin} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import { PartnerService } from '../administration/partner/partner.service';
import { LoanEnquiryService } from '../enquiry/enquiryApplication.service';
import { PartnerModel } from '../../model/partner.model';

@Injectable()
export class BusinessPartnerService {

    businessPartnerCategoryAndRole: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     */
    constructor(private _http: HttpClient, 
        private _partnerService: PartnerService,
        private _loanEnquiryService: LoanEnquiryService) { }

    /**
     * resolve()
     */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return forkJoin([
            this._partnerService.getBusinessPartnerRoleTypes(),
            this._loanEnquiryService.getStates()
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
     * getContactDetails()
     */
    getContactDetails(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerLoanContacts/search/findByPartnerId', { params: { partnerId } });
    }

    /**
     * createContactDetails()
     */
    createContactDetails(contactDetails: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerLoanContacts/create', contactDetails, { params: { businessPartnerId } });
    }

    /**
     * updateContactDetails()
     */
    updateContactDetails(contactDetails: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerLoanContacts/update', contactDetails);
    }

    /**
     * getBankDetails()
     */
    getBankDetails(partnerId: string): Observable<any> {
        return this._http.get<any>('enquiry/api/businessPartnerBankDetails/search/findByPartnerId', { params: { partnerId } });
    }

    /**
     * createBankDetails()
     */
    createBankDetails(bankDetails: any, businessPartnerId: string): Observable<any> {
        return this._http.post<any>('enquiry/api/businessPartnerBankDetails/create', bankDetails, { params: { businessPartnerId } });
    }

    /**
     * updateBankDetails()
     */
    updateBankDetails(bankDetails: any): Observable<any> {
        return this._http.put<any>('enquiry/api/businessPartnerBankDetails/update', bankDetails);
    }
}
