import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { LayoutGridModule, CardModule, DialogService } from '@fundamental-ngx/core';
import { ButtonComponent } from '@fundamental-ngx/core';
import { ComponentNgxComponent } from '../../common/component-ngx/component-ngx.component';
import { InboxService } from './inbox.service';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { MessageService } from '../../message.service';
import { CustomDialogComponent } from '../../custom-dialog.component';

@Component({
    selector: 'app-inbox',
    imports: [
        ButtonComponent,
        CardModule,
        CommonModule,
        ComponentNgxComponent,
        LayoutGridModule,
    ],
    templateUrl: './inbox.component.html',
    styleUrl: './inbox.component.scss'
})
export class InboxComponent implements OnInit, OnDestroy {

    private readonly destroy$ = new Subject<void>();

    isApproveDisabled = false;
    tasks: any[] = [];

    /**
     * Constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private dialogService: DialogService,
        private inboxService: InboxService,
        private messageService: MessageService
    ) {
    }

    /**
     * On init
     */
    ngOnInit(): void {
        this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((data: any) => {
            this.tasks = data.routeResolver.tasks;
        });
    }

    /**
     * Refresh tasks
     */
    refreshTasks() {
        this.inboxService.getTasks().subscribe((data: any) => {
            this.tasks = data;
        });
    }

    /**
     * Approve task
     */
    approveTask(task: any): void {
        this.isApproveDisabled = true;
        let workFlowProcessRequestResource = {
            'businessProcessId': task.businessProcessId,
            'processName': task.processName,
            'processInstanceId': task.id,
            'rejectionReason': ''
        }
        this.messageService.showInfo('Approval in Process.');
        this.inboxService.approveTask(workFlowProcessRequestResource).subscribe({
            next: (response) => {
                this.isApproveDisabled = false;
                this.refreshTasks();
                this.messageService.showSuccess('Task approved successfully.');
            },
            error: (error) => {
                this.isApproveDisabled = false;
                this.messageService.showError(error.message + '!! Error approving task. Please try again. If the problem persists, '
                    + 'please contact the administrator.');
            }
        });
    }

    // TODO: Implement review task feature
    
    /**
     * Reject task
     */
    rejectTask(task: any): void {
        const dialogRef = this.dialogService.open(CustomDialogComponent, {
            data: {
                title: 'Reject Task',
                description: 'Please enter the rejection reason for the task.',
                displayInput: true,
                inputRequired: true,
                facts: []
            },
            width: '500px'
        });
        dialogRef.afterClosed.subscribe({
            next: (result: any) => {
                if (result.continue) {
                    let workFlowProcessRequestResource = {
                        'businessProcessId': task.businessProcessId,
                        'processName': task.processName,
                        'processInstanceId': task.id,
                        'rejectionReason': result.inputValue
                    }
                    this.messageService.showInfo('Reject in Process.');
                    this.inboxService.rejectTask(workFlowProcessRequestResource).subscribe({
                        next: (response) => {
                            this.messageService.showSuccess( 'Selected task is rejected and email notification was sent to requestor');
                            this.refreshTasks();
                        },
                        error: (error) => {
                            this.messageService.showError(error.message + '!! Error rejecting task. Please try again. '
                                + 'If the problem persists, please contact the administrator.');
                        }
                    });
    
                }
            },
            error: (error: any) => {
            }
        });        
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
