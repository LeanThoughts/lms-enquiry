import { DecimalPipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { PortfolioSlice } from '../../homepage.service';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

interface PortfolioBar extends PortfolioSlice {
    width: number;
}

/**
 * Horizontal bar chart of the loan contract amount per group, e.g. per project type or state
 */
@Component({
    selector: 'app-portfolio-widget',
    imports: [
        DashboardCardComponent,
        DecimalPipe
    ],
    templateUrl: './portfolio-widget.component.html',
    styleUrl: './portfolio-widget.component.scss'
})
export class PortfolioWidgetComponent {

    @Input({ required: true }) title!: string;
    @Input() icon = 'bar-chart';
    @Input() loading = false;

    // Description of a slice code, used when the dashboard returns codes only
    @Input() describe: (code: string) => string = code => code;

    bars: PortfolioBar[] | null = [];

    /**
     * Largest groups by amount, null if they could not be loaded
     */
    @Input() set slices(slices: PortfolioSlice[] | null) {
        if (slices === null) {
            this.bars = null;
            return;
        }
        const maxAmount = Math.max(0, ...slices.map(slice => slice.amount));
        const maxCount = Math.max(0, ...slices.map(slice => slice.count));
        this.bars = slices.map(slice => ({
            ...slice,
            // Fall back to the count when no amounts are maintained
            width: maxAmount > 0 ? slice.amount / maxAmount * 100 : maxCount > 0 ? slice.count / maxCount * 100 : 0
        }));
    }
}
