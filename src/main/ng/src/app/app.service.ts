import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AppService {

    /**
     * Constructor
     */
    constructor(private http: HttpClient) { }
    
    /**
     * Upload vault document
     */
    uploadVaultDocument(file: FormData): Observable<any> {
        return this.http.post(environment.primaryApiHost + '/upload', file);
    }
}
