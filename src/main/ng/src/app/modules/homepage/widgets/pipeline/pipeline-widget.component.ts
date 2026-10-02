import { DecimalPipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FUNCTIONAL_STAGES } from '../../homepage.constants';
import { HomepageService, StageCount } from '../../homepage.service';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

interface PipelineStage {
    code: number;
    label: string;
    count: number;
    amount: number;
}

@Component({
    selector: 'app-pipeline-widget',
    imports: [
        DashboardCardComponent,
        DecimalPipe
    ],
    templateUrl: './pipeline-widget.component.html',
    styleUrl: './pipeline-widget.component.scss'
})
export class PipelineWidgetComponent {

    @Input() loading = false;

    stages: PipelineStage[] | null = [];
    total = 0;

    /**
     * Loan contract count and amount per functional status, null if they could not be loaded
     */
    @Input() set stageCounts(stageCounts: StageCount[] | null) {
        if (stageCounts === null) {
            this.stages = null;
            return;
        }
        const counts = new Map(stageCounts.map(stage => [stage.functionalStatus, stage]));
        this.stages = FUNCTIONAL_STAGES.map(stage => ({
            code: stage.code,
            label: stage.label,
            count: counts.get(stage.code)?.count || 0,
            amount: counts.get(stage.code)?.amount || 0
        }));
        this.total = stageCounts.reduce((total, stage) => total + stage.count, 0);
    }

    constructor(private homepageService: HomepageService) {
    }

    /**
     * Open the loan contract search filtered to a functional status
     */
    openStage(stage: PipelineStage): void {
        if (stage.count > 0) {
            this.homepageService.openLoanContractSearch({ functionalStatus: stage.code });
        }
    }
}
