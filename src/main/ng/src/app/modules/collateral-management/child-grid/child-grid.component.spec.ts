import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DialogService } from '@fundamental-ngx/core';
import { MessageService } from '../../../message.service';
import { GRIDS } from '../collateral.model';
import { CollateralService } from '../collateral.service';
import { ChildGridComponent } from './child-grid.component';

describe('ChildGridComponent', () => {
    let fixture: ComponentFixture<ChildGridComponent>;
    let component: ChildGridComponent;

    beforeEach(() => {
        const collateralService = jasmine.createSpyObj<CollateralService>('CollateralService', ['deleteChild', 'errorMessage', 'downloadUrl']);
        collateralService.downloadUrl.and.callFake((reference: string, name: string | null | undefined) => `/download/${reference}/${name}`);
        TestBed.configureTestingModule({
            imports: [ChildGridComponent],
            providers: [
                { provide: CollateralService, useValue: collateralService },
                { provide: MessageService, useValue: jasmine.createSpyObj('MessageService', ['showSuccess', 'showError']) },
                { provide: DialogService, useValue: jasmine.createSpyObj('DialogService', ['open']) }
            ]
        });
        fixture = TestBed.createComponent(ChildGridComponent);
        component = fixture.componentInstance;
        component.lists = { DOCUMENT_STAGE: [{ code: '2', description: 'Perfection' }] };
        component.itemId = 'I1';
    });

    function render(): HTMLElement {
        fixture.detectChanges();
        return fixture.nativeElement as HTMLElement;
    }

    it('formats dates, numbers and coded values', () => {
        component.grid = GRIDS.securities;
        expect(component.cell({ securitiesChangeDate: '2020-01-01' }, { key: 'securitiesChangeDate', label: 'Date', type: 'date' }))
            .toBe('01.01.2020');
        expect(component.cell({ numberOfUnits: 100 }, { key: 'numberOfUnits', label: 'Units', type: 'number', decimals: 5 }))
            .toBe('100.00000');
        component.grid = GRIDS.documents;
        expect(component.cell({ documentStage: '2' }, { key: 'documentStage', label: 'Stage', type: 'code', list: 'DOCUMENT_STAGE' }))
            .toBe('2 Perfection');
        expect(component.cell({}, { key: 'remarks', label: 'Remarks' })).toBe('');
    });

    it('links uploaded documents and marks documents kept in SAP', () => {
        component.grid = GRIDS.documents;
        component.rows = [
            { id: 'D1', serialNumber: 1, documentType: 'ZPFSLM101', fileReference: 'ref-1', fileName: 'Opinion.pdf' },
            { id: 'D2', serialNumber: 2, documentType: 'ZPFSLM101', bdsDocumentId: '0050568C' }
        ];
        const element = render();
        const link = element.querySelector('a.cm-grid-table__file') as HTMLAnchorElement;
        expect(link.textContent).toContain('Opinion.pdf');
        expect(link.getAttribute('href')).toBe('/download/ref-1/Opinion.pdf');
        expect(element.textContent).toContain('In SAP');
    });

    it('offers Create, Change and Delete only in change mode of the collateral for users with change access', () => {
        component.grid = GRIDS.coverages;
        component.rows = [];
        component.canWrite = true;
        component.editable = false;
        expect(render().textContent).not.toContain('Create');

        component.editable = true;
        fixture.componentRef.changeDetectorRef.markForCheck();
        expect(render().textContent).toContain('Create');
        expect(component.canChange).toBeTrue();

        component.itemId = null;
        expect(component.canChange).toBeFalse();
    });
});
