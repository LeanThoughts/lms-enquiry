import {PartnerModel} from "../../../model/partner.model";
import {Observable, BehaviorSubject, forkJoin} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";



@Injectable()
export class PartnerService {


  partners: BehaviorSubject<PartnerModel[]>;

  public selectedPartner: BehaviorSubject<PartnerModel> = new BehaviorSubject(new PartnerModel({}));

  public  partner: BehaviorSubject<PartnerModel>;

  selectedPartnerId: BehaviorSubject<string>;



  /**
   * constructor()
   * @param _http
   */
  constructor(private _http: HttpClient) { }

  /**
   * resolve()
   * Router resolveer, fetches data before the ui is created.
   * @param route
   * @param state
   */
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    return forkJoin([
      this.getBusinessPartnerRoleTypes()
    ]);
  }

  /**
   * getPartnerBYEmail()
   * @param mail
   */
  public getPartnerByEmail(email: string): Observable<PartnerModel> {
    return this._http.get<PartnerModel>('enquiry/api/partner/email?email=' + email);
  }

  /**
   * searchPartners()
   * Fetches a list of loan applications based on the request parameters.
   * @param request
   */
  public searchPartners(request: any): Observable<any> {
   return this._http.get<any>('enquiry/api/partner/queryParams?query=', request);
    // console.log(this.results);
    // return this.results;
  }

  public getPartners(request: Array<string>): Observable<PartnerModel[]> {

    let queryParams;

    request.forEach(function(value) {
      if (value != undefined){
        queryParams = queryParams + value + "&";
      }
    });


    return new Observable(observer => {
      const partners = new Array<PartnerModel>();
      this._http.get<PartnerModel[]>('enquiry/api/partner/queryParams?query=' + request).subscribe(result => {
        result.map(partnerModel => {
          partners.push(new PartnerModel(partnerModel));
        });
        observer.next(partners);
        observer.complete();
      });
    });
  }

    /**
     * getBusinessPartnerRoles()
     * Fetches a list of business partner roles.
     */
    getBusinessPartnerRoleTypes(): Observable<any> {
      return this._http.get<any>('enquiry/api/businessPartnerRoleTypes');
    }
}
