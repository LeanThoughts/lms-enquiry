import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BusyIndicatorComponent, BusyIndicatorExtendedDirective, BusyIndicatorSize } from '@fundamental-ngx/core/busy-indicator';
import { BusyIndicatorService } from './busy-indicator.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, BusyIndicatorComponent, BusyIndicatorExtendedDirective],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'cloudledger-sales-bo';

    size: BusyIndicatorSize = 's';

    readonly busyIndicatorService = inject(BusyIndicatorService);
}
