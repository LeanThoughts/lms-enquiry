import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class ICCApprovalService {

    _iccApproval: BehaviorSubject<any> = new BehaviorSubject({});

    /**
     * constructor()
     * @param _http
     */
    constructor(private _http: HttpClient) {
    }

    /**
     * getBoardApproval()
     */
    // public getBoardApproval(loanApplicationId: string): Observable<any> {
    //     return this._http.get("enquiry/api/boardApprovals/search/findByLoanApplicationId?loanApplicationId=" + loanApplicationId);
    // }

}
