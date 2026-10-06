import { Routes } from '@angular/router';
import { routeInterceptor } from '../../route.interceptor';
import { BupaFieldStatusComponent } from './bupa-field-status/bupa-field-status.component';
import { BupaEntitySetFieldStatusComponent } from './bupa-entity-set-field-status/bupa-entity-set-field-status.component';
import { ChecklistIdConfigComponent } from '../collateral-management/checklist-id-config/checklist-id-config.component';
import { ConfigurationShellComponent } from './configuration-shell/configuration-shell.component';
import { ConfigurationEmptyComponent } from './configuration-shell/configuration-empty.component';
import { BpTableComponent } from './bp-tables/bp-table.component';

/**
 * Menu "Configuration": the Configuration workspace with module tabs. The apps are listed per module in
 * configuration-shell/configuration-apps.ts.
 */
export default [
    {
        path: 'configuration',
        component: ConfigurationShellComponent,
        canActivate: [routeInterceptor],
        children: [
            { path: '', redirectTo: 'bupa-field-status', pathMatch: 'full' },
            // Business Partner
            { path: 'bupa-field-status', component: BupaFieldStatusComponent },
            { path: 'bupa-entity-set-field-status', component: BupaEntitySetFieldStatusComponent },
            // Tables of the CommandLineRunner configs (BpTableDefinitions), one app each
            { path: 'bp-tables/:table', component: BpTableComponent },
            // Collaterals
            { path: 'checklist-id-number-range', component: ChecklistIdConfigComponent },
            // Modules without apps yet (Loans, General Config)
            { path: ':module', component: ConfigurationEmptyComponent }
        ]
    }
] as Routes;
