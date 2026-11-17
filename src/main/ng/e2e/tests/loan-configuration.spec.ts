import { expect, Page, test } from '@playwright/test';
import { MockApi } from './mock-api';

/** Configuration > Loans: the tables of the configInitializer runners, maintained with the generic table app. */
const LOAN_CLASSES = {
    key: 'loan-classes', title: 'Loan Classes', description: 'Classes (sectors) of loans', configClass: 'Initializer',
    module: 'loans', valueField: 'code', labelField: 'value',
    fields: [
        { name: 'id', label: 'ID', type: 'NUMBER', idKind: 'GENERATED_ID', businessKey: false, required: false, maxLength: 255,
            referenceTable: null, referenceEntity: false, options: [], idField: true, visible: false, changeable: false },
        { name: 'code', label: 'Loan Class', type: 'TEXT', idKind: 'NONE', businessKey: true, required: true, maxLength: 20,
            referenceTable: null, referenceEntity: false, options: [], idField: false, visible: true, changeable: false },
        { name: 'value', label: 'Description', type: 'TEXT', idKind: 'NONE', businessKey: false, required: true, maxLength: 255,
            referenceTable: null, referenceEntity: false, options: [], idField: false, visible: true, changeable: true }
    ]
};

async function mockLoanClasses(page: Page, posted: unknown[]): Promise<void> {
    const rows = [
        { id: '6f1c', values: { code: '1', value: 'PowerGen-Convl' }, labels: {} },
        { id: '7a2d', values: { code: '2', value: 'PowerGen-Renew' }, labels: {} }
    ];
    await page.route('**/configuration/bp-tables/loan-classes**', async route => {
        const request = route.request();
        if (request.method() === 'POST') {
            const body = request.postDataJSON();
            posted.push(body);
            const row = { id: '8b3e', values: body.values, labels: {} };
            rows.push(row);
            await route.fulfill({ status: 201, json: row });
            return;
        }
        await route.fulfill({ json: { table: LOAN_CLASSES, rows, page: 0, size: 25, total: rows.length, search: '',
            canChange: true, userRole: 'ZLM023', references: {} } });
    });
}

test.describe('Configuration: Loans', () => {

    test('the Loans tab lists the loan tables; a loan class is added', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        const posted: unknown[] = [];
        await mockLoanClasses(page, posted);
        await page.goto('/configuration/loan-tables/loan-classes');

        await expect(page.getByRole('tab', { name: 'Loans' })).toHaveClass(/is-selected/);
        const apps = page.getByRole('navigation', { name: 'Loans configuration apps' });
        await expect(apps.locator('.cfg-shell__app')).toHaveCount(35);
        for (const group of ['Loan Classification', 'ICC, Sanction & Rejection', 'Fees, Interest & Accounts', 'Documentation',
            'Organization & Geography', 'Users & Authorizations', 'System']) {
            await expect(apps.locator('.cfg-shell__group', { hasText: group })).toHaveCount(1);
        }
        await expect(page.getByText('Configuration: Loan Classes')).toBeVisible();
        await expect(page.getByRole('cell', { name: 'PowerGen-Renew' })).toBeVisible();

        await page.getByRole('button', { name: 'Change' }).click();
        await page.getByRole('button', { name: 'Add Entry' }).click();
        await page.locator('#bt-code').fill('99');
        await page.locator('#bt-value').fill('Green Hydrogen');
        await page.getByRole('button', { name: 'Add', exact: true }).click();
        await expect.poll(() => posted.length).toBe(1);
        expect(posted[0]).toEqual({ values: { code: '99', value: 'Green Hydrogen' } });
        await expect(page.getByRole('cell', { name: 'Green Hydrogen' })).toBeVisible();
    });

    test('the Business Partner tab keeps its own apps', async ({ page }) => {
        const api = new MockApi({ role: 'ZLM023' });
        await api.install(page);
        await mockLoanClasses(page, []);
        await page.goto('/configuration/loan-tables/loan-classes');
        await page.getByRole('tab', { name: 'Business Partner' }).click();
        await expect(page).toHaveURL(/\/configuration\/bupa-field-status$/);
        await expect(page.getByRole('navigation', { name: 'Business Partner configuration apps' })
            .getByRole('link', { name: 'Loan Classes' })).toHaveCount(0);
    });
});
