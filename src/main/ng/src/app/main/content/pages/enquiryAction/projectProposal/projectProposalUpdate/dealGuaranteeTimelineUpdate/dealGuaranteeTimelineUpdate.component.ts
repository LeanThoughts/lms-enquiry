import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { EnquiryActionService } from '../../../enquiryAction.service';

@Component({
  selector: 'fuse-deal-guarantee-timeline-update',
  templateUrl: './dealGuaranteeTimelineUpdate.component.html',
  styleUrls: ['./dealGuaranteeTimelineUpdate.component.scss']
})
export class DealGuaranteeTimelineUpdateComponent implements OnDestroy {

    _dealGuaranteeTimelineForm: FormGroup;
    _dealGuaranteeTimeline: any = {};

    _projectProposal: any;

    @Input()
    set projectProposal(pp: any) {
        this._projectProposal = pp;
        console.log('project proposal in input is', this._projectProposal);
        if (this._projectProposal !== undefined && JSON.stringify(this._projectProposal) !== JSON.stringify({})) {
            this._enquiryActionService.getDealGuaranteeTimeline(this._projectProposal.id).subscribe(response => {
                this._dealGuaranteeTimeline = response;
                this.initializeFormValues();
            });
        }
    }

    /**
     * constructor()
     */
    constructor(private _formBuilder: FormBuilder,
                private _matSnackBar: MatSnackBar,
                public _enquiryActionService: EnquiryActionService) { 

        this._dealGuaranteeTimelineForm = this._formBuilder.group({
            dealTransactionStructure: new FormControl(''),
            statusOfPBGAndMABG: new FormControl(''),
            timelinesMilestones: new FormControl(''),
            strengths: new FormControl(''),
            fundingArrangement: new FormControl(''),
            disbursementStageSchedule: new FormControl(''),
            offensesEnquiry: new FormControl(''),
            existingRelationsPFSPTC: new FormControl(''),
            deviations: new FormControl(''),
            esmsCategorization: new FormControl(''),
            otherProjectDetails: new FormControl(''),
            environmentalSystemCategory: new FormControl('')
        });
        
        this.initializeFormValues();
    }

    /**
     * ngOnDestroy()
     */
    ngOnDestroy(): void {
    }

    /**
     * submit()
     */
    submit(): void {        
        console.log(this._dealGuaranteeTimelineForm.value);
        if (this._dealGuaranteeTimelineForm.valid) {
            var formValues = this._dealGuaranteeTimelineForm.value;
            if (JSON.stringify(this._dealGuaranteeTimeline) === JSON.stringify({})) { // Insert a new record ...
                console.log('inserting new record');
                formValues.projectProposalId = this._projectProposal.id;
                this._enquiryActionService.createDealGuaranteeTimeline(formValues).subscribe(response => {
                    this._dealGuaranteeTimeline = response;
                    this._matSnackBar.open('Deal, Guarantee, Timelines created successfully.', 'OK', { duration: 7000 });
                });
            }
            else {
                console.log('updating existing record');
                this._dealGuaranteeTimeline.dealTransactionStructure = formValues.dealTransactionStructure;
                this._dealGuaranteeTimeline.statusOfPBGAndMABG = formValues.statusOfPBGAndMABG;
                this._dealGuaranteeTimeline.timelinesMilestones = formValues.timelinesMilestones;
                this._dealGuaranteeTimeline.strengths = formValues.strengths;
                this._dealGuaranteeTimeline.fundingArrangement = formValues.fundingArrangement;
                this._dealGuaranteeTimeline.disbursementStageSchedule = formValues.disbursementStageSchedule;
                this._dealGuaranteeTimeline.offensesEnquiry = formValues.offensesEnquiry;
                this._dealGuaranteeTimeline.existingRelationsPFSPTC = formValues.existingRelationsPFSPTC;
                this._dealGuaranteeTimeline.esmsCategorization = formValues.esmsCategorization;
                this._dealGuaranteeTimeline.deviations = formValues.deviations;
                this._dealGuaranteeTimeline.otherProjectDetails = formValues.otherProjectDetails;
                this._enquiryActionService.updateDealGuaranteeTimeline(this._dealGuaranteeTimeline).subscribe(response => {
                    this._dealGuaranteeTimeline = response;
                    this._matSnackBar.open('Deal, Guarantee, Timelines updated successfully.', 'OK', { duration: 7000 });
                });
            }
        }
    }

    /**
     * initializeFormValues()
     */
    initializeFormValues(): void {
        this._dealGuaranteeTimelineForm.setValue({
            'dealTransactionStructure': this._dealGuaranteeTimeline.dealTransactionStructure || '',
            'statusOfPBGAndMABG': this._dealGuaranteeTimeline.statusOfPBGAndMABG || '',
            'timelinesMilestones': this._dealGuaranteeTimeline.timelinesMilestones || '',
            'strengths': this._dealGuaranteeTimeline.strengths || '',
            'fundingArrangement': this._dealGuaranteeTimeline.fundingArrangement || '',
            'disbursementStageSchedule': this._dealGuaranteeTimeline.disbursementStageSchedule || '',
            'offensesEnquiry': this._dealGuaranteeTimeline.offensesEnquiry || '',
            'existingRelationsPFSPTC': this._dealGuaranteeTimeline.existingRelationsPFSPTC || '',
            'esmsCategorization': this._dealGuaranteeTimeline.esmsCategorization || '',
            'deviations': this._dealGuaranteeTimeline.deviations || '',
            'otherProjectDetails': this._dealGuaranteeTimeline.otherProjectDetails || '',
            'environmentalSystemCategory': this._dealGuaranteeTimeline.environmentalSystemCategory || ''
        });
    }
}
