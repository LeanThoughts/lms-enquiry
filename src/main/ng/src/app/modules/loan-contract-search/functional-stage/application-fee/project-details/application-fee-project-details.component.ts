import { Component, Input, OnInit } from '@angular/core';
import { of } from 'rxjs';
import { ApplicationFeeService } from '../application-fee.service';
import { GenericUpdateComponent } from '../../../../generic/generic-update/generic-update.component';

@Component({
    selector: 'app-application-fee-project-details',
    imports: [
        GenericUpdateComponent
    ],
    template: `
        @if (loaded) {
            <div style="background-color: white;" [style.padding.rem]="1">
                <app-generic-update 
                    [entity]="'applicationFeeProjectDetails'"
                    [searchString1]="loanApplication.id"
                    [operation]="operation"
                    [selectedObject]="projectDetails"
                    (onCreateSuccess)="onSaveSuccess($event)"
                    (onUpdateSuccess)="onSaveSuccess($event)"
                />
            </div>
        }
    `
})
export class ApplicationFeeProjectDetailsComponent implements OnInit {

    @Input() loanApplication: any;
    @Input() applicationFeeId: string = '';

    loaded: boolean = false;
    operation: string = 'Create';
    projectDetails: any = {};

    /**
     * Constructor
     */
    constructor(private applicationFeeService: ApplicationFeeService) {
    }

    /**
     * On init. Show the saved project details or, if there are none yet, the details of the loan application.
     */
    ngOnInit(): void {
        const projectDetails$ = this.applicationFeeId ? this.applicationFeeService.getProjectDetails(this.applicationFeeId) : of(null);
        projectDetails$.subscribe((projectDetails: any) => {
            this.projectDetails = projectDetails ?? this.getDefaultsFromLoanApplication();
            this.operation = projectDetails ? 'Update' : 'Create';
            this.loaded = true;
        });
    }

    /**
     * Subsequent saves update the project details that were just saved
     */
    onSaveSuccess(projectDetails: any): void {
        this.projectDetails = projectDetails;
        this.operation = 'Update';
    }

    /**
     * Project details prefilled from the loan application
     */
    private getDefaultsFromLoanApplication(): any {
        const loanApplication = this.loanApplication ?? {};
        return {
            projectName: loanApplication.projectName,
            promoterName: loanApplication.promoterName,
            loanPurpose: loanApplication.loanPurpose,
            projectCapacity: loanApplication.projectCapacity,
            projectCapacityUnit: loanApplication.projectCapacityUnit,
            state: loanApplication.projectLocationStateCode,
            productTypeCode: loanApplication.productCode,
            term: loanApplication.term,
            enquiryCompletionDate: loanApplication.enquiryCompletionDate,
            loanType: loanApplication.loanType,
            loanClass: loanApplication.loanClass,
            assistanceType: loanApplication.assistanceType,
            financingType: loanApplication.financingType,
            projectType: loanApplication.projectType,
            projectTypeCoreSector: loanApplication.projectCoreSector,
            purposeOfLoan: loanApplication.purposeOfLoan,
            projectCost: loanApplication.projectCost,
            debt: loanApplication.projectDebtAmount,
            promoterContributionEquity: loanApplication.equity,
            debtEquityRatio: loanApplication.debtEquityRatio,
            grantSubsidyAmount: loanApplication.grantSubsidyAmount,
            debtEquityRatioWithGrant: loanApplication.debtEquityRatioWithGrant,
            pfsDebtAmount: loanApplication.pfsDebtAmount,
            rateOfInterest: loanApplication.expectedInterestRate,
            tenorYear: loanApplication.tenorYear,
            tenorMonths: loanApplication.tenorMonth,
            moratoriumPeriod: loanApplication.moratoriumPeriod,
            moratoriumPeriodUnit: loanApplication.moratoriumPeriodUnit,
            constructionPeriod: loanApplication.constructionPeriod,
            constructionPeriodUnit: loanApplication.constructionPeriodUnit
        };
    }
}
