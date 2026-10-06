import {
    GRIDS,
    TABS,
    WORKFLOW_STATUS,
    formatDate,
    toIsoDate,
    valueDescription,
    valueText,
    workflowState
} from './collateral.model';

describe('Collateral model helpers', () => {
    const lists = {
        COMPLIANCE_STATUS: [{ code: '1', description: 'Complied' }, { code: '2', description: 'Not Complied' }]
    };

    it('formats ISO dates and Jackson date arrays as dd.MM.yyyy', () => {
        expect(formatDate('2021-06-01')).toBe('01.06.2021');
        expect(formatDate('2021-06-01T10:15:00')).toBe('01.06.2021');
        expect(formatDate([2021, 6, 1])).toBe('01.06.2021');
        expect(formatDate(null)).toBe('');
        expect(formatDate('not a date')).toBe('');
    });

    it('converts date arrays and strings to ISO dates', () => {
        expect(toIsoDate([2001, 1, 9])).toBe('2001-01-09');
        expect(toIsoDate('2001-01-09')).toBe('2001-01-09');
        expect(toIsoDate(20010109)).toBeNull();
    });

    it('shows "code description" for coded values and the code when it is unknown', () => {
        expect(valueText(lists, 'COMPLIANCE_STATUS', '1')).toBe('1 Complied');
        expect(valueText(lists, 'COMPLIANCE_STATUS', '9')).toBe('9');
        expect(valueText(lists, 'COMPLIANCE_STATUS', '')).toBe('');
        expect(valueDescription(lists, 'COMPLIANCE_STATUS', '2')).toBe('Not Complied');
        expect(valueDescription(lists, 'MISSING', 'X')).toBe('X');
    });

    it('colours the workflow status: approved green, rejected red, in approval orange', () => {
        expect(workflowState(WORKFLOW_STATUS.APPROVED)).toBe('positive');
        expect(workflowState(WORKFLOW_STATUS.REJECTED)).toBe('negative');
        expect(workflowState(WORKFLOW_STATUS.SENT_FOR_APPROVAL)).toBe('critical');
        expect(workflowState(WORKFLOW_STATUS.NOT_SENT)).toBe('informative');
        expect(workflowState(null)).toBe('informative');
    });

    it('has the 14 tabs of the SAP screen, six of them with a child grid', () => {
        expect(TABS.length).toBe(14);
        expect(TABS.filter(tab => !!tab.grid).map(tab => tab.grid!.type))
            .toEqual(['coverages', 'roc', 'cersai', 'nesl', 'documents', 'securities']);
        expect(TABS.map(tab => tab.id)).toContain('securities');
    });

    it('defines required fields and upload for the documents grid', () => {
        expect(GRIDS.documents.upload).toBeTrue();
        expect(GRIDS.documents.fields.filter(field => field.required).map(field => field.key))
            .toEqual(['documentStage', 'documentType']);
        expect(GRIDS.securities.defaults).toEqual({ nominalValueCurrency: 'INR' });
    });
});
