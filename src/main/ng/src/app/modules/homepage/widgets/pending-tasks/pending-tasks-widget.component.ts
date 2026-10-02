import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent } from '@fundamental-ngx/core';
import { ContentDensityDirective } from '@fundamental-ngx/core/content-density';
import { getProcessColor, getProcessLabel, getTaskAgeInDays, getTaskAgeLabel, TASK_OVERDUE_DAYS } from '../../../inbox/inbox.constants';
import { DashboardCardComponent } from '../dashboard-card/dashboard-card.component';

interface ProcessTaskCount {
    processName: string;
    count: number;
    overdue: number;
}

const OLDEST_TASK_LIMIT = 5;

@Component({
    selector: 'app-pending-tasks-widget',
    imports: [
        ButtonComponent,
        ContentDensityDirective,
        DashboardCardComponent,
        DatePipe
    ],
    templateUrl: './pending-tasks-widget.component.html',
    styleUrl: './pending-tasks-widget.component.scss'
})
export class PendingTasksWidgetComponent {

    @Input() loading = false;

    tasks: any[] | null = [];
    overdueCount = 0;
    processes: ProcessTaskCount[] = [];
    oldestTasks: any[] = [];

    readonly overdueDays = TASK_OVERDUE_DAYS;

    /**
     * Tasks of the logged in user, null if they could not be loaded
     */
    @Input() set taskList(tasks: any[] | null) {
        this.tasks = tasks;
        const taskList = tasks || [];
        const processes = new Map<string, ProcessTaskCount>();
        taskList.forEach(task => {
            const process = processes.get(task.processName) || { processName: task.processName, count: 0, overdue: 0 };
            process.count++;
            process.overdue += this.isOverdue(task) ? 1 : 0;
            processes.set(task.processName, process);
        });
        this.processes = Array.from(processes.values()).sort((a, b) => b.count - a.count);
        this.overdueCount = taskList.filter(task => this.isOverdue(task)).length;
        this.oldestTasks = [...taskList]
            .sort((a, b) => new Date(a.requestDate).getTime() - new Date(b.requestDate).getTime())
            .slice(0, OLDEST_TASK_LIMIT);
    }

    constructor(private router: Router) {
    }

    getProcessLabel(processName: string): string {
        return getProcessLabel(processName);
    }

    getProcessColor(processName: string): number {
        return getProcessColor(processName);
    }

    getAgeLabel(task: any): string {
        return getTaskAgeLabel(task.requestDate);
    }

    isOverdue(task: any): boolean {
        return getTaskAgeInDays(task.requestDate) > TASK_OVERDUE_DAYS;
    }

    /**
     * Open the inbox, filtered to a process when one is given
     */
    openInbox(processName?: string): void {
        this.router.navigate(['/inbox'], processName ? { queryParams: { process: processName } } : {});
    }
}
