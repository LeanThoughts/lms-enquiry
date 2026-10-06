import { Locator, Page, expect } from '@playwright/test';

/** Chooses an entry of a Fundamental select (dropdown) inside the given scope, by the field's label. */
export async function choose(scope: Page | Locator, label: string, option: string): Promise<void> {
    const field = scope.locator('app-collateral-field').filter({ hasText: label }).first();
    await field.locator('.fd-select__control, fd-select').first().click();
    const page = 'page' in scope ? (scope as Locator).page() : (scope as Page);
    await page.locator('[role=option]').filter({ hasText: option }).first().click();
}

/** The input of a collateral field, by its label. */
export function input(scope: Page | Locator, label: string): Locator {
    return scope.locator('app-collateral-field').filter({ hasText: label }).first().locator('input, textarea').first();
}

/** Opens the collateral list of the mocked loan. */
export async function openList(page: Page): Promise<void> {
    await page.goto('/collateral-management/loan/L1');
    await expect(page.locator('tbody tr').first()).toBeVisible();
}

/** Opens collateral 912 (double-click in the list). */
export async function openCollateral912(page: Page): Promise<void> {
    await openList(page);
    await page.locator('tbody tr').filter({ hasText: '912' }).first().dblclick();
    await expect(page.locator('.cm-tabs')).toBeVisible();
}

export async function openTab(page: Page, label: string): Promise<void> {
    await page.locator('.cm-tabs__tab').filter({ hasText: label }).first().click();
}

/** The dialog that is open. */
export function dialog(page: Page): Locator {
    return page.locator('fd-dialog').last();
}
