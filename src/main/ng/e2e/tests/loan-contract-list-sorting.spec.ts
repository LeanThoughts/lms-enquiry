import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Loan Contracts List: sentence-case column labels, sorting all results by a header click. */
function loan(id: number, borrower: string | null, cost: number | null, projectType: string) {
    return {
        partner: borrower ? { partyName1: borrower } : null,
        loanApplication: { enquiryNo: { id }, loanEnquiryDate: '2025-01-0' + (id % 9 + 1), functionalStatus: 1,
            functionalStatusDescription: 'Enquiry Stage', busPartnerNumber: '1000' + id, projectName: 'Project ' + id,
            projectLocationState: 'Delhi', loanContractId: '00000100038' + id, projectType, loanClass: '1',
            projectCapacity: id * 10, projectCapacityUnit: 'MW', assistanceType: 'D', projectCost: cost }
    };
}

async function mockLoanContracts(page: Page): Promise<void> {
    const embedded = (name: string, rows: unknown[]) => ({ _embedded: { [name]: rows } });
    await page.route(/\/api\/functionalStatuses/, r => r.fulfill({ json: embedded('functionalStatuses', []) }));
    await page.route(/\/api\/technicalStatus/, r => r.fulfill({ json: [] }));
    await page.route(/\/api\/loanClasses/, r => r.fulfill({ json: embedded('loanClasses', [{ code: '1', value: 'PowerGen-Convl' }]) }));
    await page.route(/\/api\/projectTypes/, r => r.fulfill({ json: embedded('projectTypes',
        [{ code: '05', value: 'Renewable-Solar' }, { code: '01', value: 'Thermal-Coal' }]) }));
    await page.route(/\/api\/financingTypes/, r => r.fulfill({ json: embedded('financingTypes', []) }));
    await page.route(/\/api\/assistanceTypes/, r => r.fulfill({ json: embedded('assistanceTypes', [{ code: 'D', value: 'Debt' }]) }));
    // 12 results: more than one page of 10
    const results = [
        loan(101, 'Sunrise Power', 250.5, '05'), loan(102, 'Bharat Rail', 1200, '01'), loan(103, null, null, '05'),
        ...Array.from({ length: 9 }, (_, i) => loan(110 + i, 'Zeta Infra ' + i, 10 + i, '01'))
    ];
    await page.route(/\/api\/loanApplications\/loanContracts\/search/, r => r.fulfill({ json: results }));
}

test.describe('Loan Contracts List: sorting', () => {

    test('labels are sentence case without (Rs Cr); a header click sorts all results, the third click restores the order', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        await mockLoanContracts(page);
        await page.goto('/loan-contract-search');
        await page.locator('#partyName').fill('a');
        await page.getByRole('button', { name: 'Search', exact: true }).click();
        await expect(page.locator('.lc-results__summary')).toContainText('12 loan contracts found');

        await expect(page.locator('thead th')).toHaveText(['Enquiry', 'Functional status', 'Borrower', 'Project', 'Loan contract',
            'Project type', 'Loan class', 'Capacity', 'Assistance type', 'Project cost']);
        const firstEnquiry = page.locator('tbody tr').first().locator('td').first();
        await expect(firstEnquiry).toContainText('118');                     // default: newest enquiry first

        // project cost ascending over all 12 results (not just the page); the empty cost is last
        const cost = page.locator('th', { hasText: 'Project cost' });
        await cost.getByRole('button').click();
        await expect(cost).toHaveAttribute('aria-sort', 'ascending');
        await expect(page.locator('tbody tr').first().locator('td').last()).toHaveText('10');
        await cost.getByRole('button').click();
        await expect(page.locator('tbody tr').first().locator('td').last()).toHaveText('1200');

        // borrower ascending; the result without a borrower goes to the end (page 2)
        await page.locator('th', { hasText: 'Borrower' }).getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Bharat Rail');

        // project type sorts by the description shown, not the code
        const type = page.locator('th', { hasText: 'Project type' });
        await type.getByRole('button').click();
        await expect(page.locator('tbody tr').first()).toContainText('Renewable-Solar');
        await type.getByRole('button').click();
        await type.getByRole('button').click();
        await expect(type).not.toHaveAttribute('aria-sort', /.+/);
        await expect(firstEnquiry).toContainText('118');
    });
});
