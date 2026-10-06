import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { DialogService } from '@fundamental-ngx/core';
import { of, throwError } from 'rxjs';
import { MessageService } from '../../../message.service';
import { CollateralChecklist, CollateralItem } from '../collateral.model';
import { CollateralService } from '../collateral.service';
import { CollateralListComponent } from './collateral-list.component';

describe('CollateralListComponent', () => {
    let fixture: ComponentFixture<CollateralListComponent>;
    let component: CollateralListComponent;
    let collateralService: jasmine.SpyObj<CollateralService>;
    let messageService: jasmine.SpyObj<MessageService>;
    let dialogService: jasmine.SpyObj<DialogService>;

    const loan = { loanApplicationId: 'L1', loanContractId: '0000010003200', projectName: 'Solar Park', borrowerName: 'PTC' };
    const items = [
        { id: 'I1', checklistIdNo: 912, conditionGroup: '04', conditionDescription: 'Collateral', complianceStatus: '1' },
        { id: 'I2', checklistIdNo: 913, conditionGroup: '04', conditionDescription: 'RE Condition', complianceStatus: '2' }
    ] as unknown as CollateralItem[];

    function checklist(workFlowStatusCode: number, extra: Partial<CollateralChecklist> = {}): CollateralChecklist {
        return { id: 'C1', loan, items, workFlowStatusCode, workFlowStatusDescription: 'x', ...extra } as unknown as CollateralChecklist;
    }

    function setUp(data: CollateralChecklist, canWrite = true): void {
        collateralService = jasmine.createSpyObj<CollateralService>('CollateralService',
            ['getChecklist', 'getChecklistById', 'getValueLists', 'getAccess', 'sendForApproval', 'deleteItem', 'errorMessage']);
        collateralService.getChecklist.and.returnValue(of(data));
        collateralService.getChecklistById.and.returnValue(of(data));
        collateralService.getValueLists.and.returnValue(of({
            COMPLIANCE_STATUS: [{ code: '1', description: 'Complied' }, { code: '2', description: 'Not Complied' }]
        }));
        collateralService.getAccess.and.returnValue(of({ canWrite, userName: 'u', role: canWrite ? 'ZLM023' : 'ZLM014', writeRoles: [] }));
        collateralService.errorMessage.and.callFake((_error: unknown, fallback: string) => fallback);
        messageService = jasmine.createSpyObj<MessageService>('MessageService', ['showSuccess', 'showError', 'showInfo', 'showWarning']);
        dialogService = jasmine.createSpyObj<DialogService>('DialogService', ['open']);
        dialogService.open.and.returnValue({ afterClosed: of(true) } as never);

        TestBed.configureTestingModule({
            imports: [CollateralListComponent],
            providers: [
                provideRouter([]),
                { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ loanApplicationId: 'L1' })) } },
                { provide: CollateralService, useValue: collateralService },
                { provide: MessageService, useValue: messageService },
                { provide: DialogService, useValue: dialogService }
            ]
        });
        fixture = TestBed.createComponent(CollateralListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    }

    function text(): string {
        return (fixture.nativeElement as HTMLElement).textContent ?? '';
    }

    function button(label: string): HTMLButtonElement | undefined {
        return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll('button'))
            .find(element => element.textContent?.trim() === label) as HTMLButtonElement | undefined;
    }

    it('lists the collaterals of the loan with their compliance status', () => {
        setUp(checklist(0));
        expect(collateralService.getChecklist).toHaveBeenCalledWith('L1');
        expect(component.items.length).toBe(2);
        expect(component.compliedCount).toBe(1);
        expect(text()).toContain('0000010003200');
        expect(text()).toContain('912');
        expect(text()).toContain('1 Complied');
    });

    it('offers Create, Delete and Send for Approval to users with change access', () => {
        setUp(checklist(0));
        expect(button('Create')).toBeDefined();
        expect(button('Send for Approval')?.disabled).toBeFalse();
    });

    it('shows display-only users no Create, Delete or Send for Approval', () => {
        setUp(checklist(0), false);
        expect(button('Create')).toBeUndefined();
        expect(button('Delete')).toBeUndefined();
        expect(button('Send for Approval')).toBeUndefined();
        expect(text()).toContain('Display only');
    });

    it('locks the checklist while it waits for approval', () => {
        setUp(checklist(2));
        expect(component.inApproval).toBeTrue();
        expect(button('Create')).toBeUndefined();
        expect(button('Send for Approval')?.disabled).toBeTrue();
        expect(text()).toContain('waiting for approval');
    });

    it('shows the rejection reason of a rejected checklist', () => {
        setUp(checklist(4, { rejectionReason: 'Security trustee missing' }));
        expect(text()).toContain('Rejected by the approver: Security trustee missing');
        expect(component.canSendForApproval).toBeTrue();
    });

    it('sends the checklist for approval after confirmation', () => {
        setUp(checklist(0));
        collateralService.sendForApproval.and.returnValue(of(checklist(2)));

        component.sendForApproval();
        fixture.detectChanges();

        expect(dialogService.open).toHaveBeenCalled();
        expect(collateralService.sendForApproval).toHaveBeenCalledWith('C1');
        expect(component.inApproval).toBeTrue();
        expect(messageService.showSuccess).toHaveBeenCalled();
    });

    it('reports an error when sending fails and keeps the status', () => {
        setUp(checklist(0));
        collateralService.sendForApproval.and.returnValue(throwError(() => new Error('412')));

        component.sendForApproval();

        expect(messageService.showError).toHaveBeenCalledWith('The collateral checklist could not be sent for approval.');
        expect(component.inApproval).toBeFalse();
    });

    it('does not send when the confirmation is cancelled', () => {
        setUp(checklist(0));
        dialogService.open.and.returnValue({ afterClosed: of(false) } as never);
        component.sendForApproval();
        expect(collateralService.sendForApproval).not.toHaveBeenCalled();
    });
});
