import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EnquiryUploadService {

    /**
     * Constructor
     */
    constructor(private http: HttpClient) {         
    }

    /**
     * Upload excel document
     */
    uploadExcelDocument(formData: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/enquiriesExcelUpload', formData);
    }

    /**
     * Create excel enquiries
     */
    createExcelEnquiries() {
        return this.http.post(environment.primaryApiHost + '/createExcelEnquiries', {});
    }

    /**
     * Download enquiries
     */
    downloadEnquiries(dateFrom: Date, dateTo: Date) {
        let df = dateFrom ? dateFrom.toISOString().split('T')[0] : null;
        let dt = dateTo ? dateTo.toISOString().split('T')[0] : null;
        window.open(environment.primaryApiHost + '/enquiriesExcelDownload?enquiryDateFrom=' + df + '&enquiryDateTo=' + dt);
    }
}