import { Component, Input } from '@angular/core';
import { IconComponent } from '@fundamental-ngx/core';
import { SapSyncStatus } from '../../homepage.service';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

@Component({
    selector: 'app-sap-sync-widget',
    imports: [
        DashboardCardComponent,
        IconComponent
    ],
    templateUrl: './sap-sync-widget.component.html',
    styleUrl: './sap-sync-widget.component.scss'
})
export class SapSyncWidgetComponent {

    @Input() loading = false;

    /**
     * SAP posting status of the loan applications taken up for processing, null if it could not be loaded
     */
    @Input() status: SapSyncStatus | null = null;
}
