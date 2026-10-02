import { Component, Input } from '@angular/core';
import { BusyIndicatorComponent, IconComponent } from '@fundamental-ngx/core';

/**
 * Card shell of a homepage widget. The widget content is projected once loading has finished without an error.
 */
@Component({
    selector: 'app-dashboard-card',
    imports: [
        BusyIndicatorComponent,
        IconComponent
    ],
    templateUrl: './dashboard-card.component.html',
    styleUrl: './dashboard-card.component.scss'
})
export class DashboardCardComponent {

    @Input({ required: true }) title!: string;
    @Input() icon = '';
    @Input() subtitle = '';
    @Input() loading = false;
    @Input() error = '';
}
