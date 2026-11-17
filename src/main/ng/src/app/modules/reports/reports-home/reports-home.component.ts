import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent, IconComponent } from '@fundamental-ngx/core';
import { REPORT_MODULES, ReportDefinition, ReportModule, reportCount } from '../reports.model';

/**
 * Reports app (menu "Reports"): the four report modules — Loans Management, Collaterals, Business Partner,
 * Miscellaneous — as tiles. The button of a tile opens the module's list of reports; a report is launched from
 * the list.
 */
@Component({
    selector: 'app-reports-home',
    templateUrl: './reports-home.component.html',
    styleUrl: './reports-home.component.scss',
    imports: [ButtonComponent, IconComponent]
})
export class ReportsHomeComponent {

    readonly modules = REPORT_MODULES;
    readonly total = reportCount();

    /** The module whose report list is open (one at a time). */
    open: ReportModule | null = null;

    constructor(private router: Router) { }

    toggle(module: ReportModule): void {
        this.open = this.open === module ? null : module;
    }

    isOpen(module: ReportModule): boolean {
        return this.open === module;
    }

    launch(report: ReportDefinition): void {
        this.router.navigate(['/reports', report.path]);
    }
}
