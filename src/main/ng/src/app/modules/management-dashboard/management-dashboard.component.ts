import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, LOCALE_ID, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonComponent, IconComponent, MessageStripComponent } from '@fundamental-ngx/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DashboardSectionCardComponent } from './cards/dashboard-section-card.component';
import { BarRow, ChartLegendItem, MdBarChartComponent } from './charts/md-bar-chart.component';
import { ColumnSeries, MdColumnChartComponent } from './charts/md-column-chart.component';
import {
    BudgetActual, CardColumn, CashFlow, CollectionEfficiency, EclOverdue, InterestRate, Liquidity, LoanBook,
    ManagementDashboard, percentValue, Proposal, Rating, SmaOverdue
} from './management-dashboard.model';
import { ManagementDashboardService } from './management-dashboard.service';

const TEAL = '#0e7c86';
const TEAL_LIGHT = '#8fc5cb';
const ORANGE = '#e9730c';
const RED = '#bb0000';
const GREEN = '#107e3e';
const GREY = '#9aa3ad';

/** One tile of the KPI strip (latest quarter of the loan book). */
interface Kpi {
    label: string;
    value: string;
    detail?: string;
    tone?: 'up' | 'down' | '';
}

/**
 * Management Dashboard: the SAP snapshot of ZPFS_LMS_DASHBOARD_SRV for a run date, one card per navigation of the
 * Dashboard entity. Every chart card can be switched to a table (header switch, remembered per card).
 */
@Component({
    selector: 'app-management-dashboard',
    imports: [
        FormsModule, DatePipe, ButtonComponent, IconComponent, MessageStripComponent,
        DashboardSectionCardComponent, MdBarChartComponent, MdColumnChartComponent
    ],
    templateUrl: './management-dashboard.component.html',
    styleUrl: './management-dashboard.component.scss'
})
export class ManagementDashboardComponent implements OnInit, OnDestroy {

    runDate = isoToday();
    amountInCrore = true;

    loading = false;
    errorMessage = '';
    dashboard: ManagementDashboard | null = null;
    fetchedAt: Date | null = null;

    // Derived views, computed once per load
    kpis: Kpi[] = [];
    latestQuarter: LoanBook | null = null;
    loanBookCategories: string[] = [];
    loanBookSeries: ColumnSeries[] = [];
    loanBookTooltips: string[] = [];
    budgetRows: BarRow[] = [];
    liquidityRows: BarRow[] = [];
    smaRows: BarRow[] = [];
    smaLegend: ChartLegendItem[] = [];
    eclRows: BarRow[] = [];
    eclLegend: ChartLegendItem[] = [];
    cashFlowCategories: string[] = [];
    cashFlowSeries: ColumnSeries[] = [];
    cashFlowNotes: (string | null)[] = [];
    cashFlowTooltips: string[] = [];
    collectionCategories: string[] = [];
    collectionSeries: ColumnSeries[] = [];
    proposalRows: BarRow[] = [];

    readonly overdueLegend: ChartLegendItem[] = [
        { name: 'Principal overdue', color: TEAL }, { name: 'Interest overdue', color: TEAL_LIGHT }
    ];

    // Table views
    readonly loanBookColumns: CardColumn<LoanBook>[] = [
        { header: 'Quarter', value: r => r.quarter },
        { header: 'Gross', value: r => r.loanBookGross, align: 'end' },
        { header: 'Net of Prov.', value: r => r.loanBookNetOfProvisions, align: 'end' },
        { header: 'Gross NPA', value: r => r.grossNpa, align: 'end' },
        { header: 'Gross NPA %', value: r => r.grossNpaPercent, align: 'end' },
        { header: 'Net NPA', value: r => r.netNpa, align: 'end' },
        { header: 'Net NPA %', value: r => r.netNpaPercent, align: 'end' },
        { header: 'Prov. (all)', value: r => r.provisionAllLoans, align: 'end' },
        { header: 'Prov. (NPA)', value: r => r.provisionNpaLoans, align: 'end' },
        { header: 'PCR', value: r => r.provisionCoverageRatio, align: 'end' },
        { header: 'PCR (NPA)', value: r => r.provisionCoverageRatioNpa, align: 'end' },
        { header: 'Cost of Funds', value: r => r.costOfFunds, align: 'end' },
        { header: 'Yield %', value: r => r.yieldPercent, align: 'end' }
    ];
    readonly budgetColumns: CardColumn<BudgetActual>[] = [
        { header: 'Particulars', value: r => r.particulars },
        { header: 'Budget', value: r => r.budget, align: 'end' },
        { header: 'Actual', value: r => r.actual, align: 'end' },
        { header: 'Achieved %', value: r => r.budget ? (r.actual ?? 0) / r.budget * 100 : null, format: '1.1-1', align: 'end' }
    ];
    readonly liquidityColumns: CardColumn<Liquidity>[] = [
        { header: 'Particulars', value: r => r.particulars },
        { header: 'Amount', value: r => r.amount, align: 'end' }
    ];
    readonly smaColumns: CardColumn<SmaOverdue>[] = [
        { header: 'Category', value: r => r.particulars || r.smaCategory },
        { header: 'Principal', value: r => r.principalOverdue, align: 'end' },
        { header: 'Interest', value: r => r.interestOverdue, align: 'end' },
        { header: 'Overdue', value: r => r.overdue, align: 'end' },
        { header: 'Outstanding', value: r => r.outstanding, align: 'end' }
    ];
    readonly eclColumns: CardColumn<EclOverdue>[] = [
        { header: 'Stage', value: r => r.particulars || r.eclClassification },
        { header: 'Principal', value: r => r.principalOverdue, align: 'end' },
        { header: 'Interest', value: r => r.interestOverdue, align: 'end' },
        { header: 'Overdue', value: r => r.overdue, align: 'end' },
        { header: 'Effective Capital', value: r => r.effectiveCapital, align: 'end' }
    ];
    readonly cashFlowColumns: CardColumn<CashFlow>[] = [
        { header: 'Period', value: r => r.periodDesc || r.evaluationPeriod },
        { header: 'Inflow Principal', value: r => r.inflowPrincipal, align: 'end' },
        { header: 'Inflow Interest', value: r => r.inflowInterest, align: 'end' },
        { header: 'Prepayment', value: r => r.inflowPrepayment, align: 'end' },
        { header: 'Outflow Principal', value: r => r.outflowPrincipal, align: 'end' },
        { header: 'Outflow Interest', value: r => r.outflowInterest, align: 'end' },
        { header: 'Disbursement', value: r => r.outflowDisbursement, align: 'end' },
        { header: 'Total In', value: r => r.totalInflow, align: 'end' },
        { header: 'Total Out', value: r => r.totalOutflow, align: 'end' },
        { header: 'Net', value: r => r.netFlow, align: 'end' }
    ];
    readonly collectionColumns: CardColumn<CollectionEfficiency>[] = [
        { header: 'Month', value: r => r.monthName || r.monthNumber },
        { header: 'Collection Efficiency %', value: r => r.collectionEfficiency, align: 'end' }
    ];
    readonly interestRateColumns: CardColumn<InterestRate>[] = [
        { header: 'Particulars', value: r => r.particulars },
        { header: 'Rate %', value: r => r.percentage, align: 'end' },
        { header: 'Effective from', value: r => r.effectiveFrom ? formatDate(r.effectiveFrom) : '' }
    ];
    readonly ratingColumns: CardColumn<Rating>[] = [
        { header: 'Instrument', value: r => r.ratingTypeDesc },
        { header: 'CRISIL', value: r => r.crisilRating },
        { header: 'ICRA', value: r => r.icraRating }
    ];
    readonly proposalColumns: CardColumn<Proposal>[] = [
        { header: 'Stage', value: r => r.particulars },
        { header: 'Count', value: r => r.enquiryCount, align: 'end' },
        { header: 'Amount', value: r => r.amount, align: 'end' }
    ];

    private readonly destroy$ = new Subject<void>();
    private readonly pipe = new DecimalPipe(inject(LOCALE_ID));

    constructor(private service: ManagementDashboardService) {
    }

    ngOnInit(): void {
        this.load();
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    /** Unit of the amounts, for card subtitles. */
    get unit(): string {
        return this.amountInCrore ? '₹ Cr' : '₹';
    }

    setUnit(inCrore: boolean): void {
        if (this.amountInCrore !== inCrore) {
            this.amountInCrore = inCrore;
            this.load();
        }
    }

    load(): void {
        if (!this.runDate) {
            this.errorMessage = 'Please choose a run date.';
            return;
        }
        this.loading = true;
        this.errorMessage = '';
        this.service.getDashboard(this.runDate, this.amountInCrore).pipe(takeUntil(this.destroy$)).subscribe({
            next: dashboard => {
                this.dashboard = dashboard;
                this.fetchedAt = new Date();
                this.derive(dashboard);
                this.loading = false;
            },
            error: (error: Error) => {
                this.dashboard = null;
                this.errorMessage = error.message;
                this.loading = false;
            }
        });
    }

    /** Builds the KPI strip and the chart inputs of every card from the loaded dashboard. */
    private derive(d: ManagementDashboard): void {
        const number = (v: number | null | undefined, format = '1.0-0') => v == null ? '–' : (this.pipe.transform(v, format) ?? '–');

        // Loan book: latest quarter with figures, KPI strip, chart
        const quarters = d.loanBook.filter(q => q.loanBookGross != null);
        const latest = quarters.length ? quarters[quarters.length - 1] : null;
        const previous = quarters.length > 1 ? quarters[quarters.length - 2] : null;
        this.latestQuarter = latest;
        this.kpis = latest ? [
            { label: 'Loan Book (Gross)', value: number(latest.loanBookGross), ...change(number, latest.loanBookGross, previous?.loanBookGross, 'vs ' + (previous?.quarter ?? '')) },
            { label: 'Net of Provisions', value: number(latest.loanBookNetOfProvisions), detail: 'Provisions ' + number(latest.provisionAllLoans) },
            { label: 'Gross NPA', value: number(latest.grossNpa) + (latest.grossNpaPercent ? ' · ' + latest.grossNpaPercent + '%' : ''),
                ...changePct(number, percentValue(latest.grossNpaPercent), percentValue(previous?.grossNpaPercent), true) },
            { label: 'Net NPA', value: number(latest.netNpa) + (latest.netNpaPercent ? ' · ' + latest.netNpaPercent + '%' : ''),
                ...changePct(number, percentValue(latest.netNpaPercent), percentValue(previous?.netNpaPercent), true) },
            { label: 'Provision Coverage', value: latest.provisionCoverageRatioNpa == null ? '–' : number(latest.provisionCoverageRatioNpa, '1.1-1') + '%',
                detail: latest.provisionCoverageRatio == null ? 'NPA loans' : 'NPA loans · all loans ' + number(latest.provisionCoverageRatio, '1.1-2') + '%' },
            { label: 'Cost of Funds', value: latest.costOfFunds == null ? '–' : number(latest.costOfFunds, '1.2-2') + '%', detail: latest.quarter + ' FY ' + (d.fiscalYearFull ?? latest.fiscalYear ?? '') },
            { label: 'Yield', value: latest.yieldPercent ? latest.yieldPercent + '%' : '–',
                detail: spread(number, percentValue(latest.yieldPercent), latest.costOfFunds) }
        ] : [];
        this.loanBookCategories = d.loanBook.map(q => q.quarter);
        this.loanBookSeries = [
            { name: 'Gross', values: d.loanBook.map(q => q.loanBookGross), color: TEAL, labels: true, format: '1.0-0' },
            { name: 'Net of provisions', values: d.loanBook.map(q => q.loanBookNetOfProvisions), color: TEAL_LIGHT },
            { name: 'Gross NPA %', values: d.loanBook.map(q => percentValue(q.grossNpaPercent)), color: ORANGE, kind: 'line', labels: true, suffix: '%', format: '1.2-2' },
            { name: 'Net NPA %', values: d.loanBook.map(q => percentValue(q.netNpaPercent)), color: RED, kind: 'line', dashed: true, labels: true, suffix: '%', format: '1.2-2' }
        ];
        this.loanBookTooltips = d.loanBook.map(q => q.loanBookGross == null ? `${q.quarter}: no figures yet`
            : `${q.quarter}: Gross ${number(q.loanBookGross)} · Net ${number(q.loanBookNetOfProvisions)} · Gross NPA ${number(q.grossNpa)} (${q.grossNpaPercent ?? '–'}%)`
            + ` · Net NPA ${number(q.netNpa)} (${q.netNpaPercent ?? '–'}%) · PCR ${number(q.provisionCoverageRatioNpa, '1.1-1')}%`
            + ` · Cost of funds ${number(q.costOfFunds, '1.2-2')}% · Yield ${q.yieldPercent ?? '–'}%`);

        // Budget vs actual: actual measured against its budget
        this.budgetRows = d.budgetActual.map(b => ({
            label: b.particulars, reference: b.budget, display: b.actual, note: b.budget == null ? undefined : '/ ' + number(b.budget),
            segments: [{ value: b.actual ?? 0, color: (b.budget && (b.actual ?? 0) > b.budget) ? GREEN : TEAL }],
            tooltip: `${b.particulars}: actual ${number(b.actual)} of budget ${number(b.budget)}`
                + (b.budget ? ` (${number((b.actual ?? 0) / b.budget * 100, '1.0-0')}%)` : '')
        }));

        // Liquidity: last row emphasised as the total when it reads like one
        this.liquidityRows = d.liquidity.map((l, i) => {
            const total = i === d.liquidity.length - 1 && /net|total/i.test(l.particulars);
            const negative = (l.amount ?? 0) < 0;
            return {
                label: l.particulars, display: l.amount, emphasis: total, negative,
                segments: [{ value: Math.abs(l.amount ?? 0), color: total ? GREEN : TEAL }]
            };
        });

        // SMA / ECL: principal + interest stacked, outstanding / effective capital in the tooltip
        this.smaRows = d.smaOverdue.map(s => ({
            label: s.particulars || 'SMA-' + s.smaCategory, display: s.overdue,
            segments: [{ value: s.principalOverdue ?? 0, color: TEAL }, { value: s.interestOverdue ?? 0, color: TEAL_LIGHT }],
            tooltip: `${s.particulars}: principal ${number(s.principalOverdue, '1.2-2')} · interest ${number(s.interestOverdue, '1.2-2')}`
                + ` · overdue ${number(s.overdue, '1.2-2')} · outstanding ${number(s.outstanding, '1.2-2')}`
        }));
        this.eclRows = d.eclOverdue.map(e => ({
            label: e.particulars || 'Stage ' + e.eclClassification, display: e.overdue,
            segments: [{ value: e.principalOverdue ?? 0, color: TEAL }, { value: e.interestOverdue ?? 0, color: TEAL_LIGHT }],
            tooltip: `${e.particulars}: principal ${number(e.principalOverdue, '1.2-2')} · interest ${number(e.interestOverdue, '1.2-2')}`
                + ` · overdue ${number(e.overdue, '1.2-2')} · effective capital ${number(e.effectiveCapital, '1.2-2')}`
        }));

        // Cash flow: inflow / outflow columns, net as note
        this.cashFlowCategories = d.cashFlow.map(c => c.periodDesc || c.evaluationPeriod);
        this.cashFlowSeries = [
            { name: 'Total inflow', values: d.cashFlow.map(c => c.totalInflow), color: TEAL },
            { name: 'Total outflow', values: d.cashFlow.map(c => c.totalOutflow), color: ORANGE }
        ];
        this.cashFlowNotes = d.cashFlow.map(c => c.netFlow == null ? null : 'net ' + (c.netFlow < 0 ? '-' : '+') + number(Math.abs(c.netFlow)));
        this.cashFlowTooltips = d.cashFlow.map(c => `${c.periodDesc}: inflow principal ${number(c.inflowPrincipal)}, interest ${number(c.inflowInterest)},`
            + ` prepayment ${number(c.inflowPrepayment)} · outflow principal ${number(c.outflowPrincipal)}, interest ${number(c.outflowInterest)},`
            + ` disbursement ${number(c.outflowDisbursement)} · net ${number(c.netFlow)}`);

        // Collection efficiency per month
        this.collectionCategories = d.collectionEfficiency.map(m => (m.monthName || m.monthNumber || '').substring(0, 3));
        this.collectionSeries = [{
            name: 'Collection efficiency %', values: d.collectionEfficiency.map(m => percentValue(m.collectionEfficiency)),
            color: TEAL, labels: true, format: '1.0-1'
        }];

        // Proposals: amount per stage, count in the note
        this.proposalRows = d.proposals.map(p => ({
            label: p.particulars, display: p.amount, note: p.enquiryCount ? p.enquiryCount + ' proposals' : undefined,
            segments: [{ value: p.amount ?? 0, color: /sanction/i.test(p.particulars) ? GREEN : /declin|withdraw|reject/i.test(p.particulars) ? GREY : TEAL }],
            tooltip: `${p.particulars}: ${p.enquiryCount ?? '–'} proposals, ${number(p.amount)}`
        }));
    }
}

function isoToday(): string {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
}

function formatDate(iso: string): string {
    const [y, m, d] = iso.substring(0, 10).split('-');
    return d && m && y ? `${d}.${m}.${y}` : iso;
}

/** Change of an amount against the previous quarter as "▲ 3.4% vs Q1". */
type Formatter = (value: number | null | undefined, format?: string) => string;

function change(fmt: Formatter, current: number | null, previous: number | null | undefined, suffix: string): Pick<Kpi, 'detail' | 'tone'> {
    if (current == null || previous == null || previous === 0) {
        return { detail: '', tone: '' };
    }
    const pct = (current - previous) / Math.abs(previous) * 100;
    const text = fmt(Math.abs(pct), '1.1-1');
    return { detail: `${pct >= 0 ? '▲' : '▼'} ${text}% ${suffix}`, tone: pct >= 0 ? 'up' : 'down' };
}

/** Change of a percentage in points; for NPA a fall is the good direction. */
function changePct(fmt: Formatter, current: number | null, previous: number | null, lowerIsBetter: boolean): Pick<Kpi, 'detail' | 'tone'> {
    if (current == null || previous == null) {
        return { detail: '', tone: '' };
    }
    const diff = current - previous;
    const text = fmt(Math.abs(diff), '1.2-2');
    const good = lowerIsBetter ? diff <= 0 : diff >= 0;
    return { detail: `${diff >= 0 ? '▲' : '▼'} ${text} pp`, tone: good ? 'up' : 'down' };
}

function spread(fmt: Formatter, yieldPct: number | null, costOfFunds: number | null): string {
    if (yieldPct == null || costOfFunds == null) {
        return '';
    }
    return 'Spread ' + fmt(yieldPct - costOfFunds, '1.2-2') + ' pp';
}
