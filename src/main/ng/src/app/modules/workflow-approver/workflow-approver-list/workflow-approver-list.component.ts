import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { 
    ButtonComponent, 
    DynamicPageComponent, 
    DynamicPageContentComponent, 
    DynamicPageGlobalActionsComponent, 
    DynamicPageHeaderComponent, 
    FormModule, 
    SelectModule, 
    ToolbarComponent 
} from '@fundamental-ngx/core';
import { WorkflowApproverService } from '../workflow-approver.service';
import { GenericListComponent } from '../../generic/generic-list/generic-list.component';

@Component({
    selector: 'app-workflow-approver-list',
    imports: [
        // Dynamic Page Components
        DynamicPageComponent,
        DynamicPageContentComponent,
        DynamicPageGlobalActionsComponent,
        DynamicPageHeaderComponent,
        ToolbarComponent,
        // Other Components and Modules
        ButtonComponent,
        FormModule,
        SelectModule,
        GenericListComponent
    ],
    templateUrl: './workflow-approver-list.component.html'
})
export class WorkflowApproverListComponent {

    @ViewChild(GenericListComponent) workflowApproverList?: GenericListComponent;

    selectedDepartmentCode: string = '';
    selectedProcessName: string = '';

    /**
     * Constructor
     */
    constructor(
        private cdr: ChangeDetectorRef,
        public workflowApproverService: WorkflowApproverService
    ) {}

    /**
     * Filter passed to the generic list as 'departmentCode|processName'. It is never empty, so all approvers are listed
     * when no filter is selected.
     */
    get filter(): string {
        return `${this.selectedDepartmentCode}|${this.selectedProcessName}`;
    }

    /**
     * Whether a department or process filter is selected
     */
    get isFiltered(): boolean {
        return !!this.selectedDepartmentCode || !!this.selectedProcessName;
    }

    /**
     * Reload the approvers for the selected filters
     */
    onFilterChange(): void {
        const list = this.workflowApproverList;
        if (list) {
            this.cdr.detectChanges();
            list.clearSelection();
            list.fetchData();
        }
    }

    /**
     * Clear the department and process filters
     */
    clearFilters(): void {
        this.selectedDepartmentCode = '';
        this.selectedProcessName = '';
        this.onFilterChange();
    }
}
