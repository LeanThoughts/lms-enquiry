import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { CollateralService } from './collateral.service';

describe('CollateralService', () => {
    const base = environment.primaryApiHost + '/collaterals';
    let service: CollateralService;
    let http: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
        service = TestBed.inject(CollateralService);
        http = TestBed.inject(HttpTestingController);
    });

    afterEach(() => http.verify());

    it('loads the checklist of a loan and of a workflow task', () => {
        service.getChecklist('L1').subscribe(checklist => expect(checklist.id).toBe('C1'));
        http.expectOne(`${base}/loans/L1`).flush({ id: 'C1', items: [] });

        service.getChecklistById('C1').subscribe(checklist => expect(checklist.id).toBe('C1'));
        http.expectOne(`${base}/checklists/C1`).flush({ id: 'C1', items: [] });
    });

    it('sends a checklist for approval with its id as business process id', () => {
        service.sendForApproval('C1').subscribe();
        const request = http.expectOne(`${base}/workflow/startprocess`);
        expect(request.request.method).toBe('PUT');
        expect(request.request.body).toEqual({ businessProcessId: 'C1' });
        request.flush({ id: 'C1', workFlowStatusCode: 2 });
    });

    it('uses the child type as URL segment for child rows', () => {
        service.createChild('documents', 'I1', { documentStage: '1' }).subscribe();
        expect(http.expectOne(`${base}/items/I1/documents`).request.method).toBe('POST');
        service.updateChild('securities', 'P1', { securitiesType: 'CCD' }).subscribe();
        expect(http.expectOne(`${base}/securities/P1`).request.method).toBe('PUT');
        service.deleteChild('coverages', 'V1').subscribe();
        expect(http.expectOne(`${base}/coverages/V1`).request.method).toBe('DELETE');
    });

    it('caches the value lists', () => {
        service.getValueLists().subscribe();
        service.getValueLists().subscribe();
        http.expectOne(`${base}/value-lists`).flush({ CONDITION_GROUP: [] });
    });

    it('uploads a file and returns its reference; builds the download link', () => {
        const file = new File(['%PDF'], 'Deed of pledge.pdf', { type: 'application/pdf' });
        service.uploadFile(file).subscribe(reference => expect(reference).toBe('3f2b8c1e'));
        const request = http.expectOne(environment.primaryApiHost + '/upload');
        expect(request.request.body instanceof FormData).toBeTrue();
        request.flush({ fileReference: '3f2b8c1e' });

        expect(service.downloadUrl('3f2b8c1e', 'Deed of pledge.pdf'))
            .toBe(environment.primaryApiHost + '/download/3f2b8c1e/Deed%20of%20pledge.pdf');
    });

    it('maintains the Checklist ID number range', () => {
        service.setSapHighestNumber(912000).subscribe();
        const request = http.expectOne(`${base}/configuration/checklist-id`);
        expect(request.request.method).toBe('PUT');
        expect(request.request.body).toEqual({ sapHighestNumber: 912000 });
        request.flush({ nextNumber: 912001 });
    });

    it('reads the message of backend errors', () => {
        const backend = new HttpErrorResponse({ status: 412, error: { message: 'Condition Group is required.' } });
        expect(service.errorMessage(backend, 'fallback')).toBe('Condition Group is required.');
        expect(service.errorMessage(new HttpErrorResponse({ status: 0 }), 'fallback'))
            .toBe('The server cannot be reached. Please try again.');
        expect(service.errorMessage(new HttpErrorResponse({ status: 500, error: '<html>' }), 'fallback')).toBe('fallback');
    });
});
