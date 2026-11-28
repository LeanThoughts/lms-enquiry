import { DecimalPipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { IconComponent } from '@fundamental-ngx/core';
import { CardColumn, CardView } from '../management-dashboard.model';
import { ManagementDashboardService } from '../management-dashboard.service';

/**
 * Card of one dashboard section (one SAP navigation). It shows the projected graph by default and, through the
 * graph ⇄ table switch in its header, the same rows as a table built from {@link columns}. Cards without columns
 * have no switch (tables only, e.g. Interest Rates). The chosen view is remembered per card in the browser.
 */
@Component({
    selector: 'app-dashboard-section-card',
    imports: [IconComponent, DecimalPipe],
    templateUrl: './dashboard-section-card.component.html',
    styleUrl: './dashboard-section-card.component.scss'
})
export class DashboardSectionCardComponent implements OnInit {

    /** Key under which the view choice is remembered, e.g. 'loan-book'. */
    @Input({ required: true }) cardId!: string;
    @Input({ required: true }) title!: string;
    @Input() subtitle = '';
    /** Rows of the table view; the graph is projected content. */
    @Input() rows: any[] = [];
    /** Columns of the table view; without columns the card is a table-less (graph only) or a content-only card. */
    @Input() columns: CardColumn[] = [];
    /** Whether the card offers the graph ⇄ table switch. */
    @Input() switchable = true;
    /** Text shown when there are no rows. */
    @Input() emptyText = 'No figures for this period.';

    view: CardView = 'graph';

    constructor(private service: ManagementDashboardService) {
    }

    ngOnInit(): void {
        this.view = this.switchable ? this.service.cardView(this.cardId) : 'table';
    }

    setView(view: CardView): void {
        if (this.view !== view) {
            this.view = view;
            this.service.setCardView(this.cardId, view);
        }
    }

    isNumber(value: unknown): value is number {
        return typeof value === 'number' && !isNaN(value);
    }
}
